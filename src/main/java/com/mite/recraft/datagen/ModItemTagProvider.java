package com.mite.recraft.datagen;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.item.material.ModMaterials;
import com.mite.recraft.item.moditems.armor.ModArmorItem;
import com.mite.recraft.item.moditems.armor.ModArmorItems;
import com.mite.recraft.item.moditems.bucket.ModBucketItems;
import com.mite.recraft.item.tools.toolItem.*;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;


/**
 * 物品标签生成器 — repairs_* 修理材料标签
 */
public class ModItemTagProvider extends FabricTagsProvider.ItemTagsProvider {


    public static final TagKey<Item> BUTCHERING_WEAPON = TagKey.create(
            BuiltInRegistries.ITEM.key(),
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "butchering_weapon")
    );

    /** 击晕武器（木棒、短木棒、战锤） */
    public static final TagKey<Item> STUN_WEAPON = TagKey.create(
            BuiltInRegistries.ITEM.key(),
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "stun_weapon")
    );

    /** 吸血武器（金属剑/匕首/镰刀，不含银和秘银） */
    public static final TagKey<Item> VAMPIRIC_WEAPON = TagKey.create(
            BuiltInRegistries.ITEM.key(),
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "vampiric_weapon")
    );

    /** 杀害武器（金属战斧） */
    public static final TagKey<Item> SLAYING_WEAPON = TagKey.create(
            BuiltInRegistries.ITEM.key(),
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "slaying_weapon")
    );

    /** 收获工具（镰刀、锄、鹤嘴锄） */
    public static final TagKey<Item> HARVESTING_TOOL = TagKey.create(
            BuiltInRegistries.ITEM.key(),
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "harvesting_tool")
    );

    /** 穿透工具（镐） */
    public static final TagKey<Item> PIERCING_TOOL = TagKey.create(
            BuiltInRegistries.ITEM.key(),
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "piercing_tool")
    );

    /** 劈裂武器（金属战斧） */
    public static final TagKey<Item> CLEAVING_WEAPON = TagKey.create(
            BuiltInRegistries.ITEM.key(),
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "cleaving_weapon")
    );

    /** 砍伐工具（斧、战斧） */
    public static final TagKey<Item> TREE_FELLING_TOOL = TagKey.create(
            BuiltInRegistries.ITEM.key(),
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "tree_felling_tool")
    );
    public ModItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapper) {
        // 修理材料标签
        tag(repairs("repairs_copper")).add(key(ModMaterials.COPPER_NUGGET));
        tag(repairs("repairs_silver")).add(key(ModMaterials.SILVER_NUGGET));
        tag(repairs("repairs_iron")).add(key(ModMaterials.IRON_NUGGET));
        tag(repairs("repairs_ancient_metal")).add(key(ModMaterials.ANCIENT_METAL_NUGGET));
        tag(repairs("repairs_mithril")).add(key(ModMaterials.MITHRIL_NUGGET));
        tag(repairs("repairs_adamantium")).add(key(ModMaterials.ADAMANTIUM_NUGGET));
        tag(repairs("repairs_gold")).add(key(Items.GOLD_NUGGET));

        tag(repairs("repairs_flint")).add(key(Items.FLINT));
        tag(repairs("repairs_obsidian")).add(key(Items.OBSIDIAN));

        tag(repairs("repairs_wood")).add(key(Items.OAK_PLANKS));

        // 水桶标签 — 面团批量配方需要任意模组水桶
        var waterBuckets = TagKey.create(BuiltInRegistries.ITEM.key(),
                Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "water_buckets"));
        tag(waterBuckets)
                .add(key(ModBucketItems.COPPER_WATER_BUCKET))
                .add(key(ModBucketItems.SILVER_WATER_BUCKET))
                .add(key(ModBucketItems.GOLD_WATER_BUCKET))
                .add(key(ModBucketItems.ANCIENT_METAL_WATER_BUCKET))
                .add(key(ModBucketItems.MITHRIL_WATER_BUCKET))
                .add(key(ModBucketItems.ADAMANTIUM_WATER_BUCKET));

        // 箭矢标签 — 弓通过 minecraft:arrows 标签识别
        var arrowsTag = builder(ItemTags.ARROWS);
        arrowsTag.add(key(ArrowItems.FLINT_ARROW));
        arrowsTag.add(key(ArrowItems.OBSIDIAN_ARROW));
        arrowsTag.add(key(ArrowItems.COPPER_ARROW));
        arrowsTag.add(key(ArrowItems.SILVER_ARROW));
        arrowsTag.add(key(ArrowItems.GOLD_ARROW));
        arrowsTag.add(key(ArrowItems.RUSTED_IRON_ARROW));
        arrowsTag.add(key(ArrowItems.IRON_ARROW));
        arrowsTag.add(key(ArrowItems.ANCIENT_METAL_ARROW));
        arrowsTag.add(key(ArrowItems.MITHRIL_ARROW));
        arrowsTag.add(key(ArrowItems.ADAMANTIUM_ARROW));

        // 线标签 — 原版线 + 皮革线合并
        var strings = TagKey.create(BuiltInRegistries.ITEM.key(),
                Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "strings"));
        tag(strings)
                .add(key(Items.STRING))
                .add(key(ModMaterials.SINEW));

        // 短剑武器标签
        tag(BUTCHERING_WEAPON)
                .add(key(DaggerItems.COPPER_DAGGER))
                .add(key(DaggerItems.SILVER_DAGGER))
                .add(key(DaggerItems.GOLD_DAGGER))
                .add(key(DaggerItems.RUSTED_IRON_DAGGER))
                .add(key(DaggerItems.IRON_DAGGER))
                .add(key(DaggerItems.ANCIENT_METAL_DAGGER))
                .add(key(DaggerItems.MITHRIL_DAGGER))
                .add(key(DaggerItems.ADAMANTIUM_DAGGER));

        // 击晕武器标签 — 木棒 + 短木棒 + 战锤
        tag(STUN_WEAPON)
                .add(key(WoodenItems.CLUB)).add(key(WoodenItems.CUDGEL))
                .add(key(WarHammerItems.COPPER_WAR_HAMMER))
                .add(key(WarHammerItems.SILVER_WAR_HAMMER))
                .add(key(WarHammerItems.GOLD_WAR_HAMMER))
                .add(key(WarHammerItems.RUSTED_IRON_WAR_HAMMER))
                .add(key(WarHammerItems.IRON_WAR_HAMMER))
                .add(key(WarHammerItems.ANCIENT_METAL_WAR_HAMMER))
                .add(key(WarHammerItems.MITHRIL_WAR_HAMMER))
                .add(key(WarHammerItems.ADAMANTIUM_WAR_HAMMER));

        // 吸血武器标签 — 金属剑/匕首/镰刀（不含银和秘银）
        tag(VAMPIRIC_WEAPON)
                .add(key(SwordItems.COPPER_SWORD)).add(key(SwordItems.GOLD_SWORD))
                .add(key(SwordItems.IRON_SWORD)).add(key(SwordItems.RUSTED_IRON_SWORD))
                .add(key(SwordItems.ANCIENT_METAL_SWORD)).add(key(SwordItems.ADAMANTIUM_SWORD))
                .add(key(DaggerItems.COPPER_DAGGER)).add(key(DaggerItems.GOLD_DAGGER))
                .add(key(DaggerItems.IRON_DAGGER)).add(key(DaggerItems.RUSTED_IRON_DAGGER))
                .add(key(DaggerItems.ANCIENT_METAL_DAGGER)).add(key(DaggerItems.ADAMANTIUM_DAGGER))
                .add(key(ScytheItems.COPPER_SCYTHE)).add(key(ScytheItems.GOLD_SCYTHE))
                .add(key(ScytheItems.RUSTED_IRON_SCYTHE))
                .add(key(ScytheItems.IRON_SCYTHE))
                .add(key(ScytheItems.ANCIENT_METAL_SCYTHE)).add(key(ScytheItems.ADAMANTIUM_SCYTHE));

        // 杀害武器标签 — 全部金属战斧
        tag(SLAYING_WEAPON)
                .add(key(WarHammerItems.COPPER_WAR_HAMMER))
                .add(key(WarHammerItems.SILVER_WAR_HAMMER))
                .add(key(WarHammerItems.GOLD_WAR_HAMMER))
                .add(key(WarHammerItems.RUSTED_IRON_WAR_HAMMER))
                .add(key(WarHammerItems.IRON_WAR_HAMMER))
                .add(key(WarHammerItems.ANCIENT_METAL_WAR_HAMMER))
                .add(key(WarHammerItems.MITHRIL_WAR_HAMMER))
                .add(key(WarHammerItems.ADAMANTIUM_WAR_HAMMER));

        // 收获工具标签 — 镰刀 + 锄 + 鹤嘴锄
        tag(HARVESTING_TOOL)
                .add(key(ScytheItems.COPPER_SCYTHE)).add(key(ScytheItems.SILVER_SCYTHE))
                .add(key(ScytheItems.GOLD_SCYTHE)).add(key(ScytheItems.RUSTED_IRON_SCYTHE))
                .add(key(ScytheItems.IRON_SCYTHE)).add(key(ScytheItems.ANCIENT_METAL_SCYTHE))
                .add(key(ScytheItems.MITHRIL_SCYTHE)).add(key(ScytheItems.ADAMANTIUM_SCYTHE))
                .add(key(HoeItems.COPPER_HOE)).add(key(HoeItems.SILVER_HOE))
                .add(key(HoeItems.GOLD_HOE)).add(key(HoeItems.RUSTED_IRON_HOE))
                .add(key(HoeItems.IRON_HOE)).add(key(HoeItems.ANCIENT_METAL_HOE))
                .add(key(HoeItems.MITHRIL_HOE)).add(key(HoeItems.ADAMANTIUM_HOE))
                .add(key(MattockItems.COPPER_MATTOCK)).add(key(MattockItems.SILVER_MATTOCK))
                .add(key(MattockItems.GOLD_MATTOCK)).add(key(MattockItems.RUSTED_IRON_MATTOCK))
                .add(key(MattockItems.IRON_MATTOCK)).add(key(MattockItems.ANCIENT_METAL_MATTOCK))
                .add(key(MattockItems.MITHRIL_MATTOCK)).add(key(MattockItems.ADAMANTIUM_MATTOCK));

        // 穿透工具标签 — 全部镐
        tag(PIERCING_TOOL)
                .add(key(PickaxeItems.COPPER_PICKAXE)).add(key(PickaxeItems.SILVER_PICKAXE))
                .add(key(PickaxeItems.GOLD_PICKAXE)).add(key(PickaxeItems.RUSTED_IRON_PICKAXE))
                .add(key(PickaxeItems.IRON_PICKAXE)).add(key(PickaxeItems.ANCIENT_METAL_PICKAXE))
                .add(key(PickaxeItems.MITHRIL_PICKAXE)).add(key(PickaxeItems.ADAMANTIUM_PICKAXE));

        // 劈裂武器标签 — 全部金属战斧
        tag(CLEAVING_WEAPON)
                .add(key(WarHammerItems.COPPER_WAR_HAMMER))
                .add(key(WarHammerItems.SILVER_WAR_HAMMER))
                .add(key(WarHammerItems.GOLD_WAR_HAMMER))
                .add(key(WarHammerItems.RUSTED_IRON_WAR_HAMMER))
                .add(key(WarHammerItems.IRON_WAR_HAMMER))
                .add(key(WarHammerItems.ANCIENT_METAL_WAR_HAMMER))
                .add(key(WarHammerItems.MITHRIL_WAR_HAMMER))
                .add(key(WarHammerItems.ADAMANTIUM_WAR_HAMMER));

        // 砍伐工具标签 — 斧 + 战斧
        tag(TREE_FELLING_TOOL)
                .add(key(AexItems.FLINT_AXE)).add(key(AexItems.OBSIDIAN_AXE))
                .add(key(AexItems.COPPER_AXE)).add(key(AexItems.SILVER_AXE))
                .add(key(AexItems.GOLD_AXE)).add(key(AexItems.RUSTED_IRON_AXE))
                .add(key(AexItems.IRON_AXE)).add(key(AexItems.ANCIENT_METAL_AXE))
                .add(key(AexItems.MITHRIL_AXE)).add(key(AexItems.ADAMANTIUM_AXE))
                .add(key(BattleAxeItems.COPPER_BATTLE_AXE)).add(key(BattleAxeItems.SILVER_BATTLE_AXE))
                .add(key(BattleAxeItems.GOLD_BATTLE_AXE)).add(key(BattleAxeItems.RUSTED_IRON_BATTLE_AXE))
                .add(key(BattleAxeItems.IRON_BATTLE_AXE)).add(key(BattleAxeItems.ANCIENT_METAL_BATTLE_AXE))
                .add(key(BattleAxeItems.MITHRIL_BATTLE_AXE)).add(key(BattleAxeItems.ADAMANTIUM_BATTLE_AXE))
                .add(key(WarHammerItems.COPPER_WAR_HAMMER))
                .add(key(WarHammerItems.SILVER_WAR_HAMMER))
                .add(key(WarHammerItems.GOLD_WAR_HAMMER))
                .add(key(WarHammerItems.RUSTED_IRON_WAR_HAMMER))
                .add(key(WarHammerItems.IRON_WAR_HAMMER))
                .add(key(WarHammerItems.ANCIENT_METAL_WAR_HAMMER))
                .add(key(WarHammerItems.MITHRIL_WAR_HAMMER))
                .add(key(WarHammerItems.ADAMANTIUM_WAR_HAMMER));

        // 弓附魔标签
        var bowEnchantable = builder(ItemTags.BOW_ENCHANTABLE);
        bowEnchantable.add(key(Items.BOW));
        bowEnchantable.add(key(BowItems.WOOD_BOW));
        bowEnchantable.add(key(BowItems.ANCIENT_METAL_BOW));
        bowEnchantable.add(key(BowItems.MITHRIL_BOW));

        // 盔甲附魔标签 — 让 MITE 盔甲支持保护/耐久等原版附魔
        var armorEnch = builder(ItemTags.ARMOR_ENCHANTABLE);
        var legArmorEnch = builder(ItemTags.LEG_ARMOR_ENCHANTABLE);
        var chestArmorEnch = builder(ItemTags.CHEST_ARMOR_ENCHANTABLE);
        var footArmorEnch = builder(ItemTags.FOOT_ARMOR_ENCHANTABLE);
        for (Item item : ModArmorItems.ALL) {
            if (item instanceof ModArmorItem armor) {
                armorEnch.add(key(item));
                switch (armor.piece()) {
                    case HELMET -> {}  // 头盔没有专门的 tag
                    case CHESTPLATE -> chestArmorEnch.add(key(item));
                    case LEGGINGS -> legArmorEnch.add(key(item));
                    case BOOTS -> footArmorEnch.add(key(item));
                }
            }
        }
    }

    private static TagKey<Item> repairs(String path) {
        return TagKey.create(BuiltInRegistries.ITEM.key(),
                Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, path));
    }

    private static ResourceKey<Item> key(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow();
    }

    @Override
    public String getName() {
        return MiteRecrafted.MOD_ID + " Item Tags";
    }
}
