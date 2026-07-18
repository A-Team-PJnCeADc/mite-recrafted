package com.mite.recraft.datagen;

import com.mite.recraft.block.ModBlocks;
import com.mite.recraft.block.workbench.WorkbenchMaterial;
import com.mite.recraft.item.ModCreativeTabs;
import com.mite.recraft.item.ModItems;
import com.mite.recraft.item.quality.Quality;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

/**
 * 英文语言文件生成
 */
public class ModEnglishLanguageProvider extends FabricLanguageProvider {

    // capitalize helper
    private static String capitalize(String s) {
        if (s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    // material name with underscores replaced and each word capitalized
    private static String materialDisplay(String raw) {
        String[] parts = raw.split("_");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (!sb.isEmpty()) sb.append(" ");
            sb.append(capitalize(p));
        }
        return sb.toString();
    }

    public ModEnglishLanguageProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder tb) {
        // Creative tab
        tb.add(ModCreativeTabs.MITE_RECRAFTED_TAB_KEY, "MITE Recrafted");

        // Material items
        for (Item item : ModItems.getMaterials()) {
            String key = item.getDescriptionId();
            if (!key.startsWith("item.mite-recrafted.")) continue;
            String id = key.substring("item.mite-recrafted.".length());
            tb.add(key, buildMaterialName(id));
        }

        // Tools
        for (Item item : ModItems.getTools()) {
            String key = item.getDescriptionId();
            if (!key.startsWith("item.mite-recrafted.")) continue;
            String id = key.substring("item.mite-recrafted.".length());
            tb.add(key, buildToolName(id));
        }

        // Records
        for (Item item : ModItems.getRecords()) {
            String key = item.getDescriptionId();
            if (!key.startsWith("item.mite-recrafted.")) continue;
            String id = key.substring("item.mite-recrafted.".length());
            tb.add(key, buildRecordName(id));
        }

        // Blocks
        for (Block block : ModBlocks.getBlocks()) {
            String key = block.getDescriptionId();
            var blockKey = BuiltInRegistries.BLOCK.getKey(block);
            if (blockKey == null) continue;
            String id = blockKey.getPath();
            tb.add(key, buildBlockName(id));
        }

        // Buckets
        for (Item item : ModItems.getBuckets()) {
            String key = item.getDescriptionId();
            if (!key.startsWith("item.mite-recrafted.")) continue;
            String id = key.substring("item.mite-recrafted.".length());
            tb.add(key, buildBucketName(id));
        }

        // Quality
        String[] qualityNames = {"Wretched", "Poor", "Average", "Fine", "Excellent", "Superb", "Masterwork", "Legendary"};
        for (Quality q : Quality.values()) {
            int idx = q.ordinal();
            if (idx < qualityNames.length) {
                tb.add("quality.mite-recraft." + q.name().toLowerCase(), qualityNames[idx]);
            }
        }

        // Item tag translations (repairs_*)
        String[][] tagMats = {
                {"copper", "Copper"}, {"silver", "Silver"}, {"gold", "Gold"},
                {"iron", "Iron"}, {"rusted_iron", "Rusted Iron"},
                {"ancient_metal", "Ancient Metal"}, {"mithril", "Mithril"}, {"adamantium", "Adamantium"},
                {"flint", "Flint"}, {"obsidian", "Obsidian"}, {"wood", "Wood"}
        };
        for (String[] pair : tagMats) {
            tb.add("tag.item.mite-recrafted.repairs_" + pair[0], pair[1] + " Repair Materials");
        }

        // Jukebox song descriptions
        String[][] songs = {
                {"descent", "Descent"}, {"legends", "Legends"},
                {"underworld", "Underworld"}, {"wanderer", "Wanderer"}
        };
        for (String[] s : songs) {
            tb.add("jukebox_song.mite-recrafted." + s[0], s[1]);
        }

        // Nutrition command
        tb.add("command.mite-recrafted.cnutrition.title", "§6=== Client Nutrition Data ===");
        tb.add("command.mite-recrafted.cnutrition.satiation", "Satiation: §6%d§r / %d");
        tb.add("command.mite-recrafted.cnutrition.nutrition", "Nutrition: §a%d§r / %d");
        tb.add("command.mite-recrafted.cnutrition.protein", "Protein: §b%d");
        tb.add("command.mite-recrafted.cnutrition.phytonutrients", "Phytonutrients: §2%d");
        tb.add("command.mite-recrafted.cnutrition.fats", "Fats: §e%d");
        tb.add("command.mite-recrafted.cnutrition.sugar", "Sugar: §c%d");
        tb.add("command.mite-recrafted.cnutrition.insulin", "Insulin Resistance: §4%d / 192000");
        tb.add("command.mite-recrafted.cnutrition.no_player", "Player not found");
    }

    private String buildMaterialName(String id) {
        int lastUnderscore = id.lastIndexOf('_');
        if (lastUnderscore < 0) return materialDisplay(id);
        String material = id.substring(0, lastUnderscore);
        String type = id.substring(lastUnderscore + 1);
        return materialDisplay(material) + " " + capitalize(type);
    }

    private String buildToolName(String id) {
        int lastUnderscore = id.lastIndexOf('_');
        if (lastUnderscore < 0) return materialDisplay(id);
        String material = id.substring(0, lastUnderscore);
        String tool = id.substring(lastUnderscore + 1);
        return materialDisplay(material) + " " + capitalize(tool);
    }

    private String buildRecordName(String id) {
        return "Record " + materialDisplay(id.replace("record_", ""));
    }

    private String buildBlockName(String id) {
        for (WorkbenchMaterial mat : WorkbenchMaterial.values()) {
            if (id.equals(mat.getName() + "_workbench")) {
                return materialDisplay(mat.getName()) + " Workbench";
            }
        }
        return materialDisplay(id);
    }

    private String buildBucketName(String id) {
        if (id.endsWith("_water_bucket")) {
            String mat = id.substring(0, id.length() - "_water_bucket".length());
            return materialDisplay(mat) + " Water Bucket";
        }
        if (id.endsWith("_lava_bucket")) {
            String mat = id.substring(0, id.length() - "_lava_bucket".length());
            return materialDisplay(mat) + " Lava Bucket";
        }
        if (id.endsWith("_stone_bucket")) {
            String mat = id.substring(0, id.length() - "_stone_bucket".length());
            return materialDisplay(mat) + " Stone Bucket";
        }
        if (id.endsWith("_bucket")) {
            String mat = id.substring(0, id.length() - "_bucket".length());
            return materialDisplay(mat) + " Bucket";
        }
        return materialDisplay(id);
    }

    @Override
    public String getName() {
        return "English Translations";
    }
}
