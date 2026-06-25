package com.mite.recraft.item.tools.toolItem;

import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 战锤（War Hammer）—— numComponents=5，高耐久，镐+剑+可挖南瓜
 */
public class WarHammerItems {
    public static final Item COPPER_WAR_HAMMER      = create(ModToolMaterial.COPPER, 8.0F, -3.2F);
    public static final Item SILVER_WAR_HAMMER      = create(ModToolMaterial.SILVER, 8.0F, -3.2F);
    public static final Item GOLD_WAR_HAMMER        = create(ModToolMaterial.GOLD, 7.0F, -3.2F);
    public static final Item RUSTED_IRON_WAR_HAMMER = create(ModToolMaterial.RUSTED_IRON, 7.0F, -3.2F);
    public static final Item IRON_WAR_HAMMER        = create(ModToolMaterial.IRON, 7.0F, -3.2F);
    public static final Item ANCIENT_METAL_WAR_HAMMER = create(ModToolMaterial.ANCIENT_METAL, 9.0F, -3.2F);
    public static final Item MITHRIL_WAR_HAMMER     = create(ModToolMaterial.MITHRIL, 10.0F, -3.2F);
    public static final Item ADAMANTIUM_WAR_HAMMER  = create(ModToolMaterial.ADAMANTIUM, 11.0F, -3.2F);

    private static Item create(ModToolMaterial mat, float dmg, float speed) {
        ToolMaterial tm = ToolType.WAR_HAMMER.customToolMaterial(mat);
        return new WarHammerItem(tm, dmg, speed,
                new Item.Properties()
                        .setId(ToolType.WAR_HAMMER.itemKey(mat))
                        .pickaxe(tm, dmg, speed));
    }

    /** 战锤：镐挖掘 + 可挖南瓜 + 战斗属性 */
    public static class WarHammerItem extends Item {
        private final ToolMaterial material;
        WarHammerItem(ToolMaterial material, float a, float s, Properties p) { super(p); this.material = material; }
        @Override public float getDestroySpeed(ItemStack stack, BlockState state) {
            if (state.is(Blocks.PUMPKIN) || state.is(Blocks.CARVED_PUMPKIN)) return material.speed();
            return super.getDestroySpeed(stack, state);
        }
        @Override public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
            if (state.is(Blocks.PUMPKIN) || state.is(Blocks.CARVED_PUMPKIN)) return true;
            return super.isCorrectToolForDrops(stack, state);
        }
    }
}
