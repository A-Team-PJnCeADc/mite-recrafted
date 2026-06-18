package com.mite.recraft.item.tools.toolItem;

import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;

/**
 * 锹（Shovel）
 */
public class ShovelItems {
    //todo 获取实际的数值
    public static final ShovelItem FLINT_SHOVEL = create("flint", ModToolMaterial.FLINT, 0.5F);
    public static final ShovelItem OBSIDIAN_SHOVEL = create("obsidian", ModToolMaterial.OBSIDIAN, 1.5F);
    public static final ShovelItem COPPER_SHOVEL = create("copper", ModToolMaterial.COPPER, 2.5F);
    public static final ShovelItem SILVER_SHOVEL = create("silver", ModToolMaterial.SILVER, 2.5F);
    public static final ShovelItem GOLD_SHOVEL = create("gold", ModToolMaterial.GOLD, 1.5F);
    public static final ShovelItem RUSTED_IRON_SHOVEL = create("rusted_iron", ModToolMaterial.RUSTED_IRON, 1.5F);
    public static final ShovelItem IRON_SHOVEL = create("iron", ModToolMaterial.IRON, 2.5F);
    public static final ShovelItem ANCIENT_METAL_SHOVEL = create("ancient_metal", ModToolMaterial.ANCIENT_METAL, 3.5F);
    public static final ShovelItem MITHRIL_SHOVEL = create("mithril", ModToolMaterial.MITHRIL, 4.5F);
    public static final ShovelItem ADAMANTIUM_SHOVEL = create("adamantium", ModToolMaterial.ADAMANTIUM, 5.5F);

    private static ShovelItem create(String name, ModToolMaterial mat, float attackDamage) {
        return new ShovelItem(
                mat.getToolMaterial(),
                attackDamage,
                -3.0F,
                new Item.Properties()
                        .setId(ResourceKey.create(Registries.ITEM,
                                Identifier.fromNamespaceAndPath("mite-recrafted", name + "_shovel")))
        );
    }
}
