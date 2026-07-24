package com.mite.recraft.item.moditems.armor;

/**
 * 胸甲: armorType=1, numComponents=8
 */
public class ModChestplateItem extends ModArmorItem {
    public ModChestplateItem(ArmorMaterialType material, boolean chainmail, Properties properties) {
        super(ArmorPiece.CHESTPLATE, material, chainmail, properties);
    }
}
