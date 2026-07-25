package com.mite.recraft.enchantment;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;

/**
 * MITE 杀害 (Slaying) — 提高战斧的攻击力。
 * <p>
 * 每级增加 0.5 + 1.0 × (level - 1) 点伤害，
 * 相当于可附魔在战斧上的锋利。
 * 通过 {@code DAMAGE} 数据组件驱动。
 */
public final class Slaying {

    private Slaying() {
    }

    /**
     * 向传入的 Builder 添加 DAMAGE 效果（add）。
     *
     * @param builder 由 {@link ModEnchantments#definition} 初始化后的 Builder
     * @return 同一 Builder
     */
    public static Enchantment.Builder addEffect(Enchantment.Builder builder) {
        return builder.withEffect(
                EnchantmentEffectComponents.DAMAGE,
                new AddValue(LevelBasedValue.perLevel(1.0f, 0.5f))
        );
    }
}
