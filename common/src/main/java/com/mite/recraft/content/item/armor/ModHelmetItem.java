package com.mite.recraft.content.item.armor;

/**
 * 头盔: armorType=0, numComponents=5
 */
public class ModHelmetItem extends ModArmorItem {
    public ModHelmetItem(ArmorMaterialType material, boolean chainmail, Properties properties) {
        super(ArmorPiece.HELMET, material, chainmail, properties);
    }
}
