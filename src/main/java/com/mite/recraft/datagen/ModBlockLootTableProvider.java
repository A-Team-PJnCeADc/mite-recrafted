package com.mite.recraft.datagen;

import com.mite.recraft.block.modblock.ModBarBlocks;
import com.mite.recraft.block.modblock.ModDoorBlocks;
import com.mite.recraft.block.modblock.ModMetalBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ModBlockLootTableProvider extends FabricBlockLootSubProvider {

    public ModBlockLootTableProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generate() {
        // 门
        add(ModDoorBlocks.COPPER_DOOR,        this::createDoorTable);
        add(ModDoorBlocks.SILVER_DOOR,        this::createDoorTable);
        add(ModDoorBlocks.GOLD_DOOR,          this::createDoorTable);
        add(ModDoorBlocks.ANCIENT_METAL_DOOR, this::createDoorTable);
        add(ModDoorBlocks.MITHRIL_DOOR,       this::createDoorTable);
        add(ModDoorBlocks.ADAMANTIUM_DOOR,    this::createDoorTable);

        // 栅栏
        dropSelf(ModBarBlocks.COPPER_BARS);
        dropSelf(ModBarBlocks.SILVER_BARS);
        dropSelf(ModBarBlocks.GOLD_BARS);
        dropSelf(ModBarBlocks.IRON_BARS);
        dropSelf(ModBarBlocks.ANCIENT_METAL_BARS);
        dropSelf(ModBarBlocks.MITHRIL_BARS);
        dropSelf(ModBarBlocks.ADAMANTIUM_BARS);

        // 金属块
        dropSelf(ModMetalBlocks.COPPER_BLOCK);
        dropSelf(ModMetalBlocks.SILVER_BLOCK);
        dropSelf(ModMetalBlocks.GOLD_BLOCK);
        dropSelf(ModMetalBlocks.IRON_BLOCK);
        dropSelf(ModMetalBlocks.ANCIENT_METAL_BLOCK);
        dropSelf(ModMetalBlocks.MITHRIL_BLOCK);
        dropSelf(ModMetalBlocks.ADAMANTIUM_BLOCK);
    }
}
