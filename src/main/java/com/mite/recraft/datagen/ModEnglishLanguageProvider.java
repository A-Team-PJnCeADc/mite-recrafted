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
    }

    private String buildMaterialName(String id) {
        // Format: material_type, e.g. flint_chip → Flint Chips, copper_ingot → Copper Ingot
        int lastUnderscore = id.lastIndexOf('_');
        if (lastUnderscore < 0) return materialDisplay(id);

        String material = id.substring(0, lastUnderscore);
        String type = id.substring(lastUnderscore + 1);

        return materialDisplay(material) + " " + capitalize(type);
    }

    private String buildToolName(String id) {
        // Format: material_tool, e.g. flint_axe → Flint Axe, flint_hatchet → Flint Hatchet
        int lastUnderscore = id.lastIndexOf('_');
        if (lastUnderscore < 0) return materialDisplay(id);

        String material = id.substring(0, lastUnderscore);
        String tool = id.substring(lastUnderscore + 1);

        return materialDisplay(material) + " " + capitalize(tool);
    }

    private String buildRecordName(String id) {
        // record_descent -> "Record Descent"
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

    @Override
    public String getName() {
        return "English Translations";
    }
}
