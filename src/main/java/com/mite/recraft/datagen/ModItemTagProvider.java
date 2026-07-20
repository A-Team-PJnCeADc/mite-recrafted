package com.mite.recraft.datagen;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.item.moditems.bucket.ModBucketItems;
import com.mite.recraft.item.material.ModMaterials;
import com.mite.recraft.item.tools.toolItem.ArrowItems;
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
        tag(repairs("repairs_rusted_iron")).add(key(ModMaterials.IRON_NUGGET));

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
