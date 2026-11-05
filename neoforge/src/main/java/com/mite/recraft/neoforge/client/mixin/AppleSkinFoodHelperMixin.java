package com.mite.recraft.neoforge.client.mixin;

import com.mite.recraft.content.item.food.EdibleOverride;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 使 AppleSkin 将 {@link EdibleOverride} 物品识别为食物。
 * NeoForge 版签名为 {@code isFood(ItemStack, Player)}；Fabric 版只有一个参数，实现在 fabric 模块。
 */
@Pseudo
@Mixin(targets = "squeek.appleskin.helpers.FoodHelper", remap = false)
public class AppleSkinFoodHelperMixin {

    @Inject(method = "isFood", at = @At("RETURN"), cancellable = true, remap = false)
    private static void mite$isFood(ItemStack itemStack, Player player, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            return;
        }
        if (EdibleOverride.fromItem(itemStack.getItem()) != null) {
            cir.setReturnValue(true);
        }
    }
}
