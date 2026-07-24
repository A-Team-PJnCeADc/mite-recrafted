package com.mite.recraft.block.furnace;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * MITE 熔炉注册器。
 */
public class ModFurnaceRegistry {
    public static final List<Block> FURNACE_BLOCKS = new ArrayList<>();
    public static BlockEntityType<MiteFurnaceBlockEntity> BLOCK_ENTITY_TYPE;

    public static void register() {
        for (FurnaceTier tier : FurnaceTier.VALUES) {
            registerFurnace(tier);
        }

        // BlockEntity type — 所有熔炉共享一个 BE Type
        BLOCK_ENTITY_TYPE = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "mite_furnace"),
                FabricBlockEntityTypeBuilder.create(
                        (pos, state) -> {
                            // 从 state 反查 tier
                            Block block = state.getBlock();
                            if (block instanceof MiteFurnaceBlock fb) {
                                return new MiteFurnaceBlockEntity(BLOCK_ENTITY_TYPE, pos, state, fb.getTier());
                            }
                            // fallback — 不应发生
                            return new MiteFurnaceBlockEntity(BLOCK_ENTITY_TYPE, pos, state, FurnaceTier.STONE);
                        },
                        FURNACE_BLOCKS.toArray(new Block[0])
                ).build()
        );
    }

    private static void registerFurnace(FurnaceTier tier) {
        Identifier id = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, tier.registryId);
        var props = tier.properties.setId(ResourceKey.create(BuiltInRegistries.BLOCK.key(), id));
        Block block = new MiteFurnaceBlock(tier, props);
        Registry.register(BuiltInRegistries.BLOCK,
                ResourceKey.create(BuiltInRegistries.BLOCK.key(), id), block);
        Registry.register(BuiltInRegistries.ITEM,
                ResourceKey.create(BuiltInRegistries.ITEM.key(), id),
                new BlockItem(block, new Item.Properties()
                        .stacksTo(1)
                        .setId(ResourceKey.create(BuiltInRegistries.ITEM.key(), id))
                        .useBlockDescriptionPrefix()));
        FURNACE_BLOCKS.add(block);
        ModBlocks.addBlocks(block);
    }

    public static List<Block> getFurnaceBlocks() {
        return Collections.unmodifiableList(FURNACE_BLOCKS);
    }
}
