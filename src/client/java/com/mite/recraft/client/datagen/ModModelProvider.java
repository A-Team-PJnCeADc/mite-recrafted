package com.mite.recraft.client.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.block.modblock.ModBarBlocks;
import com.mite.recraft.block.modblock.ModDoorBlocks;
import com.mite.recraft.block.modblock.ModMetalBlocks;
import com.mite.recraft.block.workbench.WorkbenchMaterial;
import com.mite.recraft.item.tools.toolItem.AexItems;
import com.mite.recraft.item.tools.toolItem.BowItems;
import com.mite.recraft.item.tools.toolItem.ArrowItems;
import com.mite.recraft.item.tools.toolItem.BattleAxeItems;
import com.mite.recraft.item.tools.toolItem.DaggerItems;
import com.mite.recraft.item.tools.toolItem.FishingRodItems;
import com.mite.recraft.item.tools.toolItem.HatchetItems;
import com.mite.recraft.item.tools.toolItem.HoeItems;
import com.mite.recraft.item.tools.toolItem.KnifeItems;
import com.mite.recraft.item.tools.toolItem.MattockItems;
import com.mite.recraft.item.tools.toolItem.PickaxeItems;
import com.mite.recraft.item.tools.toolItem.ShearsItems;
import com.mite.recraft.item.tools.toolItem.ShovelItems;
import com.mite.recraft.item.tools.toolItem.SwordItems;
import com.mite.recraft.item.tools.toolItem.WoodenItems;
import com.mite.recraft.item.tools.toolItem.WarHammerItems;
import com.mite.recraft.item.material.ModMaterials;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.item.properties.conditional.FishingRodCast;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.mite.recraft.block.ModBlocks.getBlocks;

public class ModModelProvider extends FabricModelProvider {

    private final FabricPackOutput dataOutput;

    public ModModelProvider(FabricPackOutput output) {
        super(output);
        this.dataOutput = output;
    }

    @Override
    public void generateBlockStateModels(@NonNull BlockModelGenerators gen) {
        for (WorkbenchMaterial material : WorkbenchMaterial.values()) {
            String blockName = material.getName() + "_workbench";
            Block workbenchBlock = getBlocks().stream()
                    .filter(block -> {
                        Identifier id = BuiltInRegistries.BLOCK.getKey(block);
                        return id != null && id.getPath().equals(blockName);
                    })
                    .findFirst()
                    .orElse(null);

            if (workbenchBlock == null) continue;

            Identifier modelId;
            switch (material) {
                case FLINT -> {
                    var topMat = new Material(Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "block/workbench/flint/top"));
                    var sideMat = new Material(Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "block/workbench/crafting_table_side"));
                    var model = TexturedModel.COLUMN_ALT.get(workbenchBlock);
                    model.updateTextures(m -> { m.put(TextureSlot.END, topMat); m.put(TextureSlot.SIDE, sideMat); });
                    modelId = model.create(workbenchBlock, gen.modelOutput);
                }
                case OBSIDIAN -> {
                    var topMat = new Material(Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "block/workbench/obsidian/top"));
                    var sideMat = new Material(Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "block/workbench/crafting_table_side"));
                    var model = TexturedModel.COLUMN_ALT.get(workbenchBlock);
                    model.updateTextures(m -> { m.put(TextureSlot.END, topMat); m.put(TextureSlot.SIDE, sideMat); });
                    modelId = model.create(workbenchBlock, gen.modelOutput);
                }
                default -> {
                    String matName = material.getName();
                    var topMat = new Material(Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "block/workbench/crafting_table_top"));
                    var frontMat = new Material(Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "block/workbench/" + matName + "/front"));
                    var sideMat = new Material(Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "block/workbench/" + matName + "/side"));
                    var model = TexturedModel.ORIENTABLE.get(workbenchBlock);
                    model.updateTextures(m -> {
                        m.put(TextureSlot.TOP, topMat);
                        m.put(TextureSlot.BOTTOM, topMat);
                        m.put(TextureSlot.FRONT, frontMat);
                        m.put(TextureSlot.SIDE, sideMat);
                    });
                    modelId = model.create(workbenchBlock, gen.modelOutput);
                }
            }

            var mv = new MultiVariant(WeightedList.of(new Variant(modelId)));
            gen.blockStateOutput.accept(MultiVariantGenerator.dispatch(workbenchBlock, mv));
            gen.registerSimpleItemModel(workbenchBlock, modelId);
        }

        // 金属门
        generateDoorModels(gen);

        // 金属栅栏
        generateBarModels(gen);

        // 金属块
        generateMetalBlockModels(gen);
    }

    @Override
    public void generateItemModels(@NonNull ItemModelGenerators gen) {
        Map<Identifier, JsonObject> itemDefs = new LinkedHashMap<>();

        // 木棒/短木棒
        List<Item> wooden = List.of(WoodenItems.CLUB, WoodenItems.CUDGEL);
        generateToolModels(gen, wooden, itemDefs);

        // 弓（拉弓动画由 BowItem 属性驱动）
        List<Item> bows = List.of(BowItems.WOOD_BOW, BowItems.ANCIENT_METAL_BOW, BowItems.MITHRIL_BOW);
        generateBowModels(gen, bows, itemDefs);

        // 斧头
        List<Item> axes = List.of(
                AexItems.FLINT_AXE, AexItems.OBSIDIAN_AXE,
                AexItems.COPPER_AXE, AexItems.SILVER_AXE,
                AexItems.GOLD_AXE, AexItems.RUSTED_IRON_AXE,
                AexItems.IRON_AXE, AexItems.ANCIENT_METAL_AXE,
                AexItems.MITHRIL_AXE, AexItems.ADAMANTIUM_AXE
        );
        generateToolModels(gen, axes, itemDefs);

        // 手斧
        List<Item> hatchets = List.of(
                HatchetItems.FLINT_HATCHET, HatchetItems.OBSIDIAN_HATCHET,
                HatchetItems.COPPER_HATCHET, HatchetItems.SILVER_HATCHET,
                HatchetItems.GOLD_HATCHET, HatchetItems.RUSTED_IRON_HATCHET,
                HatchetItems.IRON_HATCHET, HatchetItems.ANCIENT_METAL_HATCHET,
                HatchetItems.MITHRIL_HATCHET, HatchetItems.ADAMANTIUM_HATCHET
        );

        // 镐子
        List<Item> pickaxes = List.of(
                PickaxeItems.COPPER_PICKAXE, PickaxeItems.SILVER_PICKAXE,
                PickaxeItems.GOLD_PICKAXE, PickaxeItems.RUSTED_IRON_PICKAXE,
                PickaxeItems.IRON_PICKAXE, PickaxeItems.ANCIENT_METAL_PICKAXE,
                PickaxeItems.MITHRIL_PICKAXE, PickaxeItems.ADAMANTIUM_PICKAXE
        );

        generateToolModels(gen, hatchets, itemDefs);

        // 战斧
        List<Item> battleAxes = List.of(
                BattleAxeItems.COPPER_BATTLE_AXE, BattleAxeItems.SILVER_BATTLE_AXE,
                BattleAxeItems.GOLD_BATTLE_AXE, BattleAxeItems.RUSTED_IRON_BATTLE_AXE,
                BattleAxeItems.IRON_BATTLE_AXE, BattleAxeItems.ANCIENT_METAL_BATTLE_AXE,
                BattleAxeItems.MITHRIL_BATTLE_AXE, BattleAxeItems.ADAMANTIUM_BATTLE_AXE
        );
        generateToolModels(gen, battleAxes, itemDefs);

        generateToolModels(gen, pickaxes, itemDefs);

        // 战锤
        List<Item> warHammers = List.of(
                WarHammerItems.COPPER_WAR_HAMMER, WarHammerItems.SILVER_WAR_HAMMER,
                WarHammerItems.GOLD_WAR_HAMMER, WarHammerItems.RUSTED_IRON_WAR_HAMMER,
                WarHammerItems.IRON_WAR_HAMMER, WarHammerItems.ANCIENT_METAL_WAR_HAMMER,
                WarHammerItems.MITHRIL_WAR_HAMMER, WarHammerItems.ADAMANTIUM_WAR_HAMMER
        );
        generateToolModels(gen, warHammers, itemDefs);

        // 锹
        List<Item> shovels = List.of(
                ShovelItems.FLINT_SHOVEL, ShovelItems.OBSIDIAN_SHOVEL,
                ShovelItems.COPPER_SHOVEL, ShovelItems.SILVER_SHOVEL,
                ShovelItems.GOLD_SHOVEL, ShovelItems.RUSTED_IRON_SHOVEL,
                ShovelItems.IRON_SHOVEL, ShovelItems.ANCIENT_METAL_SHOVEL,
                ShovelItems.MITHRIL_SHOVEL, ShovelItems.ADAMANTIUM_SHOVEL
        );
        generateToolModels(gen, shovels, itemDefs);

        // 锄
        List<Item> hoes = List.of(
                HoeItems.COPPER_HOE, HoeItems.SILVER_HOE,
                HoeItems.GOLD_HOE, HoeItems.RUSTED_IRON_HOE,
                HoeItems.IRON_HOE, HoeItems.ANCIENT_METAL_HOE,
                HoeItems.MITHRIL_HOE, HoeItems.ADAMANTIUM_HOE
        );
        generateToolModels(gen, hoes, itemDefs);

        // 鹤嘴锄
        List<Item> mattocks = List.of(
                MattockItems.COPPER_MATTOCK, MattockItems.SILVER_MATTOCK,
                MattockItems.GOLD_MATTOCK, MattockItems.RUSTED_IRON_MATTOCK,
                MattockItems.IRON_MATTOCK, MattockItems.ANCIENT_METAL_MATTOCK,
                MattockItems.MITHRIL_MATTOCK, MattockItems.ADAMANTIUM_MATTOCK
        );
        generateToolModels(gen, mattocks, itemDefs);

        // 剪刀
        List<Item> shears = List.of(
                ShearsItems.COPPER_SHEARS, ShearsItems.SILVER_SHEARS,
                ShearsItems.GOLD_SHEARS, ShearsItems.RUSTED_IRON_SHEARS,
                ShearsItems.ANCIENT_METAL_SHEARS, ShearsItems.MITHRIL_SHEARS,
                ShearsItems.ADAMANTIUM_SHEARS
        );
        generateToolModels(gen, shears, itemDefs);

        // 钓鱼竿 — 使用 gen.itemModelOutput.accept() 正确注册
        generateFishingRodModels(gen,
                FishingRodItems.FLINT_FISHING_ROD, FishingRodItems.OBSIDIAN_FISHING_ROD,
                FishingRodItems.COPPER_FISHING_ROD, FishingRodItems.SILVER_FISHING_ROD,
                FishingRodItems.GOLD_FISHING_ROD, FishingRodItems.IRON_FISHING_ROD,
                FishingRodItems.ANCIENT_METAL_FISHING_ROD, FishingRodItems.MITHRIL_FISHING_ROD,
                FishingRodItems.ADAMANTIUM_FISHING_ROD
        );

        // 剑
        List<Item> swords = List.of(
                SwordItems.COPPER_SWORD, SwordItems.GOLD_SWORD,
                SwordItems.IRON_SWORD, SwordItems.SILVER_SWORD,
                SwordItems.ANCIENT_METAL_SWORD, SwordItems.RUSTED_IRON_SWORD,
                SwordItems.MITHRIL_SWORD, SwordItems.ADAMANTIUM_SWORD
        );
        generateToolModels(gen, swords, itemDefs);

        // 短剑
        List<Item> daggers = List.of(
                DaggerItems.COPPER_DAGGER, DaggerItems.SILVER_DAGGER,
                DaggerItems.GOLD_DAGGER, DaggerItems.RUSTED_IRON_DAGGER,
                DaggerItems.IRON_DAGGER, DaggerItems.ANCIENT_METAL_DAGGER,
                DaggerItems.MITHRIL_DAGGER, DaggerItems.ADAMANTIUM_DAGGER
        );
        generateToolModels(gen, daggers, itemDefs);

        // 小刀
        List<Item> knives = List.of(
                KnifeItems.FLINT_KNIFE, KnifeItems.OBSIDIAN_KNIFE
        );
        generateToolModels(gen, knives, itemDefs);

        // 箭
        List<Item> arrows = List.of(
                ArrowItems.FLINT_ARROW, ArrowItems.OBSIDIAN_ARROW,
                ArrowItems.COPPER_ARROW, ArrowItems.SILVER_ARROW,
                ArrowItems.GOLD_ARROW, ArrowItems.RUSTED_IRON_ARROW,
                ArrowItems.IRON_ARROW, ArrowItems.ANCIENT_METAL_ARROW,
                ArrowItems.MITHRIL_ARROW, ArrowItems.ADAMANTIUM_ARROW
        );
        generateToolModels(gen, arrows, "arrows", itemDefs);

        // ====== 材料物品 (FLAT_ITEM, 不同的纹理目录) ======
        generateFlatModels(gen, "ingots", itemDefs,
                ModMaterials.COPPER_INGOT, ModMaterials.GOLD_INGOT,
                ModMaterials.IRON_INGOT, ModMaterials.SILVER_INGOT,
                ModMaterials.ANCIENT_METAL_INGOT, ModMaterials.MITHRIL_INGOT,
                ModMaterials.ADAMANTIUM_INGOT);

        generateFlatModels(gen, "nuggets", itemDefs,
                ModMaterials.COPPER_NUGGET, ModMaterials.SILVER_NUGGET,
                ModMaterials.IRON_NUGGET, ModMaterials.ANCIENT_METAL_NUGGET,
                ModMaterials.MITHRIL_NUGGET, ModMaterials.ADAMANTIUM_NUGGET);

        generateFlatModels(gen, "chains", itemDefs,
                ModMaterials.COPPER_CHAIN, ModMaterials.SILVER_CHAIN,
                ModMaterials.GOLDEN_CHAIN, ModMaterials.RUSTED_IRON_CHAIN,
                ModMaterials.IRON_CHAIN, ModMaterials.ANCIENT_METAL_CHAIN,
                ModMaterials.MITHRIL_CHAIN, ModMaterials.ADAMANTIUM_CHAIN);

        generateFlatModels(gen, "coins", itemDefs,
                ModMaterials.COPPER_COIN, ModMaterials.SILVER_COIN,
                ModMaterials.GOLDEN_COIN, ModMaterials.ANCIENT_METAL_COIN,
                ModMaterials.MITHRIL_COIN, ModMaterials.ADAMANTIUM_COIN);

        generateFlatModels(gen, "shards", itemDefs,
                ModMaterials.FLINT_CHIP, ModMaterials.OBSIDIAN_CHIP,
                ModMaterials.DIAMOND_CHIP, ModMaterials.EMERALD_CHIP,
                ModMaterials.GLASS_SHARD, ModMaterials.NETHER_QUARTZ_SHARD);

        // 写入 items/*.json
        Path itemsDir = dataOutput.getOutputFolder().resolve("assets").resolve(MiteRecrafted.MOD_ID).resolve("items");
        try {
            Files.createDirectories(itemsDir);
            var gson = new com.google.gson.GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            for (var e : itemDefs.entrySet()) {
                Files.writeString(itemsDir.resolve(e.getKey().getPath() + ".json"), gson.toJson(e.getValue()));
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to write item definitions", e);
        }
    }

    private void generateToolModels(ItemModelGenerators gen, List<Item> tools, Map<Identifier, JsonObject> itemDefs) {
        generateToolModels(gen, tools, "tools", itemDefs);
    }

    private void generateToolModels(ItemModelGenerators gen, List<Item> tools, String texDir,
                                     Map<Identifier, JsonObject> itemDefs) {
        for (Item tool : tools) {
            String itemName = BuiltInRegistries.ITEM.getKey(tool).getPath();
            Identifier modelId = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "item/tools/" + itemName);
            Identifier textureId = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "item/" + texDir + "/" + itemName);

            ModelTemplates.FLAT_HANDHELD_ITEM.create(
                    modelId,
                    TextureMapping.layer0(new Material(textureId)),
                    gen.modelOutput
            );

            JsonObject json = new JsonObject();
            JsonObject model = new JsonObject();
            model.addProperty("type", "minecraft:model");
            model.addProperty("model", modelId.toString());
            json.add("model", model);
            itemDefs.put(Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, itemName), json);
        }
    }

    /**
     * 生成普通材料物品模型（父模板 item/generated）
     * 模型和纹理都在 item/<category>/ 子目录下
     */
    private void generateFlatModels(ItemModelGenerators gen, String category,
                                     Map<Identifier, JsonObject> itemDefs, Item... items) {
        for (Item item : items) {
            String itemName = BuiltInRegistries.ITEM.getKey(item).getPath();
            Identifier modelId = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "item/" + category + "/" + itemName);
            Identifier textureId = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "item/" + category + "/" + itemName);

            ModelTemplates.FLAT_ITEM.create(
                    modelId,
                    TextureMapping.layer0(new Material(textureId)),
                    gen.modelOutput
            );

            JsonObject json = new JsonObject();
            JsonObject model = new JsonObject();
            model.addProperty("type", "minecraft:model");
            model.addProperty("model", modelId.toString());
            json.add("model", model);
            itemDefs.put(Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, itemName), json);
        }
    }

    /**
     * 鱼竿使用 gen.itemModelOutput.accept() 注册
     * 纹理路径在 item/fishing_rods/ 下。
     */
    private void generateFishingRodModels(ItemModelGenerators gen, Item... rods) {
        Identifier sharedCastTex = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "item/fishing_rods/fishing_rod_cast");

        for (Item rod : rods) {
            String itemName = BuiltInRegistries.ITEM.getKey(rod).getPath();
            Identifier modelId = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "item/tools/" + itemName);
            Identifier texId = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "item/fishing_rods/" + itemName);

            // 未抛竿模型（杆+线，材质特有）
            ModelTemplates.FLAT_HANDHELD_ROD_ITEM.create(
                    modelId,
                    TextureMapping.layer0(new Material(texId)),
                    gen.modelOutput
            );

            // 抛竿模型（纯杆，所有材质共用 fishing_rod_cast.png）
            Identifier castModelId = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "item/tools/" + itemName + "_cast");
            ModelTemplates.FLAT_HANDHELD_ROD_ITEM.create(
                    castModelId,
                    TextureMapping.layer0(new Material(sharedCastTex)),
                    gen.modelOutput
            );

            var uncast = ItemModelUtils.plainModel(modelId);
            var cast = ItemModelUtils.plainModel(castModelId);
            gen.itemModelOutput.accept(rod,
                    ItemModelUtils.conditional(new FishingRodCast(), cast, uncast));
        }
    }
    
    private void generateBowModels(ItemModelGenerators gen, List<Item> bows, Map<Identifier, JsonObject> itemDefs) {
        String modId = MiteRecrafted.MOD_ID;
        String[] arrowMaterials = {"flint","obsidian","copper","silver","gold",
                "rusted_iron","iron","ancient_metal","mithril","adamantium"};

        for (Item bow : bows) {
            String itemName = BuiltInRegistries.ITEM.getKey(bow).getPath();
            String bowMat = itemName.replace("_bow", "");

            // standby
            Identifier standbyId = Identifier.fromNamespaceAndPath(modId, "item/tools/" + itemName + "_standby");
            ModelTemplates.FLAT_ITEM.create(standbyId,
                    TextureMapping.layer0(new Material(Identifier.fromNamespaceAndPath(modId,
                            "item/bows/" + bowMat + "/standby"))), gen.modelOutput);

            // 10箭种 × 3拉弓阶段
            String[][][] pullIds = new String[3][10][1];
            for (int pull = 0; pull < 3; pull++)
                for (int ai = 0; ai < 10; ai++) {
                    String fn = itemName + "_" + arrowMaterials[ai] + "_" + pull;
                    Identifier mid = Identifier.fromNamespaceAndPath(modId, "item/tools/" + fn);
                    ModelTemplates.FLAT_ITEM.create(mid,
                            TextureMapping.layer0(new Material(Identifier.fromNamespaceAndPath(modId,
                                    "item/bows/" + bowMat + "/" + arrowMaterials[ai] + "_arrow_" + pull))),
                            gen.modelOutput);
                    pullIds[pull][ai][0] = mid.toString();
                }

            // select 节点（按 nocked_arrow 分发）
            java.util.function.Function<String[], JsonObject> makeSelect = (models) -> {
                JsonObject sel = new JsonObject();
                sel.addProperty("type", "minecraft:select");
                sel.addProperty("property", "mite-recrafted:nocked_arrow");
                JsonArray cases = new JsonArray();
                for (int i = 0; i < models.length; i++) {
                    JsonObject c = new JsonObject();
                    JsonArray when = new JsonArray(); when.add(i);
                    c.add("when", when);
                    JsonObject m = new JsonObject(); m.addProperty("type", "minecraft:model"); m.addProperty("model", models[i]);
                    c.add("model", m); cases.add(c);
                }
                sel.add("cases", cases);
                return sel;
            };

            // items JSON
            JsonObject root = new JsonObject();
            JsonObject cond = new JsonObject();
            cond.addProperty("type", "minecraft:condition");
            cond.addProperty("property", "minecraft:using_item");
            JsonObject off = new JsonObject(); off.addProperty("type", "minecraft:model"); off.addProperty("model", standbyId.toString());
            cond.add("on_false", off);

            JsonObject rd = new JsonObject();
            rd.addProperty("type", "minecraft:range_dispatch");
            rd.addProperty("property", "minecraft:use_duration");
            rd.addProperty("scale", 0.05);

            JsonArray entries = new JsonArray();
            String[] stage1 = new String[10]; for (int i = 0; i < 10; i++) stage1[i] = pullIds[1][i][0];
            JsonObject e1 = new JsonObject(); e1.addProperty("threshold", 0.65); e1.add("model", makeSelect.apply(stage1)); entries.add(e1);
            String[] stage2 = new String[10]; for (int i = 0; i < 10; i++) stage2[i] = pullIds[2][i][0];
            JsonObject e2 = new JsonObject(); e2.addProperty("threshold", 0.9); e2.add("model", makeSelect.apply(stage2)); entries.add(e2);
            rd.add("entries", entries);
            String[] stage0 = new String[10]; for (int i = 0; i < 10; i++) stage0[i] = pullIds[0][i][0];
            rd.add("fallback", makeSelect.apply(stage0));
            cond.add("on_true", rd);
            root.add("model", cond);

            itemDefs.put(Identifier.fromNamespaceAndPath(modId, itemName), root);
        }
    }


    private void generateDoorModels(BlockModelGenerators gen) {
        String[] mats = {"copper", "silver", "gold", "ancient_metal", "mithril", "adamantium"};
        Block[] doors = {ModDoorBlocks.COPPER_DOOR, ModDoorBlocks.SILVER_DOOR, ModDoorBlocks.GOLD_DOOR,
                ModDoorBlocks.ANCIENT_METAL_DOOR, ModDoorBlocks.MITHRIL_DOOR, ModDoorBlocks.ADAMANTIUM_DOOR};
        String modId = MiteRecrafted.MOD_ID;

        for (int i = 0; i < mats.length; i++) {
            String mat = mats[i];
            Block door = doors[i];
            Identifier bottom = Identifier.fromNamespaceAndPath(modId, "block/door/door_" + mat + "_lower");
            Identifier top = Identifier.fromNamespaceAndPath(modId, "block/door/door_" + mat + "_upper");
            TextureMapping mapping = new TextureMapping()
                    .put(TextureSlot.BOTTOM, new Material(bottom))
                    .put(TextureSlot.TOP, new Material(top));

            // 8 个门模型变体
            MultiVariant bl = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_LEFT.create(door, mapping, gen.modelOutput));
            MultiVariant blOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_LEFT_OPEN.create(door, mapping, gen.modelOutput));
            MultiVariant br = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_RIGHT.create(door, mapping, gen.modelOutput));
            MultiVariant brOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN.create(door, mapping, gen.modelOutput));
            MultiVariant tl = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_LEFT.create(door, mapping, gen.modelOutput));
            MultiVariant tlOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_LEFT_OPEN.create(door, mapping, gen.modelOutput));
            MultiVariant tr = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_RIGHT.create(door, mapping, gen.modelOutput));
            MultiVariant trOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_RIGHT_OPEN.create(door, mapping, gen.modelOutput));

            // 物品模型
            Identifier itemTex = Identifier.fromNamespaceAndPath(modId, "item/doors/" + mat);
            Identifier itemModelId = Identifier.fromNamespaceAndPath(modId, "block/" + mat + "_door");
            ModelTemplates.FLAT_ITEM.create(itemModelId,
                    TextureMapping.layer0(new Material(itemTex)), gen.modelOutput);

            // Blockstate
            gen.blockStateOutput.accept(BlockModelGenerators.createDoor(
                    door, bl, blOpen, br, brOpen, tl, tlOpen, tr, trOpen));
        }
    }

    private void generateBarModels(BlockModelGenerators gen) {
        String[] mats = {"copper", "silver", "gold", "iron", "ancient_metal", "mithril", "adamantium"};
        Block[] barsArr = {ModBarBlocks.COPPER_BARS, ModBarBlocks.SILVER_BARS, ModBarBlocks.GOLD_BARS,
                ModBarBlocks.IRON_BARS, ModBarBlocks.ANCIENT_METAL_BARS, ModBarBlocks.MITHRIL_BARS,
                ModBarBlocks.ADAMANTIUM_BARS};
        String modId = MiteRecrafted.MOD_ID;

        for (int i = 0; i < mats.length; i++) {
            String mat = mats[i];
            Block bars = barsArr[i];
            Identifier tex = Identifier.fromNamespaceAndPath(modId, "block/bar/" + mat + "_bars");
            Material texMat = new Material(tex);

            TextureMapping mapping = new TextureMapping()
                    .put(TextureSlot.BARS, texMat)
                    .put(TextureSlot.EDGE, texMat);

            // 6 个栅栏模型变体
            Identifier postEnds = ModelTemplates.BARS_POST_ENDS.create(bars, mapping, gen.modelOutput);
            Identifier post = ModelTemplates.BARS_POST.create(bars, mapping, gen.modelOutput);
            Identifier cap = ModelTemplates.BARS_CAP.create(bars, mapping, gen.modelOutput);
            Identifier capAlt = ModelTemplates.BARS_CAP_ALT.create(bars, mapping, gen.modelOutput);
            Identifier side = ModelTemplates.BARS_POST_SIDE.create(bars, mapping, gen.modelOutput);
            Identifier sideAlt = ModelTemplates.BARS_POST_SIDE_ALT.create(bars, mapping, gen.modelOutput);

            // blockstate (multipart)
            gen.createBars(bars, postEnds, post, cap, capAlt, side, sideAlt);

            // 物品模型
            Identifier itemModelId = Identifier.fromNamespaceAndPath(modId, "block/" + mat + "_bars");
            ModelTemplates.FLAT_ITEM.create(itemModelId,
                    TextureMapping.layer0(texMat), gen.modelOutput);
        }
    }

    private void generateMetalBlockModels(BlockModelGenerators gen) {
        String[] mats = {"copper", "silver", "gold", "iron", "ancient_metal", "mithril", "adamantium"};
        Block[] blocks = {ModMetalBlocks.COPPER_BLOCK, ModMetalBlocks.SILVER_BLOCK, ModMetalBlocks.GOLD_BLOCK,
                ModMetalBlocks.IRON_BLOCK, ModMetalBlocks.ANCIENT_METAL_BLOCK, ModMetalBlocks.MITHRIL_BLOCK,
                ModMetalBlocks.ADAMANTIUM_BLOCK};
        String modId = MiteRecrafted.MOD_ID;

        for (int i = 0; i < mats.length; i++) {
            String mat = mats[i];
            Block block = blocks[i];
            Identifier tex = Identifier.fromNamespaceAndPath(modId, "block/metal/" + mat + "_block");
            Material texMat = new Material(tex);

            // cube_all 方块模型
            var model = TexturedModel.CUBE.get(block);
            model.updateTextures(m -> m.put(TextureSlot.ALL, texMat));
            Identifier modelId = model.create(block, gen.modelOutput);

            // blockstate
            var mv = new MultiVariant(WeightedList.of(new Variant(modelId)));
            gen.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, mv));
        }
    }

    @Override
    @NonNull
    public String getName() {
        return MiteRecrafted.MOD_ID + " Models";
    }
}
