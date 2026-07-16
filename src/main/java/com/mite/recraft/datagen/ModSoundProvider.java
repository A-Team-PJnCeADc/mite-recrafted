package com.mite.recraft.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mite.recraft.MiteRecrafted;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * 生成 sounds.json — 唱片 sound events
 */
public record ModSoundProvider(FabricPackOutput output) implements DataProvider {

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        Path assetsDir = output.getOutputFolder()
                .resolve("assets").resolve(MiteRecrafted.MOD_ID);

        JsonObject root = new JsonObject();
        String[][] records = {
                {"descent", "records/imported/descent"},
                {"legends", "records/imported/legends"},
                {"underworld", "records/imported/underworld"},
                {"wanderer", "records/imported/wanderer"}
        };

        for (String[] r : records) {
            JsonObject evt = new JsonObject();
            JsonObject snd = new JsonObject();
            snd.addProperty("name", MiteRecrafted.MOD_ID + ":" + r[1]);
            snd.addProperty("stream", true);
            JsonArray arr = new JsonArray();
            arr.add(snd);
            evt.add("sounds", arr);
            root.add("music_disc." + r[0], evt);
        }

        return DataProvider.saveStable(cachedOutput, root, assetsDir.resolve("sounds.json"));
    }

    @Override
    public String getName() {
        return MiteRecrafted.MOD_ID + " Sounds";
    }
}
