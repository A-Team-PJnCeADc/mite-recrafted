package com.mite.recraft.item.moditems.armor;

/**
 * 护甲部件枚举 —— 定义每种护甲槽位的 numComponents 和 armorType。
 *
 * MITE 原版数值：
 *   HELMET     = 5 components, type 0, texture "helmet"
 *   CHESTPLATE = 8 components, type 1, texture "chestplate"
 *   LEGGINGS   = 7 components, type 2, texture "leggings"
 *   BOOTS      = 4 components, type 3, texture "boots"
 *
 * 命名规则（匹配 textures/item/armor/ 下的纹理）:
 *   普通护甲: {material}_{piece}          e.g. copper_helmet
 *   锁链护甲: {material}_chainmail_{piece}  e.g. copper_chainmail_helmet
 */
public enum ArmorPiece {
    HELMET("helmet", 5, 0),
    CHESTPLATE("chestplate", 8, 1),
    LEGGINGS("leggings", 7, 2),
    BOOTS("boots", 4, 3);

    private final String pieceName;     // "helmet", "chestplate", "leggings", "boots"
    private final int numComponents;    // 用于耐久计算: numComponents × material.durability
    private final int armorType;        // 0-3, 对应原版 armor type

    ArmorPiece(String pieceName, int numComponents, int armorType) {
        this.pieceName = pieceName;
        this.numComponents = numComponents;
        this.armorType = armorType;
    }

    public String pieceName() { return pieceName; }
    public int numComponents() { return numComponents; }
    public int armorType() { return armorType; }

    /**
     * 构建物品 ID: {materialName}_{pieceName}
     * e.g. "copper_helmet"
     */
    public String itemId(String materialName, boolean chainmail) {
        if (chainmail) return materialName + "_chainmail_" + pieceName;
        return materialName + "_" + pieceName;
    }

    /**
     * 构建纹理路径: item/armor/{materialName}_{pieceName}
     * e.g. "item/armor/copper_helmet"
     */
    public String texturePath(String materialName, boolean chainmail) {
        if (chainmail) return "item/armor/" + materialName + "_chainmail_" + pieceName;
        return "item/armor/" + materialName + "_" + pieceName;
    }
}
