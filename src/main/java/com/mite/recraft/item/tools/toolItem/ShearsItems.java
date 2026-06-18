package com.mite.recraft.item.tools.toolItem;

import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShearsItem;

/**
 * 剪刀（Shears）
 */
public class ShearsItems {
    //todo 获取实际的数值
    public static final ShearsItem COPPER_SHEARS = create("copper", ModToolMaterial.COPPER);
    public static final ShearsItem SILVER_SHEARS = create("silver", ModToolMaterial.SILVER);
    public static final ShearsItem GOLD_SHEARS = create("gold", ModToolMaterial.GOLD);
    public static final ShearsItem RUSTED_IRON_SHEARS = create("rusted_iron", ModToolMaterial.RUSTED_IRON);
    public static final ShearsItem ANCIENT_METAL_SHEARS = create("ancient_metal", ModToolMaterial.ANCIENT_METAL);
    public static final ShearsItem MITHRIL_SHEARS = create("mithril", ModToolMaterial.MITHRIL);
    public static final ShearsItem ADAMANTIUM_SHEARS = create("adamantium", ModToolMaterial.ADAMANTIUM);

    private static ShearsItem create(String name, ModToolMaterial mat) {
        return new ShearsItem(new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM,
                        Identifier.fromNamespaceAndPath("mite-recrafted", name + "_shears"))));
    }
}
