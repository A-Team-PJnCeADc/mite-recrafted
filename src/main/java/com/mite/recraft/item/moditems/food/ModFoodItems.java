package com.mite.recraft.item.moditems.food;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.item.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

/**
 * MITE 食物物品注册 — 由 {@link FoodType} 枚举驱动创建和注册。
 *
 * <p>所有食物通过 {@link #register(FoodType)} 统一注册，自动处理：
 * <ul>
 *   <li>Item 注册（含 FoodProperties 组件）</li>
 *   <li>ModItems.FOODS 列表添加</li>
 *   <li>容器返回逻辑</li>
 * </ul>
 * </p>
 */
public class ModFoodItems {

    // 蔬菜汤
    public static final Item VEGETABLE_SOUP = register(BowlFood.VEGETABLE_SOUP);
    // 水碗
    public static final Item WATER_BOWL = register(BowlFood.WATER_BOWL);
    //牛奶碗
    public static final Item MILK_BOWL = register(BowlFood.MILK_BOWL);
    //洋葱
    public static final Item ONION = register(CommonFood.ONION);

    //奶桶
    public static final Item COPPER_MILK_BUCKET       = register(BucketFood.COPPER_MILK_BUCKET);
    public static final Item SILVER_MILK_BUCKET       = register(BucketFood.SILVER_MILK_BUCKET);
    public static final Item GOLD_MILK_BUCKET         = register(BucketFood.GOLD_MILK_BUCKET);
    public static final Item ANCIENT_METAL_MILK_BUCKET = register(BucketFood.ANCIENT_METAL_MILK_BUCKET);
    public static final Item MITHRIL_MILK_BUCKET       = register(BucketFood.MITHRIL_MILK_BUCKET);
    public static final Item ADAMANTIUM_MILK_BUCKET    = register(BucketFood.ADAMANTIUM_MILK_BUCKET);

    /**
     * 根据 {@link FoodType} 创建并注册食物物品。
     */
    private static Item register(FoodType foodType) {
        Identifier id = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, foodType.itemId());
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);

        FoodProperties.Builder foodBuilder = new FoodProperties.Builder()
                .nutrition(foodType.hunger())
                .saturationModifier(foodType.saturationModifier());
        if (foodType.clearsEffects()) {
            foodBuilder.alwaysEdible();
        }

        Item.Properties props = new Item.Properties()
                .setId(key)
                .stacksTo(foodType.maxStackSize())
                .food(foodBuilder.build());

        Item item = new ModFoodItem(foodType, props);
        ModItems.addFood(item);
        return Registry.register(BuiltInRegistries.ITEM, id, item);
    }

    public static void init() {}
}
