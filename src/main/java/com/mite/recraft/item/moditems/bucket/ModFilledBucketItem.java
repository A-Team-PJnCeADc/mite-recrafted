package com.mite.recraft.item.moditems.bucket;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * MITE 金属满桶 — 匹配原版 BucketItem 的倒液逻辑。
 */
public class ModFilledBucketItem extends Item implements DispensibleContainerItem {

    public final Fluid fluid;
    public Item emptyPeer;

    public ModFilledBucketItem(Fluid fluid, Properties props) {
        super(props);
        this.fluid = fluid;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

        if (hit.getType() == HitResult.Type.MISS)
            return InteractionResult.PASS;
        if (hit.getType() != HitResult.Type.BLOCK)
            return InteractionResult.PASS;

        BlockPos clickedPos = hit.getBlockPos();
        Direction face = hit.getDirection();
        BlockPos placePos = clickedPos.relative(face);

        if (!level.mayInteract(player, clickedPos))
            return InteractionResult.FAIL;
        if (!player.mayUseItemAt(placePos, face, held))
            return InteractionResult.FAIL;

        BlockState clicked = level.getBlockState(clickedPos);

        // 炼药锅（非潜行时）：液体匹配则倒入消耗桶，不匹配则 PASS（保留桶）
        // 潜行时跳过，由 emptyContents 处理相邻放置
        if (!player.isSecondaryUseActive()
            && (clicked.is(Blocks.CAULDRON) || clicked.is(Blocks.WATER_CAULDRON)
                || clicked.is(Blocks.LAVA_CAULDRON))) {
            if (handleCauldron(level, player, clickedPos, clicked)) {
                player.awardStat(Stats.ITEM_USED.get(this));
                ItemStack emptyResult = getEmptySuccessItem(held, player);
                ItemStack result = ItemUtils.createFilledResult(held, player, emptyResult);
                return InteractionResult.SUCCESS.heldItemTransformedTo(result);
            }
            return InteractionResult.PASS; // 液体不匹配 → 不消耗桶
        }

        // 普通位置 → emptyContents
        if (this.emptyContents(player, level, clickedPos, hit)) {
            player.awardStat(Stats.ITEM_USED.get(this));
            ItemStack emptyResult = getEmptySuccessItem(held, player);
            ItemStack result = ItemUtils.createFilledResult(held, player, emptyResult);
            return InteractionResult.SUCCESS.heldItemTransformedTo(result);
        }

        return InteractionResult.PASS;
    }

    /** 尝试将液体倒入炼药锅。只处理方块变化 + 音效，不修改物品栏。 */
    private boolean handleCauldron(Level level, Player player, BlockPos pos, BlockState state) {
        boolean isWater = this.fluid.isSame(Fluids.WATER);
        boolean isLava = this.fluid.isSame(Fluids.LAVA);
        if (!isWater && !isLava)
            return false;

        if (state.is(Blocks.CAULDRON)) {
            if (level.isClientSide()) return true;
            if (isWater) {
                level.setBlock(pos, Blocks.WATER_CAULDRON.defaultBlockState()
                    .setValue(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MAX_FILL_LEVEL), 3);
            } else {
                level.setBlock(pos, Blocks.LAVA_CAULDRON.defaultBlockState(), 3);
            }
            player.awardStat(Stats.FILL_CAULDRON);
            level.playSound(null, pos, isWater ? SoundEvents.BUCKET_EMPTY : SoundEvents.BUCKET_EMPTY_LAVA,
                SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            return true;

        } else if (state.is(Blocks.WATER_CAULDRON) && isWater) {
            int current = state.getValue(LayeredCauldronBlock.LEVEL);
            if (current >= LayeredCauldronBlock.MAX_FILL_LEVEL) {
                // 已满，仍返回 true 消耗桶，但不改方块
                if (!level.isClientSide()) {
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
                }
                return true;
            }
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MAX_FILL_LEVEL), 3);
                player.awardStat(Stats.FILL_CAULDRON);
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return true;

        } else if (state.is(Blocks.LAVA_CAULDRON) && isLava) {
            // 已满，仍返回 true 消耗桶，但不改方块
            if (!level.isClientSide()) {
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return true;
        }

        return false;
    }

    /** 匹配原版 BucketItem.emptyContents 完整逻辑 */
    @Override
    public boolean emptyContents(LivingEntity entity, Level level, BlockPos pos, BlockHitResult hit) {
        Player player = entity instanceof Player p ? p : null;
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        FluidState fluidState = this.fluid.defaultFluidState();

        boolean canReplace = state.canBeReplaced(this.fluid);
        boolean shifting = player != null && player.isShiftKeyDown();

        // canPlace：可替换 || 是容器且容器接受此液体
        boolean canPlace = canReplace
            || (block instanceof LiquidBlockContainer container
                && container.canPlaceLiquid(player, level, pos, state, this.fluid));

        // 空位置 || 可放置且非潜行 → 进行放置
        boolean proceed = state.isAir() || (canPlace && !shifting);

        if (!proceed) {
            // 潜行时尝试相邻位置（hit 为 null 防止递归）
            if (hit != null) {
                BlockPos placePos = hit.getBlockPos().relative(hit.getDirection());
                return this.emptyContents(entity, level, placePos, null);
            }
            return false;
        }

        // 水在蒸发维度 → 播放灭火声效 + 烟雾粒子
        var random = level.getRandom();
        if (level.environmentAttributes()
                .getValue(EnvironmentAttributes.WATER_EVAPORATES, pos)
                && this.fluid.isSame(Fluids.WATER)) {
            level.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS,
                0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F);
            for (int i = 0; i < 8; i++) {
                level.addParticle(ParticleTypes.LARGE_SMOKE,
                    (double) pos.getX() + random.nextFloat(),
                    (double) pos.getY() + random.nextFloat(),
                    (double) pos.getZ() + random.nextFloat(),
                    0.0, 0.0, 0.0);
            }
            return true;
        }

        // 向液体容器注入（炼药锅等）
        if (!canReplace && block instanceof LiquidBlockContainer container
            && this.fluid instanceof Fluid) {
            if (container.placeLiquid(level, pos, state, fluidState)) {
                this.playEmptySound(entity, level, pos);
                return true;
            }
            return false;
        }

        // 放置流体方块
        if (!level.isClientSide() && canReplace && state.getFluidState().isEmpty())
            level.destroyBlock(pos, true);

        if (level.setBlock(pos, fluidState.createLegacyBlock(), 11)
            || state.getFluidState().isSource()) {
            this.playEmptySound(entity, level, pos);
            return true;
        }

        return false;
    }

    protected void playEmptySound(LivingEntity entity, LevelAccessor level, BlockPos pos) {
        level.playSound(entity, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(entity, GameEvent.FLUID_PLACE, pos);
    }

    protected ItemStack getEmptySuccessItem(ItemStack stack, Player player) {
        return !player.hasInfiniteMaterials() && emptyPeer != null
            ? new ItemStack(emptyPeer)
            : stack.copy();
    }
}
