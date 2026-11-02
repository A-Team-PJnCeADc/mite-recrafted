package com.mite.recraft.api;

import com.mite.recraft.content.block.furnace.MiteFuels;
import com.mite.recraft.content.item.food.FoodType;
import com.mite.recraft.content.item.food.ModFoodItems;
import com.mite.recraft.content.item.tool.ModToolMaterial;
import com.mite.recraft.content.item.tool.ToolType;
import com.mite.recraft.registry.ModBlocks;
import com.mite.recraft.registry.ModItems;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * 注册入口。注册的方块与物品同时登记到创造模式物品栏的对应分类。
 *
 * <p>调用时机不早于 {@link com.mite.recraft.MiteRecrafted#onInitialize()}；物品栏内容在
 * 客户端打开时读取，注册完成后不需要额外刷新。</p>
 */
public final class MiteRegistration {

    private MiteRegistration() {
    }

    public static Item registerItem(ItemCategory category, String path, Function<Item.Properties, Item> factory) {
        return registerItem(category, MiteIds.id(path), factory);
    }

    /**
     * 注册物品并加入创造模式物品栏。
     *
     * @param id      完整资源 ID，决定注册键与默认模型路径
     * @param factory 接收已写入注册键的 {@link Item.Properties}
     */
    public static Item registerItem(ItemCategory category, Identifier id, Function<Item.Properties, Item> factory) {
        ResourceKey<Item> key = MiteIds.itemKey(id);
        Item item = factory.apply(new Item.Properties().setId(key));
        ModItems.add(category, item);
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    /** 注册无特殊行为的物品，指定最大堆叠数。 */
    public static Item registerStackedItem(ItemCategory category, String path, int maxStackSize) {
        return registerItem(category, path, props -> new Item(props.stacksTo(maxStackSize)));
    }

    /**
     * 注册方块与同名 BlockItem，二者共用路径。
     *
     * @param itemProperties 追加 BlockItem 的属性，返回入参本身或改写后的副本
     */
    public static Block registerBlock(Identifier id, Block block, UnaryOperator<Item.Properties> itemProperties) {
        ResourceKey<Block> blockKey = MiteIds.blockKey(id);
        ResourceKey<Item> itemKey = MiteIds.itemKey(id);
        Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
        ModBlocks.add(block);

        Item.Properties properties = itemProperties.apply(new Item.Properties()
                .setId(itemKey)
                .useBlockDescriptionPrefix());
        BlockItem item = new BlockItem(block, properties);
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        ModItems.add(ItemCategory.BLOCK, item);
        return block;
    }

    public static Block registerBlock(Identifier id, Block block, int maxStackSize) {
        return registerBlock(id, block, props -> props.stacksTo(maxStackSize));
    }

    /** 由工厂构造方块并注册，工厂接收已写入注册键的 {@link BlockBehaviour.Properties}。 */
    public static Block registerBlock(Identifier id, Function<BlockBehaviour.Properties, Block> factory,
                                      UnaryOperator<Item.Properties> itemProperties) {
        ResourceKey<Block> blockKey = MiteIds.blockKey(id);
        Block block = factory.apply(BlockBehaviour.Properties.of().setId(blockKey));
        return registerBlock(id, block, itemProperties);
    }

    /** 按 {@link FoodType} 注册食物；营养值、容器返还与 AppleSkin 预览一并生效。 */
    public static Item registerFood(FoodType foodType) {
        return ModFoodItems.register(foodType);
    }

    /**
     * 按 {@link ToolType} 注册工具，写入 MITE 工具组件（部件数、材料等级、修理标签）。
     *
     * @param id 完整资源 ID，例如 {@code MiteIds.id("mithril_hammer")}
     */
    public static Item registerTool(Identifier id, ToolType type, ModToolMaterial material,
                                    float attackDamage, float attackSpeed) {
        ResourceKey<Item> key = MiteIds.itemKey(id);
        Item item = type.create(material, key, attackDamage, attackSpeed);
        ModItems.add(ItemCategory.TOOL, item);
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    /** 登记燃料：burnTicks 为燃烧时长（tick），heat 取值 1~4。 */
    public static void registerFuel(Item item, int burnTicks, int heat) {
        MiteFuels.register(item, burnTicks, heat);
    }
}
