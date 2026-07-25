package com.mite.recraft.enchantment;

/**
 * MITE 迅捷 (Quickness) — 提高拉弓速度。
 * <p>
 * 每级 20% 蓄力加速，Lv5 = 2x = 约 0.5 秒满拉。
 * <p>
 * 参考 MITE 1.6.4：{@code getTicksForMaxPull = 20 - getEnchantmentLevelFractionOfInteger(quickness, stack, 10)}
 * （ItemBow.java:49）。
 */
public final class Quickness {

    private Quickness() {
    }

    /** Quickness 拉弓速度倍率：1 + level × 0.2 */
    public static float multiplier(int level) {
        return 1.0f + level * 0.2f;
    }
}
