package com.mite.recraft.enchantment;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.RemoveBinomial;

/**
 * MITE 耐久 (Unbreaking) — 减缓工具/盔甲损耗。
 * <p>
 * 每级减少 15% 耐久消耗概率，即 Lv1=15%、Lv2=30%、…、Lv5=75% 概率抵消耐久消耗。
 * 原版公式为 1/(level+1)，MITE 改为线性 15%/级，且最高 V 级。
 */
public final class Unbreaking {

    private Unbreaking() {
    }

    /**
     * 向传入的 Builder 添加 ITEM_DAMAGE 效果（remove_binomial）。
     *
     * @param builder 由 {@link ModEnchantments#definition} 初始化后的 Builder
     * @return 同一 Builder（便于链式调用）
     */
    public static Enchantment.Builder addEffect(Enchantment.Builder builder) {
        return builder.withEffect(
                EnchantmentEffectComponents.ITEM_DAMAGE,
                new RemoveBinomial(LevelBasedValue.perLevel(0.15f, 0.15f))
        );
    }
}
