package com.mite.recraft.item.tools.toolItem;

import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;

public class AexItems {
    //todo 获取实际的数值
    public static final AxeItem FLINT_AXE = new AxeItem(
            ModToolMaterial.FLINT.getToolMaterial(),
            3.0F,
            -3.0F,
            new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("mite-recrafted", "flint_axe")))
    );

    public static final AxeItem OBSIDIAN_AXE = new AxeItem(
            ModToolMaterial.OBSIDIAN.getToolMaterial(),
            4.0F,
            -3.0F,
            new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("mite-recrafted", "obsidian_axe")))
    );

    public static final AxeItem COPPER_AXE = new AxeItem(
            ModToolMaterial.COPPER.getToolMaterial(),
            5.0F,
            -3.0F,
            new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("mite-recrafted", "copper_axe")))
    );

    public static final AxeItem SILVER_AXE = new AxeItem(
            ModToolMaterial.SILVER.getToolMaterial(),
            5F,
            -3.0F,
            new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("mite-recrafted", "silver_axe")))
    );

    public static final AxeItem GOLD_AXE = new AxeItem(
            ModToolMaterial.GOLD.getToolMaterial(),
            4F,
            -3.0F,
            new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("mite-recrafted", "gold_axe")))
    );

    public static final AxeItem RUSTED_IRON_AXE = new AxeItem(
            ModToolMaterial.RUSTED_IRON.getToolMaterial(),
            4.0F,
            -3.0F,
            new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("mite-recrafted", "rusted_iron_axe")))
    );

    public static final AxeItem IRON_AXE = new AxeItem(
            ModToolMaterial.IRON.getToolMaterial(),
            4.0F,
            -3.1F,
            new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("mite-recrafted", "iron_axe")))
    );

    public static final AxeItem ANCIENT_METAL_AXE = new AxeItem(
            ModToolMaterial.ANCIENT_METAL.getToolMaterial(),
            6.0F,
            -3.0F,
            new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("mite-recrafted", "ancient_metal_axe")))
    );

    public static final AxeItem MITHRIL_AXE = new AxeItem(
            ModToolMaterial.MITHRIL.getToolMaterial(),
            7.0F,
            -3.0F,
            new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("mite-recrafted", "mithril_axe")))
    );

    public static final AxeItem ADAMANTIUM_AXE = new AxeItem(
            ModToolMaterial.ADAMANTIUM.getToolMaterial(),
            8.0F,
            -3.0F,
            new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("mite-recrafted", "adamantium_axe")))
    );
}
