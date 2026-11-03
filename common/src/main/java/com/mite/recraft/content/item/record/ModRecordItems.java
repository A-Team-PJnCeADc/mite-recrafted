package com.mite.recraft.content.item.record;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.api.ItemCategory;
import com.mite.recraft.registry.ModItems;
import com.mite.recraft.registry.ModJukeboxSongs;

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
public class ModRecordItems {
    public static final Item RECORD_DESCENT = register("record_descent", ModJukeboxSongs.DESCENT);
    public static final Item RECORD_LEGENDS = register("record_legends", ModJukeboxSongs.LEGENDS);
    public static final Item RECORD_UNDERWORLD = register("record_underworld", ModJukeboxSongs.UNDERWORLD);
    public static final Item RECORD_WANDERER = register("record_wanderer", ModJukeboxSongs.WANDERER);

    private static Item register(String name, ResourceKey<JukeboxSong> songKey) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, name));

        Item item = new Item(new Item.Properties()
                .setId(itemKey)
                .stacksTo(1)
                .rarity(Rarity.UNCOMMON)
                .jukeboxPlayable(songKey));

        ModItems.add(ItemCategory.RECORD, item);
        return Registry.register(BuiltInRegistries.ITEM, itemKey, item);
    }

    public static void register() {}
}
