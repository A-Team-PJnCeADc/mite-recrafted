package com.mite.recraft.item.tools.toolItem;

import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

/**
 * 木质武器（唯一材质，不适用 ToolType）
 * 木棒（Club）：攻击+2，燃料 200 ticks
 * 短木棒（Cudgel）：攻击+1，燃料 200 ticks
 */
public class WoodenItems {
    private static final int COMPONENTS_CLUB   = 2;
    private static final int COMPONENTS_CUDGEL = 1;

    public static final Item CLUB = new Item(new Item.Properties()
            .setId(id("wood_club"))
            .sword(ModToolMaterial.WOOD.getToolMaterial(), 2.0F, -3.0F)
            .durability(ModToolMaterial.WOOD.getToolMaterial().durability() * COMPONENTS_CLUB));

    public static final Item CUDGEL = new Item(new Item.Properties()
            .setId(id("wood_cudgel"))
            .sword(ModToolMaterial.WOOD.getToolMaterial(), 1.0F, -2.0F)
            .durability(ModToolMaterial.WOOD.getToolMaterial().durability() * COMPONENTS_CUDGEL));

    private static ResourceKey<Item> id(String name) {
        return ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath("mite-recrafted", name));
    }
}
