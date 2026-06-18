package com.mite.recraft.item.tools.toolItem;

import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;

/**
 * 手斧（Hatchet)
 */
public class HatchetItems {
    //todo 获取实际的数值
    public static final AxeItem FLINT_HATCHET = create("flint", ModToolMaterial.FLINT, 1.0F);
    public static final AxeItem OBSIDIAN_HATCHET = create("obsidian", ModToolMaterial.OBSIDIAN, 2.0F);
    public static final AxeItem COPPER_HATCHET = create("copper", ModToolMaterial.COPPER, 3.0F);
    public static final AxeItem SILVER_HATCHET = create("silver", ModToolMaterial.SILVER, 3.0F);
    public static final AxeItem GOLD_HATCHET = create("gold", ModToolMaterial.GOLD, 2.0F);
    public static final AxeItem RUSTED_IRON_HATCHET = create("rusted_iron", ModToolMaterial.RUSTED_IRON, 2.0F);
    public static final AxeItem IRON_HATCHET = create("iron", ModToolMaterial.IRON, 2.0F);
    public static final AxeItem ANCIENT_METAL_HATCHET = create("ancient_metal", ModToolMaterial.ANCIENT_METAL, 4.0F);
    public static final AxeItem MITHRIL_HATCHET = create("mithril", ModToolMaterial.MITHRIL, 5.0F);
    public static final AxeItem ADAMANTIUM_HATCHET = create("adamantium", ModToolMaterial.ADAMANTIUM, 5.5F);

    private static AxeItem create(String name, ModToolMaterial mat, float attackDamage) {
        return new AxeItem(
                mat.getToolMaterial(),
                attackDamage,
                -3.0F,
                new Item.Properties()
                        .setId(ResourceKey.create(Registries.ITEM,
                                Identifier.fromNamespaceAndPath("mite-recrafted", name + "_hatchet")))
        );
    }
}
