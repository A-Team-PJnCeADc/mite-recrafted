package com.mite.recraft.enchantment;

/**
 * MITE 灵活移动 (Free Action) — 削弱缓慢效果和蜘蛛网限制。
 * <p>
 * 每级提供 20% 缓慢抗性，最高 IV 级（Lv1=20%, Lv2=40%, Lv3=60%, Lv4=80%）。
 * 对缓慢药水效果和蜘蛛网的减速均有抵消作用。
 */
public final class FreeAction {

    private static final float RESISTANCE_PER_LEVEL = 0.20f;

    private FreeAction() {
    }

    /**
     * 根据 Free Action 等级计算减免后的缓慢放大器。
     *
     * @param originalAmplifier 原始缓慢效果放大器（0 = 缓慢 I）
     * @param level             Free Action 等级（1～4）
     * @return 减免后的有效放大器
     */
    public static int getReducedAmplifier(int originalAmplifier, int level) {
        if (level <= 0) return originalAmplifier;
        // 每级抵消 20% 缓慢效果
        float reduction = RESISTANCE_PER_LEVEL * level;
        // 有效放大器 = 原始放大器 × (1 - 减免比例)
        int reduced = (int) Math.round(originalAmplifier * (1.0f - reduction));
        return Math.min(reduced, originalAmplifier);
    }
}
