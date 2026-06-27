package com.mite.recraft.block.modblock;

import com.mite.recraft.block.ModBlockType;
import net.minecraft.world.level.block.Block;

/**
 * 金属块
 */
public class ModMetalBlocks {
    public static final Block COPPER_BLOCK        = ModBlockType.METAL_BLOCK.create("copper",         5.0F);
    public static final Block SILVER_BLOCK        = ModBlockType.METAL_BLOCK.create("silver",         5.0F);
    public static final Block GOLD_BLOCK          = ModBlockType.METAL_BLOCK.create("gold",           3.0F);
    public static final Block IRON_BLOCK          = ModBlockType.METAL_BLOCK.create("iron",           5.0F);
    public static final Block ANCIENT_METAL_BLOCK = ModBlockType.METAL_BLOCK.create("ancient_metal",  5.0F);
    public static final Block MITHRIL_BLOCK       = ModBlockType.METAL_BLOCK.create("mithril",        8.0F);
    public static final Block ADAMANTIUM_BLOCK    = ModBlockType.METAL_BLOCK.create("adamantium",    12.0F);
}
