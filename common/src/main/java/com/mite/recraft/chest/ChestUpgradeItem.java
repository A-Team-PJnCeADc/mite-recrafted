package com.mite.recraft.chest;

import com.mite.recraft.registry.ModChests;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;

/**
 * 升级物品：对来源等级箱子使用后替换为目标等级，并迁移内容与朝向。
 *
 * <p>以下情况不生效：客户端、无建造权限、方块等级不符、箱子正被打开。</p>
 */
public class ChestUpgradeItem extends Item {

    private final ChestUpgrade upgrade;

    public ChestUpgradeItem(ChestUpgrade upgrade, Properties properties) {
        super(properties);
        this.upgrade = upgrade;
    }

    public ChestUpgrade upgrade() {
        return this.upgrade;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        Player player = context.getPlayer();
        if (player == null || !player.mayBuild()) {
            return InteractionResult.PASS;
        }

        BlockPos pos = context.getClickedPos();
        BlockState current = level.getBlockState(pos);
        if (!matchesSource(current)) {
            return InteractionResult.PASS;
        }

        if (!(level.getBlockEntity(pos) instanceof ChestBlockEntity chest)) {
            return InteractionResult.PASS;
        }
        if (ChestBlockEntity.getOpenCount(level, pos) > 0 || !chest.stillValid(player)) {
            return InteractionResult.PASS;
        }

        Direction facing = current.getValue(ChestBlock.FACING);
        boolean waterlogged = current.getValue(ChestBlock.WATERLOGGED);
        CompoundTag contents = chest.saveWithoutMetadata(level.registryAccess());

        level.removeBlockEntity(pos);
        level.removeBlock(pos, false);
        level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(current));

        BlockState replaced = ModChests.block(this.upgrade.to())
                .defaultBlockState()
                .setValue(ChestBlock.FACING, facing)
                .setValue(ChestBlock.WATERLOGGED, waterlogged);
        level.setBlock(pos, replaced, 3);
        level.sendBlockUpdated(pos, replaced, replaced, 3);

        BlockEntity storage = level.getBlockEntity(pos);
        if (storage != null) {
            ValueInput input = TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), contents);
            storage.loadWithComponents(input);
            if (storage instanceof VariantChestStorage chestStorage) {
                chestStorage.normalizeCapacity();
            }
            storage.setChanged();
        }

        level.playSound(null, pos, current.getSoundType().getBreakSound(), SoundSource.BLOCKS,
                current.getSoundType().getVolume(), current.getSoundType().getPitch());
        level.playSound(null, pos, replaced.getSoundType().getPlaceSound(), SoundSource.BLOCKS,
                replaced.getSoundType().getVolume(), replaced.getSoundType().getPitch());
        if (!player.getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.SUCCESS;
    }

    private boolean matchesSource(BlockState state) {
        if (this.upgrade.from() == null) {
            return state.is(Blocks.CHEST) || state.is(Blocks.TRAPPED_CHEST);
        }
        return state.is(ModChests.block(this.upgrade.from()));
    }
}
