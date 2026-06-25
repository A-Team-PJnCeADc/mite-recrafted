package com.mite.recraft.block;

import com.mite.recraft.block.register.ModBlockRegister;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModBlocks {
    private static final List<Block> BLOCKS = new ArrayList<>();

    public static void addBlocks(Block block) {
        BLOCKS.add(block);
    }

    public static List<Block> getBlocks() {
        return Collections.unmodifiableList(BLOCKS);
    }

    public static void init() {
        ModBlockRegister.init();
    }
}
