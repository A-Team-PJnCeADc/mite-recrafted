package com.mite.recraft.block.register;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.block.ModBlockType;
import com.mite.recraft.block.ModBlocks;
import com.mite.recraft.block.modbarblock.ModBarBlocks;
import com.mite.recraft.block.moddoorblock.ModDoorBlocks;
import com.mite.recraft.block.workbench.ModWorkbenchBlock;
import com.mite.recraft.block.workbench.WorkbenchMaterial;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * 方块统一注册器
 */
public class ModBlockRegister {
    public static void registerAll() {
        // 工作台
        for (WorkbenchMaterial material : WorkbenchMaterial.values()) {
            String name = material.getName() + "_workbench";
            Block.Properties props = Block.Properties.ofFullCopy(Blocks.CRAFTING_TABLE);
            ModWorkbenchBlock block = new ModWorkbenchBlock(material, props.setId(blockKey(name)));
            register(name, block);
            ModBlocks.addBlocks(block);
        }

        // 金属门
        register("copper_door", ModDoorBlocks.COPPER_DOOR);
        register("silver_door", ModDoorBlocks.SILVER_DOOR);
        register("gold_door", ModDoorBlocks.GOLD_DOOR);
        register("ancient_metal_door", ModDoorBlocks.ANCIENT_METAL_DOOR);
        register("mithril_door", ModDoorBlocks.MITHRIL_DOOR);
        register("adamantium_door", ModDoorBlocks.ADAMANTIUM_DOOR);
        for (Block door : new Block[]{ModDoorBlocks.COPPER_DOOR, ModDoorBlocks.SILVER_DOOR, ModDoorBlocks.GOLD_DOOR,
                ModDoorBlocks.ANCIENT_METAL_DOOR, ModDoorBlocks.MITHRIL_DOOR, ModDoorBlocks.ADAMANTIUM_DOOR}) {
            ModBlocks.addBlocks(door);
        }

        // 金属栅栏
        register("copper_bars", ModBarBlocks.COPPER_BARS);
        register("silver_bars", ModBarBlocks.SILVER_BARS);
        register("gold_bars", ModBarBlocks.GOLD_BARS);
        register("iron_bars", ModBarBlocks.IRON_BARS);
        register("ancient_metal_bars", ModBarBlocks.ANCIENT_METAL_BARS);
        register("mithril_bars", ModBarBlocks.MITHRIL_BARS);
        register("adamantium_bars", ModBarBlocks.ADAMANTIUM_BARS);
        for (Block bars : new Block[]{ModBarBlocks.COPPER_BARS, ModBarBlocks.SILVER_BARS, ModBarBlocks.GOLD_BARS,
                ModBarBlocks.IRON_BARS, ModBarBlocks.ANCIENT_METAL_BARS, ModBarBlocks.MITHRIL_BARS,
                ModBarBlocks.ADAMANTIUM_BARS}) {
            ModBlocks.addBlocks(bars);
        }
    }

    private static void register(String name, Block block) {
        int maxStackSize = ModBlockType.getMaxStackSize(block);
        Registry.register(BuiltInRegistries.BLOCK, blockKey(name), block);
        Registry.register(BuiltInRegistries.ITEM, itemKey(name),
                new BlockItem(block, new Item.Properties().stacksTo(maxStackSize)
                        .setId(itemKey(name)).useBlockDescriptionPrefix()));
    }

    private static ResourceKey<Block> blockKey(String name) {
        return ResourceKey.create(BuiltInRegistries.BLOCK.key(),
                Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, name));
    }

    private static ResourceKey<Item> itemKey(String name) {
        return ResourceKey.create(BuiltInRegistries.ITEM.key(),
                Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, name));
    }

    public static void init() {
        registerAll();
    }
}
