package com.mite.recraft.registry;

import com.mite.recraft.api.ItemCategory;
import com.mite.recraft.api.MiteIds;
import com.mite.recraft.api.MiteRegistration;
import com.mite.recraft.content.block.strongbox.StrongboxBlock;
import com.mite.recraft.content.block.strongbox.StrongboxBlockEntity;
import com.mite.recraft.content.block.strongbox.StrongboxScreenHandler;
import com.mite.recraft.content.block.strongbox.StrongboxType;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * 保险箱注册。方块先注册（BlockEntityType 以延迟供应商解析），之后注册 BlockEntityType、物品与菜单。
 */
public final class ModStrongboxes {

    public static final Map<StrongboxType, Block> BLOCKS = new EnumMap<>(StrongboxType.class);
    public static final Map<StrongboxType, BlockItem> ITEMS = new EnumMap<>(StrongboxType.class);
    public static final Map<StrongboxType, BlockEntityType<StrongboxBlockEntity>> BLOCK_ENTITY_TYPES =
            new EnumMap<>(StrongboxType.class);
    public static final Map<StrongboxType, MenuType<StrongboxScreenHandler>> MENU_TYPES =
            new EnumMap<>(StrongboxType.class);

    private ModStrongboxes() {
    }

    /** 方块阶段：只写方块表，BlockItem 延迟到物品阶段。 */
    public static void registerBlocks() {
        for (StrongboxType type : StrongboxType.VALUES) {
            registerBlock(type);
        }
    }

    /** 方块实体类型阶段。 */
    public static void registerBlockEntityTypes() {
        for (StrongboxType type : StrongboxType.VALUES) {
            registerBlockEntity(type);
        }
    }

    /** 菜单阶段。 */
    public static void registerMenus() {
        for (StrongboxType type : StrongboxType.VALUES) {
            Registry.register(BuiltInRegistries.MENU, MiteIds.id(type.registryId), MENU_TYPES.get(type));
        }
    }

    static {
        // 菜单类型在声明期构造：界面注册早于注册表写入
        for (StrongboxType type : StrongboxType.VALUES) {
            MENU_TYPES.put(type, new MenuType<>(
                    (syncId, inventory) -> new StrongboxScreenHandler(
                            MENU_TYPES.get(type), type, syncId, inventory,
                            StrongboxScreenHandler.createClientContainer(type)),
                    FeatureFlags.VANILLA_SET));
        }
    }

    private static void registerBlock(StrongboxType type) {
        Identifier id = MiteIds.id(type.registryId);
        Block block = new StrongboxBlock(
                BlockBehaviour.Properties.of()
                        .strength(5.0F, 6.0F)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
                        .setId(MiteIds.blockKey(id)),
                type,
                () -> BLOCK_ENTITY_TYPES.get(type)
        );
        BLOCKS.put(type, Registry.register(BuiltInRegistries.BLOCK, id, block));
        ModBlocks.add(block);
        MiteRegistration.deferItem(() -> registerItem(type));
    }

    private static void registerBlockEntity(StrongboxType type) {
        Identifier id = MiteIds.id(type.registryId);
        BlockEntityType<StrongboxBlockEntity> type1 = new BlockEntityType<>(
                (pos, state) -> new StrongboxBlockEntity(type, pos, state),
                Set.of(BLOCKS.get(type)));
        BLOCK_ENTITY_TYPES.put(type, Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, type1));
    }

    private static void registerItem(StrongboxType type) {
        Identifier id = MiteIds.id(type.registryId);
        BlockItem item = new BlockItem(BLOCKS.get(type), new Item.Properties()
                .setId(MiteIds.itemKey(id))
                .useBlockDescriptionPrefix());
        Registry.register(BuiltInRegistries.ITEM, id, item);
        ITEMS.put(type, item);
        ModItems.add(ItemCategory.BLOCK, item);
    }

    public static Map<StrongboxType, Block> blocks() {
        return Collections.unmodifiableMap(BLOCKS);
    }
}
