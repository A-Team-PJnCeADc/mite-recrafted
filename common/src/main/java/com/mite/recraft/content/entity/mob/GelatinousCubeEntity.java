package com.mite.recraft.content.entity.mob;

import com.mite.recraft.content.combat.MiteDissolve;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 胶质方块生物基类。对应 MITE 1.6.4 的 EntityCubic + EntityGelatinousCube。
 *
 * <p>体型、分裂、生命值（体型平方）沿用原版史莱姆；吞噬方块、腐蚀地面掉落物、
 * 进食时减速按 MITE 的逻辑实现，攻击力按类型倍率放大。</p>
 */
public abstract class GelatinousCubeEntity extends Slime {

    /** 每 20 tick 每个方块累积的溶解进度。 */
    private static final int DISSOLVE_CYCLE = 20;
    /** 地面物品被侵蚀的累积上限：5 个周期后销毁。 */
    private static final int ITEM_DISSOLVE_TICKS = 100;

    private final Map<BlockPos, Integer> dissolvingBlocks = new HashMap<>();
    private final Map<UUID, Integer> dissolvingItems = new HashMap<>();
    private int feedingTicks;

    protected GelatinousCubeEntity(EntityType<? extends GelatinousCubeEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes();
    }

    /** 各类型的攻击力倍率，取自 MITE 的 getAttackStrengthMultiplierForType。 */
    protected abstract int attackMultiplier();

    /** 溶解类型：粘液/凝胶用 pepsin，软泥/布丁用 acid。 */
    protected abstract MiteDissolve.Type dissolveType();

    /** MITE 里只有粘液（blob）命中后附带缓慢。 */
    protected boolean appliesSlowness() {
        return false;
    }

    @Override
    protected float getAttackDamage() {
        return super.getAttackDamage() * attackMultiplier();
    }

    /** MITE EntityCubic：生成体型取 1、2、4（1 << rand(3)）。 */
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level,
                                        DifficultyInstance difficulty,
                                        EntitySpawnReason reason,
                                        SpawnGroupData spawnData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData);
        setSize(1 << getRandom().nextInt(3), true);
        return data;
    }

    @Override
    public void tick() {
        super.tick();

        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }

        if (tickCount % DISSOLVE_CYCLE == 0) {
            dissolveBlocks(serverLevel);
            dissolveItems(serverLevel);
        }

        if (feedingTicks > 0) {
            --feedingTicks;
            var movement = getDeltaMovement();
            setDeltaMovement(movement.x * 0.4D, movement.y, movement.z * 0.4D);
        }
    }

    /** 扫描自身占据的方块，按溶解时间累积进度，达到阈值后无掉落销毁。 */
    private void dissolveBlocks(ServerLevel level) {
        var box = getBoundingBox();
        int minX = (int) Math.floor(box.minX);
        int minY = (int) Math.floor(box.minY);
        int minZ = (int) Math.floor(box.minZ);
        int maxX = (int) Math.floor(box.maxX);
        int maxY = (int) Math.floor(box.maxY);
        int maxZ = (int) Math.floor(box.maxZ);

        boolean anyProgress = false;
        for (BlockPos pos : BlockPos.betweenClosed(minX, minY, minZ, maxX, maxY, maxZ)) {
            BlockPos key = pos.immutable();
            BlockState state = level.getBlockState(key);
            int period = MiteDissolve.period(state, dissolveType());

            if (period < 0) {
                dissolvingBlocks.remove(key);
                continue;
            }
            if (period == 0) {
                destroyBlock(level, key);
                dissolvingBlocks.remove(key);
                continue;
            }

            int progress = dissolvingBlocks.getOrDefault(key, 0) + getSize() * DISSOLVE_CYCLE;
            if (progress >= period) {
                destroyBlock(level, key);
                dissolvingBlocks.remove(key);
            } else {
                dissolvingBlocks.put(key, progress);
                anyProgress = true;
                level.sendParticles(ParticleTypes.CLOUD,
                        key.getX() + 0.5D, key.getY() + 0.5D, key.getZ() + 0.5D, 2, 0.2D, 0.2D, 0.2D, 0.0D);
            }
        }

        if (!anyProgress) {
            dissolvingBlocks.clear();
        }
        feedingTicks = anyProgress ? DISSOLVE_CYCLE : 0;
    }

    private void destroyBlock(ServerLevel level, BlockPos pos) {
        level.destroyBlock(pos, false);
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.25F, 1.4F);
    }

    /** 落在体内的物品被侵蚀：符合该溶解类型的物品累积到阈值后销毁。 */
    private void dissolveItems(ServerLevel level) {
        boolean anyItem = false;
        for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, getBoundingBox(),
                entity -> !entity.isRemoved())) {
            ItemStack stack = item.getItem();
            if (!MiteDissolve.harmsItem(stack, dissolveType())) {
                continue;
            }
            anyItem = true;
            int progress = dissolvingItems.getOrDefault(item.getUUID(), 0) + DISSOLVE_CYCLE;
            if (progress >= ITEM_DISSOLVE_TICKS) {
                level.sendParticles(ParticleTypes.CLOUD,
                        item.getX(), item.getY() + 0.2D, item.getZ(), 8, 0.2D, 0.2D, 0.2D, 0.02D);
                level.playSound(null, item.blockPosition(), SoundEvents.FIRE_EXTINGUISH,
                        SoundSource.NEUTRAL, 0.3F, 1.6F);
                item.discard();
                dissolvingItems.remove(item.getUUID());
            } else {
                dissolvingItems.put(item.getUUID(), progress);
            }
        }

        if (!anyItem) {
            dissolvingItems.clear();
        } else {
            feedingTicks = DISSOLVE_CYCLE;
        }
    }

    /** MITE：粘液命中后附加 50 tick、等级 V 的缓慢。 */
    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);
        if (appliesSlowness() && player.isAlive()) {
            player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 50, 5), this);
        }
    }
}
