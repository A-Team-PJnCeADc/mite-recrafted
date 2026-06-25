package com.mite.recraft.item.tools.toolItem;

import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.world.item.Item;

/**
 * 小刀（Knife）
 */
public class KnifeItems {
    public static final Item FLINT_KNIFE       = ToolType.KNIFE.create(ModToolMaterial.FLINT, 1.0F, -1.5F);
    public static final Item OBSIDIAN_KNIFE    = ToolType.KNIFE.create(ModToolMaterial.OBSIDIAN, 2.0F, -1.5F);
}
