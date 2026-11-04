package com.mite.recraft.fabric.client.datagen;

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
 * 输出: assets/mite_recrafted/equipment/<material>_horse.json
 */
public class ModEquipmentAssetProvider implements DataProvider {

    private final FabricPackOutput output;
    private static final String[] MATERIALS = {"copper", "silver", "ancient_metal", "mithril", "adamantium"};

    /** 人形盔甲材质。iron 没有 MITE 贴图，层纹理指向原版。 */
    private static final String[] HUMANOID_MATERIALS = {
            "copper", "silver", "gold", "rusted_iron", "iron", "ancient_metal", "mithril", "adamantium"};

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
        for (String mat : HUMANOID_MATERIALS) {
            writeHumanoid(cachedOutput, futures, mat, false);
            writeHumanoid(cachedOutput, futures, mat, true);
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    /** 人形盔甲：humanoid 层放头盔/胸甲/靴子，humanoid_leggings 层放护腿。 */
    private void writeHumanoid(CachedOutput cachedOutput, List<CompletableFuture<?>> futures,
                               String mat, boolean chainmail) {
        String name = chainmail ? mat + "_chainmail" : mat;
        String texture = "iron".equals(mat)
                ? (chainmail ? "minecraft:chainmail" : "minecraft:iron")
                : MiteRecrafted.MOD_ID + ":" + name;

        JsonObject root = new JsonObject();
        JsonObject layers = new JsonObject();
        for (String layer : new String[]{"humanoid", "humanoid_leggings"}) {
            JsonArray array = new JsonArray();
            JsonObject entry = new JsonObject();
            entry.addProperty("texture", texture);
            array.add(entry);
            layers.add(layer, array);
        }
        root.add("layers", layers);

        Identifier id = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, name);
        Path path = output.getOutputFolder().resolve(
                "assets/" + id.getNamespace() + "/equipment/" + id.getPath() + ".json");
        futures.add(DataProvider.saveStable(cachedOutput, JsonParser.parseString(root.toString()), path));
    }

    @Override
    public String getName() {
        return MiteRecrafted.MOD_ID + " Equipment Assets";
    }
}
