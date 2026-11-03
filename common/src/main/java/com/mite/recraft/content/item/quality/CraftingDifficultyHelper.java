package com.mite.recraft.content.item.quality;

import com.mite.recraft.content.item.quality.Quality;

/**
 * 制作难度与品质辅助计算。
 *
 * MITE 原版公式:
 *   quality_adjusted = base_difficulty × 2^(quality_ordinal − average_ordinal)
 *   xp_cost = round(quality_adjusted / 5.0)
 */
public final class CraftingDifficultyHelper {

    private CraftingDifficultyHelper() {}

    private static final int AVERAGE_ORDINAL = Quality.AVERAGE.ordinal();

    /**
     * 品质修正后的制作难度。
     * 公式: base × 2^(quality − average)，低于 AVERAGE 不翻倍。
     */
    public static float getQualityAdjustedDifficulty(float baseDifficulty, Quality quality) {
        if (quality == null) return baseDifficulty;
        int levelsAbove = quality.ordinal() - AVERAGE_ORDINAL;
        float adjusted = baseDifficulty;
        for (int i = 0; i < levelsAbove; i++) {
            adjusted *= 2.0F;
        }
        return adjusted;
    }

    /**
     * XP 消耗 = round(quality_adjusted_difficulty / 5.0)
     */
    public static int getXpCost(float qualityAdjustedDifficulty) {
        return Math.round(qualityAdjustedDifficulty / 5.0F);
    }

    /**
     * 制作耗时 (tick)，用于 calculateCraftingPeriod。
     * MITE 原版公式: clamp(difficulty × 70, 25, 200)
     */
    public static int getBaseCraftingPeriod(float qualityAdjustedDifficulty) {
        int raw = Math.round(qualityAdjustedDifficulty * 70.0F);
        return Math.clamp(raw, 25, 200);
    }
}
