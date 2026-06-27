package com.mite.recraft.item.tools.toolItem;

import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 镰刀 — 高草/成熟小麦加速收割
 */
public class ScytheItems extends Item {
    private static final float SYCTHE_SPEED = 15.0F;

    public static final Item COPPER_SCYTHE        = ToolType.SCYTHE.create(ModToolMaterial.COPPER, 4.0F, -3.0F);
    public static final Item SILVER_SCYTHE        = ToolType.SCYTHE.create(ModToolMaterial.SILVER, 4.0F, -3.0F);
    public static final Item GOLD_SCYTHE          = ToolType.SCYTHE.create(ModToolMaterial.GOLD, 4.0F, -3.0F);
    public static final Item RUSTED_IRON_SCYTHE   = ToolType.SCYTHE.create(ModToolMaterial.RUSTED_IRON, 4.0F, -3.0F);
    public static final Item IRON_SCYTHE          = ToolType.SCYTHE.create(ModToolMaterial.IRON, 4.0F, -3.0F);
    public static final Item ANCIENT_METAL_SCYTHE = ToolType.SCYTHE.create(ModToolMaterial.ANCIENT_METAL, 5.0F, -3.0F);
    public static final Item MITHRIL_SCYTHE       = ToolType.SCYTHE.create(ModToolMaterial.MITHRIL, 6.0F, -3.0F);
    public static final Item ADAMANTIUM_SCYTHE    = ToolType.SCYTHE.create(ModToolMaterial.ADAMANTIUM, 7.0F, -3.0F);

    public ScytheItems(ToolMaterial tm, float dmg, float speed, Properties props) {
        super(props.hoe(tm, dmg, speed));
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Block block = state.getBlock();
        // 高草类瞬间收割
        if (block == Blocks.SHORT_GRASS || block == Blocks.TALL_GRASS
                || block == Blocks.FERN || block == Blocks.LARGE_FERN) {
            return SYCTHE_SPEED;
        }
        // 成熟小麦加速（其他作物无加成）
        if (block instanceof CropBlock crop && crop.isMaxAge(state)
                && crop == Blocks.WHEAT) {
            return SYCTHE_SPEED;
        }
        return super.getDestroySpeed(stack, state);
    }
}
