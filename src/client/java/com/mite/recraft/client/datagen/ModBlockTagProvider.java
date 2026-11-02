package com.mite.recraft.client.datagen;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.content.block.metal.ModAnvilBlocks;
import com.mite.recraft.content.block.metal.ModBarBlocks;
import com.mite.recraft.content.block.metal.ModDoorBlocks;
import com.mite.recraft.content.block.metal.ModMetalBlocks;
import com.mite.recraft.content.block.strongbox.StrongboxType;
import com.mite.recraft.registry.ModBlockTags;
import com.mite.recraft.registry.ModFurnaces;
import com.mite.recraft.registry.ModStrongboxes;

import com.mite.recraft.chest.ChestVariant;
import com.mite.recraft.registry.ModChests;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

    public ModBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapper) {
        // mineable/pickaxe
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
        pickaxe.add(key(ModAnvilBlocks.COPPER_ANVIL));
        pickaxe.add(key(ModAnvilBlocks.SILVER_ANVIL));
        pickaxe.add(key(ModAnvilBlocks.GOLD_ANVIL));
        pickaxe.add(key(ModAnvilBlocks.ANCIENT_METAL_ANVIL));
        pickaxe.add(key(ModAnvilBlocks.MITHRIL_ANVIL));
        pickaxe.add(key(ModAnvilBlocks.ADAMANTIUM_ANVIL));

        // Strongboxes
        for (StrongboxType type : StrongboxType.VALUES) {
            pickaxe.add(key(ModStrongboxes.BLOCKS.get(type)));
        }
        // 箱子（金属等级）
        pickaxe.add(key(ModChests.block(ChestVariant.COPPER)));
        pickaxe.add(key(ModChests.block(ChestVariant.IRON)));
        pickaxe.add(key(ModChests.block(ChestVariant.GOLD)));
        pickaxe.add(key(ModChests.block(ChestVariant.DIAMOND)));
        pickaxe.add(key(ModChests.block(ChestVariant.EMERALD)));
        pickaxe.add(key(ModChests.block(ChestVariant.CRYSTAL)));
        pickaxe.add(key(ModChests.block(ChestVariant.OBSIDIAN)));
        pickaxe.add(key(ModChests.block(ChestVariant.NETHERITE)));
        // MITE 熔炉
        for (var furnace : ModFurnaces.getFurnaceBlocks()) {
            pickaxe.add(key(furnace));
        }

        // 原版 needs 标签：箱子按材质档位挂在对应工具等级
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(key(ModChests.block(ChestVariant.COPPER)))
                .add(key(ModChests.block(ChestVariant.GOLD)))
                .add(key(ModChests.block(ChestVariant.IRON)));
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(key(ModChests.block(ChestVariant.CRYSTAL)))
                .add(key(ModChests.block(ChestVariant.DIAMOND)))
                .add(key(ModChests.block(ChestVariant.EMERALD)));
        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(key(ModChests.block(ChestVariant.NETHERITE)))
                .add(key(ModChests.block(ChestVariant.OBSIDIAN)));

        // 圣诞箱可以斧头采集
        tag(BlockTags.MINEABLE_WITH_AXE).add(key(ModChests.block(ChestVariant.CHRISTMAS)));

        // 通用箱子标签，供其他模组识别
        tag(TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "chests")))
                .add(key(ModChests.block(ChestVariant.COPPER)))
                .add(key(ModChests.block(ChestVariant.IRON)))
                .add(key(ModChests.block(ChestVariant.GOLD)))
                .add(key(ModChests.block(ChestVariant.DIAMOND)))
                .add(key(ModChests.block(ChestVariant.EMERALD)))
                .add(key(ModChests.block(ChestVariant.CRYSTAL)))
                .add(key(ModChests.block(ChestVariant.OBSIDIAN)))
                .add(key(ModChests.block(ChestVariant.NETHERITE)))
                .add(key(ModChests.block(ChestVariant.CHRISTMAS)));

        // fastchest 兼容标签：水晶箱有自己的展示逻辑，不列入
        tag(TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("fastchest", "compatible_chests")))
                .add(key(ModChests.block(ChestVariant.CHRISTMAS)))
                .add(key(ModChests.block(ChestVariant.COPPER)))
                .add(key(ModChests.block(ChestVariant.DIAMOND)))
                .add(key(ModChests.block(ChestVariant.EMERALD)))
                .add(key(ModChests.block(ChestVariant.GOLD)))
                .add(key(ModChests.block(ChestVariant.IRON)))
                .add(key(ModChests.block(ChestVariant.NETHERITE)))
                .add(key(ModChests.block(ChestVariant.OBSIDIAN)));

        // needs 标签
        tag(ModBlockTags.NEEDS_COPPER_TOOL)
                .add(key(ModDoorBlocks.COPPER_DOOR)).add(key(ModBarBlocks.COPPER_BARS)).add(key(ModMetalBlocks.COPPER_BLOCK)).add(key(ModAnvilBlocks.COPPER_ANVIL))
                .add(key(ModChests.block(ChestVariant.COPPER)));
        tag(ModBlockTags.NEEDS_SILVER_TOOL)
                .add(key(ModDoorBlocks.SILVER_DOOR)).add(key(ModBarBlocks.SILVER_BARS)).add(key(ModMetalBlocks.SILVER_BLOCK)).add(key(ModAnvilBlocks.SILVER_ANVIL))
                .add(key(ModStrongboxes.BLOCKS.get(StrongboxType.SILVER)));
        tag(ModBlockTags.NEEDS_GOLD_TOOL)
                .add(key(ModDoorBlocks.GOLD_DOOR)).add(key(ModBarBlocks.GOLD_BARS)).add(key(ModMetalBlocks.GOLD_BLOCK)).add(key(ModAnvilBlocks.GOLD_ANVIL))
                .add(key(ModChests.block(ChestVariant.GOLD)));
        tag(ModBlockTags.NEEDS_RUSTED_IRON_TOOL);
        tag(ModBlockTags.NEEDS_IRON_TOOL)
                .add(key(ModBarBlocks.IRON_BARS)).add(key(ModMetalBlocks.IRON_BLOCK))
                .add(key(ModChests.block(ChestVariant.IRON)));
        tag(ModBlockTags.NEEDS_ANCIENT_METAL_TOOL).add(key(ModDoorBlocks.ANCIENT_METAL_DOOR)).add(key(ModBarBlocks.ANCIENT_METAL_BARS)).add(key(ModMetalBlocks.ANCIENT_METAL_BLOCK)).add(key(ModAnvilBlocks.ANCIENT_METAL_ANVIL))
                .add(key(ModStrongboxes.BLOCKS.get(StrongboxType.ANCIENT_METAL)))
                .add(key(ModChests.block(ChestVariant.DIAMOND))).add(key(ModChests.block(ChestVariant.EMERALD))).add(key(ModChests.block(ChestVariant.OBSIDIAN)));
        tag(ModBlockTags.NEEDS_MITHRIL_TOOL).add(key(ModDoorBlocks.MITHRIL_DOOR)).add(key(ModBarBlocks.MITHRIL_BARS)).add(key(ModMetalBlocks.MITHRIL_BLOCK)).add(key(ModAnvilBlocks.MITHRIL_ANVIL))
                .add(key(ModStrongboxes.BLOCKS.get(StrongboxType.MITHRIL)))
                .add(key(ModChests.block(ChestVariant.CRYSTAL)));
        tag(ModBlockTags.NEEDS_ADAMANTIUM_TOOL).add(key(ModDoorBlocks.ADAMANTIUM_DOOR)).add(key(ModBarBlocks.ADAMANTIUM_BARS)).add(key(ModMetalBlocks.ADAMANTIUM_BLOCK)).add(key(ModAnvilBlocks.ADAMANTIUM_ANVIL))
                .add(key(ModStrongboxes.BLOCKS.get(StrongboxType.ADAMANTIUM)))
                .add(key(ModChests.block(ChestVariant.NETHERITE)));

        tag(BlockTags.ANVIL).add(key(ModAnvilBlocks.COPPER_ANVIL)).add(key(ModAnvilBlocks.SILVER_ANVIL))
                .add(key(ModAnvilBlocks.GOLD_ANVIL)).add(key(ModAnvilBlocks.ANCIENT_METAL_ANVIL))
                .add(key(ModAnvilBlocks.MITHRIL_ANVIL)).add(key(ModAnvilBlocks.ADAMANTIUM_ANVIL));

        // incorrect 嵌套（含原版 needs 标签）
        // L0 燧石=黑曜石 -> 只能挖不在任何 needs 标签内的方块（木头/圆石/煤等）
        tag(ModBlockTags.INCORRECT_FOR_FLINT_TOOL)
                .addTag(BlockTags.NEEDS_STONE_TOOL).addTag(BlockTags.NEEDS_IRON_TOOL).addTag(BlockTags.NEEDS_DIAMOND_TOOL)
                .addTag(ModBlockTags.NEEDS_IRON_TOOL).addTag(ModBlockTags.NEEDS_ANCIENT_METAL_TOOL)
                .addTag(ModBlockTags.NEEDS_MITHRIL_TOOL).addTag(ModBlockTags.NEEDS_ADAMANTIUM_TOOL);
        tag(ModBlockTags.INCORRECT_FOR_OBSIDIAN_TOOL)
                .addTag(BlockTags.NEEDS_STONE_TOOL).addTag(BlockTags.NEEDS_IRON_TOOL).addTag(BlockTags.NEEDS_DIAMOND_TOOL)
                .addTag(ModBlockTags.NEEDS_IRON_TOOL).addTag(ModBlockTags.NEEDS_ANCIENT_METAL_TOOL)
                .addTag(ModBlockTags.NEEDS_MITHRIL_TOOL).addTag(ModBlockTags.NEEDS_ADAMANTIUM_TOOL);

        // L1 铜=银=金=锈铁 -> 可挖 needs_stone，挖不动 needs_iron 及以上
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

        // L2 铁 -> 可挖 needs_iron，挖不动 needs_diamond 及以上
        tag(ModBlockTags.INCORRECT_FOR_IRON_TOOL)
                .addTag(BlockTags.NEEDS_DIAMOND_TOOL)
                .addTag(ModBlockTags.NEEDS_MITHRIL_TOOL).addTag(ModBlockTags.NEEDS_ADAMANTIUM_TOOL);

        // L3 远古金属=钻石 -> 可挖 needs_diamond，挖不动秘银级及以上
        tag(ModBlockTags.INCORRECT_FOR_ANCIENT_METAL_TOOL)
                .addTag(ModBlockTags.NEEDS_MITHRIL_TOOL).addTag(ModBlockTags.NEEDS_ADAMANTIUM_TOOL);
        tag(ModBlockTags.INCORRECT_FOR_DIAMOND_TOOL)
                .addTag(ModBlockTags.NEEDS_MITHRIL_TOOL).addTag(ModBlockTags.NEEDS_ADAMANTIUM_TOOL);

        // L4 秘银 -> 可挖秘银级，挖不动艾德曼
        tag(ModBlockTags.INCORRECT_FOR_MITHRIL_TOOL).addTag(ModBlockTags.NEEDS_ADAMANTIUM_TOOL);

        // L5 艾德曼 -> 无限制
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
