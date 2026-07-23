package com.mite.recraft.client.mixin;

import com.mite.recraft.item.moditems.food.EdibleOverride;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 使 AppleSkin 将 {@link EdibleOverride} 物品识别为食物。
 *
 * <p>AppleSkin 的 {@code FoodHelper.isFood} 检查 ItemStack 是否有
 * FOOD + CONSUMABLE 组件。EdibleOverride 物品无此组件（避免渲染马赛克），
 * 导致 AppleSkin 不显示预览值。此 mixin 绕过该检查。
 * 食物预览值由 {@link com.mite.recraft.client.AppleSkinEventHandler}</p>
 */
@Pseudo
@Mixin(targets = "squeek.appleskin.helpers.FoodHelper", remap = false)
public class AppleSkinFoodHelperMixin {

    @Inject(method = "isFood", at = @At("RETURN"), cancellable = true, remap = false)
    private static void mite$isFood(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) return;
        if (EdibleOverride.fromItem(itemStack.getItem()) != null) {
            cir.setReturnValue(true);
        }
    }
}
