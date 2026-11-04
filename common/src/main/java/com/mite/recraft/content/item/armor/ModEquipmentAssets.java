package com.mite.recraft.content.item.armor;

import com.mite.recraft.MiteRecrafted;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

/**
 * MITE 装备的 EquipmentAsset key。
 *
 * <p>马铠用 {@code <material>_horse}，人形盔甲用 {@code <material>} 与 {@code <material>_chainmail}。
 * 对应的装备 JSON 在 assets/mite_recrafted/equipment/<name>.json。</p>
 */
public class ModEquipmentAssets {
    public static final ResourceKey<EquipmentAsset> COPPER_HORSE = create("copper_horse");
    public static final ResourceKey<EquipmentAsset> SILVER_HORSE = create("silver_horse");
    public static final ResourceKey<EquipmentAsset> ANCIENT_METAL_HORSE = create("ancient_metal_horse");
    public static final ResourceKey<EquipmentAsset> MITHRIL_HORSE = create("mithril_horse");
    public static final ResourceKey<EquipmentAsset> ADAMANTIUM_HORSE = create("adamantium_horse");

    public static final ResourceKey<EquipmentAsset> COPPER_HUMANOID = create("copper");
    public static final ResourceKey<EquipmentAsset> COPPER_CHAINMAIL = create("copper_chainmail");
    public static final ResourceKey<EquipmentAsset> SILVER_HUMANOID = create("silver");
    public static final ResourceKey<EquipmentAsset> SILVER_CHAINMAIL = create("silver_chainmail");
    public static final ResourceKey<EquipmentAsset> GOLD_HUMANOID = create("gold");
    public static final ResourceKey<EquipmentAsset> GOLD_CHAINMAIL = create("gold_chainmail");
    public static final ResourceKey<EquipmentAsset> RUSTED_IRON_HUMANOID = create("rusted_iron");
    public static final ResourceKey<EquipmentAsset> RUSTED_IRON_CHAINMAIL = create("rusted_iron_chainmail");
    public static final ResourceKey<EquipmentAsset> IRON_HUMANOID = create("iron");
    public static final ResourceKey<EquipmentAsset> IRON_CHAINMAIL = create("iron_chainmail");
    public static final ResourceKey<EquipmentAsset> ANCIENT_METAL_HUMANOID = create("ancient_metal");
    public static final ResourceKey<EquipmentAsset> ANCIENT_METAL_CHAINMAIL = create("ancient_metal_chainmail");
    public static final ResourceKey<EquipmentAsset> MITHRIL_HUMANOID = create("mithril");
    public static final ResourceKey<EquipmentAsset> MITHRIL_CHAINMAIL = create("mithril_chainmail");
    public static final ResourceKey<EquipmentAsset> ADAMANTIUM_HUMANOID = create("adamantium");
    public static final ResourceKey<EquipmentAsset> ADAMANTIUM_CHAINMAIL = create("adamantium_chainmail");

    /** 人形盔甲对应的 asset：锁链版带 _chainmail 后缀。 */
    public static ResourceKey<EquipmentAsset> humanoid(ArmorMaterialType material, boolean chainmail) {
        ResourceKey<EquipmentAsset> plate = switch (material) {
            case COPPER -> COPPER_HUMANOID;
            case SILVER -> SILVER_HUMANOID;
            case GOLD -> GOLD_HUMANOID;
            case RUSTED_IRON -> RUSTED_IRON_HUMANOID;
            case IRON -> IRON_HUMANOID;
            case ANCIENT_METAL -> ANCIENT_METAL_HUMANOID;
            case MITHRIL -> MITHRIL_HUMANOID;
            case ADAMANTIUM -> ADAMANTIUM_HUMANOID;
        };
        if (!chainmail) {
            return plate;
        }
        return switch (material) {
            case COPPER -> COPPER_CHAINMAIL;
            case SILVER -> SILVER_CHAINMAIL;
            case GOLD -> GOLD_CHAINMAIL;
            case RUSTED_IRON -> RUSTED_IRON_CHAINMAIL;
            case IRON -> IRON_CHAINMAIL;
            case ANCIENT_METAL -> ANCIENT_METAL_CHAINMAIL;
            case MITHRIL -> MITHRIL_CHAINMAIL;
            case ADAMANTIUM -> ADAMANTIUM_CHAINMAIL;
        };
    }

    private static ResourceKey<EquipmentAsset> create(String name) {
        return ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, name));
    }

    public static void register() {}
}
