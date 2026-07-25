package com.mite.recraft.enchantment;

/**
 * MITE 速度 (Speed) — 提高穿戴者的移动速度。
 * <p>
 * 每级提供 5% 移速加成，最高 V 级（Lv1=+5%, Lv5=+25%）。
 */
public final class Speed {

    private static final float BOOST_PER_LEVEL = 0.05f;

    private Speed() {
    }

    /**
     * 计算速度附魔的移速倍率。
     *
     * @param level 附魔等级（1～5）
     * @return 移速倍率（1.0 = 无加成）
     */
    public static float getMultiplier(int level) {
        if (level <= 0) return 1.0f;
        return 1.0f + BOOST_PER_LEVEL * level;
    }
}
