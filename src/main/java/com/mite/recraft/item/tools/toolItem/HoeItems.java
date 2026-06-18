package com.mite.recraft.item.tools.toolItem;

import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;

/**
 * 锄（Hoe）
 */
public class HoeItems {
    //todo 获取实际的数值
    public static final HoeItem COPPER_HOE = create("copper", ModToolMaterial.COPPER, 0.0F);
    public static final HoeItem SILVER_HOE = create("silver", ModToolMaterial.SILVER, 0.0F);
    public static final HoeItem GOLD_HOE = create("gold", ModToolMaterial.GOLD, 0.0F);
    public static final HoeItem RUSTED_IRON_HOE = create("rusted_iron", ModToolMaterial.RUSTED_IRON, 0.0F);
    public static final HoeItem IRON_HOE = create("iron", ModToolMaterial.IRON, 0.0F);
    public static final HoeItem ANCIENT_METAL_HOE = create("ancient_metal", ModToolMaterial.ANCIENT_METAL, 0.0F);
    public static final HoeItem MITHRIL_HOE = create("mithril", ModToolMaterial.MITHRIL, 0.0F);
    public static final HoeItem ADAMANTIUM_HOE = create("adamantium", ModToolMaterial.ADAMANTIUM, 0.0F);

    private static HoeItem create(String name, ModToolMaterial mat, float attackDamage) {
        return new HoeItem(
                mat.getToolMaterial(),
                attackDamage,
                -3.0F,
                new Item.Properties()
                        .setId(ResourceKey.create(Registries.ITEM,
                                Identifier.fromNamespaceAndPath("mite-recrafted", name + "_hoe")))
        );
    }
}
