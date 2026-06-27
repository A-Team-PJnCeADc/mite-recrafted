package com.mite.recraft.datagen;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.block.modblock.ModBarBlocks;
import com.mite.recraft.block.modblock.ModDoorBlocks;
import com.mite.recraft.block.modblock.ModMetalBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

    public ModBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapper) {
        // ── mineable/pickaxe ──
        var pickaxe = tag(BlockTags.MINEABLE_WITH_PICKAXE);
        pickaxe.add(key(ModDoorBlocks.COPPER_DOOR));
        pickaxe.add(key(ModDoorBlocks.SILVER_DOOR));
        pickaxe.add(key(ModDoorBlocks.GOLD_DOOR));
        pickaxe.add(key(ModDoorBlocks.ANCIENT_METAL_DOOR));
        pickaxe.add(key(ModDoorBlocks.MITHRIL_DOOR));
        pickaxe.add(key(ModDoorBlocks.ADAMANTIUM_DOOR));
        pickaxe.add(key(ModBarBlocks.COPPER_BARS));
        pickaxe.add(key(ModBarBlocks.SILVER_BARS));
        pickaxe.add(key(ModBarBlocks.GOLD_BARS));
        pickaxe.add(key(ModBarBlocks.IRON_BARS));
        pickaxe.add(key(ModBarBlocks.ANCIENT_METAL_BARS));
        pickaxe.add(key(ModBarBlocks.MITHRIL_BARS));
        pickaxe.add(key(ModBarBlocks.ADAMANTIUM_BARS));
        pickaxe.add(key(ModMetalBlocks.COPPER_BLOCK));
        pickaxe.add(key(ModMetalBlocks.SILVER_BLOCK));
        pickaxe.add(key(ModMetalBlocks.GOLD_BLOCK));
        pickaxe.add(key(ModMetalBlocks.IRON_BLOCK));
        pickaxe.add(key(ModMetalBlocks.ANCIENT_METAL_BLOCK));
        pickaxe.add(key(ModMetalBlocks.MITHRIL_BLOCK));
        pickaxe.add(key(ModMetalBlocks.ADAMANTIUM_BLOCK));

        // 原版 needs 标签
        tag(BlockTags.NEEDS_STONE_TOOL);
        tag(BlockTags.NEEDS_IRON_TOOL);
        tag(BlockTags.NEEDS_DIAMOND_TOOL);

        // ── needs 标签 ──
        tag(ModBlockTags.NEEDS_COPPER_TOOL).add(key(ModDoorBlocks.COPPER_DOOR)).add(key(ModBarBlocks.COPPER_BARS)).add(key(ModMetalBlocks.COPPER_BLOCK));
        tag(ModBlockTags.NEEDS_SILVER_TOOL).add(key(ModDoorBlocks.SILVER_DOOR)).add(key(ModBarBlocks.SILVER_BARS)).add(key(ModMetalBlocks.SILVER_BLOCK));
        tag(ModBlockTags.NEEDS_GOLD_TOOL).add(key(ModDoorBlocks.GOLD_DOOR)).add(key(ModBarBlocks.GOLD_BARS)).add(key(ModMetalBlocks.GOLD_BLOCK));
        tag(ModBlockTags.NEEDS_RUSTED_IRON_TOOL);
        tag(ModBlockTags.NEEDS_IRON_TOOL).add(key(ModBarBlocks.IRON_BARS)).add(key(ModMetalBlocks.IRON_BLOCK));
        tag(ModBlockTags.NEEDS_ANCIENT_METAL_TOOL).add(key(ModDoorBlocks.ANCIENT_METAL_DOOR)).add(key(ModBarBlocks.ANCIENT_METAL_BARS)).add(key(ModMetalBlocks.ANCIENT_METAL_BLOCK));
        tag(ModBlockTags.NEEDS_MITHRIL_TOOL).add(key(ModDoorBlocks.MITHRIL_DOOR)).add(key(ModBarBlocks.MITHRIL_BARS)).add(key(ModMetalBlocks.MITHRIL_BLOCK));
        tag(ModBlockTags.NEEDS_ADAMANTIUM_TOOL).add(key(ModDoorBlocks.ADAMANTIUM_DOOR)).add(key(ModBarBlocks.ADAMANTIUM_BARS)).add(key(ModMetalBlocks.ADAMANTIUM_BLOCK));

        // ── incorrect 嵌套（含原版 needs 标签）──
        // L0 燧石=黑曜石 → 只能挖不在任何 needs 标签内的方块（木头/圆石/煤等）
        tag(ModBlockTags.INCORRECT_FOR_FLINT_TOOL)
                .addTag(BlockTags.NEEDS_STONE_TOOL).addTag(BlockTags.NEEDS_IRON_TOOL).addTag(BlockTags.NEEDS_DIAMOND_TOOL)
                .addTag(ModBlockTags.NEEDS_IRON_TOOL).addTag(ModBlockTags.NEEDS_ANCIENT_METAL_TOOL)
                .addTag(ModBlockTags.NEEDS_MITHRIL_TOOL).addTag(ModBlockTags.NEEDS_ADAMANTIUM_TOOL);
        tag(ModBlockTags.INCORRECT_FOR_OBSIDIAN_TOOL)
                .addTag(BlockTags.NEEDS_STONE_TOOL).addTag(BlockTags.NEEDS_IRON_TOOL).addTag(BlockTags.NEEDS_DIAMOND_TOOL)
                .addTag(ModBlockTags.NEEDS_IRON_TOOL).addTag(ModBlockTags.NEEDS_ANCIENT_METAL_TOOL)
                .addTag(ModBlockTags.NEEDS_MITHRIL_TOOL).addTag(ModBlockTags.NEEDS_ADAMANTIUM_TOOL);

        // L1 铜=银=金=锈铁 → 可挖 needs_stone，挖不动 needs_iron 及以上
        tag(ModBlockTags.INCORRECT_FOR_COPPER_TOOL)
                .addTag(BlockTags.NEEDS_IRON_TOOL).addTag(BlockTags.NEEDS_DIAMOND_TOOL)
                .addTag(ModBlockTags.NEEDS_ANCIENT_METAL_TOOL)
                .addTag(ModBlockTags.NEEDS_MITHRIL_TOOL).addTag(ModBlockTags.NEEDS_ADAMANTIUM_TOOL);
        tag(ModBlockTags.INCORRECT_FOR_SILVER_TOOL)
                .addTag(BlockTags.NEEDS_IRON_TOOL).addTag(BlockTags.NEEDS_DIAMOND_TOOL)
                .addTag(ModBlockTags.NEEDS_ANCIENT_METAL_TOOL)
                .addTag(ModBlockTags.NEEDS_MITHRIL_TOOL).addTag(ModBlockTags.NEEDS_ADAMANTIUM_TOOL);
        tag(ModBlockTags.INCORRECT_FOR_GOLD_TOOL)
                .addTag(BlockTags.NEEDS_IRON_TOOL).addTag(BlockTags.NEEDS_DIAMOND_TOOL)
                .addTag(ModBlockTags.NEEDS_ANCIENT_METAL_TOOL)
                .addTag(ModBlockTags.NEEDS_MITHRIL_TOOL).addTag(ModBlockTags.NEEDS_ADAMANTIUM_TOOL);
        tag(ModBlockTags.INCORRECT_FOR_RUSTED_IRON_TOOL)
                .addTag(BlockTags.NEEDS_IRON_TOOL).addTag(BlockTags.NEEDS_DIAMOND_TOOL)
                .addTag(ModBlockTags.NEEDS_ANCIENT_METAL_TOOL)
                .addTag(ModBlockTags.NEEDS_MITHRIL_TOOL).addTag(ModBlockTags.NEEDS_ADAMANTIUM_TOOL);

        // L2 铁 → 可挖 needs_iron，挖不动 needs_diamond 及以上
        tag(ModBlockTags.INCORRECT_FOR_IRON_TOOL)
                .addTag(BlockTags.NEEDS_DIAMOND_TOOL)
                .addTag(ModBlockTags.NEEDS_MITHRIL_TOOL).addTag(ModBlockTags.NEEDS_ADAMANTIUM_TOOL);

        // L3 远古金属=钻石 → 可挖 needs_diamond，挖不动秘银级及以上
        tag(ModBlockTags.INCORRECT_FOR_ANCIENT_METAL_TOOL)
                .addTag(ModBlockTags.NEEDS_MITHRIL_TOOL).addTag(ModBlockTags.NEEDS_ADAMANTIUM_TOOL);
        tag(ModBlockTags.INCORRECT_FOR_DIAMOND_TOOL)
                .addTag(ModBlockTags.NEEDS_MITHRIL_TOOL).addTag(ModBlockTags.NEEDS_ADAMANTIUM_TOOL);

        // L4 秘银 → 可挖秘银级，挖不动艾德曼
        tag(ModBlockTags.INCORRECT_FOR_MITHRIL_TOOL).addTag(ModBlockTags.NEEDS_ADAMANTIUM_TOOL);

        // L5 艾德曼 → 无限制
        tag(ModBlockTags.INCORRECT_FOR_ADAMANTIUM_TOOL);
    }

    @Override
    public String getName() {
        return MiteRecrafted.MOD_ID + " Block Tags";
    }

    private static ResourceKey<Block> key(Block block) {
        return BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow();
    }
}
