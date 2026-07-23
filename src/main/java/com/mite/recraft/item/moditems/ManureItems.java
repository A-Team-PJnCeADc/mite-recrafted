package com.mite.recraft.item.moditems;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.item.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

//todo 可以作为肥料，对耕地使用，使耕地（湿润耕地）转化为肥沃耕地（湿润肥沃耕地）。在湿润肥沃耕地上的作物增加50%的生长速度。当作物成熟时，有50%的概率使湿润肥沃耕地转化为湿润耕地。
// 可以用来制作菌丝。在黑暗处的湿润肥沃耕地上种植褐色蘑菇，一段时间后会将湿润肥沃耕地转化为菌丝。
public class ManureItems {

    public static final Item MANURE = register("manure", 16);

    private static Item register(String path, int maxStack) {
        Identifier id = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, path);
        Item item = new Item(new Item.Properties()
                .stacksTo(maxStack)
                .setId(ResourceKey.create(Registries.ITEM, id))
        );
        ModItems.addMaterial(item);
        return Registry.register(BuiltInRegistries.ITEM, id, item);
    }

    public static void init() {
    }
}
