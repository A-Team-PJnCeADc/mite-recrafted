package com.mite.recraft.item.tools.register;

import com.mite.recraft.item.ModItems;
import com.mite.recraft.item.tools.toolItem.AexItems;
import com.mite.recraft.item.tools.toolItem.BowItems;
import com.mite.recraft.item.tools.toolItem.ArrowItems;
import com.mite.recraft.item.tools.toolItem.BattleAxeItems;
import com.mite.recraft.item.tools.toolItem.DaggerItems;
import com.mite.recraft.item.tools.toolItem.FishingRodItems;
import com.mite.recraft.item.tools.toolItem.HatchetItems;
import com.mite.recraft.item.tools.toolItem.HoeItems;
import com.mite.recraft.item.tools.toolItem.KnifeItems;
import com.mite.recraft.item.tools.toolItem.MattockItems;
import com.mite.recraft.item.tools.toolItem.PickaxeItems;
import com.mite.recraft.item.tools.toolItem.ShearsItems;
import com.mite.recraft.item.tools.toolItem.ShovelItems;
import com.mite.recraft.item.tools.toolItem.ScytheItems;
import com.mite.recraft.item.tools.toolItem.WoodenItems;
import com.mite.recraft.item.tools.toolItem.SwordItems;
import com.mite.recraft.item.tools.toolItem.WarHammerItems;
import net.fabricmc.fabric.api.registry.FuelValueEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class ModToolRegister {
    public static void registerAll() {
        // 木棒/短木棒
        register("wood_club", WoodenItems.CLUB);
        register("wood_cudgel", WoodenItems.CUDGEL);

        // 弓
        register("wood_bow", BowItems.WOOD_BOW);
        register("ancient_metal_bow", BowItems.ANCIENT_METAL_BOW);
        register("mithril_bow", BowItems.MITHRIL_BOW);

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

        // 战斧（剑+斧混合工具，仅金属）
        register("copper_battle_axe", BattleAxeItems.COPPER_BATTLE_AXE);
        register("silver_battle_axe", BattleAxeItems.SILVER_BATTLE_AXE);
        register("gold_battle_axe", BattleAxeItems.GOLD_BATTLE_AXE);
        register("rusted_iron_battle_axe", BattleAxeItems.RUSTED_IRON_BATTLE_AXE);
        register("iron_battle_axe", BattleAxeItems.IRON_BATTLE_AXE);
        register("ancient_metal_battle_axe", BattleAxeItems.ANCIENT_METAL_BATTLE_AXE);
        register("mithril_battle_axe", BattleAxeItems.MITHRIL_BATTLE_AXE);
        register("adamantium_battle_axe", BattleAxeItems.ADAMANTIUM_BATTLE_AXE);

        // 战锤（镐+剑混合，高耐久，仅金属）
        register("copper_war_hammer", WarHammerItems.COPPER_WAR_HAMMER);
        register("silver_war_hammer", WarHammerItems.SILVER_WAR_HAMMER);
        register("gold_war_hammer", WarHammerItems.GOLD_WAR_HAMMER);
        register("rusted_iron_war_hammer", WarHammerItems.RUSTED_IRON_WAR_HAMMER);
        register("iron_war_hammer", WarHammerItems.IRON_WAR_HAMMER);
        register("ancient_metal_war_hammer", WarHammerItems.ANCIENT_METAL_WAR_HAMMER);
        register("mithril_war_hammer", WarHammerItems.MITHRIL_WAR_HAMMER);
        register("adamantium_war_hammer", WarHammerItems.ADAMANTIUM_WAR_HAMMER);

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

        // 镰刀（仅金属；锈铁镰刀无配方，靠僵尸掉落）
        register("copper_scythe", ScytheItems.COPPER_SCYTHE);
        register("silver_scythe", ScytheItems.SILVER_SCYTHE);
        register("gold_scythe", ScytheItems.GOLD_SCYTHE);
        register("rusted_iron_scythe", ScytheItems.RUSTED_IRON_SCYTHE);
        register("iron_scythe", ScytheItems.IRON_SCYTHE);
        register("ancient_metal_scythe", ScytheItems.ANCIENT_METAL_SCYTHE);
        register("mithril_scythe", ScytheItems.MITHRIL_SCYTHE);
        register("adamantium_scythe", ScytheItems.ADAMANTIUM_SCYTHE);

        // 鹤嘴锄（铲+锄混合工具，仅金属）
        register("copper_mattock", MattockItems.COPPER_MATTOCK);
        register("silver_mattock", MattockItems.SILVER_MATTOCK);
        register("gold_mattock", MattockItems.GOLD_MATTOCK);
        register("rusted_iron_mattock", MattockItems.RUSTED_IRON_MATTOCK);
        register("iron_mattock", MattockItems.IRON_MATTOCK);
        register("ancient_metal_mattock", MattockItems.ANCIENT_METAL_MATTOCK);
        register("mithril_mattock", MattockItems.MITHRIL_MATTOCK);
        register("adamantium_mattock", MattockItems.ADAMANTIUM_MATTOCK);

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

        // 短剑
        register("copper_dagger", DaggerItems.COPPER_DAGGER);
        register("silver_dagger", DaggerItems.SILVER_DAGGER);
        register("gold_dagger", DaggerItems.GOLD_DAGGER);
        register("rusted_iron_dagger", DaggerItems.RUSTED_IRON_DAGGER);
        register("iron_dagger", DaggerItems.IRON_DAGGER);
        register("ancient_metal_dagger", DaggerItems.ANCIENT_METAL_DAGGER);
        register("mithril_dagger", DaggerItems.MITHRIL_DAGGER);
        register("adamantium_dagger", DaggerItems.ADAMANTIUM_DAGGER);

        // 小刀
        register("flint_knife", KnifeItems.FLINT_KNIFE);
        register("obsidian_knife", KnifeItems.OBSIDIAN_KNIFE);

        // 箭
        register("flint_arrow", ArrowItems.FLINT_ARROW);
        register("obsidian_arrow", ArrowItems.OBSIDIAN_ARROW);
        register("copper_arrow", ArrowItems.COPPER_ARROW);
        register("silver_arrow", ArrowItems.SILVER_ARROW);
        register("gold_arrow", ArrowItems.GOLD_ARROW);
        register("rusted_iron_arrow", ArrowItems.RUSTED_IRON_ARROW);
        register("iron_arrow", ArrowItems.IRON_ARROW);
        register("ancient_metal_arrow", ArrowItems.ANCIENT_METAL_ARROW);
        register("mithril_arrow", ArrowItems.MITHRIL_ARROW);
        register("adamantium_arrow", ArrowItems.ADAMANTIUM_ARROW);
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
