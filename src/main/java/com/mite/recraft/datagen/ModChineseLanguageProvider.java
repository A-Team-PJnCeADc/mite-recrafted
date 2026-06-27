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

import java.util.Map;
import java.util.concurrent.CompletableFuture;
/**
 * 中文语言文件生成
 */
public class ModChineseLanguageProvider extends FabricLanguageProvider {

    private static final Map<String, String> MATERIAL_NAMES = Map.ofEntries(
            Map.entry("flint", "燧石"),
            Map.entry("obsidian", "黑曜石"),
            Map.entry("copper", "铜"),
            Map.entry("silver", "银"),
            Map.entry("gold", "金"),
            Map.entry("rusted_iron", "锈铁"),
            Map.entry("iron", "铁"),
            Map.entry("ancient_metal", "远古金属"),
            Map.entry("mithril", "秘银"),
            Map.entry("adamantium", "艾德曼"),
            Map.entry("emerald", "绿宝石"),
            Map.entry("diamond", "钻石"),
            Map.entry("nether_quartz", "下界石英"),
            Map.entry("glass", "玻璃")
    );

    private static final String[] QUALITY_NAMES = {
            "劣质", "粗糙", "普通", "精良", "卓越", "优质", "大师", "传说"
    };

    public ModChineseLanguageProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, "zh_cn", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder tb) {
        // 创造模式标签页
        tb.add(ModCreativeTabs.MITE_RECRAFTED_TAB_KEY, "MITE 重铸版");

        // 所有材料物品翻译
        for (Item item : ModItems.getMaterials()) {
            String key = item.getDescriptionId();
            if (!key.startsWith("item.mite-recrafted.")) continue;
            String id = key.substring("item.mite-recrafted.".length());
            String name = buildMaterialName(id);
            if (name != null) tb.add(key, name);
        }

        // 所有工具翻译
        for (Item item : ModItems.getTools()) {
            String key = item.getDescriptionId();
            if (!key.startsWith("item.mite-recrafted.")) continue;
            String id = key.substring("item.mite-recrafted.".length());
            String name = buildToolName(id);
            if (name != null) tb.add(key, name);
        }

        // 所有方块翻译
        for (Block block : ModBlocks.getBlocks()) {
            String key = block.getDescriptionId();
            var blockKey = BuiltInRegistries.BLOCK.getKey(block);
            if (blockKey == null) continue;
            String id = blockKey.getPath();
            String name = buildBlockName(id);
            if (name != null) tb.add(key, name);
        }

        // 品质翻译
        for (Quality q : Quality.values()) {
            int idx = q.ordinal();
            if (idx < QUALITY_NAMES.length) {
                tb.add("quality.mite-recraft." + q.name().toLowerCase(), QUALITY_NAMES[idx]);
            }
        }
    }

    private String buildMaterialName(String id) {
        for (var entry : MATERIAL_NAMES.entrySet()) {
            String mat = entry.getKey();
            String name = entry.getValue();
            if (id.equals(mat + "_chip")) return name + "碎片";
            if (id.equals(mat + "_nugget")) return name + "粒";
            if (id.equals(mat + "_ingot")) return name + "锭";
            if (id.equals(mat + "_chain")) return name + "锁链";
            if (id.equals(mat + "_coin")) return name + "币";
            if (id.equals(mat + "_shard")) return name + "碎片";
            if (id.equals("golden_chain")) return "金锁链";
            if (id.equals("golden_coin")) return "金币";
        }
        return null;
    }

    private String buildToolName(String id) {
        for (var entry : MATERIAL_NAMES.entrySet()) {
            String mat = entry.getKey();
            String name = entry.getValue();
            if (id.equals(mat + "_axe")) return name + "斧";
            if (id.equals(mat + "_hatchet")) return name + "手斧";
            if (id.equals(mat + "_pickaxe")) return name + "镐";
            if (id.equals(mat + "_shovel")) return name + "锹";
            if (id.equals(mat + "_hoe")) return name + "锄";
            if (id.equals(mat + "_scythe")) return name + "镰刀";
            if (id.equals(mat + "_shears")) return name + "剪刀";
            if (id.equals(mat + "_fishing_rod")) return name + "钓鱼竿";
            if (id.equals(mat + "_sword")) return name + "剑";
            if (id.equals(mat + "_dagger")) return name + "短剑";
            if (id.equals(mat + "_knife")) return name + "小刀";
            if (id.equals(mat + "_arrow")) return name + "箭";
            if (id.equals(mat + "_mattock")) return name + "鹤嘴锄";
            if (id.equals(mat + "_battle_axe")) return name + "战斧";
            if (id.equals(mat + "_war_hammer")) return name + "战锤";
        }
        if (id.equals("wood_club")) return "木棒";
        if (id.equals("wood_cudgel")) return "短木棒";
        if (id.equals("wood_bow")) return "木弓";
        if (id.equals("ancient_metal_bow")) return "远古金属弓";
        if (id.equals("mithril_bow")) return "秘银弓";
        return null;
    }

    private String buildBlockName(String id) {
        // 工作台
        for (WorkbenchMaterial mat : WorkbenchMaterial.values()) {
            String matName = mat.getName();
            String cnName = MATERIAL_NAMES.getOrDefault(matName, matName);
            if (id.equals(matName + "_workbench")) return cnName + "工作台";
        }

        // 金属门
        for (var entry : MATERIAL_NAMES.entrySet()) {
            String mat = entry.getKey();
            String name = entry.getValue();
            if (id.equals(mat + "_door")) return name + "门";
            if (id.equals(mat + "_bars")) return name + "栅栏";
            if (id.equals(mat + "_block")) return name + "块";
        }
        return null;
    }

    @Override
    public String getName() {
        return "Chinese Translations";
    }
}
