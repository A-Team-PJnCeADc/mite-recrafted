package com.mite.recraft.datagen;

import com.mite.recraft.item.tools.toolItem.ArrowItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagsProvider.ItemTagsProvider {

    public ModItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapper) {
        var tag = builder(ItemTags.ARROWS);
        for (Item arrow : new Item[]{
                ArrowItems.FLINT_ARROW, ArrowItems.OBSIDIAN_ARROW,
                ArrowItems.COPPER_ARROW, ArrowItems.SILVER_ARROW,
                ArrowItems.GOLD_ARROW, ArrowItems.RUSTED_IRON_ARROW,
                ArrowItems.IRON_ARROW, ArrowItems.ANCIENT_METAL_ARROW,
                ArrowItems.MITHRIL_ARROW, ArrowItems.ADAMANTIUM_ARROW
        }) {
            tag.add(BuiltInRegistries.ITEM.getResourceKey(arrow).orElseThrow());
        }
    }
}
