package com.mite.recraft.fabric.client.datagen;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.api.ItemCategory;
import com.mite.recraft.chest.ChestUpgrade;
import com.mite.recraft.chest.ChestVariant;
import com.mite.recraft.content.block.workbench.WorkbenchMaterial;
import com.mite.recraft.content.item.quality.Quality;
import com.mite.recraft.registry.ModBlocks;
import com.mite.recraft.registry.ModCreativeTabs;
import com.mite.recraft.registry.ModItems;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 英文语言文件生成
 */
public class ModEnglishLanguageProvider extends FabricLanguageProvider {

    /** 箱子材质名，键为注册 id 去掉 {@code _chest} 后的部分。 */
    private static final Map<String, String> CHEST_MATERIALS = Map.ofEntries(
            Map.entry("copper", "Copper"),
            Map.entry("iron", "Iron"),
            Map.entry("gold", "Gold"),
            Map.entry("diamond", "Diamond"),
            Map.entry("emerald", "Emerald"),
            Map.entry("crystal", "Crystal"),
            Map.entry("obsidian", "Obsidian"),
            Map.entry("netherite", "Netherite"),
            Map.entry("christmas", "Christmas"),
            Map.entry("wood", "Wooden")
    );

    /** 原版箱子的容量，用于“木箱升级”的提示。 */
    private static final int VANILLA_CHEST_CAPACITY = 27;


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
        addChestTranslations(tb);

        // Creative tab
        tb.add(ModCreativeTabs.TAB_KEY, "MITE Recrafted");

        // Material items
        for (Item item : ModItems.get(ItemCategory.MATERIAL)) {
            String key = item.getDescriptionId();
            if (!key.startsWith("item.mite_recrafted.")) continue;
            String id = key.substring("item.mite_recrafted.".length());
            tb.add(key, buildMaterialName(id));
        }

        // Tools
        for (Item item : ModItems.get(ItemCategory.TOOL)) {
            String key = item.getDescriptionId();
            if (!key.startsWith("item.mite_recrafted.")) continue;
            String id = key.substring("item.mite_recrafted.".length());
            tb.add(key, buildToolName(id));
        }

        // Records
        for (Item item : ModItems.get(ItemCategory.RECORD)) {
            String key = item.getDescriptionId();
            if (!key.startsWith("item.mite_recrafted.")) continue;
            String id = key.substring("item.mite_recrafted.".length());
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
        for (Item item : ModItems.get(ItemCategory.BUCKET)) {
            String key = item.getDescriptionId();
            if (!key.startsWith("item.mite_recrafted.")) continue;
            String id = key.substring("item.mite_recrafted.".length());
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

        // Foods
        for (Item item : ModItems.get(ItemCategory.FOOD)) {
            String key = item.getDescriptionId();
            if (!key.startsWith("item.mite_recrafted.")) continue;
            String id = key.substring("item.mite_recrafted.".length());
            String enName = switch (id) {
                case "vegetable_soup" -> "Vegetable Soup";
                case "onion" -> "Onion";
                case "orange" -> "Orange";
                case "cheese" -> "Cheese";
                case "blueberries" -> "Blueberries";
                case "chocolate" -> "Chocolate";
                case "salad" -> "Salad";
                case "blueberry_porridge" -> "Blueberry Porridge";
                case "pumpkin_soup" -> "Pumpkin Soup";
                case "cream_of_mushroom_soup" -> "Cream of Mushroom Soup";
                case "cream_of_vegetable_soup" -> "Cream of Vegetable Soup";
                case "chicken_soup" -> "Chicken Soup";
                case "beef_stew" -> "Beef Stew";
                case "sorbet" -> "Sorbet";
                case "mashed_potato" -> "Mashed Potato";
                case "ice_cream" -> "Ice Cream";
                case "cereal" -> "Cereal";
                case "water_bowl" -> "Water Bowl";
                case "milk_bowl" -> "Milk Bowl";
                case "worm_raw" -> "Worm";
                case "worm_cooked" -> "Cooked Worm";
                case "copper_milk_bucket" -> "Copper Bucket of Milk";
                case "silver_milk_bucket" -> "Silver Bucket of Milk";
                case "gold_milk_bucket" -> "Golden Bucket of Milk";
                case "ancient_metal_milk_bucket" -> "Ancient Metal Bucket of Milk";
                case "mithril_milk_bucket" -> "Mithril Bucket of Milk";
                case "adamantium_milk_bucket" -> "Adamantium Bucket of Milk";
                default -> null;
            };
            if (enName != null) tb.add(key, enName);
        }

        // Armor
        for (Item item : ModItems.get(ItemCategory.ARMOR)) {
            String key = item.getDescriptionId();
            if (!key.startsWith("item.mite_recrafted.")) continue;
            String id = key.substring("item.mite_recrafted.".length());
            tb.add(key, buildArmorName(id));
        }

        // Item tag translations (repairs_*)
        String[][] tagMats = {
                {"copper", "Copper"}, {"silver", "Silver"}, {"gold", "Gold"},
                {"iron", "Iron"}, {"rusted_iron", "Rusted Iron"},
                {"ancient_metal", "Ancient Metal"}, {"mithril", "Mithril"}, {"adamantium", "Adamantium"},
                {"flint", "Flint"}, {"obsidian", "Obsidian"}, {"wood", "Wood"}
        };
        for (String[] pair : tagMats) {
            tb.add("tag.item.mite_recrafted.repairs_" + pair[0], pair[1] + " Repair Materials");
        }

        // Jukebox song descriptions
        String[][] songs = {
                {"descent", "Descent"}, {"legends", "Legends"},
                {"underworld", "Underworld"}, {"wanderer", "Wanderer"}
        };
        for (String[] s : songs) {
            tb.add("jukebox_song.mite_recrafted." + s[0], s[1]);
        }

        // Enchantments
        tb.add("enchantment.mite_recrafted.arrow_recovery", "Arrow Recovery");
        tb.add("enchantment.mite_recrafted.quickness", "Quickness");
        tb.add("enchantment.mite_recrafted.true_flight", "True Flight");
        tb.add("enchantment.mite_recrafted.poison", "Poison");
        tb.add("enchantment.mite_recrafted.stun", "Stun");
        tb.add("enchantment.mite_recrafted.vampiric", "Vampiric");
        tb.add("enchantment.mite_recrafted.disarming", "Disarming");
        tb.add("enchantment.mite_recrafted.butchering", "Butchering");
        tb.add("enchantment.mite_recrafted.tree_felling", "Tree Felling");
        tb.add("enchantment.mite_recrafted.harvesting", "Harvesting");
        tb.add("enchantment.mite_recrafted.fertility", "Fertility");
        tb.add("enchantment.mite_recrafted.fishing_fortune", "Fishing Fortune");
        tb.add("enchantment.mite_recrafted.speed", "Speed");
        tb.add("enchantment.mite_recrafted.regeneration", "Regeneration");
        tb.add("enchantment.mite_recrafted.free_action", "Free Action");
        tb.add("enchantment.mite_recrafted.endurance", "Endurance");
        tb.add("enchantment.mite_recrafted.unbreaking", "Unbreaking");
        tb.add("enchantment.mite_recrafted.accuracy", "Accuracy");
        tb.add("enchantment.mite_recrafted.slaying", "Slaying");
        tb.add("enchantment.mite_recrafted.piercing", "Piercing");
        tb.add("enchantment.mite_recrafted.cleaving", "Cleaving");
        tb.add("enchantment.mite_recrafted.protection", "Protection");

        // Nutrition command
        tb.add("command.mite_recrafted.cnutrition.title", "§6=== Client Nutrition Data ===");
        tb.add("command.mite_recrafted.cnutrition.protein", "Protein: §b%d");
        tb.add("command.mite_recrafted.cnutrition.phytonutrients", "Phytonutrients: §2%d");
        tb.add("command.mite_recrafted.cnutrition.fats", "Fats: §e%d");
        tb.add("command.mite_recrafted.cnutrition.sugar", "Sugar: §c%d");
        tb.add("command.mite_recrafted.cnutrition.insulin", "Insulin Resistance: §4%d / 192000");
        tb.add("command.mite_recrafted.cnutrition.no_player", "Player not found");

        // Milking message
        tb.add("message.mite_recrafted.cow_already_milked", "This cow has already been milked today");

        // Item tag translations
        tb.add("tag.item.mite_recrafted.water_buckets", "Water Buckets");
        tb.add("tag.item.mite_recrafted.strings", "Strings");

        // 自定义物品标签 - 魔咒作用域
        tb.add("tag.item.mite_recrafted.butchering_weapon", "Butchering Weapons");
        tb.add("tag.item.mite_recrafted.stun_weapon", "Stun Weapons");
        tb.add("tag.item.mite_recrafted.vampiric_weapon", "Vampiric Weapons");
        tb.add("tag.item.mite_recrafted.slaying_weapon", "Slaying Weapons");
        tb.add("tag.item.mite_recrafted.harvesting_tool", "Harvesting Tools");
        tb.add("tag.item.mite_recrafted.piercing_tool", "Piercing Tools");
        tb.add("tag.item.mite_recrafted.cleaving_weapon", "Cleaving Weapons");
        tb.add("tag.item.mite_recrafted.tree_felling_tool", "Tree Felling Tools");

        // Furnace container titles
        tb.add("container.clay_oven", "Clay Oven");
        tb.add("container.large_clay_oven", "Large Clay Oven");
        tb.add("container.sandstone_furnace", "Sandstone Furnace");
        tb.add("container.stone_furnace", "Stone Furnace");
        tb.add("container.obsidian_furnace", "Obsidian Furnace");
        tb.add("container.netherrack_furnace", "Netherrack Furnace");

        // 生物
        tb.add("entity.mite_recrafted.ghoul", "Ghoul");
        tb.add("entity.mite_recrafted.longdead", "Longdead");
        tb.add("entity.mite_recrafted.revenant", "Revenant");
        tb.add("entity.mite_recrafted.wight", "Wight");
        tb.add("entity.mite_recrafted.bone_lord", "Bone Lord");
        tb.add("entity.mite_recrafted.ancient_bone_lord", "Ancient Bone Lord");
        tb.add("entity.mite_recrafted.wood_spider", "Wood Spider");
        tb.add("entity.mite_recrafted.black_widow_spider", "Black Widow Spider");
        tb.add("entity.mite_recrafted.phase_spider", "Phase Spider");
        tb.add("entity.mite_recrafted.demon_spider", "Demon Spider");
        tb.add("entity.mite_recrafted.dire_wolf", "Dire Wolf");
        tb.add("entity.mite_recrafted.hellhound", "Hellhound");
        tb.add("entity.mite_recrafted.copperspine", "Copperspine");
        tb.add("entity.mite_recrafted.hoary_silverfish", "Hoary Silverfish");
        tb.add("entity.mite_recrafted.netherspawn", "Netherspawn");
        tb.add("entity.mite_recrafted.longdead_guardian", "Longdead Guardian");
        tb.add("entity.mite_recrafted.infernal_creeper", "Infernal Creeper");
        tb.add("entity.mite_recrafted.invisible_stalker", "Invisible Stalker");
        tb.add("entity.mite_recrafted.blob", "Blob");
        tb.add("entity.mite_recrafted.ooze", "Ooze");
        tb.add("entity.mite_recrafted.jelly", "Jelly");
        tb.add("entity.mite_recrafted.pudding", "Pudding");
        tb.add("entity.mite_recrafted.vampire_bat", "Vampire Bat");
        tb.add("entity.mite_recrafted.giant_vampire_bat", "Giant Vampire Bat");
        tb.add("entity.mite_recrafted.nightwing", "Nightwing");
        tb.add("entity.mite_recrafted.shadow", "Shadow");
        tb.add("entity.mite_recrafted.web", "Web");
        tb.add("entity.mite_recrafted.brick", "Brick");
        tb.add("entity.mite_recrafted.gelatinous_sphere", "Gelatinous Sphere");
        tb.add("entity.mite_recrafted.fire_elemental", "Fire Elemental");
        tb.add("entity.mite_recrafted.earth_elemental", "Earth Elemental");
        tb.add("entity.mite_recrafted.clay_golem", "Clay Golem");
        tb.add("entity.mite_recrafted.giant_zombie", "Giant Zombie");
        tb.add("entity.mite_recrafted.cave_spider", "Cave Spider");
tb.add("item.mite_recrafted.ghoul_spawn_egg", "Ghoul Spawn Egg");
        tb.add("item.mite_recrafted.longdead_spawn_egg", "Longdead Spawn Egg");
        tb.add("item.mite_recrafted.revenant_spawn_egg", "Revenant Spawn Egg");
        tb.add("item.mite_recrafted.wight_spawn_egg", "Wight Spawn Egg");
        tb.add("item.mite_recrafted.bone_lord_spawn_egg", "Bone Lord Spawn Egg");
        tb.add("item.mite_recrafted.ancient_bone_lord_spawn_egg", "Ancient Bone Lord Spawn Egg");
        tb.add("item.mite_recrafted.wood_spider_spawn_egg", "Wood Spider Spawn Egg");
        tb.add("item.mite_recrafted.black_widow_spider_spawn_egg", "Black Widow Spider Spawn Egg");
        tb.add("item.mite_recrafted.phase_spider_spawn_egg", "Phase Spider Spawn Egg");
        tb.add("item.mite_recrafted.demon_spider_spawn_egg", "Demon Spider Spawn Egg");
        tb.add("item.mite_recrafted.dire_wolf_spawn_egg", "Dire Wolf Spawn Egg");
        tb.add("item.mite_recrafted.hellhound_spawn_egg", "Hellhound Spawn Egg");
        tb.add("item.mite_recrafted.copperspine_spawn_egg", "Copperspine Spawn Egg");
        tb.add("item.mite_recrafted.hoary_silverfish_spawn_egg", "Hoary Silverfish Spawn Egg");
        tb.add("item.mite_recrafted.netherspawn_spawn_egg", "Netherspawn Spawn Egg");
        tb.add("item.mite_recrafted.infernal_creeper_spawn_egg", "Infernal Creeper Spawn Egg");
        tb.add("item.mite_recrafted.invisible_stalker_spawn_egg", "Invisible Stalker Spawn Egg");
        tb.add("item.mite_recrafted.blob_spawn_egg", "Blob Spawn Egg");
        tb.add("item.mite_recrafted.ooze_spawn_egg", "Ooze Spawn Egg");
        tb.add("item.mite_recrafted.jelly_spawn_egg", "Jelly Spawn Egg");
        tb.add("item.mite_recrafted.pudding_spawn_egg", "Pudding Spawn Egg");
        tb.add("item.mite_recrafted.shadow_spawn_egg", "Shadow Spawn Egg");
        tb.add("item.mite_recrafted.earth_elemental_spawn_egg", "Earth Elemental Spawn Egg");
        tb.add("item.mite_recrafted.clay_golem_spawn_egg", "Clay Golem Spawn Egg");
        tb.add("item.mite_recrafted.fire_elemental_spawn_egg", "Fire Elemental Spawn Egg");
        tb.add("item.mite_recrafted.earth_elemental_obsidian_spawn_egg", "Obsidian Earth Elemental Spawn Egg");
        tb.add("item.mite_recrafted.earth_elemental_netherrack_spawn_egg", "Netherrack Earth Elemental Spawn Egg");
        tb.add("item.mite_recrafted.earth_elemental_end_stone_spawn_egg", "End Stone Earth Elemental Spawn Egg");
        tb.add("item.mite_recrafted.clay_golem_hardened_spawn_egg", "Hardened Clay Golem Spawn Egg");

        // Workbench quality display
        tb.add("gui.mite-recraft.workbench.quality_prefix", "%s %s");
        tb.add("gui.mite-recraft.workbench.xp_cost", "Crafting Cost: %d level(s)");
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

    /** e.g. "copper_chainmail_helmet" -> "Copper Chainmail Helmet" */
    private String buildArmorName(String id) {
        // Try matching chainmail first
        for (int i = 0; i < id.length(); i++) {
            if (id.startsWith("_chainmail_", i)) {
                String material = id.substring(0, i);
                String piece = id.substring(i + "_chainmail_".length());
                return materialDisplay(material) + " Chainmail " + materialDisplay(piece);
            }
        }
        // Regular armor: {material}_{piece}
        int lastUnderscore = id.lastIndexOf('_');
        if (lastUnderscore < 0) return materialDisplay(id);
        return materialDisplay(id.substring(0, lastUnderscore)) + " " + materialDisplay(id.substring(lastUnderscore + 1));
    }

    /** 箱子方块、升级物品与升级提示，按注册数据生成。 */
    private void addChestTranslations(TranslationBuilder tb) {
        for (ChestVariant variant : ChestVariant.REGISTERED) {
            tb.add(blockKey(variant), chestName(material(variant.id())) + " Chest");
        }

        for (ChestUpgrade upgrade : ChestUpgrade.ALL) {
            String itemKey = itemKey(upgrade.id());
            String source = upgrade.from() == null
                    ? CHEST_MATERIALS.get("wood")
                    : chestName(material(upgrade.from().id()));
            tb.add(itemKey, source + " Chest to " + chestName(material(upgrade.to().id())) + " Chest Upgrade");
            tb.add(itemKey + ".tooltip", "Capacity: " + capacity(upgrade.from()) + " → " + upgrade.to().capacity());
        }
    }

    private static String material(String chestId) {
        return chestId.endsWith("_chest") ? chestId.substring(0, chestId.length() - "_chest".length()) : chestId;
    }

    private static String chestName(String material) {
        return CHEST_MATERIALS.getOrDefault(material, material);
    }

    private static int capacity(ChestVariant variant) {
        return variant == null ? VANILLA_CHEST_CAPACITY : variant.capacity();
    }

    private static String blockKey(ChestVariant variant) {
        return "block." + MiteRecrafted.MOD_ID + "." + variant.id();
    }

    private static String itemKey(String path) {
        return "item." + MiteRecrafted.MOD_ID + "." + path;
    }

    @Override
    public String getName() {
        return "English Translations";
    }
}
