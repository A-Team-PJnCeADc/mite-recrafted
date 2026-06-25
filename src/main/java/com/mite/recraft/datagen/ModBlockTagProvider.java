package com.mite.recraft.datagen;

import com.mite.recraft.MiteRecrafted;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {


    static final TagKey<Block> NEEDS_COPPER_TOOL = tag("needs_copper_tool");
    static final TagKey<Block> NEEDS_SILVER_TOOL = tag("needs_silver_tool");
    static final TagKey<Block> NEEDS_GOLD_TOOL = tag("needs_gold_tool");
    static final TagKey<Block> NEEDS_RUSTED_IRON_TOOL = tag("needs_rusted_iron_tool");
    static final TagKey<Block> NEEDS_IRON_TOOL = tag("needs_iron_tool");
    static final TagKey<Block> NEEDS_ANCIENT_METAL_TOOL = tag("needs_ancient_metal_tool");
    static final TagKey<Block> NEEDS_MITHRIL_TOOL = tag("needs_mithril_tool");
    static final TagKey<Block> NEEDS_ADAMANTIUM_TOOL = tag("needs_adamantium_tool");

    // 工具 incorrect 标签
    static final TagKey<Block> INCORRECT_FOR_FLINT_TOOL = tag("incorrect_for_flint_tool");
    static final TagKey<Block> INCORRECT_FOR_OBSIDIAN_TOOL = tag("incorrect_for_obsidian_tool");
    static final TagKey<Block> INCORRECT_FOR_COPPER_TOOL = tag("incorrect_for_copper_tool");
    static final TagKey<Block> INCORRECT_FOR_SILVER_TOOL = tag("incorrect_for_silver_tool");
    static final TagKey<Block> INCORRECT_FOR_GOLD_TOOL = tag("incorrect_for_gold_tool");
    static final TagKey<Block> INCORRECT_FOR_RUSTED_IRON_TOOL = tag("incorrect_for_rusted_iron_tool");
    static final TagKey<Block> INCORRECT_FOR_IRON_TOOL = tag("incorrect_for_iron_tool");
    static final TagKey<Block> INCORRECT_FOR_ANCIENT_METAL_TOOL = tag("incorrect_for_ancient_metal_tool");
    static final TagKey<Block> INCORRECT_FOR_DIAMOND_TOOL = tag("incorrect_for_diamond_tool");
    static final TagKey<Block> INCORRECT_FOR_MITHRIL_TOOL = tag("incorrect_for_mithril_tool");
    static final TagKey<Block> INCORRECT_FOR_ADAMANTIUM_TOOL = tag("incorrect_for_adamantium_tool");

    public ModBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapper) {
        //todo 填写needs
        tag(NEEDS_COPPER_TOOL); tag(NEEDS_SILVER_TOOL); tag(NEEDS_GOLD_TOOL); tag(NEEDS_RUSTED_IRON_TOOL);
        tag(NEEDS_IRON_TOOL);
        tag(NEEDS_ANCIENT_METAL_TOOL);
        tag(NEEDS_MITHRIL_TOOL);
        tag(NEEDS_ADAMANTIUM_TOOL);

        // L0 燧石=黑曜石 → 可挖铜级，挖不动铁级及以上
        tag(INCORRECT_FOR_FLINT_TOOL).addTag(NEEDS_IRON_TOOL).addTag(NEEDS_ANCIENT_METAL_TOOL).addTag(NEEDS_MITHRIL_TOOL).addTag(NEEDS_ADAMANTIUM_TOOL);
        tag(INCORRECT_FOR_OBSIDIAN_TOOL).addTag(NEEDS_IRON_TOOL).addTag(NEEDS_ANCIENT_METAL_TOOL).addTag(NEEDS_MITHRIL_TOOL).addTag(NEEDS_ADAMANTIUM_TOOL);

        // L1 铜=银=金=锈铁 → 可挖铁级，挖不动远古金属级及以上
        tag(INCORRECT_FOR_COPPER_TOOL).addTag(NEEDS_ANCIENT_METAL_TOOL).addTag(NEEDS_MITHRIL_TOOL).addTag(NEEDS_ADAMANTIUM_TOOL);
        tag(INCORRECT_FOR_SILVER_TOOL).addTag(NEEDS_ANCIENT_METAL_TOOL).addTag(NEEDS_MITHRIL_TOOL).addTag(NEEDS_ADAMANTIUM_TOOL);
        tag(INCORRECT_FOR_GOLD_TOOL).addTag(NEEDS_ANCIENT_METAL_TOOL).addTag(NEEDS_MITHRIL_TOOL).addTag(NEEDS_ADAMANTIUM_TOOL);
        tag(INCORRECT_FOR_RUSTED_IRON_TOOL).addTag(NEEDS_ANCIENT_METAL_TOOL).addTag(NEEDS_MITHRIL_TOOL).addTag(NEEDS_ADAMANTIUM_TOOL);

        // L2 铁 → 可挖远古金属级，挖不动秘银级及以上
        tag(INCORRECT_FOR_IRON_TOOL).addTag(NEEDS_MITHRIL_TOOL).addTag(NEEDS_ADAMANTIUM_TOOL);

        // L3 远古金属=钻石 → 可挖秘银级，挖不动艾德曼
        tag(INCORRECT_FOR_ANCIENT_METAL_TOOL).addTag(NEEDS_ADAMANTIUM_TOOL);
        tag(INCORRECT_FOR_DIAMOND_TOOL).addTag(NEEDS_ADAMANTIUM_TOOL);

        // L4 秘银 → 可挖艾德曼级
        tag(INCORRECT_FOR_MITHRIL_TOOL);

        // L5 艾德曼
        tag(INCORRECT_FOR_ADAMANTIUM_TOOL);
    }

    @Override
    public String getName() {
        return MiteRecrafted.MOD_ID + " Block Tags";
    }

    private static TagKey<Block> tag(String name) {
        return TagKey.create(Registries.BLOCK,
                Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, name));
    }
}
