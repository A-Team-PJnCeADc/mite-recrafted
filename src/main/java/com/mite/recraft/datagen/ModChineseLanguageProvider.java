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

        // 所有唱片翻译
        for (Item item : ModItems.getRecords()) {
            String key = item.getDescriptionId();
            if (!key.startsWith("item.mite-recrafted.")) continue;
            String id = key.substring("item.mite-recrafted.".length());
            tb.add(key, buildRecordName(id));
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

        // 所有桶翻译
        for (Item item : ModItems.getBuckets()) {
            String key = item.getDescriptionId();
            if (!key.startsWith("item.mite-recrafted.")) continue;
            String id = key.substring("item.mite-recrafted.".length());
            String name = buildBucketName(id);
            if (name != null) tb.add(key, name);
        }

        // 品质翻译
        for (Quality q : Quality.values()) {
            int idx = q.ordinal();
            if (idx < QUALITY_NAMES.length) {
                tb.add("quality.mite-recraft." + q.name().toLowerCase(), QUALITY_NAMES[idx]);
            }
        }

        // 所有食物翻译
        for (Item item : ModItems.getFoods()) {
            String key = item.getDescriptionId();
            if (!key.startsWith("item.mite-recrafted.")) continue;
            String id = key.substring("item.mite-recrafted.".length());
            // 对已知食物的中文名称
            String cnName = switch (id) {
                case "vegetable_soup" -> "蔬菜汤";
                case "onion" -> "洋葱";
                case "orange" -> "橘子";
                case "banana" -> "香蕉";
                case "cheese" -> "奶酪";
                case "dough" -> "面团";
                case "blueberries" -> "蓝莓";
                case "chocolate" -> "巧克力";
                case "salad" -> "沙拉";
                case "blueberry_porridge" -> "蓝莓粥";
                case "pumpkin_soup" -> "南瓜汤";
                case "cream_of_mushroom_soup" -> "奶油蘑菇汤";
                case "cream_of_vegetable_soup" -> "奶油蔬菜汤";
                case "chicken_soup" -> "鸡汤";
                case "beef_stew" -> "牛肉羹";
                case "sorbet" -> "果汁雪糕";
                case "mashed_potato" -> "土豆泥";
                case "ice_cream" -> "冰淇淋";
                case "cereal" -> "麦片粥";
                case "water_bowl" -> "水碗";
                case "milk_bowl" -> "牛奶碗";
                case "worm_raw" -> "虫子";
                case "worm_cooked" -> "熟虫子";
                case "copper_milk_bucket" -> "装满牛奶的铜桶";
                case "silver_milk_bucket" -> "装满牛奶的银桶";
                case "gold_milk_bucket" -> "装满牛奶的金桶";
                case "ancient_metal_milk_bucket" -> "装满牛奶的远古金属桶";
                case "mithril_milk_bucket" -> "装满牛奶的秘银桶";
                case "adamantium_milk_bucket" -> "装满牛奶的艾德曼桶";
                default -> null;
            };
            if (cnName != null) tb.add(key, cnName);
        }

        // 所有护甲翻译
        for (Item item : ModItems.getArmors()) {
            String key = item.getDescriptionId();
            if (!key.startsWith("item.mite-recrafted.")) continue;
            String id = key.substring("item.mite-recrafted.".length());
            String cnName = buildArmorName(id);
            if (cnName != null) tb.add(key, cnName);
        }

        // 物品标签翻译 (repairs_*)
        String[][] tagMats = {
                {"copper", "铜"}, {"silver", "银"}, {"gold", "金"},
                {"iron", "铁"}, {"rusted_iron", "锈铁"},
                {"ancient_metal", "远古金属"}, {"mithril", "秘银"}, {"adamantium", "艾德曼"},
                {"flint", "燧石"}, {"obsidian", "黑曜石"}, {"wood", "木"}
        };
        for (String[] pair : tagMats) {
            tb.add("tag.item.mite-recrafted.repairs_" + pair[0], pair[1] + "修理材料");
        }

        // 唱片说明
        String[][] songs = {
                {"descent", "Descent"}, {"legends", "Legends"},
                {"underworld", "Underworld"}, {"wanderer", "Wanderer"}
        };
        for (String[] s : songs) {
            tb.add("jukebox_song.mite-recrafted." + s[0], s[1]);
        }

        // 魔咒翻译
        tb.add("enchantment.mite-recrafted.arrow_recovery", "箭矢回收");
        tb.add("enchantment.mite-recrafted.quickness", "急速");
        tb.add("enchantment.mite-recrafted.true_flight", "真直");
        tb.add("enchantment.mite-recrafted.poison", "淬毒");
        tb.add("enchantment.mite-recrafted.stun", "眩晕");
        tb.add("enchantment.mite-recrafted.vampiric", "吸血");
        tb.add("enchantment.mite-recrafted.disarming", "缴械");
        tb.add("enchantment.mite-recrafted.butchering", "屠宰");
        tb.add("enchantment.mite-recrafted.tree_felling", "砍伐");
        tb.add("enchantment.mite-recrafted.harvesting", "丰收");
        tb.add("enchantment.mite-recrafted.fertility", "肥沃");
        tb.add("enchantment.mite-recrafted.fishing_fortune", "钓鱼幸运");
        tb.add("enchantment.mite-recrafted.speed", "轻盈");
        tb.add("enchantment.mite-recrafted.regeneration", "再生");
        tb.add("enchantment.mite-recrafted.free_action", "自由行动");
        tb.add("enchantment.mite-recrafted.endurance", "坚韧");
        tb.add("enchantment.mite-recrafted.unbreaking", "耐久");
        tb.add("enchantment.mite-recrafted.accuracy", "精准");
        tb.add("enchantment.mite-recrafted.slaying", "杀害");
        tb.add("enchantment.mite-recrafted.piercing", "穿透");
        tb.add("enchantment.mite-recrafted.cleaving", "劈裂");
        tb.add("enchantment.mite-recrafted.protection", "保护");

        // 营养命令
        tb.add("command.mite-recrafted.cnutrition.title", "§6=== 客户端营养数据 ===");
        tb.add("command.mite-recrafted.cnutrition.protein", "蛋白质: §b%d");
        tb.add("command.mite-recrafted.cnutrition.phytonutrients", "植物营养素: §2%d");
        tb.add("command.mite-recrafted.cnutrition.fats", "脂肪: §e%d");
        tb.add("command.mite-recrafted.cnutrition.sugar", "糖分: §c%d");
        tb.add("command.mite-recrafted.cnutrition.insulin", "胰岛素抵抗: §4%d / 192000");
        tb.add("command.mite-recrafted.cnutrition.no_player", "玩家不存在");

        // 挤奶提示
        tb.add("message.mite-recrafted.cow_already_milked", "这头牛今天已经挤过奶了");

        // 物品标签翻译
        tb.add("tag.item.mite-recrafted.water_buckets", "水桶");
        tb.add("tag.item.mite-recrafted.strings", "线");

        // 自定义物品标签 — 魔咒作用域
        tb.add("tag.item.mite-recrafted.butchering_weapon", "屠宰武器");
        tb.add("tag.item.mite-recrafted.stun_weapon", "击晕武器");
        tb.add("tag.item.mite-recrafted.vampiric_weapon", "吸血武器");
        tb.add("tag.item.mite-recrafted.slaying_weapon", "杀害武器");
        tb.add("tag.item.mite-recrafted.harvesting_tool", "收获工具");
        tb.add("tag.item.mite-recrafted.piercing_tool", "穿透工具");
        tb.add("tag.item.mite-recrafted.cleaving_weapon", "劈裂武器");
        tb.add("tag.item.mite-recrafted.tree_felling_tool", "砍伐工具");

        // 熔炉界面标题
        tb.add("container.clay_oven", "粘土熔炉");
        tb.add("container.large_clay_oven", "大粘土烤炉");
        tb.add("container.sandstone_furnace", "沙石熔炉");
        tb.add("container.stone_furnace", "圆石熔炉");
        tb.add("container.obsidian_furnace", "黑曜石熔炉");
        tb.add("container.netherrack_furnace", "地狱岩熔炉");

        // 工作台品质显示
        tb.add("gui.mite-recraft.workbench.quality_prefix", "%s的%s");
        tb.add("gui.mite-recraft.workbench.xp_cost", "合成花费: %d级");
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
            if (id.equals("flour")) return "面粉";
            if (id.equals("manure")) return "粪便";
            if (id.equals("sinew")) return "皮革线";
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
        return switch (id) {
            case "wood_club" -> "木棒";
            case "wood_cudgel" -> "短木棒";
            case "wood_bow" -> "木弓";
            case "ancient_metal_bow" -> "远古金属弓";
            case "mithril_bow" -> "秘银弓";
            default -> null;
        };
    }

    private String buildRecordName(String id) {
        String name = id.replace("record_", "");
        return "唱片 - " + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private String buildBlockName(String id) {
        for (WorkbenchMaterial mat : WorkbenchMaterial.values()) {
            String matName = mat.getName();
            String cnName = MATERIAL_NAMES.getOrDefault(matName, matName);
            if (id.equals(matName + "_workbench")) return cnName + "工作台";
        }

        for (var entry : MATERIAL_NAMES.entrySet()) {
            String mat = entry.getKey();
            String name = entry.getValue();
            if (id.equals(mat + "_door")) return name + "门";
            if (id.equals(mat + "_bars")) return name + "栅栏";
            if (id.equals(mat + "_block")) return name + "块";
            if (id.equals(mat + "_anvil")) return name + "砧";
        }

        // 箱子
        if (id.endsWith("_strongbox")) {
            String mat = id.substring(0, id.length() - "_strongbox".length());
            String cnName = MATERIAL_NAMES.getOrDefault(mat, mat);
            return cnName + "箱子";
        }

        // 熔炉/烤炉
        if (id.endsWith("_furnace") || id.endsWith("_oven")) {
            if (id.equals("stone_furnace")) return "圆石熔炉";
            if (id.equals("sandstone_furnace")) return "沙石熔炉";
            if (id.equals("clay_oven")) return "粘土熔炉";
            if (id.equals("large_clay_oven")) return "大粘土烤炉";
            if (id.equals("obsidian_furnace")) return "黑曜石熔炉";
            if (id.equals("netherrack_furnace")) return "地狱岩熔炉";
            return net.minecraft.world.item.Items.AIR.getDescriptionId(); // fallback
        }

        return null;
    }

    private String buildBucketName(String id) {
        for (var entry : MATERIAL_NAMES.entrySet()) {
            String mat = entry.getKey();
            String name = entry.getValue();
            if (id.equals(mat + "_bucket")) return name + "桶";
            if (id.equals(mat + "_water_bucket")) return "装满水的" + name + "桶";
            if (id.equals(mat + "_lava_bucket")) return "装满岩浆的" + name + "桶";
            if (id.equals(mat + "_stone_bucket")) return "装满石头的" + name + "桶";
        }
        return null;
    }

    private String buildArmorName(String id) {
        for (var entry : MATERIAL_NAMES.entrySet()) {
            String mat = entry.getKey();
            String name = entry.getValue();
            String prefix;
            String piece;
            boolean chainmail = false;
            if (id.startsWith(mat + "_chainmail_")) {
                chainmail = true;
                prefix = mat + "_chainmail_";
            } else if (id.startsWith(mat + "_")) {
                prefix = mat + "_";
            } else {
                continue;
            }
            piece = id.substring(prefix.length());
            String pieceName = switch (piece) {
                case "helmet" -> "头盔";
                case "chestplate" -> "胸甲";
                case "leggings" -> "护腿";
                case "boots" -> "靴子";
                case "horse_armor" -> "马铠";
                default -> null;
            };
            if (pieceName == null) continue;
            return chainmail ? name + "锁链" + pieceName : name + pieceName;
        }
        return null;
    }

    @Override
    public String getName() {
        return "Chinese Translations";
    }
}
