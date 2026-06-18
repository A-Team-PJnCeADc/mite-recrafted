package com.mite.recraft.item.tools.register;

import com.mite.recraft.item.ModItems;
import com.mite.recraft.item.tools.toolItem.AexItems;
import com.mite.recraft.item.tools.toolItem.FishingRodItems;
import com.mite.recraft.item.tools.toolItem.HatchetItems;
import com.mite.recraft.item.tools.toolItem.HoeItems;
import com.mite.recraft.item.tools.toolItem.PickaxeItems;
import com.mite.recraft.item.tools.toolItem.ShearsItems;
import com.mite.recraft.item.tools.toolItem.ShovelItems;
import com.mite.recraft.item.tools.toolItem.SwordItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class ModToolRegister {
    public static void registerAll() {
        // 斧头
        register("flint_axe", AexItems.FLINT_AXE);
        register("obsidian_axe", AexItems.OBSIDIAN_AXE);
        register("copper_axe", AexItems.COPPER_AXE);
        register("silver_axe", AexItems.SILVER_AXE);
        register("gold_axe", AexItems.GOLD_AXE);
        register("rusted_iron_axe", AexItems.RUSTED_IRON_AXE);
        register("iron_axe", AexItems.IRON_AXE);
        register("ancient_metal_axe", AexItems.ANCIENT_METAL_AXE);
        register("mithril_axe", AexItems.MITHRIL_AXE);
        register("adamantium_axe", AexItems.ADAMANTIUM_AXE);

        // 手斧
        register("flint_hatchet", HatchetItems.FLINT_HATCHET);
        register("obsidian_hatchet", HatchetItems.OBSIDIAN_HATCHET);
        register("copper_hatchet", HatchetItems.COPPER_HATCHET);
        register("silver_hatchet", HatchetItems.SILVER_HATCHET);
        register("gold_hatchet", HatchetItems.GOLD_HATCHET);
        register("rusted_iron_hatchet", HatchetItems.RUSTED_IRON_HATCHET);
        register("iron_hatchet", HatchetItems.IRON_HATCHET);
        register("ancient_metal_hatchet", HatchetItems.ANCIENT_METAL_HATCHET);
        register("mithril_hatchet", HatchetItems.MITHRIL_HATCHET);
        register("adamantium_hatchet", HatchetItems.ADAMANTIUM_HATCHET);

        // 镐子
        register("copper_pickaxe", PickaxeItems.COPPER_PICKAXE);
        register("silver_pickaxe", PickaxeItems.SILVER_PICKAXE);
        register("gold_pickaxe", PickaxeItems.GOLD_PICKAXE);
        register("rusted_iron_pickaxe", PickaxeItems.RUSTED_IRON_PICKAXE);
        register("iron_pickaxe", PickaxeItems.IRON_PICKAXE);
        register("ancient_metal_pickaxe", PickaxeItems.ANCIENT_METAL_PICKAXE);
        register("mithril_pickaxe", PickaxeItems.MITHRIL_PICKAXE);
        register("adamantium_pickaxe", PickaxeItems.ADAMANTIUM_PICKAXE);

        // 锹
        register("flint_shovel", ShovelItems.FLINT_SHOVEL);
        register("obsidian_shovel", ShovelItems.OBSIDIAN_SHOVEL);
        register("copper_shovel", ShovelItems.COPPER_SHOVEL);
        register("silver_shovel", ShovelItems.SILVER_SHOVEL);
        register("gold_shovel", ShovelItems.GOLD_SHOVEL);
        register("rusted_iron_shovel", ShovelItems.RUSTED_IRON_SHOVEL);
        register("iron_shovel", ShovelItems.IRON_SHOVEL);
        register("ancient_metal_shovel", ShovelItems.ANCIENT_METAL_SHOVEL);
        register("mithril_shovel", ShovelItems.MITHRIL_SHOVEL);
        register("adamantium_shovel", ShovelItems.ADAMANTIUM_SHOVEL);

        // 锄
        register("copper_hoe", HoeItems.COPPER_HOE);
        register("silver_hoe", HoeItems.SILVER_HOE);
        register("gold_hoe", HoeItems.GOLD_HOE);
        register("rusted_iron_hoe", HoeItems.RUSTED_IRON_HOE);
        register("iron_hoe", HoeItems.IRON_HOE);
        register("ancient_metal_hoe", HoeItems.ANCIENT_METAL_HOE);
        register("mithril_hoe", HoeItems.MITHRIL_HOE);
        register("adamantium_hoe", HoeItems.ADAMANTIUM_HOE);

        // 剪刀
        register("copper_shears", ShearsItems.COPPER_SHEARS);
        register("silver_shears", ShearsItems.SILVER_SHEARS);
        register("gold_shears", ShearsItems.GOLD_SHEARS);
        register("rusted_iron_shears", ShearsItems.RUSTED_IRON_SHEARS);
        register("ancient_metal_shears", ShearsItems.ANCIENT_METAL_SHEARS);
        register("mithril_shears", ShearsItems.MITHRIL_SHEARS);
        register("adamantium_shears", ShearsItems.ADAMANTIUM_SHEARS);

        // 钓鱼竿
        register("flint_fishing_rod", FishingRodItems.FLINT_FISHING_ROD);
        register("obsidian_fishing_rod", FishingRodItems.OBSIDIAN_FISHING_ROD);
        register("copper_fishing_rod", FishingRodItems.COPPER_FISHING_ROD);
        register("silver_fishing_rod", FishingRodItems.SILVER_FISHING_ROD);
        register("gold_fishing_rod", FishingRodItems.GOLD_FISHING_ROD);
        register("iron_fishing_rod", FishingRodItems.IRON_FISHING_ROD);
        register("ancient_metal_fishing_rod", FishingRodItems.ANCIENT_METAL_FISHING_ROD);
        register("mithril_fishing_rod", FishingRodItems.MITHRIL_FISHING_ROD);
        register("adamantium_fishing_rod", FishingRodItems.ADAMANTIUM_FISHING_ROD);

        // 剑
        register("copper_sword", SwordItems.COPPER_SWORD);
        register("gold_sword", SwordItems.GOLD_SWORD);
        register("iron_sword", SwordItems.IRON_SWORD);
        register("silver_sword", SwordItems.SILVER_SWORD);
        register("rusted_iron_sword", SwordItems.RUSTED_IRON_SWORD);
        register("ancient_metal_sword", SwordItems.ANCIENT_METAL_SWORD);
        register("mithril_sword", SwordItems.MITHRIL_SWORD);
        register("adamantium_sword", SwordItems.ADAMANTIUM_SWORD);
    }

    private static void register(String name, Item item) {
        Registry.register(
                BuiltInRegistries.ITEM,
                Identifier.fromNamespaceAndPath("mite-recrafted", name),
                item
        );
        ModItems.addTool(item);
    }

    public static void init() {
        registerAll();
    }
}
