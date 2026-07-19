package com.mite.recraft.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 修改原版物品的堆叠数。
 */
@Mixin(Item.class)
public class ItemMaxStackMixin {

    @Inject(method = "getDefaultMaxStackSize", at = @At("HEAD"), cancellable = true)
    private void onGetMaxStack(CallbackInfoReturnable<Integer> cir) {
        Item self = (Item) (Object) this;
        if (self == Items.POTATO || self == Items.CARROT) {
            cir.setReturnValue(64);
        }
    }
}
