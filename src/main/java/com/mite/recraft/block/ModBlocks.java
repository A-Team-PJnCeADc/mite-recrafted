package com.mite.recraft.block;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.block.workbench.ModWorkbenchBlock;
import com.mite.recraft.block.workbench.WorkbenchMaterial;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

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
        registerWorkbenches();
    }

    //注册工作台方块
    private static void registerWorkbenches() {
        WorkbenchMaterial[] materials = WorkbenchMaterial.values();

        for (WorkbenchMaterial material : materials) {
            String name = material.getName() + "_workbench";

            Block.Properties properties = Block.Properties.ofFullCopy(Blocks.CRAFTING_TABLE);

            ModWorkbenchBlock workbench = register(
                    name,
                    (props) -> new ModWorkbenchBlock(material, props),
                    properties,
                    true
            );

            addBlocks(workbench);
        }
    }

    /**
     * 通用方块注册方法
     *
     * @param name 方块名称（不含模组ID前缀）
     * @param blockFactory 方块工厂函数，用于创建方块实例
     * @param properties 方块属性设置
     * @param shouldRegisterItem 是否同时注册对应的方块物品
     * @return 注册后的方块实例
     */
    private static <T extends Block> T register(String name, java.util.function.Function<Block.Properties, T> blockFactory, Block.Properties properties, boolean shouldRegisterItem) {
        ResourceKey<Block> blockKey = keyOfBlock(name);

        T block = blockFactory.apply(properties.setId(blockKey));

        if (shouldRegisterItem) {
            ResourceKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    //创建方块的资源键
    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(BuiltInRegistries.BLOCK.key(),
                net.minecraft.resources.Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, name));
    }

    //创建物品的资源键
    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(BuiltInRegistries.ITEM.key(),
                net.minecraft.resources.Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, name));
    }
}