package com.mite.recraft.mixin;

import com.mite.recraft.item.moditems.food.CommonFood;
import com.mite.recraft.item.moditems.food.FoodType;
import com.mite.recraft.item.moditems.food.NutritionSystem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 吃原版食物时覆盖为 CommonFood 枚举中定义的 MITE 数值。
 */
@Mixin(FoodProperties.class)
public class VanillaFoodOverrideMixin {

    @Inject(method = "onConsume", at = @At("TAIL"))
    private void onConsume(Level level, net.minecraft.world.entity.LivingEntity entity,
                           ItemStack stack, Consumable consumable, CallbackInfo ci) {
        if (!(entity instanceof Player player)) return;
        if (!(player instanceof ServerPlayer sp)) return;

        FoodType miteFood = null;
        if (stack.is(Items.POTATO)) miteFood = CommonFood.POTATO;
        else if (stack.is(Items.CARROT)) miteFood = CommonFood.CARROT;

        if (miteFood == null) return;

        FoodProperties fp = stack.get(DataComponents.FOOD);
        if (fp == null) return;

        // 差值修正：原版已加的值 → 调整到 MITE 目标值
        int targetHunger = miteFood.hunger();
        // MC 26.2: actualSat = hunger × satMod × 2
        float targetSat = miteFood.saturationModifier() * targetHunger * 2;
        adjustFood(player, targetHunger - fp.nutrition(), targetSat - fp.saturation());
        NutritionSystem.onEat(sp, miteFood.nutrition(), miteFood.protein(),
                miteFood.phytonutrients(), miteFood.essentialFats(), miteFood.sugar());
        NutritionSystem.sendSyncPacket(sp);
    }

    private static void adjustFood(Player player, int deltaFood, float deltaSat) {
        var fd = player.getFoodData();
        int newFood = Math.clamp(fd.getFoodLevel() + deltaFood, 0, 20);
        float newSat = Math.clamp(fd.getSaturationLevel() + deltaSat, 0, newFood);
        fd.setFoodLevel(newFood);
        fd.setSaturation(newSat);
    }
}
