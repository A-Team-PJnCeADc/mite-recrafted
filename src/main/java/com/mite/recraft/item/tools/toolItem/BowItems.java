package com.mite.recraft.item.tools.toolItem;

import com.mite.recraft.component.ModDataComponents;
import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;

/**
 * 弓（Bow）—— wood(32), ancient_metal(64), mithril(128)
 * 拉弓动画 + 箭矢材质匹配由 SelectItemModelProperty (NockedArrowProperty) + 模型 JSON select 实现
 */
public class BowItems {
    public static final Item WOOD_BOW = new BowItem(new Item.Properties()
            .setId(id("wood_bow"))
            .durability(32));

    public static final Item ANCIENT_METAL_BOW = new BowItem(new Item.Properties()
            .setId(id("ancient_metal_bow"))
            .durability(64)
            .component(ModDataComponents.TOOL_COMPONENTS, 2)
            .component(ModDataComponents.TOOL_MATERIAL_TIER, ModToolMaterial.ANCIENT_METAL.getDurabilityCoefficient())
            .component(ModDataComponents.TOOL_REPAIR_TAG, ModToolMaterial.ANCIENT_METAL.getRepairTagName()));

    public static final Item MITHRIL_BOW = new BowItem(new Item.Properties()
            .setId(id("mithril_bow"))
            .durability(128)
            .component(ModDataComponents.TOOL_COMPONENTS, 2)
            .component(ModDataComponents.TOOL_MATERIAL_TIER, ModToolMaterial.MITHRIL.getDurabilityCoefficient())
            .component(ModDataComponents.TOOL_REPAIR_TAG, ModToolMaterial.MITHRIL.getRepairTagName()));

    private static ResourceKey<Item> id(String name) {
        return ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath("mite-recrafted", name));
    }
}
