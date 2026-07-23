package com.mite.recraft.item.moditems.strongbox;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;

import java.util.EnumMap;
import java.util.Map;

public class StrongboxRegistry {
    public static final Map<StrongboxType, Block> BLOCKS = new EnumMap<>(StrongboxType.class);
    public static final Map<StrongboxType, BlockItem> ITEMS = new EnumMap<>(StrongboxType.class);
    public static final Map<StrongboxType, BlockEntityType<StrongboxBlockEntity>> BLOCK_ENTITY_TYPES =
            new EnumMap<>(StrongboxType.class);
    public static final Map<StrongboxType, MenuType<StrongboxScreenHandler>> MENU_TYPES =
            new EnumMap<>(StrongboxType.class);

    public static void register() {
        // Phase 1: create blocks (with lazy BE supplier — BE not yet registered)
        for (StrongboxType type : StrongboxType.VALUES) {
            registerBlock(type);
        }
        // Phase 2: create BE types (need blocks first)
        for (StrongboxType type : StrongboxType.VALUES) {
            registerBlockEntity(type);
        }
        // Phase 3: items and menus (need BE types registered)
        for (StrongboxType type : StrongboxType.VALUES) {
            registerItem(type);
            registerMenu(type);
        }
    }

    private static void registerBlock(StrongboxType type) {
        Identifier id = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, type.registryId);
        Block block = new StrongboxBlock(
                BlockBehaviour.Properties.of()
                        .strength(5.0F, 6.0F)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
                        .setId(ResourceKey.create(BuiltInRegistries.BLOCK.key(), id)),
                type,
                // Lazy supplier — resolved at runtime, after BE is registered
                () -> BLOCK_ENTITY_TYPES.get(type)
        );
        Block registered = Registry.register(BuiltInRegistries.BLOCK, id, block);
        BLOCKS.put(type, registered);
        ModBlocks.addBlocks(registered);
    }

    private static void registerItem(StrongboxType type) {
        Identifier id = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, type.registryId);
        BlockItem item = new BlockItem(
                BLOCKS.get(type),
                new Item.Properties()
                        .setId(ResourceKey.create(BuiltInRegistries.ITEM.key(), id))
                        .useBlockDescriptionPrefix()
        );
        Registry.register(BuiltInRegistries.ITEM, id, item);
        ITEMS.put(type, item);
    }

    private static void registerBlockEntity(StrongboxType type) {
        Identifier id = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, type.registryId);
        BlockEntityType<StrongboxBlockEntity> beType = FabricBlockEntityTypeBuilder.create(
                (pos, state) -> new StrongboxBlockEntity(type, pos, state),
                BLOCKS.get(type)
        ).build();
        BlockEntityType<StrongboxBlockEntity> registered = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE, id, beType
        );
        BLOCK_ENTITY_TYPES.put(type, registered);
    }

    private static void registerMenu(StrongboxType type) {
        Identifier id = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, type.registryId);
        MenuType<StrongboxScreenHandler> menuType = new MenuType<>(
                (syncId, inventory) -> new StrongboxScreenHandler(
                        MENU_TYPES.get(type), type, syncId, inventory,
                        StrongboxScreenHandler.createClientContainer(type)
                ),
                FeatureFlags.VANILLA_SET
        );
        MenuType<StrongboxScreenHandler> registered = Registry.register(
                BuiltInRegistries.MENU, id, menuType
        );
        MENU_TYPES.put(type, registered);
    }
}
