package com.mite.recraft.chest;

import com.mite.recraft.MiteRecrafted;

import java.util.ArrayList;
import java.util.List;

/**
 * 升级关系：把来源等级的箱子替换为目标等级。
 *
 * <p>{@code from == null} 表示来源是原版箱子。目标集合是数据表，不按容量推导，
 * 因为同容量的规格之间也存在升级关系（绿宝石箱 -> 水晶箱/黑曜石箱）。</p>
 */
public record ChestUpgrade(String id, ChestVariant from, ChestVariant to) {

    public static final List<ChestUpgrade> ALL = build();

    private static List<ChestUpgrade> build() {
        List<ChestUpgrade> upgrades = new ArrayList<>();

        // 原版箱子 -> 各等级
        addAll(upgrades, null, ChestVariant.COPPER, ChestVariant.IRON, ChestVariant.GOLD,
                ChestVariant.DIAMOND, ChestVariant.EMERALD, ChestVariant.CRYSTAL,
                ChestVariant.OBSIDIAN, ChestVariant.CHRISTMAS);

        addAll(upgrades, ChestVariant.COPPER, ChestVariant.IRON, ChestVariant.GOLD,
                ChestVariant.DIAMOND, ChestVariant.EMERALD, ChestVariant.CRYSTAL, ChestVariant.OBSIDIAN);
        addAll(upgrades, ChestVariant.IRON, ChestVariant.GOLD, ChestVariant.DIAMOND,
                ChestVariant.EMERALD, ChestVariant.CRYSTAL, ChestVariant.OBSIDIAN);
        addAll(upgrades, ChestVariant.GOLD, ChestVariant.DIAMOND, ChestVariant.EMERALD,
                ChestVariant.CRYSTAL, ChestVariant.OBSIDIAN);
        addAll(upgrades, ChestVariant.DIAMOND, ChestVariant.CRYSTAL, ChestVariant.OBSIDIAN);
        addAll(upgrades, ChestVariant.EMERALD, ChestVariant.CRYSTAL, ChestVariant.OBSIDIAN);

        return List.copyOf(upgrades);
    }

    private static void addAll(List<ChestUpgrade> upgrades, ChestVariant from, ChestVariant... targets) {
        for (ChestVariant target : targets) {
            String id = (from == null ? "wood" : material(from)) + "_" + material(target) + "_upgrade";
            upgrades.add(new ChestUpgrade(id, from, target));
        }
    }

    private static String material(ChestVariant variant) {
        String id = variant.id();
        return id.endsWith("_chest") ? id.substring(0, id.length() - "_chest".length()) : id;
    }

    public String tooltipKey() {
        return "item." + MiteRecrafted.MOD_ID + "." + this.id + ".tooltip";
    }
}
