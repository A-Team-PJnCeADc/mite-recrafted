package com.mite.recraft.client.compat.appleskin;

import com.mite.recraft.content.item.food.CommonFood;
import com.mite.recraft.content.item.food.EdibleOverride;
import com.mite.recraft.content.item.food.FoodType;
import com.mite.recraft.content.item.food.ModFoodItem;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

/**
 * AppleSkin 兼容层。两端的 API 不同：Fabric 用 {@code FoodValuesEvent.EVENT} 注册监听，
 * NeoForge 的 {@code FoodValuesEvent} 是 NeoForge 事件总线事件；字段名也不同。
 * 这里只保留与平台无关的预览值计算，注册动作由各平台的 {@code AppleSkinCompatImpl} 完成。
 */
public final class AppleSkinCompat {

    private AppleSkinCompat() {
    }

    /** 预览用食物属性；不需要覆盖的物品返回 null，交由 AppleSkin 按原值显示。 */
    public static FoodProperties preview(ItemStack stack, Player player) {
        if (stack.getItem() instanceof ModFoodItem foodItem) {
            return build(foodItem.getFoodType());
        }
        FoodType miteFood = CommonFood.fromItem(stack.getItem());
        if (miteFood != null) {
            return build(miteFood);
        }
        FoodType edibleOverride = EdibleOverride.fromItem(stack.getItem());
        return edibleOverride != null ? build(edibleOverride) : null;
    }

    private static FoodProperties build(FoodType type) {
        return new FoodProperties.Builder()
                .nutrition(type.hunger())
                .saturationModifier(type.saturationModifier())
                .build();
    }
}
