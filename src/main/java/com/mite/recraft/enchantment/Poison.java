package com.mite.recraft.enchantment;

import com.mite.recraft.enchantment.effects.SilentApplyMobEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.ApplyMobEffect;

import java.util.List;

/**
 * MITE 淬毒 (Poison) — 使被箭射中的生物中毒。
 * <p>
 * 参考 MITE 1.6.4（EntityArrow.java:610-612）：
 * <pre>
 *   duration = 160 + Math.round(level/5 * 240) ticks
 *           = 8 + level × 2.4 秒
 *   amplifier = 0（毒 I 仅）
 * </pre>
 * Lv1=10.4s, Lv2=12.8s, Lv3=15.2s, Lv4=17.6s, Lv5=20.0s。
 * 效果由 {@code POST_ATTACK} + {@link ApplyMobEffect} 数据组件驱动。
 */
public final class Poison {

    private Poison() {
    }

    /**
     * 向传入的 Builder 添加 POST_ATTACK 毒药效果。
     *
     * @param builder      由 {@link ModEnchantments#definition} 初始化后的 Builder
     * @param poisonHolder 毒药效果 Holder（{@link MobEffects#POISON}）
     * @return 同一 Builder
     */
    public static Enchantment.Builder addEffect(Enchantment.Builder builder, Holder<MobEffect> poisonHolder) {
        return builder.withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER,
                EnchantmentTarget.VICTIM,
                new SilentApplyMobEffect(
                        HolderSet.direct(List.of(poisonHolder)),
                        LevelBasedValue.perLevel(10.4f, 2.4f),   // minDuration (秒)
                        LevelBasedValue.perLevel(10.4f, 2.4f),   // maxDuration
                        LevelBasedValue.constant(0.0f),           // minAmplifier (毒 I)
                        LevelBasedValue.constant(0.0f)            // maxAmplifier
                )
        );
    }
}
