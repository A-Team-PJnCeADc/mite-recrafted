package com.mite.recraft.fabric.client.datagen;

import com.mite.recraft.chest.ChestVariant;
import com.mite.recraft.content.block.metal.ModAnvilBlocks;
import com.mite.recraft.content.block.metal.ModBarBlocks;
import com.mite.recraft.content.block.metal.ModDoorBlocks;
import com.mite.recraft.content.block.metal.ModMetalBlocks;
import com.mite.recraft.registry.ModChests;
import com.mite.recraft.registry.ModFurnaces;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;

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

        // 金属砧（保留 damage NBT）
        Block[] anvils = {ModAnvilBlocks.COPPER_ANVIL, ModAnvilBlocks.SILVER_ANVIL, ModAnvilBlocks.GOLD_ANVIL,
                ModAnvilBlocks.ANCIENT_METAL_ANVIL, ModAnvilBlocks.MITHRIL_ANVIL, ModAnvilBlocks.ADAMANTIUM_ANVIL};
        for (Block anvil : anvils) {
            this.add(anvil, createSingleItemTable(anvil)
                    .apply(net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction
                            .copyComponentsFromBlockEntity(net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_ENTITY)
                            .include(DataComponents.DAMAGE)
                            .build()));
        }

        // MITE 熔炉
        for (Block furnace : ModFurnaces.getFurnaceBlocks()) {
            dropSelf(furnace);
        }

        // 箱子：掉落自身并保留自定义名称
        for (ChestVariant variant : ChestVariant.REGISTERED) {
            Block chest = ModChests.block(variant);
            this.add(chest, createSingleItemTable(chest)
                    .apply(CopyNameFunction.copyName(
                            LootContext.BlockEntityTarget.BLOCK_ENTITY)));
        }
    }
}
