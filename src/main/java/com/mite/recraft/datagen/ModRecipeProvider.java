package com.mite.recraft.datagen;

import com.mite.recraft.item.material.ModMaterials;
import com.mite.recraft.item.tools.toolItem.*;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {

                // ============ 斧头 (3材料) ============
                axe(Items.FLINT, AexItems.FLINT_AXE);
                axe(ModMaterials.OBSIDIAN_CHIP, AexItems.OBSIDIAN_AXE);
                axe(ModMaterials.COPPER_INGOT, AexItems.COPPER_AXE);
                axe(ModMaterials.SILVER_INGOT, AexItems.SILVER_AXE);
                axe(ModMaterials.GOLD_INGOT, AexItems.GOLD_AXE);
                axe(ModMaterials.IRON_INGOT, AexItems.RUSTED_IRON_AXE);
                axe(ModMaterials.IRON_INGOT, AexItems.IRON_AXE);
                axe(ModMaterials.ANCIENT_METAL_INGOT, AexItems.ANCIENT_METAL_AXE);
                axe(ModMaterials.MITHRIL_INGOT, AexItems.MITHRIL_AXE);
                axe(ModMaterials.ADAMANTIUM_INGOT, AexItems.ADAMANTIUM_AXE);

                // ============ 手斧 (2材料) ============
                hatchet(Items.FLINT, HatchetItems.FLINT_HATCHET);
                hatchet(ModMaterials.OBSIDIAN_CHIP, HatchetItems.OBSIDIAN_HATCHET);
                hatchet(ModMaterials.COPPER_INGOT, HatchetItems.COPPER_HATCHET);
                hatchet(ModMaterials.SILVER_INGOT, HatchetItems.SILVER_HATCHET);
                hatchet(ModMaterials.GOLD_INGOT, HatchetItems.GOLD_HATCHET);
                hatchet(ModMaterials.IRON_INGOT, HatchetItems.RUSTED_IRON_HATCHET);
                hatchet(ModMaterials.IRON_INGOT, HatchetItems.IRON_HATCHET);
                hatchet(ModMaterials.ANCIENT_METAL_INGOT, HatchetItems.ANCIENT_METAL_HATCHET);
                hatchet(ModMaterials.MITHRIL_INGOT, HatchetItems.MITHRIL_HATCHET);
                hatchet(ModMaterials.ADAMANTIUM_INGOT, HatchetItems.ADAMANTIUM_HATCHET);

                // ============ 战斧 (4材料+2棍, 仅金属) ============
                battleAxe(ModMaterials.COPPER_INGOT, BattleAxeItems.COPPER_BATTLE_AXE);
                battleAxe(ModMaterials.SILVER_INGOT, BattleAxeItems.SILVER_BATTLE_AXE);
                battleAxe(ModMaterials.GOLD_INGOT, BattleAxeItems.GOLD_BATTLE_AXE);
                battleAxe(ModMaterials.IRON_INGOT, BattleAxeItems.RUSTED_IRON_BATTLE_AXE);
                battleAxe(ModMaterials.IRON_INGOT, BattleAxeItems.IRON_BATTLE_AXE);
                battleAxe(ModMaterials.ANCIENT_METAL_INGOT, BattleAxeItems.ANCIENT_METAL_BATTLE_AXE);
                battleAxe(ModMaterials.MITHRIL_INGOT, BattleAxeItems.MITHRIL_BATTLE_AXE);
                battleAxe(ModMaterials.ADAMANTIUM_INGOT, BattleAxeItems.ADAMANTIUM_BATTLE_AXE);

                // ============ 战锤 (3材料+2棍, 高耐久, 仅金属) ============
                warHammer(ModMaterials.COPPER_INGOT, WarHammerItems.COPPER_WAR_HAMMER);
                warHammer(ModMaterials.SILVER_INGOT, WarHammerItems.SILVER_WAR_HAMMER);
                warHammer(ModMaterials.GOLD_INGOT, WarHammerItems.GOLD_WAR_HAMMER);
                warHammer(ModMaterials.IRON_INGOT, WarHammerItems.RUSTED_IRON_WAR_HAMMER);
                warHammer(ModMaterials.IRON_INGOT, WarHammerItems.IRON_WAR_HAMMER);
                warHammer(ModMaterials.ANCIENT_METAL_INGOT, WarHammerItems.ANCIENT_METAL_WAR_HAMMER);
                warHammer(ModMaterials.MITHRIL_INGOT, WarHammerItems.MITHRIL_WAR_HAMMER);
                warHammer(ModMaterials.ADAMANTIUM_INGOT, WarHammerItems.ADAMANTIUM_WAR_HAMMER);

                // ============ 镐 (3材料) ============
                pickaxe(ModMaterials.COPPER_INGOT, PickaxeItems.COPPER_PICKAXE);
                pickaxe(ModMaterials.SILVER_INGOT, PickaxeItems.SILVER_PICKAXE);
                pickaxe(ModMaterials.GOLD_INGOT, PickaxeItems.GOLD_PICKAXE);
                pickaxe(ModMaterials.IRON_INGOT, PickaxeItems.RUSTED_IRON_PICKAXE);
                pickaxe(ModMaterials.IRON_INGOT, PickaxeItems.IRON_PICKAXE);
                pickaxe(ModMaterials.ANCIENT_METAL_INGOT, PickaxeItems.ANCIENT_METAL_PICKAXE);
                pickaxe(ModMaterials.MITHRIL_INGOT, PickaxeItems.MITHRIL_PICKAXE);
                pickaxe(ModMaterials.ADAMANTIUM_INGOT, PickaxeItems.ADAMANTIUM_PICKAXE);

                // ============ 锹 (1材料) ============
                shovel(Items.FLINT, ShovelItems.FLINT_SHOVEL);
                shovel(ModMaterials.OBSIDIAN_CHIP, ShovelItems.OBSIDIAN_SHOVEL);
                shovel(ModMaterials.COPPER_INGOT, ShovelItems.COPPER_SHOVEL);
                shovel(ModMaterials.SILVER_INGOT, ShovelItems.SILVER_SHOVEL);
                shovel(ModMaterials.GOLD_INGOT, ShovelItems.GOLD_SHOVEL);
                shovel(ModMaterials.IRON_INGOT, ShovelItems.RUSTED_IRON_SHOVEL);
                shovel(ModMaterials.IRON_INGOT, ShovelItems.IRON_SHOVEL);
                shovel(ModMaterials.ANCIENT_METAL_INGOT, ShovelItems.ANCIENT_METAL_SHOVEL);
                shovel(ModMaterials.MITHRIL_INGOT, ShovelItems.MITHRIL_SHOVEL);
                shovel(ModMaterials.ADAMANTIUM_INGOT, ShovelItems.ADAMANTIUM_SHOVEL);

                // ============ 锄 (2材料, 仅金属) ============
                hoe(ModMaterials.COPPER_INGOT, HoeItems.COPPER_HOE);
                hoe(ModMaterials.SILVER_INGOT, HoeItems.SILVER_HOE);
                hoe(ModMaterials.GOLD_INGOT, HoeItems.GOLD_HOE);
                hoe(ModMaterials.IRON_INGOT, HoeItems.RUSTED_IRON_HOE);
                hoe(ModMaterials.IRON_INGOT, HoeItems.IRON_HOE);
                hoe(ModMaterials.ANCIENT_METAL_INGOT, HoeItems.ANCIENT_METAL_HOE);
                hoe(ModMaterials.MITHRIL_INGOT, HoeItems.MITHRIL_HOE);
                hoe(ModMaterials.ADAMANTIUM_INGOT, HoeItems.ADAMANTIUM_HOE);

                // ============ 鹤嘴锄 (2材料+2棍, 仅金属) ============
                mattock(ModMaterials.COPPER_INGOT, MattockItems.COPPER_MATTOCK);
                mattock(ModMaterials.SILVER_INGOT, MattockItems.SILVER_MATTOCK);
                mattock(ModMaterials.GOLD_INGOT, MattockItems.GOLD_MATTOCK);
                mattock(ModMaterials.IRON_INGOT, MattockItems.RUSTED_IRON_MATTOCK);
                mattock(ModMaterials.IRON_INGOT, MattockItems.IRON_MATTOCK);
                mattock(ModMaterials.ANCIENT_METAL_INGOT, MattockItems.ANCIENT_METAL_MATTOCK);
                mattock(ModMaterials.MITHRIL_INGOT, MattockItems.MITHRIL_MATTOCK);
                mattock(ModMaterials.ADAMANTIUM_INGOT, MattockItems.ADAMANTIUM_MATTOCK);

                // ============ 剪刀 (对角2锭, 仅金属) ============
                shears(ModMaterials.COPPER_INGOT, ShearsItems.COPPER_SHEARS);
                shears(ModMaterials.SILVER_INGOT, ShearsItems.SILVER_SHEARS);
                shears(ModMaterials.GOLD_INGOT, ShearsItems.GOLD_SHEARS);
                shears(ModMaterials.IRON_INGOT, ShearsItems.RUSTED_IRON_SHEARS);
                shears(ModMaterials.ANCIENT_METAL_INGOT, ShearsItems.ANCIENT_METAL_SHEARS);
                shears(ModMaterials.MITHRIL_INGOT, ShearsItems.MITHRIL_SHEARS);
                shears(ModMaterials.ADAMANTIUM_INGOT, ShearsItems.ADAMANTIUM_SHEARS);

                // ============ 钓鱼竿 (1粒/碎片+3棍+2线, AND解锁) ============
                fishingRod(ModMaterials.FLINT_CHIP, FishingRodItems.FLINT_FISHING_ROD);
                fishingRod(ModMaterials.OBSIDIAN_CHIP, FishingRodItems.OBSIDIAN_FISHING_ROD);
                fishingRod(ModMaterials.COPPER_NUGGET, FishingRodItems.COPPER_FISHING_ROD);
                fishingRod(ModMaterials.SILVER_NUGGET, FishingRodItems.SILVER_FISHING_ROD);
                fishingRod(Items.GOLD_NUGGET, FishingRodItems.GOLD_FISHING_ROD);
                fishingRod(ModMaterials.IRON_NUGGET, FishingRodItems.IRON_FISHING_ROD);
                fishingRod(ModMaterials.ANCIENT_METAL_NUGGET, FishingRodItems.ANCIENT_METAL_FISHING_ROD);
                fishingRod(ModMaterials.MITHRIL_NUGGET, FishingRodItems.MITHRIL_FISHING_ROD);
                fishingRod(ModMaterials.ADAMANTIUM_NUGGET, FishingRodItems.ADAMANTIUM_FISHING_ROD);

                // ============ 剑 (2锭+1棍) ============
                sword(ModMaterials.COPPER_INGOT, SwordItems.COPPER_SWORD);
                sword(ModMaterials.GOLD_INGOT, SwordItems.GOLD_SWORD);
                sword(ModMaterials.IRON_INGOT, SwordItems.IRON_SWORD);
                sword(ModMaterials.SILVER_INGOT, SwordItems.SILVER_SWORD);
                sword(ModMaterials.IRON_INGOT, SwordItems.RUSTED_IRON_SWORD);
                sword(ModMaterials.ANCIENT_METAL_INGOT, SwordItems.ANCIENT_METAL_SWORD);
                sword(ModMaterials.MITHRIL_INGOT, SwordItems.MITHRIL_SWORD);
                sword(ModMaterials.ADAMANTIUM_INGOT, SwordItems.ADAMANTIUM_SWORD);

                // ============ 短剑 (1锭+1棍, 2x1配方) ============
                dagger(ModMaterials.COPPER_INGOT, DaggerItems.COPPER_DAGGER);
                dagger(ModMaterials.SILVER_INGOT, DaggerItems.SILVER_DAGGER);
                dagger(ModMaterials.GOLD_INGOT, DaggerItems.GOLD_DAGGER);
                dagger(ModMaterials.IRON_INGOT, DaggerItems.RUSTED_IRON_DAGGER);
                dagger(ModMaterials.IRON_INGOT, DaggerItems.IRON_DAGGER);
                dagger(ModMaterials.ANCIENT_METAL_INGOT, DaggerItems.ANCIENT_METAL_DAGGER);
                dagger(ModMaterials.MITHRIL_INGOT, DaggerItems.MITHRIL_DAGGER);
                dagger(ModMaterials.ADAMANTIUM_INGOT, DaggerItems.ADAMANTIUM_DAGGER);

                // ============ 箭 (碎片/粒+棍+羽毛, 3x1) ============
                arrow(ModMaterials.FLINT_CHIP, ArrowItems.FLINT_ARROW);
                arrow(ModMaterials.OBSIDIAN_CHIP, ArrowItems.OBSIDIAN_ARROW);
                arrow(ModMaterials.COPPER_NUGGET, ArrowItems.COPPER_ARROW);
                arrow(ModMaterials.SILVER_NUGGET, ArrowItems.SILVER_ARROW);
                arrow(Items.GOLD_NUGGET, ArrowItems.GOLD_ARROW);
                arrow(ModMaterials.IRON_NUGGET, ArrowItems.RUSTED_IRON_ARROW);
                arrow(ModMaterials.IRON_NUGGET, ArrowItems.IRON_ARROW);
                arrow(ModMaterials.ANCIENT_METAL_NUGGET, ArrowItems.ANCIENT_METAL_ARROW);
                arrow(ModMaterials.MITHRIL_NUGGET, ArrowItems.MITHRIL_ARROW);
                arrow(ModMaterials.ADAMANTIUM_NUGGET, ArrowItems.ADAMANTIUM_ARROW);
            }

            void axe(ItemLike m, Item result) {
                shaped(RecipeCategory.TOOLS, result)
                        .pattern("MM ").pattern("MS ").pattern(" S ")
                        .define('M', m).define('S', Items.STICK)
                        .unlockedBy(getHasName(m), has(m)).save(output);
            }

            void hatchet(ItemLike m, Item result) {
                shaped(RecipeCategory.TOOLS, result)
                        .pattern("MS").pattern(" S")
                        .define('M', m).define('S', Items.STICK)
                        .unlockedBy(getHasName(m), has(m)).save(output);
            }

            void battleAxe(ItemLike m, Item result) {
                shaped(RecipeCategory.TOOLS, result)
                        .pattern("M M").pattern("MSM").pattern(" S ")
                        .define('M', m).define('S', Items.STICK)
                        .unlockedBy(getHasName(m), has(m)).save(output);
            }

            void warHammer(ItemLike m, Item result) {
                shaped(RecipeCategory.TOOLS, result)
                        .pattern("MMM").pattern("MSM").pattern(" S ")
                        .define('M', m).define('S', Items.STICK)
                        .unlockedBy(getHasName(m), has(m)).save(output);
            }

            void pickaxe(ItemLike m, Item result) {
                shaped(RecipeCategory.TOOLS, result)
                        .pattern("MMM").pattern(" S ").pattern(" S ")
                        .define('M', m).define('S', Items.STICK)
                        .unlockedBy(getHasName(m), has(m)).save(output);
            }

            void shovel(ItemLike m, Item result) {
                shaped(RecipeCategory.TOOLS, result)
                        .pattern("M").pattern("S").pattern("S")
                        .define('M', m).define('S', Items.STICK)
                        .unlockedBy(getHasName(m), has(m)).save(output);
            }

            void hoe(ItemLike m, Item result) {
                shaped(RecipeCategory.TOOLS, result)
                        .pattern("MM").pattern(" S").pattern(" S")
                        .define('M', m).define('S', Items.STICK)
                        .unlockedBy(getHasName(m), has(m)).save(output);
            }

            void mattock(ItemLike m, Item result) {
                shaped(RecipeCategory.TOOLS, result)
                        .pattern("MMM").pattern(" SM").pattern(" S ")
                        .define('M', m).define('S', Items.STICK)
                        .unlockedBy(getHasName(m), has(m)).save(output);
            }

            void shears(ItemLike m, Item result) {
                shaped(RecipeCategory.TOOLS, result)
                        .pattern("M ").pattern(" M")
                        .define('M', m)
                        .unlockedBy(getHasName(m), has(m)).save(output);
            }

            void fishingRod(ItemLike hook, Item result) {
                shaped(RecipeCategory.TOOLS, result)
                        .pattern("  S").pattern(" SL").pattern("SML")
                        .define('M', hook).define('S', Items.STICK).define('L', Items.STRING)
                        .unlockedBy(getHasName(Items.STRING), has(Items.STRING))
                        .unlockedBy(getHasName(hook), has(hook))
                        .save(output);
            }

            void sword(ItemLike m, Item result) {
                shaped(RecipeCategory.TOOLS, result)
                        .pattern("M").pattern("M").pattern("S")
                        .define('M', m).define('S', Items.STICK)
                        .unlockedBy(getHasName(m), has(m)).save(output);
            }

            void dagger(ItemLike m, Item result) {
                shaped(RecipeCategory.TOOLS, result)
                        .pattern("M").pattern("S")
                        .define('M', m).define('S', Items.STICK)
                        .unlockedBy(getHasName(m), has(m)).save(output);
            }

            void arrow(ItemLike tip, Item result) {
                shaped(RecipeCategory.COMBAT, result)
                        .pattern("M").pattern("S").pattern("F")
                        .define('M', tip).define('S', Items.STICK).define('F', Items.FEATHER)
                        .unlockedBy(getHasName(tip), has(tip)).save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "MITE Recrafted Recipes";
    }
}
