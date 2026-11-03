package com.mite.recraft.registry;

import com.mite.recraft.api.ItemCategory;
import com.mite.recraft.api.MiteIds;
import com.mite.recraft.api.MiteRegistration;
import com.mite.recraft.content.block.ModBlockType;
import com.mite.recraft.content.block.anvil.ModAnvilBlock;
import com.mite.recraft.content.block.metal.ModAnvilBlocks;
import com.mite.recraft.content.block.metal.ModBarBlocks;
import com.mite.recraft.content.block.metal.ModDoorBlocks;
import com.mite.recraft.content.block.metal.ModMetalBlocks;
import com.mite.recraft.content.block.workbench.ModWorkbenchBlock;
import com.mite.recraft.content.block.workbench.WorkbenchMaterial;
import com.mite.recraft.content.item.tool.ModToolMaterial;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * 方块与 BlockItem 注册。方块写入方块表，BlockItem 通过 {@link MiteRegistration#deferItem}
 * 留到物品阶段，保证每个阶段只写一张注册表。
 */
public final class ModBlockRegister {

    private ModBlockRegister() {
    }

    public static void registerBlocks() {
        for (WorkbenchMaterial material : WorkbenchMaterial.values()) {
            String path = material.getName() + "_workbench";
            ModWorkbenchBlock block = new ModWorkbenchBlock(material,
                    Block.Properties.ofFullCopy(Blocks.CRAFTING_TABLE).setId(MiteIds.blockKey(path)));
            registerBlock(path, block);
        }

        registerBlock("copper_door", ModDoorBlocks.COPPER_DOOR);
        registerBlock("silver_door", ModDoorBlocks.SILVER_DOOR);
        registerBlock("gold_door", ModDoorBlocks.GOLD_DOOR);
        registerBlock("ancient_metal_door", ModDoorBlocks.ANCIENT_METAL_DOOR);
        registerBlock("mithril_door", ModDoorBlocks.MITHRIL_DOOR);
        registerBlock("adamantium_door", ModDoorBlocks.ADAMANTIUM_DOOR);

        registerBlock("copper_bars", ModBarBlocks.COPPER_BARS);
        registerBlock("silver_bars", ModBarBlocks.SILVER_BARS);
        registerBlock("gold_bars", ModBarBlocks.GOLD_BARS);
        registerBlock("iron_bars", ModBarBlocks.IRON_BARS);
        registerBlock("ancient_metal_bars", ModBarBlocks.ANCIENT_METAL_BARS);
        registerBlock("mithril_bars", ModBarBlocks.MITHRIL_BARS);
        registerBlock("adamantium_bars", ModBarBlocks.ADAMANTIUM_BARS);

        registerBlock("copper_block", ModMetalBlocks.COPPER_BLOCK);
        registerBlock("silver_block", ModMetalBlocks.SILVER_BLOCK);
        registerBlock("gold_block", ModMetalBlocks.GOLD_BLOCK);
        registerBlock("iron_block", ModMetalBlocks.IRON_BLOCK);
        registerBlock("ancient_metal_block", ModMetalBlocks.ANCIENT_METAL_BLOCK);
        registerBlock("mithril_block", ModMetalBlocks.MITHRIL_BLOCK);
        registerBlock("adamantium_block", ModMetalBlocks.ADAMANTIUM_BLOCK);

        registerAnvilBlock("copper_anvil", ModAnvilBlocks.COPPER_ANVIL);
        registerAnvilBlock("silver_anvil", ModAnvilBlocks.SILVER_ANVIL);
        registerAnvilBlock("gold_anvil", ModAnvilBlocks.GOLD_ANVIL);
        registerAnvilBlock("ancient_metal_anvil", ModAnvilBlocks.ANCIENT_METAL_ANVIL);
        registerAnvilBlock("mithril_anvil", ModAnvilBlocks.MITHRIL_ANVIL);
        registerAnvilBlock("adamantium_anvil", ModAnvilBlocks.ADAMANTIUM_ANVIL);
    }

    private static void registerBlock(String path, Block block) {
        Registry.register(BuiltInRegistries.BLOCK, MiteIds.blockKey(path), block);
        ModBlocks.add(block);

        int maxStackSize = ModBlockType.getMaxStackSize(block);
        MiteRegistration.deferItem(() -> {
            ResourceKey<Item> itemKey = MiteIds.itemKey(path);
            BlockItem item = new BlockItem(block, new Item.Properties()
                    .stacksTo(maxStackSize)
                    .setId(itemKey)
                    .useBlockDescriptionPrefix());
            Registry.register(BuiltInRegistries.ITEM, itemKey, item);
            ModItems.add(ItemCategory.BLOCK, item);
        });
    }

    private static void registerAnvilBlock(String path, Block block) {
        if (!(block instanceof ModAnvilBlock anvil)) {
            return;
        }

        Registry.register(BuiltInRegistries.BLOCK, MiteIds.blockKey(path), block);
        ModBlocks.add(block);

        int maxStackSize = ModBlockType.getMaxStackSize(block);
        MiteRegistration.deferItem(() -> {
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

            BlockItem item = new BlockItem(anvil, props);
            Registry.register(BuiltInRegistries.ITEM, itemKey, item);
            ModItems.add(ItemCategory.BLOCK, item);
        });
    }
}
