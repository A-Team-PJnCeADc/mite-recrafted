package com.mite.recraft.registry;

import com.mite.recraft.api.ItemCategory;
import com.mite.recraft.api.MiteIds;
import com.mite.recraft.chest.ChestMenu;
import com.mite.recraft.chest.ChestUpgrade;
import com.mite.recraft.chest.ChestUpgradeItem;
import com.mite.recraft.chest.ChestVariant;
import com.mite.recraft.chest.CrystalChestStorage;
import com.mite.recraft.chest.VariantChestBlock;
import com.mite.recraft.chest.VariantChestStorage;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 箱子内容注册：方块、方块实体类型、菜单、BlockItem 与升级物品。
 *
 * <p>顺序固定为方块 -> 方块实体类型 -> 菜单 -> 物品。规格对象不持有注册结果，
 * 映射统一保存在本类。</p>
 */
public final class ModChests {

    private static final Map<ChestVariant, Block> BLOCKS = new LinkedHashMap<>();
    private static final Map<ChestVariant, BlockItem> ITEMS = new LinkedHashMap<>();
    private static final Map<ChestVariant, BlockEntityType<? extends VariantChestStorage>> STORAGE_TYPES =
            new LinkedHashMap<>();
    private static final Map<ChestVariant, MenuType<ChestMenu>> MENU_TYPES = new LinkedHashMap<>();
    private static final Map<ChestUpgrade, Item> UPGRADE_ITEMS = new LinkedHashMap<>();

    private ModChests() {
    }

    public static void register() {
        registerBlocks();
        registerStorageTypes();
        registerMenus();
        registerBlockItems();
        registerUpgradeItems();
    }

    private static void registerBlocks() {
        for (ChestVariant variant : ChestVariant.REGISTERED) {
            Identifier id = variant.blockId();
            Block block = new VariantChestBlock(variant, variant.blockProperties().setId(MiteIds.blockKey(id)));
            Registry.register(BuiltInRegistries.BLOCK, id, block);
            BLOCKS.put(variant, block);
        }
    }

    private static void registerStorageTypes() {
        for (ChestVariant variant : ChestVariant.REGISTERED) {
            Identifier id = variant.blockId();
            Block block = BLOCKS.get(variant);
            BlockEntityType<? extends VariantChestStorage> type = FabricBlockEntityTypeBuilder
                    .<VariantChestStorage>create(
                            (pos, state) -> createStorage(variant, pos, state),
                            block)
                    .build();
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, type);
            STORAGE_TYPES.put(variant, type);
        }
    }

    private static void registerMenus() {
        for (ChestVariant variant : ChestVariant.REGISTERED) {
            Identifier id = variant.blockId();
            MenuType<ChestMenu> menuType = new MenuType<>(
                    (syncId, inventory) -> new ChestMenu(menuType(variant), variant, syncId, inventory,
                            ChestMenu.clientStorage(variant)),
                    FeatureFlags.VANILLA_SET);
            Registry.register(BuiltInRegistries.MENU, id, menuType);
            MENU_TYPES.put(variant, menuType);
        }
    }

    private static void registerBlockItems() {
        for (ChestVariant variant : ChestVariant.REGISTERED) {
            Identifier id = variant.blockId();
            Item.Properties properties = new Item.Properties()
                    .setId(MiteIds.itemKey(id))
                    .useBlockDescriptionPrefix();
            if (variant == ChestVariant.NETHERITE) {
                properties.fireResistant();
            }
            BlockItem item = new BlockItem(BLOCKS.get(variant), properties);
            Registry.register(BuiltInRegistries.ITEM, id, item);
            ITEMS.put(variant, item);
            ModItems.add(ItemCategory.CHEST, item);
        }
    }

    private static void registerUpgradeItems() {
        for (ChestUpgrade upgrade : ChestUpgrade.ALL) {
            Identifier id = MiteIds.id(upgrade.id());
            Item item = new ChestUpgradeItem(upgrade, new Item.Properties()
                    .setId(MiteIds.itemKey(id))
                    .component(DataComponents.LORE, new ItemLore(List.of(), List.of(
                            Component.translatable(upgrade.tooltipKey()).withStyle(ChatFormatting.GREEN)))));
            Registry.register(BuiltInRegistries.ITEM, id, item);
            UPGRADE_ITEMS.put(upgrade, item);
            ModItems.add(ItemCategory.CHEST, item);
        }
    }

    /** 方块实体构造分发：水晶箱使用独立实现。 */
    public static VariantChestStorage createStorage(ChestVariant variant, BlockPos pos, BlockState state) {
        if (variant == ChestVariant.CRYSTAL) {
            return new CrystalChestStorage(pos, state);
        }
        return new VariantChestStorage(variant, pos, state);
    }

    public static Block block(ChestVariant variant) {
        Block block = BLOCKS.get(variant);
        return block != null ? block : net.minecraft.world.level.block.Blocks.CHEST;
    }

    public static BlockItem item(ChestVariant variant) {
        return ITEMS.get(variant);
    }

    public static Item upgradeItem(ChestUpgrade upgrade) {
        return UPGRADE_ITEMS.get(upgrade);
    }

    public static BlockEntityType<? extends VariantChestStorage> storageType(ChestVariant variant) {
        BlockEntityType<? extends VariantChestStorage> type = STORAGE_TYPES.get(variant);
        return type != null ? type : null;
    }

    public static MenuType<ChestMenu> menuType(ChestVariant variant) {
        return MENU_TYPES.get(variant);
    }

    public static Map<ChestVariant, Block> blocks() {
        return Collections.unmodifiableMap(BLOCKS);
    }

    public static Map<ChestVariant, BlockItem> items() {
        return Collections.unmodifiableMap(ITEMS);
    }

    public static Map<ChestVariant, MenuType<ChestMenu>> menuTypes() {
        return Collections.unmodifiableMap(MENU_TYPES);
    }

    /** 判断方块实体是否为箱子存储，供渲染与开启者计数使用。 */
    public static boolean isChestStorage(BlockEntity blockEntity) {
        return blockEntity instanceof VariantChestStorage;
    }
}
