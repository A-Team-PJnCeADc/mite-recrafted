package com.mite.recraft.item.tools.toolItem;

import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

/**
 * 短剑（Dagger / Short Sword）
 * 使用原版 .sword() 属性，攻击力比长剑低
 */
public class DaggerItems {
    //todo 获取实际的数值
    public static final Item COPPER_DAGGER = create("copper", ModToolMaterial.COPPER, 2.0F, -1.5F);
    public static final Item SILVER_DAGGER = create("silver", ModToolMaterial.SILVER, 2.0F, -1.5F);
    public static final Item GOLD_DAGGER = create("gold", ModToolMaterial.GOLD, 2.0F, -1.5F);
    public static final Item RUSTED_IRON_DAGGER = create("rusted_iron", ModToolMaterial.RUSTED_IRON, 2.0F, -1.5F);
    public static final Item IRON_DAGGER = create("iron", ModToolMaterial.IRON, 2.0F, -1.5F);
    public static final Item ANCIENT_METAL_DAGGER = create("ancient_metal", ModToolMaterial.ANCIENT_METAL, 2.0F, -1.5F);
    public static final Item MITHRIL_DAGGER = create("mithril", ModToolMaterial.MITHRIL, 2.0F, -1.5F);
    public static final Item ADAMANTIUM_DAGGER = create("adamantium", ModToolMaterial.ADAMANTIUM, 2.0F, -1.5F);

    private static Item create(String name, ModToolMaterial mat, float attackDamage, float attackSpeed) {
        return new Item(new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM,
                        Identifier.fromNamespaceAndPath("mite-recrafted", name + "_dagger")))
                .sword(mat.getToolMaterial(), attackDamage, attackSpeed));
    }
}
