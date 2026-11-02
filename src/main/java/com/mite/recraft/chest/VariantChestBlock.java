package com.mite.recraft.chest;

import com.mite.recraft.registry.ModChests;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

/**
 * 按规格定义容量的箱子方块。朝向、开合与水logged 逻辑复用原版 {@link ChestBlock}，
 * 只替换方块实体类型与音效；不生成双联箱。
 */
public class VariantChestBlock extends ChestBlock {

    private final ChestVariant variant;

    public VariantChestBlock(ChestVariant variant, Properties properties) {
        super(() -> ModChests.storageType(variant), openSound(variant), closeSound(variant), properties);
        this.variant = variant;
    }

    public ChestVariant variant() {
        return this.variant;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModChests.createStorage(this.variant, pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(TYPE, ChestType.SINGLE)
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction direction, BlockPos neighborPos, BlockState neighborState,
                                     RandomSource random) {
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random)
                .setValue(TYPE, ChestType.SINGLE);
    }

    private static SoundEvent openSound(ChestVariant variant) {
        return metalSound(variant) ? SoundEvents.COPPER_CHEST_OPEN : SoundEvents.CHEST_OPEN;
    }

    private static SoundEvent closeSound(ChestVariant variant) {
        return metalSound(variant) ? SoundEvents.COPPER_CHEST_CLOSE : SoundEvents.CHEST_CLOSE;
    }

    private static boolean metalSound(ChestVariant variant) {
        return variant == ChestVariant.COPPER || variant == ChestVariant.IRON
                || variant == ChestVariant.GOLD || variant == ChestVariant.NETHERITE;
    }
}
