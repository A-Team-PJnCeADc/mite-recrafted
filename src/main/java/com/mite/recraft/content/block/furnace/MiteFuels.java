package com.mite.recraft.content.block.furnace;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

/**
 * MITE 燃料表：物品 -> 燃烧时长（tick）与热量等级。
 *
 * <p>热量等级 1~4。未登记燃烧时长的物品回退到原版数据组件，未登记热量的物品按 1 处理。</p>
 */
public final class MiteFuels {

    /** 未登记燃烧时长，查询时回退到原版。 */
    public static final int VANILLA_BURN_TICKS = -1;

    private static final Map<Item, Integer> BURN_TICKS = new HashMap<>();
    private static final Map<Item, Integer> HEAT = new HashMap<>();

    private MiteFuels() {
    }

    /**
     * @param burnTicks 燃烧时长（tick），传 {@link #VANILLA_BURN_TICKS} 表示沿用原版
     * @param heat      热量等级 1~4
     */
    public static void register(Item item, int burnTicks, int heat) {
        if (burnTicks != VANILLA_BURN_TICKS && burnTicks <= 0) {
            throw new IllegalArgumentException("burnTicks 必须为正数或 " + VANILLA_BURN_TICKS + ": " + item);
        }
        if (heat < 1 || heat > 4) {
            throw new IllegalArgumentException("heat 必须在 1~4 之间: " + heat);
        }
        BURN_TICKS.put(item, burnTicks);
        HEAT.put(item, heat);
    }

    /** 只覆盖热量等级，燃烧时长沿用原版。 */
    public static void registerHeat(Item item, int heat) {
        register(item, VANILLA_BURN_TICKS, heat);
    }

    public static int burnTicks(ItemStack stack) {
        return BURN_TICKS.getOrDefault(stack.getItem(), VANILLA_BURN_TICKS);
    }

    public static int heat(ItemStack stack) {
        return HEAT.getOrDefault(stack.getItem(), 1);
    }

    //todo
    /** 原版物品的 MITE 热量与燃烧时长覆盖值。 */
    public static void registerDefaults() {
        registerHeat(Items.COAL, 2);
        registerHeat(Items.CHARCOAL, 1);
        registerHeat(Items.LAVA_BUCKET, 3);
        registerHeat(Items.BLAZE_ROD, 4);

        register(Items.TORCH, 800, 1);
        register(Items.PAPER, 25, 1);
        register(Items.MAP, 50, 1);
        register(Items.FILLED_MAP, 50, 1);
        register(Items.BOOK, 100, 1);
        register(Items.WRITABLE_BOOK, 100, 1);
        register(Items.WRITTEN_BOOK, 100, 1);
        register(Items.ENCHANTED_BOOK, 100, 1);
        register(Items.FISHING_ROD, 200, 1);
        register(Items.CARROT_ON_A_STICK, 200, 1);
        register(Items.WARPED_FUNGUS_ON_A_STICK, 200, 1);
    }
}
