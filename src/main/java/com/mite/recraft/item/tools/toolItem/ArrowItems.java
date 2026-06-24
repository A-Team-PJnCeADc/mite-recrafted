package com.mite.recraft.item.tools.toolItem;

import com.mite.recraft.component.ModDataComponents;
import com.mite.recraft.entity.arrowentity.ArrowEntity;
import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ArrowItems {
    public static final ArrowItem FLINT_ARROW       = create("flint",        ModToolMaterial.FLINT,          1.0f, 0.3f);
    public static final ArrowItem OBSIDIAN_ARROW    = create("obsidian",     ModToolMaterial.OBSIDIAN,       2.0f, 0.4f);
    public static final ArrowItem COPPER_ARROW      = create("copper",       ModToolMaterial.COPPER,         3.0f, 0.6f);
    public static final ArrowItem SILVER_ARROW      = create("silver",       ModToolMaterial.SILVER,         3.0f, 0.6f);
    public static final ArrowItem GOLD_ARROW        = create("gold",         ModToolMaterial.GOLD,           3.0f, 0.6f);
    public static final ArrowItem RUSTED_IRON_ARROW = create("rusted_iron",  ModToolMaterial.RUSTED_IRON,    2.0f, 0.5f);
    public static final ArrowItem IRON_ARROW        = create("iron",         ModToolMaterial.IRON,          4.0f, 0.7f);
    public static final ArrowItem ANCIENT_METAL_ARROW = create("ancient_metal", ModToolMaterial.ANCIENT_METAL, 4.0f, 0.8f);
    public static final ArrowItem MITHRIL_ARROW     = create("mithril",      ModToolMaterial.MITHRIL,      5.0f, 0.8f);
    public static final ArrowItem ADAMANTIUM_ARROW  = create("adamantium",   ModToolMaterial.ADAMANTIUM,   6.0f, 0.9f);

    private static ArrowItem create(String name, ModToolMaterial mat, float baseDamage, float recoveryChance) {
        return new ArrowItem(new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM,
                        Identifier.fromNamespaceAndPath("mite-recrafted", name + "_arrow")))
                .stacksTo(16)
                .component(ModDataComponents.BASE_DAMAGE, baseDamage)
                .component(ModDataComponents.RECOVERY_CHANCE, recoveryChance)) {
            @Override
            public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, ItemStack weapon) {
                return new ArrowEntity(level, shooter, ammo.copyWithCount(1), weapon);
            }
        };
    }
}
