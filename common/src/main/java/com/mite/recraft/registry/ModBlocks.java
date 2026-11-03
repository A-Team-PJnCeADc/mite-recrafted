package com.mite.recraft.registry;

import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 方块登记表：记录已注册的方块，供创造模式物品栏与数据生成遍历。
 */
public final class ModBlocks {

    private static final List<Block> BLOCKS = new ArrayList<>();

    private ModBlocks() {
    }

    public static void add(Block block) {
        BLOCKS.add(block);
    }

    public static List<Block> getBlocks() {
        return Collections.unmodifiableList(BLOCKS);
    }
}
