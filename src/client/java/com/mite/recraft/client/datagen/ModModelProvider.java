package com.mite.recraft.client.datagen;

import com.google.gson.JsonObject;
import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.block.ModBlocks;
import com.mite.recraft.block.workbench.WorkbenchMaterial;
import com.mite.recraft.item.tools.toolItem.FishingRodItems;
import com.mite.recraft.item.tools.toolItem.HatchetItems;
import com.mite.recraft.item.tools.toolItem.HoeItems;
import com.mite.recraft.item.tools.toolItem.PickaxeItems;
import com.mite.recraft.item.tools.toolItem.ShearsItems;
import com.mite.recraft.item.tools.toolItem.ShovelItems;
import com.mite.recraft.item.tools.toolItem.SwordItems;
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
            Block workbenchBlock = ModBlocks.getBlocks().stream()
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
    }

    @Override
    public void generateItemModels(@NonNull ItemModelGenerators gen) {
        Map<Identifier, JsonObject> itemDefs = new LinkedHashMap<>();

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
        generateToolModels(gen, pickaxes, itemDefs);

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
        for (Item tool : tools) {
            String itemName = BuiltInRegistries.ITEM.getKey(tool).getPath();
            Identifier modelId = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "item/" + itemName);
            Identifier textureId = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "item/tools/" + itemName);

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
     * 鱼竿使用 gen.itemModelOutput.accept() 注册
     * 纹理路径在 item/fishing_rods/ 下。
     */
    private void generateFishingRodModels(ItemModelGenerators gen, Item... rods) {
        Identifier sharedCastTex = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "item/fishing_rods/fishing_rod_cast");

        for (Item rod : rods) {
            String itemName = BuiltInRegistries.ITEM.getKey(rod).getPath();
            Identifier modelId = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "item/" + itemName);
            Identifier texId = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "item/fishing_rods/" + itemName);

            // 未抛竿模型（杆+线，材质特有）
            ModelTemplates.FLAT_HANDHELD_ROD_ITEM.create(
                    modelId,
                    TextureMapping.layer0(new Material(texId)),
                    gen.modelOutput
            );

            // 鱼漂暂无贴图
            // 抛竿模型（纯杆，所有材质共用 fishing_rod_cast.png）
            Identifier castModelId = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "item/" + itemName + "_cast");
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

    @Override
    @NonNull
    public String getName() {
        return MiteRecrafted.MOD_ID + " Models";
    }
}
