package com.mite.recraft.block.furnace;

import com.mite.recraft.MiteRecrafted;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class MiteFurnaceBlock extends AbstractFurnaceBlock {

    private static final MapCodec<MiteFurnaceBlock> CODEC = BaseEntityBlock.simpleCodec(
            props -> new MiteFurnaceBlock(FurnaceTier.STONE, props)
    );
    // 注意：正确的 tier 由 registry 传入，codec 仅满足序列化需求

    private final FurnaceTier tier;

    public MiteFurnaceBlock(FurnaceTier tier, BlockBehaviour.Properties properties) {
        super(properties);
        this.tier = tier;
    }

    public FurnaceTier getTier() {
        return tier;
    }

    @Override
    protected MapCodec<? extends AbstractFurnaceBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MiteFurnaceBlockEntity(
                ModFurnaceRegistry.BLOCK_ENTITY_TYPE,
                pos, state, tier
        );
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return createTickerHelper(type, ModFurnaceRegistry.BLOCK_ENTITY_TYPE,
                (lvl, pos, st, be) -> AbstractFurnaceBlockEntity.serverTick((ServerLevel) lvl, pos, st, be));
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, net.minecraft.world.entity.player.Player player) {
        // 前方有实心方块时无法打开 GUI（MITE R126+）
        Direction facing = level.getBlockState(pos).getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING);
        BlockPos frontPos = pos.relative(facing);
        BlockState frontState = level.getBlockState(frontPos);
        if (frontState.isFaceSturdy(level, frontPos, facing.getOpposite())) {
            return; // 不打开 GUI
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof MiteFurnaceBlockEntity fbe) {
            player.openMenu(fbe);
        }
    }
}
