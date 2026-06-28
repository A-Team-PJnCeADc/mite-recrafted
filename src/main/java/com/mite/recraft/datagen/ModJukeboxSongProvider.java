package com.mite.recraft.datagen;

import com.google.gson.JsonObject;
import com.mite.recraft.MiteRecrafted;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.JukeboxSong;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * Datagen: 生成 jukebox_song/*.json
 */
public class ModJukeboxSongProvider implements DataProvider {

    public static final ResourceKey<JukeboxSong> DESCENT = key("descent");
    public static final ResourceKey<JukeboxSong> LEGENDS = key("legends");
    public static final ResourceKey<JukeboxSong> UNDERWORLD = key("underworld");
    public static final ResourceKey<JukeboxSong> WANDERER = key("wanderer");

    private final Path outputDir;

    public static ResourceKey<JukeboxSong> key(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG,
                Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, name));
    }

    public ModJukeboxSongProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> f) {
        this.outputDir = output.getOutputFolder().resolve("data").resolve(MiteRecrafted.MOD_ID).resolve("jukebox_song");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        try {
            Files.createDirectories(outputDir);
            var gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();

            write(gson, "descent",  "music_disc.descent",  119.4f, 1);
            write(gson, "legends", "music_disc.legends",   62.6f, 2);
            write(gson, "underworld", "music_disc.underworld", 122.9f, 3);
            write(gson, "wanderer", "music_disc.wanderer", 123.8f, 4);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate jukebox_song", e);
        }
        return CompletableFuture.completedFuture(null);
    }

    private void write(com.google.gson.Gson gson, String name, String sound, float sec, int comp) throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("comparator_output", comp);
        JsonObject desc = new JsonObject();
        desc.addProperty("translate", "jukebox_song.mite-recrafted." + name);
        json.add("description", desc);
        json.addProperty("length_in_seconds", sec);
        json.addProperty("sound_event", MiteRecrafted.MOD_ID + ":" + sound);
        Files.writeString(outputDir.resolve(name + ".json"), gson.toJson(json));
    }

    @Override
    public String getName() {
        return MiteRecrafted.MOD_ID + " Jukebox Songs";
    }
}
