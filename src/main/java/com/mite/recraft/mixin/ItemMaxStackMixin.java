package com.mite.recraft.mixin;

import com.mite.recraft.item.moditems.food.CommonFood;
import com.mite.recraft.item.moditems.food.EdibleOverride;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 修改原版物品的堆叠数 — 数值来自 FoodType。
 */
@Mixin(Item.class)
public class ItemMaxStackMixin {

    @Inject(method = "getDefaultMaxStackSize", at = @At("HEAD"), cancellable = true)
    private void onGetMaxStack(CallbackInfoReturnable<Integer> cir) {
        Item self = (Item) (Object) this;
        CommonFood food = CommonFood.fromItem(self);
        if (food != null) {
            cir.setReturnValue(food.maxStackSize());
            return;
        }
        EdibleOverride edible = EdibleOverride.fromItem(self);
        if (edible != null) {
            cir.setReturnValue(edible.maxStackSize());
        }
    }
}
