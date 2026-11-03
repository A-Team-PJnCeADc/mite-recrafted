package com.mite.recraft.registry;

import com.mite.recraft.api.MiteIds;
import com.mite.recraft.api.MiteRegistration;
import com.mite.recraft.content.block.furnace.FurnaceTier;
import com.mite.recraft.content.block.furnace.MiteFurnaceBlock;
import com.mite.recraft.content.block.furnace.MiteFurnaceBlockEntity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 熔炉方块注册。所有熔炉共用同一个 BlockEntityType，tier 由方块实例携带。
 */
public final class ModFurnaces {

    private static final List<Block> FURNACE_BLOCKS = new ArrayList<>();
    private static final String BLOCK_ENTITY_PATH = "mite_furnace";

    private static BlockEntityType<MiteFurnaceBlockEntity> blockEntityType;

    private ModFurnaces() {
    }

    /** 方块阶段：方块与 BlockItem 由 {@link MiteRegistration#registerBlock} 拆分到两个阶段。 */
    public static void registerBlocks() {
        for (FurnaceTier tier : FurnaceTier.VALUES) {
            registerFurnace(tier);
        }
    }

    /** 方块实体类型阶段。 */
    public static void registerBlockEntityType() {
        blockEntityType = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                MiteIds.id(BLOCK_ENTITY_PATH),
                new BlockEntityType<>(
                        (pos, state) -> {
                            if (state.getBlock() instanceof MiteFurnaceBlock furnace) {
                                return new MiteFurnaceBlockEntity(blockEntityType, pos, state, furnace.getTier());
                            }
                            return new MiteFurnaceBlockEntity(blockEntityType, pos, state, FurnaceTier.STONE);
                        },
                        Set.copyOf(FURNACE_BLOCKS)
                )
        );
    }

    private static void registerFurnace(FurnaceTier tier) {
        Identifier id = MiteIds.id(tier.registryId);
        Block block = new MiteFurnaceBlock(tier, tier.properties.setId(MiteIds.blockKey(id)));
        FURNACE_BLOCKS.add(MiteRegistration.registerBlock(id, block, props -> props.stacksTo(1)));
    }

    public static BlockEntityType<MiteFurnaceBlockEntity> blockEntityType() {
        return blockEntityType;
    }

    public static List<Block> getFurnaceBlocks() {
        return Collections.unmodifiableList(FURNACE_BLOCKS);
    }
}
