package com.mite.recraft.enchantment;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * MITE 收获 (Harvesting) — 使用工具收获农作物时增加掉落。
 * <p>
 * 参考 MITE 1.6.4（BlockCrops.java:480）：
 * {@code harvesting_enchantment = getEnchantmentLevelFraction(harvesting, stack) × 0.5F}
 * {@code num_drops = baseYield × (1.0 + harvesting_enchantment)}
 * <p>
 * 区别对待工具类型：
 * <ul>
 *   <li>镰刀 (Scythe) + 小麦：概率使小麦数量翻倍</li>
 *   <li>锄 (Hoe) / 鹤嘴锄 (Mattock) + 其他作物：提高掉落基数</li>
 * </ul>
 */
public final class Harvesting {

    /** 最大等级 */
    public static final int MAX_LEVEL = 5;

    private Harvesting() {
    }

    /**
     * 计算收获附魔的额外因子。
     * MITE 公式：{@code level / MAX_LEVEL × 0.5}
     */
    public static float getMultiplier(int level) {
        if (level <= 0) return 0.0f;
        return (float) level / MAX_LEVEL * 0.5f;
    }

    /**
     * 从物品栈中提取作物 ID。
     */
    public static String getCropId(ItemStack stack) {
        if (stack.is(Items.WHEAT)) return "wheat";
        if (stack.is(Items.CARROT)) return "carrot";
        if (stack.is(Items.POTATO)) return "potato";
        if (stack.is(Items.BEETROOT)) return "beetroot";
        if (stack.is(Items.NETHER_WART)) return "nether_wart";
        if (stack.is(Items.COCOA_BEANS)) return "cocoa";
        return "";
    }
}
