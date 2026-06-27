package com.mite.recraft.block.modblock;

import com.mite.recraft.block.ModBlockType;
import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.world.level.block.Block;

/**
 * 金属砧实例
 */
public class ModAnvilBlocks {
    public static final Block COPPER_ANVIL        = ModBlockType.ANVIL.create("copper",         5.0F, ModToolMaterial.COPPER);
    public static final Block SILVER_ANVIL        = ModBlockType.ANVIL.create("silver",         5.0F, ModToolMaterial.SILVER);
    public static final Block GOLD_ANVIL          = ModBlockType.ANVIL.create("gold",           3.0F, ModToolMaterial.GOLD);
    public static final Block ANCIENT_METAL_ANVIL = ModBlockType.ANVIL.create("ancient_metal",  5.0F, ModToolMaterial.ANCIENT_METAL);
    public static final Block MITHRIL_ANVIL       = ModBlockType.ANVIL.create("mithril",        8.0F, ModToolMaterial.MITHRIL);
    public static final Block ADAMANTIUM_ANVIL    = ModBlockType.ANVIL.create("adamantium",    12.0F, ModToolMaterial.ADAMANTIUM);
}
