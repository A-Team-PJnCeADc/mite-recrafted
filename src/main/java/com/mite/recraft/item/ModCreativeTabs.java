package com.mite.recraft.item;

import com.mite.recraft.block.ModBlocks;
import com.mite.recraft.item.material.ModMaterials;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ModCreativeTabs {

    // 创建物品组
    public static final ResourceKey<CreativeModeTab> MITE_RECRAFTED_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(), // 注册表类型
            Identifier.fromNamespaceAndPath("mite-recrafted", "general") // 物品组的唯一ID
    );

    public static final CreativeModeTab MITE_RECRAFTED_TAB = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(ModMaterials.FLINT_CHIP)) // 设置物品组的图标
            .title(Component.translatable("itemGroup.mite-recrafted.general")) // 设置显示名称（使用翻译键）
            .displayItems((displayContext, entries) -> {
                for (Item item : ModItems.getMaterials()) {
                    entries.accept(item);
                }
                for (Item item : ModItems.getTools()) {
                    entries.accept(item);
                }
                for (Block block : ModBlocks.getBlocks()) {
                    entries.accept(block.asItem());
                }
                for (Item item : ModItems.getRecords()) {
                    entries.accept(item);
                }
            })
            .build();

    // 注册物品组
    public static void init() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MITE_RECRAFTED_TAB_KEY, MITE_RECRAFTED_TAB);
    }
}
