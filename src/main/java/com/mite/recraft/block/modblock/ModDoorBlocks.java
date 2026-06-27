package com.mite.recraft.block.modblock;

import com.mite.recraft.block.ModBlockType;
import net.minecraft.world.level.block.Block;

/**
 * 金属门 —— 同铁门，仅红石可开
 */
public class ModDoorBlocks {
    public static final Block COPPER_DOOR        = ModBlockType.DOOR.create("copper",         5.0F);
    public static final Block SILVER_DOOR        = ModBlockType.DOOR.create("silver",         5.0F);
    public static final Block GOLD_DOOR          = ModBlockType.DOOR.create("gold",           3.0F);
    public static final Block ANCIENT_METAL_DOOR = ModBlockType.DOOR.create("ancient_metal",  5.0F);
    public static final Block MITHRIL_DOOR       = ModBlockType.DOOR.create("mithril",        8.0F);
    public static final Block ADAMANTIUM_DOOR    = ModBlockType.DOOR.create("adamantium",    12.0F);
}
