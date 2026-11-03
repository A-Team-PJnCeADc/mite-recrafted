package com.mite.recraft.content.item.material;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.api.ItemCategory;
import com.mite.recraft.registry.ModItems;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

// 可以用来制作菌丝。在黑暗处的湿润肥沃耕地上种植褐色蘑菇，一段时间后会将湿润肥沃耕地转化为菌丝。
public class ManureItems {

    public static final Item MANURE = register("manure", 16);

    private static Item register(String path, int maxStack) {
        Identifier id = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, path);
        Item item = new Item(new Item.Properties()
                .stacksTo(maxStack)
                .setId(ResourceKey.create(Registries.ITEM, id))
        );
        ModItems.add(ItemCategory.MATERIAL, item);
        return Registry.register(BuiltInRegistries.ITEM, id, item);
    }

    public static void register() {
    }
}
