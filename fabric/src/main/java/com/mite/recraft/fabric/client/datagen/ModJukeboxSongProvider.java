package com.mite.recraft.fabric.client.datagen;

import com.mite.recraft.MiteRecrafted;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Datagen: 生成 jukebox_song/*.json
 */
public class ModJukeboxSongProvider implements DataProvider {

    private final Path outputDir;

    public ModJukeboxSongProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> f) {
        this.outputDir = output.getOutputFolder().resolve("data").resolve(MiteRecrafted.MOD_ID).resolve("jukebox_song");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        futures.add(write(cachedOutput, "descent",  "music_disc.descent",  119.4f, 1));
        futures.add(write(cachedOutput, "legends", "music_disc.legends",   62.6f, 2));
        futures.add(write(cachedOutput, "underworld", "music_disc.underworld", 122.9f, 3));
        futures.add(write(cachedOutput, "wanderer", "music_disc.wanderer", 123.8f, 4));
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private CompletableFuture<?> write(CachedOutput cachedOutput,
                                       String name, String sound, float sec, int comp) {
        JsonObject json = new JsonObject();
        json.addProperty("comparator_output", comp);
        JsonObject desc = new JsonObject();
        desc.addProperty("translate", "jukebox_song.mite_recrafted." + name);
        json.add("description", desc);
        json.addProperty("length_in_seconds", sec);
        json.addProperty("sound_event", MiteRecrafted.MOD_ID + ":" + sound);
        return DataProvider.saveStable(cachedOutput, json, outputDir.resolve(name + ".json"));
    }

    @Override
    public String getName() {
        return MiteRecrafted.MOD_ID + " Jukebox Songs";
    }
}
