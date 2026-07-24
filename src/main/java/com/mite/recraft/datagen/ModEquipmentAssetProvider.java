package com.mite.recraft.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mite.recraft.MiteRecrafted;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.Identifier;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 马铠装备层纹理 datagen。
 * 输出: assets/mite-recrafted/equipment/<material>_horse.json
 */
public class ModEquipmentAssetProvider implements DataProvider {

    private final FabricPackOutput output;
    private static final String[] MATERIALS = {"copper", "silver", "ancient_metal", "mithril", "adamantium"};

    public ModEquipmentAssetProvider(FabricPackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (String mat : MATERIALS) {
            JsonObject root = new JsonObject();
            JsonObject layers = new JsonObject();

            JsonArray horseBody = new JsonArray();
            JsonObject entry = new JsonObject();
            entry.addProperty("texture", MiteRecrafted.MOD_ID + ":" + mat + "_horse");
            horseBody.add(entry);

            layers.add("horse_body", horseBody);
            root.add("layers", layers);

            Identifier id = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, mat + "_horse");
            Path path = output.getOutputFolder().resolve(
                    "assets/" + id.getNamespace() + "/equipment/" + id.getPath() + ".json");
            futures.add(DataProvider.saveStable(cachedOutput, JsonParser.parseString(root.toString()), path));
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    @Override
    public String getName() {
        return MiteRecrafted.MOD_ID + " Equipment Assets";
    }
}
