package com.mite.recraft.item.moditems.armor;

import com.mite.recraft.item.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import static com.mite.recraft.MiteRecrafted.MOD_ID;

/**
 * 护甲物品注册器 —— 将所有护甲物品注册到原版 Registry 并加入 ModItems 追踪列表。
 */
public class ModArmorRegister {
    public static void registerAll() {
        for (Item armor : ModArmorItems.ALL) {
            register((ModArmorItem) armor);
        }
    }

    private static void register(ModArmorItem item) {
        Identifier id = Identifier.fromNamespaceAndPath(MOD_ID, item.itemId());
        Registry.register(BuiltInRegistries.ITEM, id, item);
        ModItems.addArmor(item);
    }

    public static void init() {
        registerAll();
    }
}
