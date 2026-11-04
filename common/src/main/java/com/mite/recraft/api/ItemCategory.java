package com.mite.recraft.api;

/**
 * 创造模式物品栏分类。枚举声明顺序即物品栏内的展示顺序。
 *
 * <p>{@link MiteRegistration#registerItem} 与 {@link MiteRegistration#registerStackedItem}
 * 会把注册的物品加入对应分类。</p>
 */
public enum ItemCategory {
    MATERIAL,
    TOOL,
    BLOCK,
    RECORD,
    BUCKET,
    FOOD,
    ARMOR,
    CHEST,
    SPAWN_EGG
}
