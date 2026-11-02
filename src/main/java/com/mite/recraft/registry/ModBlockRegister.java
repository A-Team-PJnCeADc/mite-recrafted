package com.mite.recraft.registry;

import com.mite.recraft.api.ItemCategory;
import com.mite.recraft.api.MiteIds;
import com.mite.recraft.content.block.ModBlockType;
import com.mite.recraft.content.block.anvil.ModAnvilBlock;
import com.mite.recraft.content.block.metal.ModAnvilBlocks;
import com.mite.recraft.content.block.metal.ModBarBlocks;
import com.mite.recraft.content.block.metal.ModDoorBlocks;
import com.mite.recraft.content.block.metal.ModMetalBlocks;
import com.mite.recraft.content.block.workbench.ModWorkbenchBlock;
import com.mite.recraft.content.block.workbench.WorkbenchMaterial;
import com.mite.recraft.content.item.tool.ModToolMaterial;
import com.mite.recraft.registry.ModFurnaces;
import com.mite.recraft.registry.ModStrongboxes;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * 方块与 BlockItem 注册，按方块类型分派到统一的注册路径。
 */
public final class ModBlockRegister {

    private ModBlockRegister() {
    }

    public static void register() {
        ModAnvilBlock.registerBlockEntity();

        for (WorkbenchMaterial material : WorkbenchMaterial.values()) {
            String path = material.getName() + "_workbench";
            ModWorkbenchBlock block = new ModWorkbenchBlock(material,
                    Block.Properties.ofFullCopy(Blocks.CRAFTING_TABLE).setId(MiteIds.blockKey(path)));
            registerBlockItem(path, block);
        }

        registerBlockItem("copper_door", ModDoorBlocks.COPPER_DOOR);
        registerBlockItem("silver_door", ModDoorBlocks.SILVER_DOOR);
        registerBlockItem("gold_door", ModDoorBlocks.GOLD_DOOR);
        registerBlockItem("ancient_metal_door", ModDoorBlocks.ANCIENT_METAL_DOOR);
        registerBlockItem("mithril_door", ModDoorBlocks.MITHRIL_DOOR);
        registerBlockItem("adamantium_door", ModDoorBlocks.ADAMANTIUM_DOOR);

        registerBlockItem("copper_bars", ModBarBlocks.COPPER_BARS);
        registerBlockItem("silver_bars", ModBarBlocks.SILVER_BARS);
        registerBlockItem("gold_bars", ModBarBlocks.GOLD_BARS);
        registerBlockItem("iron_bars", ModBarBlocks.IRON_BARS);
        registerBlockItem("ancient_metal_bars", ModBarBlocks.ANCIENT_METAL_BARS);
        registerBlockItem("mithril_bars", ModBarBlocks.MITHRIL_BARS);
        registerBlockItem("adamantium_bars", ModBarBlocks.ADAMANTIUM_BARS);

        registerBlockItem("copper_block", ModMetalBlocks.COPPER_BLOCK);
        registerBlockItem("silver_block", ModMetalBlocks.SILVER_BLOCK);
        registerBlockItem("gold_block", ModMetalBlocks.GOLD_BLOCK);
        registerBlockItem("iron_block", ModMetalBlocks.IRON_BLOCK);
        registerBlockItem("ancient_metal_block", ModMetalBlocks.ANCIENT_METAL_BLOCK);
        registerBlockItem("mithril_block", ModMetalBlocks.MITHRIL_BLOCK);
        registerBlockItem("adamantium_block", ModMetalBlocks.ADAMANTIUM_BLOCK);

        registerAnvil("copper_anvil", ModAnvilBlocks.COPPER_ANVIL);
        registerAnvil("silver_anvil", ModAnvilBlocks.SILVER_ANVIL);
        registerAnvil("gold_anvil", ModAnvilBlocks.GOLD_ANVIL);
        registerAnvil("ancient_metal_anvil", ModAnvilBlocks.ANCIENT_METAL_ANVIL);
        registerAnvil("mithril_anvil", ModAnvilBlocks.MITHRIL_ANVIL);
        registerAnvil("adamantium_anvil", ModAnvilBlocks.ADAMANTIUM_ANVIL);

        ModFurnaces.register();
        ModStrongboxes.register();
    }

    private static void registerBlockItem(String path, Block block) {
        int maxStackSize = ModBlockType.getMaxStackSize(block);
        Registry.register(BuiltInRegistries.BLOCK, MiteIds.blockKey(path), block);
        ModBlocks.add(block);

        ResourceKey<Item> itemKey = MiteIds.itemKey(path);
        BlockItem item = new BlockItem(block, new Item.Properties()
                .stacksTo(maxStackSize)
                .setId(itemKey)
                .useBlockDescriptionPrefix());
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        ModItems.add(ItemCategory.BLOCK, item);
    }

    private static void registerAnvil(String path, Block block) {
        if (!(block instanceof ModAnvilBlock anvil)) {
            return;
        }

        int maxStackSize = ModBlockType.getMaxStackSize(block);
        Registry.register(BuiltInRegistries.BLOCK, MiteIds.blockKey(path), block);
        ModBlocks.add(block);

        ResourceKey<Item> itemKey = MiteIds.itemKey(path);
        var props = new Item.Properties()
                .setId(itemKey)
                .useBlockDescriptionPrefix()
                .stacksTo(maxStackSize)
                .component(DataComponents.MAX_DAMAGE, anvil.getDurability());

        // 砧物品沿用工具修理组件，使其可用对应材料的粒修复
        ModToolMaterial material = anvil.getModToolMaterial();
        if (material != null) {
            props.component(ModDataComponents.TOOL_COMPONENTS, 31)
                    .component(ModDataComponents.TOOL_MATERIAL_TIER, material.getDurabilityCoefficient())
                    .component(ModDataComponents.TOOL_REPAIR_TAG, material.getRepairTagName());
        }

        BlockItem item = new BlockItem(block, props);
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        ModItems.add(ItemCategory.BLOCK, item);
    }
}
