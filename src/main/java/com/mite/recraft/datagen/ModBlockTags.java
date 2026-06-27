package com.mite.recraft.datagen;

import com.mite.recraft.MiteRecrafted;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/**
 * MITE 方块标签常量 —— 供 ModToolMaterial 和 ModBlockTagProvider 共享
 */
public final class ModBlockTags {
    private ModBlockTags() {}

    // needs 标签
    public static final TagKey<Block> NEEDS_COPPER_TOOL = tag("needs_copper_tool");
    public static final TagKey<Block> NEEDS_SILVER_TOOL = tag("needs_silver_tool");
    public static final TagKey<Block> NEEDS_GOLD_TOOL = tag("needs_gold_tool");
    public static final TagKey<Block> NEEDS_RUSTED_IRON_TOOL = tag("needs_rusted_iron_tool");
    public static final TagKey<Block> NEEDS_IRON_TOOL = tag("needs_iron_tool");
    public static final TagKey<Block> NEEDS_ANCIENT_METAL_TOOL = tag("needs_ancient_metal_tool");
    public static final TagKey<Block> NEEDS_MITHRIL_TOOL = tag("needs_mithril_tool");
    public static final TagKey<Block> NEEDS_ADAMANTIUM_TOOL = tag("needs_adamantium_tool");

    // incorrect 标签
    public static final TagKey<Block> INCORRECT_FOR_FLINT_TOOL = tag("incorrect_for_flint_tool");
    public static final TagKey<Block> INCORRECT_FOR_OBSIDIAN_TOOL = tag("incorrect_for_obsidian_tool");
    public static final TagKey<Block> INCORRECT_FOR_COPPER_TOOL = tag("incorrect_for_copper_tool");
    public static final TagKey<Block> INCORRECT_FOR_SILVER_TOOL = tag("incorrect_for_silver_tool");
    public static final TagKey<Block> INCORRECT_FOR_GOLD_TOOL = tag("incorrect_for_gold_tool");
    public static final TagKey<Block> INCORRECT_FOR_RUSTED_IRON_TOOL = tag("incorrect_for_rusted_iron_tool");
    public static final TagKey<Block> INCORRECT_FOR_IRON_TOOL = tag("incorrect_for_iron_tool");
    public static final TagKey<Block> INCORRECT_FOR_ANCIENT_METAL_TOOL = tag("incorrect_for_ancient_metal_tool");
    public static final TagKey<Block> INCORRECT_FOR_DIAMOND_TOOL = tag("incorrect_for_diamond_tool");
    public static final TagKey<Block> INCORRECT_FOR_MITHRIL_TOOL = tag("incorrect_for_mithril_tool");
    public static final TagKey<Block> INCORRECT_FOR_ADAMANTIUM_TOOL = tag("incorrect_for_adamantium_tool");

    private static TagKey<Block> tag(String name) {
        return TagKey.create(Registries.BLOCK,
                Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, name));
    }
}
