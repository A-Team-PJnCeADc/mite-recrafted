package com.mite.recraft.item.record;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.datagen.ModJukeboxSongProvider;
import com.mite.recraft.item.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.Rarity;

/**
 * MITE 新唱片：Descent / Legends / Underworld / Wanderer
 */
public class RecordItems {
    public static final Item RECORD_DESCENT = register("record_descent", ModJukeboxSongProvider.DESCENT);
    public static final Item RECORD_LEGENDS = register("record_legends", ModJukeboxSongProvider.LEGENDS);
    public static final Item RECORD_UNDERWORLD = register("record_underworld", ModJukeboxSongProvider.UNDERWORLD);
    public static final Item RECORD_WANDERER = register("record_wanderer", ModJukeboxSongProvider.WANDERER);

    private static Item register(String name, ResourceKey<JukeboxSong> songKey) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, name));

        Item item = new Item(new Item.Properties()
                .setId(itemKey)
                .stacksTo(1)
                .rarity(Rarity.UNCOMMON)
                .jukeboxPlayable(songKey));

        ModItems.addRecord(item);
        return Registry.register(BuiltInRegistries.ITEM, itemKey, item);
    }

    public static void init() {}
}
