package com.mite.recraft.item;

import com.mite.recraft.item.material.ModMaterials;
import com.mite.recraft.item.moditems.ModRecordItems;
import com.mite.recraft.item.moditems.ManureItems;
import com.mite.recraft.item.moditems.armor.ModArmorItems;
import com.mite.recraft.item.moditems.armor.ModArmorRegister;
import com.mite.recraft.item.moditems.armor.ModEquipmentAssets;
import com.mite.recraft.item.moditems.armor.ModHorseArmorItems;
import com.mite.recraft.item.moditems.bucket.ModBucketItems;
import com.mite.recraft.item.moditems.food.ModFoodItems;
import com.mite.recraft.item.moditems.strongbox.StrongboxRegistry;
import com.mite.recraft.item.tools.ModTools;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModItems {
    private static final List<Item> MATERIALS = new ArrayList<>();
    private static final List<Item> TOOLS = new ArrayList<>();
    private static final List<Item> RECORDS = new ArrayList<>();
    private static final List<Item> BUCKETS = new ArrayList<>();
    private static final List<Item> FOODS = new ArrayList<>();
    private static final List<Item> ARMORS = new ArrayList<>();
    private static final List<Item> ENCHANTED_BOOKS = new ArrayList<>();

    public static void addMaterial(Item item) {
        MATERIALS.add(item);
    }

    public static void addTool(Item item) {
        TOOLS.add(item);
    }

    public static void addRecord(Item item) {
        RECORDS.add(item);
    }

    public static void addBucket(Item item) {
        BUCKETS.add(item);
    }

    public static void addFood(Item item) {
        FOODS.add(item);
    }

    public static void addArmor(Item item) {
        ARMORS.add(item);
    }

    public static void addEnchantedBook(Item item) {
        ENCHANTED_BOOKS.add(item);
    }

    public static List<Item> getMaterials() {
        return Collections.unmodifiableList(MATERIALS);
    }

    public static List<Item> getTools() {
        return Collections.unmodifiableList(TOOLS);
    }

    public static List<Item> getRecords() {
        return Collections.unmodifiableList(RECORDS);
    }

    public static List<Item> getBuckets() {
        return Collections.unmodifiableList(BUCKETS);
    }

    public static List<Item> getFoods() {
        return Collections.unmodifiableList(FOODS);
    }

    public static List<Item> getArmors() {
        return Collections.unmodifiableList(ARMORS);
    }

    public static List<Item> getEnchantedBooks() {
        return Collections.unmodifiableList(ENCHANTED_BOOKS);
    }

    public static void init() {
        ModMaterials.init();
        ModTools.init();
        ModRecordItems.init();
        ModBucketItems.init();
        ManureItems.init();
        ModFoodItems.init();
        ModArmorItems.init();
        ModArmorRegister.init();
        ModHorseArmorItems.init();
        ModEquipmentAssets.init();
        // Strongboxes
        StrongboxRegistry.register();
        // 附魔书
        addEnchantedBook(Items.ENCHANTED_BOOK);
    }
}
