package com.mite.recraft.content.block.furnace;

import com.mite.recraft.registry.ModFurnaces;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jspecify.annotations.Nullable;

public class MiteFurnaceBlock extends AbstractFurnaceBlock {

    private final FurnaceTier tier;

    public MiteFurnaceBlock(FurnaceTier tier, BlockBehaviour.Properties properties) {
        super(properties);
        this.tier = tier;
    }

    public FurnaceTier getTier() {
        return tier;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MiteFurnaceBlockEntity(ModFurnaces.blockEntityType(), pos, state, tier);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return createTickerHelper(type, ModFurnaces.blockEntityType(),
                (lvl, pos, st, be) -> AbstractFurnaceBlockEntity.serverTick((ServerLevel) lvl, pos, st, be));
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        Direction facing = level.getBlockState(pos).getValue(BlockStateProperties.HORIZONTAL_FACING);
        BlockPos frontPos = pos.relative(facing);
        if (level.getBlockState(frontPos).isFaceSturdy(level, frontPos, facing.getOpposite())) {
            return;
        }
        if (level.getBlockEntity(pos) instanceof MiteFurnaceBlockEntity furnace) {
            player.openMenu(furnace);
        }
    }
}
