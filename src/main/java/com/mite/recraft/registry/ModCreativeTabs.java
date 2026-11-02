package com.mite.recraft.registry;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.api.ItemCategory;
import com.mite.recraft.content.item.material.ModMaterials;
import com.mite.recraft.registry.ModEnchantments;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * 创造模式物品栏。条目顺序为 {@link ItemCategory} 声明顺序，附魔书追加在末尾。
 */
public final class ModCreativeTabs {

    public static final ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "general")
    );

    public static final CreativeModeTab TAB = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(ModMaterials.FLINT_CHIP))
            .title(Component.translatable("itemGroup." + MiteRecrafted.MOD_ID + ".general"))
            .displayItems((displayContext, entries) -> {
                for (ItemCategory category : ItemCategory.values()) {
                    for (Item item : ModItems.get(category)) {
                        entries.accept(item);
                    }
                }

                var enchantmentLookup = displayContext.holders().lookupOrThrow(Registries.ENCHANTMENT);
                for (Item bookItem : ModItems.getEnchantedBooks()) {
                    for (ModEnchantments enchantment : ModEnchantments.values()) {
                        ItemStack book = new ItemStack(bookItem);
                        book.enchant(enchantmentLookup.getOrThrow(enchantment.key()), enchantment.maxLevel());
                        entries.accept(book);
                    }
                }
            })
            .build();

    private ModCreativeTabs() {
    }

    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAB_KEY, TAB);
    }
}
