package com.mite.recraft.client.mixin;

import com.mite.recraft.enchantment.ModEnchantments;
import com.mite.recraft.enchantment.Quickness;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * 使第一人称弓手部拉弓位置同步 Quickness 加速。
 * <p>
 * 手部位置渲染内联了 power 公式。通过修改蓄力 ticks（local 16, 第 2 次 fstore）
 * 实现平滑加速：初始无加速，随蓄力进度渐进到满倍率，避免突变。
 */
@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @ModifyVariable(method = "submitArmWithItem",
            at = @At(value = "STORE", ordinal = 1),
            index = 16)
    private float mite$scaleBowCharge(float charge,
                                      AbstractClientPlayer player, float partialTick, float pitch,
                                      InteractionHand hand, float swingProgress, ItemStack stack,
                                      float equipProgress) {
        if (charge <= 0) return charge;
        int ql = EnchantmentHelper.getItemEnchantmentLevel(
                player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(ModEnchantments.QUICKNESS.key()),
                stack
        );
        if (ql <= 0) return charge;

        float multiplier = Quickness.multiplier(ql);  // Lv5 = 2.0
        // 进度因子 t = charge / 20，范围 0→1
        float t = Math.min(charge / 20.0f, 1.0f);
        // 加速系数：从 1.0 渐进到 multiplier
        // 拉弓初期几乎无加速，越接近满蓄加速越明显
        float accel = 1.0f + (multiplier - 1.0f) * t * t;
        return charge * accel;
    }
}
