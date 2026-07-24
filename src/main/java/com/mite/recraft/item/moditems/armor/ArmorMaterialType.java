package com.mite.recraft.item.moditems.armor;

import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import com.mite.recraft.item.quality.Quality;

/**
 * 护甲材料枚举 —— 定义每种材料的护甲值 (protection) 和品质上限。
 *
 * 护甲值来源：原版 MITE 1.6.4 ItemArmor.getMaterialProtection()
 *   copper       = 7 (chain -2 → 5)
 *   silver       = 7 (chain -2 → 5)
 *   gold         = 6 (chain -2 → 4)
 *   rusted_iron  = 6 (chain -2 → 4)
 *   iron         = 8 (chain -2 → 6)
 *   ancient_metal= 8 (chain -2 → 6)
 *   mithril      = 9 (chain -2 → 7)
 *   adamantium   = 10 (chain -2 → 8)
 *
 * 耐久公式: numComponents × material.durability × (chainmail ? 1.0 : 2.0)
 * 保护值公式: (numComponents × protection) / 24.0
 */
public enum ArmorMaterialType {
    COPPER(ModToolMaterial.COPPER, 7),
    SILVER(ModToolMaterial.SILVER, 7),
    GOLD(ModToolMaterial.GOLD, 6),
    RUSTED_IRON(ModToolMaterial.RUSTED_IRON, 6),
    IRON(ModToolMaterial.IRON, 8),
    ANCIENT_METAL(ModToolMaterial.ANCIENT_METAL, 8),
    MITHRIL(ModToolMaterial.MITHRIL, 9),
    ADAMANTIUM(ModToolMaterial.ADAMANTIUM, 10);

    private final ModToolMaterial toolMaterial;
    private final int protection;  // base protection before chainmail penalty

    ArmorMaterialType(ModToolMaterial toolMaterial, int protection) {
        this.toolMaterial = toolMaterial;
        this.protection = protection;
    }

    public ModToolMaterial toolMaterial() { return toolMaterial; }
    public String materialName() { return toolMaterial.getName(); }
    public float durabilityCoefficient() { return toolMaterial.getDurabilityCoefficient(); }
    public Quality maxQuality() { return toolMaterial.getMaxQuality(); }
    public String repairTagName() { return toolMaterial.getRepairTagName(); }

    /**
     * 基础护甲值（未减锁链惩罚）。
     */
    public int baseProtection() { return protection; }

    /**
     * 实际护甲值（锁链护甲 -2）。
     */
    public int protection(boolean chainmail) {
        return chainmail ? protection - 2 : protection;
    }

    /**
     * 附魔性，来自旧版 MITE EnumEquipmentMaterial.enchantability。
     */
    public int enchantability() {
        return switch (this) {
            case COPPER -> 30;
            case SILVER -> 30;
            case GOLD -> 50;
            case RUSTED_IRON -> 1;   // MC 26.2 要求 enchantable > 0，旧 MITE 为 0
            case IRON -> 30;
            case ANCIENT_METAL -> 40;
            case MITHRIL -> 100;
            case ADAMANTIUM -> 40;
        };
    }

    /**
     * 实际耐久值: numComponents × durabilityCoefficient × (chainmail ? 1.0 : 2.0)
     */
    public int durability(int numComponents, boolean chainmail) {
        float base = numComponents * durabilityCoefficient();
        if (!chainmail) base *= 2.0F;
        return (int) base;
    }

    /**
     * 最终护甲减伤倍率: (numComponents × protection) / 24.0
     */
    public float multipliedProtection(int numComponents, boolean chainmail) {
        return (float) (numComponents * protection(chainmail)) / 24.0F;
    }
}
