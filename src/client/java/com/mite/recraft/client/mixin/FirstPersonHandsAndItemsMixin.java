package com.mite.recraft.client.mixin;

import com.mite.recraft.content.enchantment.Quickness;
import com.mite.recraft.registry.ModEnchantments;

import net.minecraft.client.player.FirstPersonHandsAndItems;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 使第一人称拉弓动画的蓄力进度与 Quickness 加速一致。
 *
 * <p>渲染器用 {@code mainHandChargeDuration} 作为蓄力进度分母，缩短该值即加快动画。</p>
 */
@Mixin(FirstPersonHandsAndItems.class)
public class FirstPersonHandsAndItemsMixin {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void mite$scaleBowCharge(LocalPlayer player, float partialTick,
                                     FirstPersonHandsAndItemsRenderState state, CallbackInfo ci) {
        ItemStack mainHand = state.mainHandItem;
        if (mainHand.isEmpty() || state.mainHandChargeDuration <= 1) return;

        int level = EnchantmentHelper.getItemEnchantmentLevel(
                player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(ModEnchantments.QUICKNESS.key()),
                mainHand
        );
        if (level <= 0) return;

        state.mainHandChargeDuration = Math.max(1,
                Math.round(state.mainHandChargeDuration / Quickness.multiplier(level)));
    }
}
