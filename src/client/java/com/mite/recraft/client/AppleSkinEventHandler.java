package com.mite.recraft.client;

import com.mite.recraft.item.moditems.food.CommonFood;
import com.mite.recraft.item.moditems.food.EdibleOverride;
import com.mite.recraft.item.moditems.food.FoodType;
import com.mite.recraft.item.moditems.food.ModFoodItem;
import net.minecraft.world.food.FoodProperties;
import squeek.appleskin.api.AppleSkinApi;
import squeek.appleskin.api.event.FoodValuesEvent;

/**
 * AppleSkin 事件处理器 — 使预览值匹配实际 MITE 数值。
 */
public class AppleSkinEventHandler implements AppleSkinApi {

    @Override
    public void registerEvents() {
        FoodValuesEvent.EVENT.register(event -> {
            var stack = event.itemStack;

            // MITE 自定义食物
            if (stack.getItem() instanceof ModFoodItem mfi) {
                var ft = mfi.getFoodType();
                var fp = buildFoodComponent(ft);
                event.defaultFoodComponent = fp;
                event.modifiedFoodComponent = fp;
                return;
            }

            // 原版食物 MITE 重制（定义在 CommonFood 中）
            FoodType miteFood = CommonFood.fromItem(stack.getItem());

            if (miteFood != null) {
                var fp = buildFoodComponent(miteFood);
                event.defaultFoodComponent = fp;
                event.modifiedFoodComponent = fp;
                return;
            }

            // 原版不可食物品 EdibleOverride
            FoodType edibleOverride = EdibleOverride.fromItem(stack.getItem());
            if (edibleOverride != null) {
                var fp = buildFoodComponent(edibleOverride);
                event.defaultFoodComponent = fp;
                event.modifiedFoodComponent = fp;
            }
        });
    }

    private static FoodProperties buildFoodComponent(FoodType ft) {
        return new FoodProperties.Builder()
                .nutrition(ft.hunger())
                .saturationModifier(ft.saturationModifier())
                .build();
    }
}
