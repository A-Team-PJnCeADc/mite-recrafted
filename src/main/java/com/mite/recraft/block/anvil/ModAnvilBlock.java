package com.mite.recraft.block.anvil;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.block.modblock.ModAnvilBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class ModAnvilBlock extends AnvilBlock implements EntityBlock {
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 2);
    private static final int NUM_COMPONENTS = 31;
    private float durabilityCoefficient;
    private int perIngotDurability;

    private static BlockEntityType<ModAnvilBlockEntity> anvilBlockEntity;

    public static BlockEntityType<ModAnvilBlockEntity> getAnvilBlockEntityType() {
        if (anvilBlockEntity == null) {
            anvilBlockEntity = new BlockEntityType<>(ModAnvilBlockEntity::new,
                    Set.of(ModAnvilBlocks.COPPER_ANVIL, ModAnvilBlocks.SILVER_ANVIL, ModAnvilBlocks.GOLD_ANVIL,
                            ModAnvilBlocks.ANCIENT_METAL_ANVIL, ModAnvilBlocks.MITHRIL_ANVIL, ModAnvilBlocks.ADAMANTIUM_ANVIL));
        }
        return anvilBlockEntity;
    }

    public static void registerBlockEntity() {
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "anvil"),
                getAnvilBlockEntityType());
    }

    public ModAnvilBlock(Properties properties, float durabilityCoefficient) {
        super(properties.sound(SoundType.ANVIL));
        this.durabilityCoefficient = durabilityCoefficient;
        registerDefaultState(defaultBlockState().setValue(STAGE, 0));
    }

    public void setMaterialData(float coefficient, int perIngot) {
        this.durabilityCoefficient = coefficient;
        this.perIngotDurability = perIngot;
    }

    public float getMaterialTier() { return durabilityCoefficient; }

    public int getDurability() {
        return perIngotDurability * NUM_COMPONENTS * (int) durabilityCoefficient;
    }

    public int getDamageStage(int damage) {
        float factor = (float) damage / (float) getDurability();
        if (factor >= 0.75F) return 2;  // 25% 耐久剩余
        if (factor >= 0.25F) return 1;  // 75% 耐久剩余
        return 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STAGE);
    }

    /** 砧顶上有非流体方块时阻止打开 GUI */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);
        if (!aboveState.isAir() && aboveState.getFluidState().isEmpty()) {
            // 上方是非流体方块（非空气、非液体），阻止打开
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, level, pos, player, hit);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ModAnvilBlockEntity(pos, state);
    }

    @Override
    @Nullable
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider(
                (containerId, inventory, p) -> new ModAnvilMenu(containerId, inventory, ContainerLevelAccess.create(level, pos)),
                net.minecraft.network.chat.Component.translatable(getDescriptionId()));
    }
}
