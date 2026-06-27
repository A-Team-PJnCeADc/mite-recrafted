package com.mite.recraft.block.register;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.block.ModBlockType;
import com.mite.recraft.block.ModBlocks;
import com.mite.recraft.block.anvil.ModAnvilBlock;
import com.mite.recraft.block.modblock.ModAnvilBlocks;

import com.mite.recraft.block.modblock.ModBarBlocks;
import com.mite.recraft.block.modblock.ModDoorBlocks;
import com.mite.recraft.block.modblock.ModMetalBlocks;
import com.mite.recraft.block.workbench.ModWorkbenchBlock;
import com.mite.recraft.block.workbench.WorkbenchMaterial;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
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
        // BlockEntity 注册
        ModAnvilBlock.registerBlockEntity();

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

        // 金属块
        register("copper_block", ModMetalBlocks.COPPER_BLOCK);
        register("silver_block", ModMetalBlocks.SILVER_BLOCK);
        register("gold_block", ModMetalBlocks.GOLD_BLOCK);
        register("iron_block", ModMetalBlocks.IRON_BLOCK);
        register("ancient_metal_block", ModMetalBlocks.ANCIENT_METAL_BLOCK);
        register("mithril_block", ModMetalBlocks.MITHRIL_BLOCK);
        register("adamantium_block", ModMetalBlocks.ADAMANTIUM_BLOCK);
        for (Block block : new Block[]{ModMetalBlocks.COPPER_BLOCK, ModMetalBlocks.SILVER_BLOCK, ModMetalBlocks.GOLD_BLOCK,
                ModMetalBlocks.IRON_BLOCK, ModMetalBlocks.ANCIENT_METAL_BLOCK, ModMetalBlocks.MITHRIL_BLOCK,
                ModMetalBlocks.ADAMANTIUM_BLOCK}) {
            ModBlocks.addBlocks(block);
        }

        // 金属砧（自定义 BlockItem 带耐久）
        registerAnvil("copper_anvil", ModAnvilBlocks.COPPER_ANVIL);
        registerAnvil("silver_anvil", ModAnvilBlocks.SILVER_ANVIL);
        registerAnvil("gold_anvil", ModAnvilBlocks.GOLD_ANVIL);
        registerAnvil("ancient_metal_anvil", ModAnvilBlocks.ANCIENT_METAL_ANVIL);
        registerAnvil("mithril_anvil", ModAnvilBlocks.MITHRIL_ANVIL);
        registerAnvil("adamantium_anvil", ModAnvilBlocks.ADAMANTIUM_ANVIL);
        for (Block block : new Block[]{ModAnvilBlocks.COPPER_ANVIL, ModAnvilBlocks.SILVER_ANVIL, ModAnvilBlocks.GOLD_ANVIL,
                ModAnvilBlocks.ANCIENT_METAL_ANVIL, ModAnvilBlocks.MITHRIL_ANVIL, ModAnvilBlocks.ADAMANTIUM_ANVIL}) {
            ModBlocks.addBlocks(block);
        }
    }

    private static void register(String name, Block block) {
        int maxStackSize = ModBlockType.getMaxStackSize(block);
        Registry.register(BuiltInRegistries.BLOCK, blockKey(name), block);
        Registry.register(BuiltInRegistries.ITEM, itemKey(name),
                new BlockItem(block, new Item.Properties().stacksTo(maxStackSize)
                        .setId(itemKey(name)).useBlockDescriptionPrefix()));
    }

    private static void registerAnvil(String name, Block block) {
        if (!(block instanceof ModAnvilBlock anvil)) return;
        Registry.register(BuiltInRegistries.BLOCK, blockKey(name), block);
        int maxStackSize = ModBlockType.getMaxStackSize(block);
        var props = new Item.Properties()
                .setId(itemKey(name)).useBlockDescriptionPrefix()
                .stacksTo(maxStackSize)
                .component(DataComponents.MAX_DAMAGE, anvil.getDurability());
        Registry.register(BuiltInRegistries.ITEM, itemKey(name),
                new BlockItem(block, props));
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
