package com.mite.recraft.datagen;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.item.material.ModMaterials;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
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
