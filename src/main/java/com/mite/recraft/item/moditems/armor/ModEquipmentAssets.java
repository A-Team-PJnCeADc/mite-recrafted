package com.mite.recraft.item.moditems.armor;

import com.mite.recraft.MiteRecrafted;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

/**
 * MITE 马铠 EquipmentAsset key。
 * 对应的装备 JSON 在 assets/mite-recrafted/equipment/<name>.json。
 */
public class ModEquipmentAssets {
    public static final ResourceKey<EquipmentAsset> COPPER_HORSE = create("copper_horse");
    public static final ResourceKey<EquipmentAsset> SILVER_HORSE = create("silver_horse");
    public static final ResourceKey<EquipmentAsset> ANCIENT_METAL_HORSE = create("ancient_metal_horse");
    public static final ResourceKey<EquipmentAsset> MITHRIL_HORSE = create("mithril_horse");
    public static final ResourceKey<EquipmentAsset> ADAMANTIUM_HORSE = create("adamantium_horse");

    private static ResourceKey<EquipmentAsset> create(String name) {
        return ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, name));
    }

    public static void init() {}
}
