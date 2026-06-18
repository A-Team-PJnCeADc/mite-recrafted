package com.mite.recraft.item.tools.toolItem;

import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;

/**
 * 钓鱼竿（Fishing Rod）
 */
public class FishingRodItems {
    public static final FishingRodItem FLINT_FISHING_ROD = create("flint", ModToolMaterial.FLINT);
    public static final FishingRodItem OBSIDIAN_FISHING_ROD = create("obsidian", ModToolMaterial.OBSIDIAN);
    public static final FishingRodItem COPPER_FISHING_ROD = create("copper", ModToolMaterial.COPPER);
    public static final FishingRodItem SILVER_FISHING_ROD = create("silver", ModToolMaterial.SILVER);
    public static final FishingRodItem GOLD_FISHING_ROD = create("gold", ModToolMaterial.GOLD);
    public static final FishingRodItem IRON_FISHING_ROD = create("iron", ModToolMaterial.IRON);
    public static final FishingRodItem ANCIENT_METAL_FISHING_ROD = create("ancient_metal", ModToolMaterial.ANCIENT_METAL);
    public static final FishingRodItem MITHRIL_FISHING_ROD = create("mithril", ModToolMaterial.MITHRIL);
    public static final FishingRodItem ADAMANTIUM_FISHING_ROD = create("adamantium", ModToolMaterial.ADAMANTIUM);

    private static FishingRodItem create(String name, ModToolMaterial mat) {
        return new FishingRodItem(new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM,
                        Identifier.fromNamespaceAndPath("mite-recrafted", name + "_fishing_rod")))
                .durability((int) (64 * mat.getDurabilityCoefficient()))
                .enchantable(mat.getToolMaterial() != null
                        ? mat.getToolMaterial().enchantmentValue()
                        : 1));
    }
}
