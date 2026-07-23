package com.mite.recraft.item.moditems.strongbox;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Supplier;

public class StrongboxBlock extends ChestBlock {
    private final StrongboxType type;
    private final Supplier<BlockEntityType<? extends ChestBlockEntity>> beSupplier;

    public StrongboxBlock(BlockBehaviour.Properties properties, StrongboxType type,
                          Supplier<BlockEntityType<? extends ChestBlockEntity>> beSupplier) {
        super(beSupplier, getOpenSound(), getCloseSound(), properties);
        this.type = type;
        this.beSupplier = beSupplier;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StrongboxBlockEntity(this.type, pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection().getOpposite();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(FACING, direction)
                .setValue(TYPE, ChestType.SINGLE)
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    public StrongboxType getStrongboxType() {
        return type;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level,
                                     net.minecraft.world.level.ScheduledTickAccess tickView,
                                     BlockPos pos, Direction direction, BlockPos neighborPos,
                                     BlockState neighborState, RandomSource random) {
        return super.updateShape(state, level, tickView, pos, direction, neighborPos, neighborState, random)
                .setValue(TYPE, ChestType.SINGLE);
    }

    private static SoundEvent getOpenSound() {
        return SoundEvents.CHEST_OPEN;
    }

    private static SoundEvent getCloseSound() {
        return SoundEvents.CHEST_CLOSE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide()
                ? (BlockEntityTicker<T>) (BlockEntityTicker<ChestBlockEntity>) ChestBlockEntity::lidAnimateTick
                : null;
    }
}
