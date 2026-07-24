package com.mite.recraft.item.moditems.armor;

import com.mite.recraft.component.ModDataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import static com.mite.recraft.MiteRecrafted.MOD_ID;

/**
 * 护甲物品工厂 —— 产出所有 64 个护甲实例 (8 材料 × 4 槽位 × 2 变体)。
 *
 * 命名规则:
 *   普通护甲: {MATERIAL}_{PIECE}          e.g. COPPER_HELMET
 *   锁链护甲: {MATERIAL}_CHAINMAIL_{PIECE}  e.g. COPPER_CHAINMAIL_HELMET
 *
 * 物品 ID:
 *   普通: copper_helmet, copper_chestplate, ...
 *   锁链: copper_chainmail_helmet, copper_chainmail_chestplate, ...
 */
public class ModArmorItems {

    // ===== COPPER =====
    public static final Item COPPER_HELMET = createHelmet(ArmorMaterialType.COPPER, false);
    public static final Item COPPER_CHESTPLATE = createChestplate(ArmorMaterialType.COPPER, false);
    public static final Item COPPER_LEGGINGS = createLeggings(ArmorMaterialType.COPPER, false);
    public static final Item COPPER_BOOTS = createBoots(ArmorMaterialType.COPPER, false);
    public static final Item COPPER_CHAINMAIL_HELMET = createHelmet(ArmorMaterialType.COPPER, true);
    public static final Item COPPER_CHAINMAIL_CHESTPLATE = createChestplate(ArmorMaterialType.COPPER, true);
    public static final Item COPPER_CHAINMAIL_LEGGINGS = createLeggings(ArmorMaterialType.COPPER, true);
    public static final Item COPPER_CHAINMAIL_BOOTS = createBoots(ArmorMaterialType.COPPER, true);

    // ===== SILVER =====
    public static final Item SILVER_HELMET = createHelmet(ArmorMaterialType.SILVER, false);
    public static final Item SILVER_CHESTPLATE = createChestplate(ArmorMaterialType.SILVER, false);
    public static final Item SILVER_LEGGINGS = createLeggings(ArmorMaterialType.SILVER, false);
    public static final Item SILVER_BOOTS = createBoots(ArmorMaterialType.SILVER, false);
    public static final Item SILVER_CHAINMAIL_HELMET = createHelmet(ArmorMaterialType.SILVER, true);
    public static final Item SILVER_CHAINMAIL_CHESTPLATE = createChestplate(ArmorMaterialType.SILVER, true);
    public static final Item SILVER_CHAINMAIL_LEGGINGS = createLeggings(ArmorMaterialType.SILVER, true);
    public static final Item SILVER_CHAINMAIL_BOOTS = createBoots(ArmorMaterialType.SILVER, true);

    // ===== GOLD =====
    public static final Item GOLD_HELMET = createHelmet(ArmorMaterialType.GOLD, false);
    public static final Item GOLD_CHESTPLATE = createChestplate(ArmorMaterialType.GOLD, false);
    public static final Item GOLD_LEGGINGS = createLeggings(ArmorMaterialType.GOLD, false);
    public static final Item GOLD_BOOTS = createBoots(ArmorMaterialType.GOLD, false);
    public static final Item GOLD_CHAINMAIL_HELMET = createHelmet(ArmorMaterialType.GOLD, true);
    public static final Item GOLD_CHAINMAIL_CHESTPLATE = createChestplate(ArmorMaterialType.GOLD, true);
    public static final Item GOLD_CHAINMAIL_LEGGINGS = createLeggings(ArmorMaterialType.GOLD, true);
    public static final Item GOLD_CHAINMAIL_BOOTS = createBoots(ArmorMaterialType.GOLD, true);

    // ===== RUSTED_IRON =====
    public static final Item RUSTED_IRON_HELMET = createHelmet(ArmorMaterialType.RUSTED_IRON, false);
    public static final Item RUSTED_IRON_CHESTPLATE = createChestplate(ArmorMaterialType.RUSTED_IRON, false);
    public static final Item RUSTED_IRON_LEGGINGS = createLeggings(ArmorMaterialType.RUSTED_IRON, false);
    public static final Item RUSTED_IRON_BOOTS = createBoots(ArmorMaterialType.RUSTED_IRON, false);
    public static final Item RUSTED_IRON_CHAINMAIL_HELMET = createHelmet(ArmorMaterialType.RUSTED_IRON, true);
    public static final Item RUSTED_IRON_CHAINMAIL_CHESTPLATE = createChestplate(ArmorMaterialType.RUSTED_IRON, true);
    public static final Item RUSTED_IRON_CHAINMAIL_LEGGINGS = createLeggings(ArmorMaterialType.RUSTED_IRON, true);
    public static final Item RUSTED_IRON_CHAINMAIL_BOOTS = createBoots(ArmorMaterialType.RUSTED_IRON, true);

    // ===== IRON =====
    public static final Item IRON_HELMET = createHelmet(ArmorMaterialType.IRON, false);
    public static final Item IRON_CHESTPLATE = createChestplate(ArmorMaterialType.IRON, false);
    public static final Item IRON_LEGGINGS = createLeggings(ArmorMaterialType.IRON, false);
    public static final Item IRON_BOOTS = createBoots(ArmorMaterialType.IRON, false);
    public static final Item IRON_CHAINMAIL_HELMET = createHelmet(ArmorMaterialType.IRON, true);
    public static final Item IRON_CHAINMAIL_CHESTPLATE = createChestplate(ArmorMaterialType.IRON, true);
    public static final Item IRON_CHAINMAIL_LEGGINGS = createLeggings(ArmorMaterialType.IRON, true);
    public static final Item IRON_CHAINMAIL_BOOTS = createBoots(ArmorMaterialType.IRON, true);

    // ===== ANCIENT_METAL =====
    public static final Item ANCIENT_METAL_HELMET = createHelmet(ArmorMaterialType.ANCIENT_METAL, false);
    public static final Item ANCIENT_METAL_CHESTPLATE = createChestplate(ArmorMaterialType.ANCIENT_METAL, false);
    public static final Item ANCIENT_METAL_LEGGINGS = createLeggings(ArmorMaterialType.ANCIENT_METAL, false);
    public static final Item ANCIENT_METAL_BOOTS = createBoots(ArmorMaterialType.ANCIENT_METAL, false);
    public static final Item ANCIENT_METAL_CHAINMAIL_HELMET = createHelmet(ArmorMaterialType.ANCIENT_METAL, true);
    public static final Item ANCIENT_METAL_CHAINMAIL_CHESTPLATE = createChestplate(ArmorMaterialType.ANCIENT_METAL, true);
    public static final Item ANCIENT_METAL_CHAINMAIL_LEGGINGS = createLeggings(ArmorMaterialType.ANCIENT_METAL, true);
    public static final Item ANCIENT_METAL_CHAINMAIL_BOOTS = createBoots(ArmorMaterialType.ANCIENT_METAL, true);

    // ===== MITHRIL =====
    public static final Item MITHRIL_HELMET = createHelmet(ArmorMaterialType.MITHRIL, false);
    public static final Item MITHRIL_CHESTPLATE = createChestplate(ArmorMaterialType.MITHRIL, false);
    public static final Item MITHRIL_LEGGINGS = createLeggings(ArmorMaterialType.MITHRIL, false);
    public static final Item MITHRIL_BOOTS = createBoots(ArmorMaterialType.MITHRIL, false);
    public static final Item MITHRIL_CHAINMAIL_HELMET = createHelmet(ArmorMaterialType.MITHRIL, true);
    public static final Item MITHRIL_CHAINMAIL_CHESTPLATE = createChestplate(ArmorMaterialType.MITHRIL, true);
    public static final Item MITHRIL_CHAINMAIL_LEGGINGS = createLeggings(ArmorMaterialType.MITHRIL, true);
    public static final Item MITHRIL_CHAINMAIL_BOOTS = createBoots(ArmorMaterialType.MITHRIL, true);

    // ===== ADAMANTIUM =====
    public static final Item ADAMANTIUM_HELMET = createHelmet(ArmorMaterialType.ADAMANTIUM, false);
    public static final Item ADAMANTIUM_CHESTPLATE = createChestplate(ArmorMaterialType.ADAMANTIUM, false);
    public static final Item ADAMANTIUM_LEGGINGS = createLeggings(ArmorMaterialType.ADAMANTIUM, false);
    public static final Item ADAMANTIUM_BOOTS = createBoots(ArmorMaterialType.ADAMANTIUM, false);
    public static final Item ADAMANTIUM_CHAINMAIL_HELMET = createHelmet(ArmorMaterialType.ADAMANTIUM, true);
    public static final Item ADAMANTIUM_CHAINMAIL_CHESTPLATE = createChestplate(ArmorMaterialType.ADAMANTIUM, true);
    public static final Item ADAMANTIUM_CHAINMAIL_LEGGINGS = createLeggings(ArmorMaterialType.ADAMANTIUM, true);
    public static final Item ADAMANTIUM_CHAINMAIL_BOOTS = createBoots(ArmorMaterialType.ADAMANTIUM, true);

    // ===== 工厂方法 =====

    private static Item createHelmet(ArmorMaterialType mat, boolean chainmail) {
        String itemId = ArmorPiece.HELMET.itemId(mat.materialName(), chainmail);
        return new ModHelmetItem(mat, chainmail, baseProps(mat, chainmail, itemId, ArmorPiece.HELMET));
    }

    private static Item createChestplate(ArmorMaterialType mat, boolean chainmail) {
        String itemId = ArmorPiece.CHESTPLATE.itemId(mat.materialName(), chainmail);
        return new ModChestplateItem(mat, chainmail, baseProps(mat, chainmail, itemId, ArmorPiece.CHESTPLATE));
    }

    private static Item createLeggings(ArmorMaterialType mat, boolean chainmail) {
        String itemId = ArmorPiece.LEGGINGS.itemId(mat.materialName(), chainmail);
        return new ModLeggingsItem(mat, chainmail, baseProps(mat, chainmail, itemId, ArmorPiece.LEGGINGS));
    }

    private static Item createBoots(ArmorMaterialType mat, boolean chainmail) {
        String itemId = ArmorPiece.BOOTS.itemId(mat.materialName(), chainmail);
        return new ModBootsItem(mat, chainmail, baseProps(mat, chainmail, itemId, ArmorPiece.BOOTS));
    }

    /**
     * 构建物品属性：equippable 组件 + ARMOR 属性修饰符 + MITE 耐久 + 附魔性。
     */
    private static Item.Properties baseProps(ArmorMaterialType mat, boolean chainmail, String itemId, ArmorPiece piece) {
        EquipmentSlot slot = switch (piece) {
            case HELMET -> EquipmentSlot.HEAD;
            case CHESTPLATE -> EquipmentSlot.CHEST;
            case LEGGINGS -> EquipmentSlot.LEGS;
            case BOOTS -> EquipmentSlot.FEET;
        };

        // MITE: (numComponents × protection) / 24.0 = 每点 4% 减伤
        float armorValue = (float) (piece.numComponents() * mat.protection(chainmail)) / 24.0f;
        if (armorValue < 0.5f) armorValue = 0.5f;

        // ARMOR 属性修饰符：作用于对应槽位组
        ItemAttributeModifiers armorAttrs = ItemAttributeModifiers.builder()
                .add(Attributes.ARMOR,
                        new AttributeModifier(
                                Identifier.fromNamespaceAndPath(MOD_ID, "armor." + itemId),
                                armorValue,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.bySlot(slot))
                .build();

        return new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM,
                        Identifier.fromNamespaceAndPath(MOD_ID, itemId)))
                .stacksTo(1)
                .durability(mat.durability(piece.numComponents(), chainmail))
                .enchantable(mat.enchantability())
                .equippable(slot)
                .attributes(armorAttrs)
                .component(ModDataComponents.CRAFTING_DIFFICULTY,
                        mat.durabilityCoefficient() * 100.0F * piece.numComponents());
    }

    /** 所有护甲物品数组（用于批量注册和模型生成） */
    public static final Item[] ALL = {
            // Copper
            COPPER_HELMET, COPPER_CHESTPLATE, COPPER_LEGGINGS, COPPER_BOOTS,
            COPPER_CHAINMAIL_HELMET, COPPER_CHAINMAIL_CHESTPLATE, COPPER_CHAINMAIL_LEGGINGS, COPPER_CHAINMAIL_BOOTS,
            // Silver
            SILVER_HELMET, SILVER_CHESTPLATE, SILVER_LEGGINGS, SILVER_BOOTS,
            SILVER_CHAINMAIL_HELMET, SILVER_CHAINMAIL_CHESTPLATE, SILVER_CHAINMAIL_LEGGINGS, SILVER_CHAINMAIL_BOOTS,
            // Gold
            GOLD_HELMET, GOLD_CHESTPLATE, GOLD_LEGGINGS, GOLD_BOOTS,
            GOLD_CHAINMAIL_HELMET, GOLD_CHAINMAIL_CHESTPLATE, GOLD_CHAINMAIL_LEGGINGS, GOLD_CHAINMAIL_BOOTS,
            // Rusted Iron
            RUSTED_IRON_HELMET, RUSTED_IRON_CHESTPLATE, RUSTED_IRON_LEGGINGS, RUSTED_IRON_BOOTS,
            RUSTED_IRON_CHAINMAIL_HELMET, RUSTED_IRON_CHAINMAIL_CHESTPLATE, RUSTED_IRON_CHAINMAIL_LEGGINGS, RUSTED_IRON_CHAINMAIL_BOOTS,
            // Iron
            IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS,
            IRON_CHAINMAIL_HELMET, IRON_CHAINMAIL_CHESTPLATE, IRON_CHAINMAIL_LEGGINGS, IRON_CHAINMAIL_BOOTS,
            // Ancient Metal
            ANCIENT_METAL_HELMET, ANCIENT_METAL_CHESTPLATE, ANCIENT_METAL_LEGGINGS, ANCIENT_METAL_BOOTS,
            ANCIENT_METAL_CHAINMAIL_HELMET, ANCIENT_METAL_CHAINMAIL_CHESTPLATE, ANCIENT_METAL_CHAINMAIL_LEGGINGS, ANCIENT_METAL_CHAINMAIL_BOOTS,
            // Mithril
            MITHRIL_HELMET, MITHRIL_CHESTPLATE, MITHRIL_LEGGINGS, MITHRIL_BOOTS,
            MITHRIL_CHAINMAIL_HELMET, MITHRIL_CHAINMAIL_CHESTPLATE, MITHRIL_CHAINMAIL_LEGGINGS, MITHRIL_CHAINMAIL_BOOTS,
            // Adamantium
            ADAMANTIUM_HELMET, ADAMANTIUM_CHESTPLATE, ADAMANTIUM_LEGGINGS, ADAMANTIUM_BOOTS,
            ADAMANTIUM_CHAINMAIL_HELMET, ADAMANTIUM_CHAINMAIL_CHESTPLATE, ADAMANTIUM_CHAINMAIL_LEGGINGS, ADAMANTIUM_CHAINMAIL_BOOTS,
    };

    public static void init() {}
}
