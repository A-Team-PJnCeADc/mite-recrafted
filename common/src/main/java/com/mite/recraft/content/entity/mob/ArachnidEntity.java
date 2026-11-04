package com.mite.recraft.content.entity.mob;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * 蛛形生物基类。MITE 的 EntityArachnid：爬墙、吐网、白昼不主动攻击。
 *
 * <p>数值取自 MITE 1.6.4 EntityArachnid：生命 12、跟随距离 28、移动速度 1.0、攻击 4。
 * 吐网逻辑：存活时带 0～3 张网，每隔 {@link #getTicksBetweenWebThrows()} tick 对 8 格内、
 * 视线可达的目标在脚下生成蜘蛛网。</p>
 */
public class ArachnidEntity extends Spider {

    private static final String NBT_WEBS = "num_webs";

    protected int numWebs;

    public ArachnidEntity(EntityType<? extends ArachnidEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Spider.createAttributes()
                .add(Attributes.MAX_HEALTH, 12.0)
                .add(Attributes.FOLLOW_RANGE, 28.0)
                .add(Attributes.MOVEMENT_SPEED, 1.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0);
    }

    /** MITE 普通蛛形生物 500 tick 一张，洞穴蛛与恶魔蛛 200 tick 一张。 */
    protected int getTicksBetweenWebThrows() {
        return 500;
    }

    @Override
    public void tick() {
        super.tick();

        if (level() instanceof ServerLevel serverLevel
                && numWebs > 0 && tickCount % getTicksBetweenWebThrows() == 0) {
            LivingEntity target = getTarget();
            if (target != null && distanceTo(target) <= 8.0D && hasLineOfSight(target)) {
                throwWeb(serverLevel, target);
            }
        }
    }

    /** 投出蛛网实体，命中后在目标脚下结网。对应 MITE 的 EntityWeb 投掷。 */
    protected void throwWeb(ServerLevel level, LivingEntity target) {
        com.mite.recraft.content.entity.projectile.WebEntity web =
                new com.mite.recraft.content.entity.projectile.WebEntity(
                        com.mite.recraft.registry.ModEntityTypes.WEB, getX(), getEyeY() - 0.1D, getZ(), level);
        web.setOwner(this);
        web.shoot(target.getX() - getX(), target.getY(0.5D) - web.getY(),
                target.getZ() - getZ(), 0.6F, 1.0F);
        level.addFreshEntity(web);
        --numWebs;
        playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 1.2F);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt(NBT_WEBS, numWebs);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        numWebs = input.getIntOr(NBT_WEBS, 0);
    }

    public int getNumWebs() {
        return numWebs;
    }

    public void setNumWebs(int numWebs) {
        this.numWebs = numWebs;
    }

    /** MITE 在生成时给 0～3 张网。 */
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        EntitySpawnReason reason, SpawnGroupData spawnData) {
        numWebs = getRandom().nextInt(4);
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }
}
