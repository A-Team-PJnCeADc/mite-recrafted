package com.mite.recraft.api;

import com.mite.recraft.content.block.strongbox.StrongboxType;
import com.mite.recraft.registry.ModBlocks;
import com.mite.recraft.registry.ModFurnaces;
import com.mite.recraft.registry.ModItems;
import com.mite.recraft.registry.ModStrongboxes;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Map;

/**
 * 已注册内容的只读查询入口。
 */
public final class MiteContent {

    private MiteContent() {
    }

    /** 某一物品栏分类下的物品，按注册顺序。 */
    public static List<Item> items(ItemCategory category) {
        return ModItems.get(category);
    }

    /** 全部方块物品来源方块，按注册顺序。 */
    public static List<Block> blocks() {
        return ModBlocks.getBlocks();
    }

    /** 全部熔炉方块。 */
    public static List<Block> furnaces() {
        return ModFurnaces.getFurnaceBlocks();
    }

    /** Strongbox 类型 -> 方块。 */
    public static Map<StrongboxType, Block> strongboxes() {
        return ModStrongboxes.blocks();
    }
}
