package com.mite.recraft.registry;

import com.mite.recraft.api.ItemCategory;
import com.mite.recraft.content.item.armor.ModArmorItems;
import com.mite.recraft.content.item.armor.ModArmorRegister;
import com.mite.recraft.content.item.armor.ModEquipmentAssets;
import com.mite.recraft.content.item.armor.ModHorseArmorItems;
import com.mite.recraft.content.item.bucket.ModBucketItems;
import com.mite.recraft.content.item.food.ModFoodItems;
import com.mite.recraft.content.item.material.ManureItems;
import com.mite.recraft.content.item.material.ModMaterials;
import com.mite.recraft.content.item.record.ModRecordItems;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 物品登记表：记录已注册物品与所属的创造模式物品栏分类，并由 {@link ModCreativeTabs} 消费。
 */
public final class ModItems {

    private static final Map<ItemCategory, List<Item>> BY_CATEGORY = new EnumMap<>(ItemCategory.class);
    private static final List<Item> ENCHANTED_BOOKS = new ArrayList<>();

    private ModItems() {
    }

    public static void add(ItemCategory category, Item item) {
        BY_CATEGORY.computeIfAbsent(category, key -> new ArrayList<>()).add(item);
    }

    public static List<Item> get(ItemCategory category) {
        return Collections.unmodifiableList(BY_CATEGORY.getOrDefault(category, List.of()));
    }

    public static void addEnchantedBook(Item item) {
        ENCHANTED_BOOKS.add(item);
    }

    public static List<Item> getEnchantedBooks() {
        return Collections.unmodifiableList(ENCHANTED_BOOKS);
    }

    public static void register() {
        ModMaterials.register();
        ModToolRegister.register();
        ModRecordItems.register();
        ModBucketItems.register();
        ManureItems.register();
        ModFoodItems.register();
        ModArmorItems.register();
        ModArmorRegister.register();
        ModHorseArmorItems.register();
        ModEquipmentAssets.register();
        addEnchantedBook(Items.ENCHANTED_BOOK);
    }
}
