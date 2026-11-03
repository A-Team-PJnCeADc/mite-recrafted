package com.mite.recraft.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.JukeboxSong;
import static com.mite.recraft.MiteRecrafted.MOD_ID;

/**
 * 唱片机曲目注册键。曲目 JSON 由 client 源集的 ModJukeboxSongProvider 产出。
 */
public final class ModJukeboxSongs {

    private ModJukeboxSongs() {
    }

    public static final ResourceKey<JukeboxSong> DESCENT = key("descent");
    public static final ResourceKey<JukeboxSong> LEGENDS = key("legends");
    public static final ResourceKey<JukeboxSong> UNDERWORLD = key("underworld");
    public static final ResourceKey<JukeboxSong> WANDERER = key("wanderer");

    public static ResourceKey<JukeboxSong> key(String path) {
        return ResourceKey.create(Registries.JUKEBOX_SONG,
                Identifier.fromNamespaceAndPath(MOD_ID, path));
    }
}
