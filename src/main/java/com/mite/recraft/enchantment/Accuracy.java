package com.mite.recraft.enchantment;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.MultiplyValue;

/**
 * MITE 精准 (Accuracy) — 提高箭矢起始精准度。
 * <p>
 * 每级减少 20% 散射，即 Lv1=0.8x、Lv2=0.6x、…、Lv5=0.0x（零散射）。
 * 通过 {@code PROJECTILE_SPREAD} 数据组件驱动，依赖 {@code AccuracyMixin} 使弓调用
 * {@link EnchantmentHelper#processProjectileSpread}。
 */
public final class Accuracy {

    private Accuracy() {
    }

    /**
     * 向传入的 Builder 添加 PROJECTILE_SPREAD 效果（multiply）。
     *
     * @param builder 由 {@link ModEnchantments#definition} 初始化后的 Builder
     * @return 同一 Builder（便于链式调用）
     */
    public static Enchantment.Builder addEffect(Enchantment.Builder builder) {
        return builder.withEffect(
                EnchantmentEffectComponents.PROJECTILE_SPREAD,
                new MultiplyValue(LevelBasedValue.perLevel(0.8f, -0.2f))
        );
    }
}
