package com.mite.recraft.item.moditems.armor;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;

/**
 * MITE 自定义护甲基类 —— 不依赖原版 ArmorItem/ArmorMaterial 体系。
 *
 * 每个子类携带 ArmorPiece 枚举值（类型+部件数+纹理名）和 ArmorMaterialType（材料保护值）。
 * 锁链护甲通过 chainmail 标志区分。
 *
 * MC 26.2 穿戴机制: 通过 Equippable 数据组件 + ARMOR 属性修饰符实现，
 * 不实现任何 Java 接口。
 *
 * MITE 护甲公式:
 *   耐久 = numComponents × material.durability × (chainmail ? 1.0 : 2.0)
 *   保护 = (numComponents × protection) / 24.0
 *
 * 物品 ID 格式:
 *   普通: {material}_{piece}          e.g. "copper_helmet"
 *   锁链: {material}_chainmail_{piece}  e.g. "copper_chainmail_helmet"
 */
public abstract class ModArmorItem extends Item {
    protected final ArmorPiece piece;
    protected final ArmorMaterialType material;
    protected final boolean chainmail;

    protected ModArmorItem(ArmorPiece piece, ArmorMaterialType material, boolean chainmail, Properties properties) {
        super(properties);
        this.piece = piece;
        this.material = material;
        this.chainmail = chainmail;
    }

    /** 护甲部件名: "helmet", "chestplate", "leggings", "boots" */
    public ArmorPiece piece() { return piece; }
    /** 护甲材料 */
    public ArmorMaterialType material() { return material; }
    /** 是否为锁链护甲 */
    public boolean chainmail() { return chainmail; }

    /** 护甲类型 0-3 */
    public int armorType() { return piece.armorType(); }
    /** 部件数: 头盔5/胸甲8/护腿7/靴子4 */
    public int numComponents() { return piece.numComponents(); }
    /** 护甲值（含锁链惩罚） */
    public int protection() { return material.protection(chainmail); }
    /** 基础材料护甲值（未减锁链） */
    public int baseProtection() { return material.baseProtection(); }

    /** MC 26.2 装备槽位: 头盔→HEAD, 胸甲→CHEST, 护腿→LEGS, 靴子→FEET */
    public EquipmentSlot equipmentSlot() {
        return switch (piece) {
            case HELMET -> EquipmentSlot.HEAD;
            case CHESTPLATE -> EquipmentSlot.CHEST;
            case LEGGINGS -> EquipmentSlot.LEGS;
            case BOOTS -> EquipmentSlot.FEET;
        };
    }

    /**
     * MC 26.2 ARMOR 属性值 (浮点)
     * MITE 公式: (numComponents × protection) / 24.0
     * 现代 MC 每点 ARMOR = 4% 减伤，与 MITE 1:1 对应。
     *
     * 示例 (copper, protection=7):
     *   头盔: 1.5  胸甲: 2.3  护腿: 2.0  靴子: 1.2  总计 7.0 (28%)
     */
    public float armorAttributeValue() {
        float raw = (float) protection() * numComponents() / 24.0f;
        return Math.max(raw, 0.5f);
    }

    /** MITE 耐久值: numComponents × durability × (chainmail ? 1 : 2) */
    public int getMiteDurability() {
        return material.durability(piece.numComponents(), chainmail);
    }

    /** MITE 保护倍率: (numComponents × protection) / 24.0 */
    public float getMiteProtection() {
        return material.multipliedProtection(piece.numComponents(), chainmail);
    }

    /** 物品 ID（不含命名空间）*/
    public String itemId() {
        return piece.itemId(material.materialName(), chainmail);
    }

    /** 纹理路径 */
    public String texturePath() {
        return piece.texturePath(material.materialName(), chainmail);
    }
}
