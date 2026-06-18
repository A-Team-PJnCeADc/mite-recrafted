package com.mite.recraft.item.tools.toolItem;

import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

/**
 * 镐子（Pickaxe）
 */

//todo 挖掘行为
public class PickaxeItems {
    //todo 获取实际的数值
    public static final Item COPPER_PICKAXE = create("copper", ModToolMaterial.COPPER);
    public static final Item SILVER_PICKAXE = create("silver", ModToolMaterial.SILVER);
    public static final Item GOLD_PICKAXE = create("gold", ModToolMaterial.GOLD);
    public static final Item RUSTED_IRON_PICKAXE = create("rusted_iron", ModToolMaterial.RUSTED_IRON);
    public static final Item IRON_PICKAXE = create("iron", ModToolMaterial.IRON);
    public static final Item ANCIENT_METAL_PICKAXE = create("ancient_metal", ModToolMaterial.ANCIENT_METAL);
    public static final Item MITHRIL_PICKAXE = create("mithril", ModToolMaterial.MITHRIL);
    public static final Item ADAMANTIUM_PICKAXE = create("adamantium", ModToolMaterial.ADAMANTIUM);

    private static Item create(String name, ModToolMaterial mat) {
        return new Item(new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM,
                        Identifier.fromNamespaceAndPath("mite-recrafted", name + "_pickaxe"))));
    }
}
