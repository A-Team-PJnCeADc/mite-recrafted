package com.mite.recraft.client.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.block.modblock.ModBarBlocks;
import com.mite.recraft.block.modblock.ModDoorBlocks;
import com.mite.recraft.block.modblock.ModMetalBlocks;
import com.mite.recraft.block.workbench.WorkbenchMaterial;
import com.mite.recraft.client.renderer.item.NockedArrowProperty;
import com.mite.recraft.item.moditems.strongbox.StrongboxType;
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
import com.mite.recraft.item.tools.toolItem.ScytheItems;
import com.mite.recraft.item.tools.toolItem.SwordItems;
import com.mite.recraft.item.moditems.ModRecordItems;
import com.mite.recraft.item.moditems.ManureItems;
import com.mite.recraft.item.moditems.food.ModFoodItem;
import com.mite.recraft.item.moditems.food.ContainerType;
import com.mite.recraft.item.tools.toolItem.WoodenItems;
import com.mite.recraft.item.tools.toolItem.WarHammerItems;
import com.mite.recraft.item.material.ModMaterials;
import com.mite.recraft.item.moditems.bucket.ModBucketItems;
import com.mite.recraft.item.ModItems;
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
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.conditional.FishingRodCast;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.mite.recraft.block.ModBlocks.getBlocks;

public class ModModelProvider extends FabricModelProvider {

    private final FabricPackOutput dataOutput;

    public ModModelProvider(FabricPackOutput output) {
        super(output);
        this.dataOutput = output;
    }

    @Override
    public void generateBlockStateModels(@NonNull BlockModelGenerators gen) {
        // 工作台：每种材质有不同的顶面/侧面纹理，燧石和黑曜石用 COLUMN_ALT，金属用 ORIENTABLE
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

            // 工作台只有一种朝向（无 facing），使用单变体 blockstate
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

        // 金属砧
        generateAnvilModels(gen);

        // 箱子
        generateStrongboxModels(gen);

        // MITE 熔炉
        generateFurnaceModels(gen);
    }

    @Override
    public void generateItemModels(@NonNull ItemModelGenerators gen) {
        // 门的物品模型（flat item → models/item/，覆盖自动生成的 block/ 路径）
        generateDoorItemModels(gen);

        // 木棒 / 短木棒（木材材质，独立于 ToolType 体系）
        for (Item item : new Item[]{WoodenItems.CLUB, WoodenItems.CUDGEL}) {
            generateToolModel(gen, item, "tools");
        }

        // 弓：conditional(using_item) → range_dispatch(use_duration) → select(nocked_arrow)
        generateBowModels(gen,
                BowItems.WOOD_BOW, BowItems.ANCIENT_METAL_BOW, BowItems.MITHRIL_BOW);

        // 钓鱼竿：conditional(FishingRodCast, cast, uncast)
        generateFishingRodModels(gen,
                FishingRodItems.FLINT_FISHING_ROD, FishingRodItems.OBSIDIAN_FISHING_ROD,
                FishingRodItems.COPPER_FISHING_ROD, FishingRodItems.SILVER_FISHING_ROD,
                FishingRodItems.GOLD_FISHING_ROD, FishingRodItems.IRON_FISHING_ROD,
                FishingRodItems.ANCIENT_METAL_FISHING_ROD, FishingRodItems.MITHRIL_FISHING_ROD,
                FishingRodItems.ADAMANTIUM_FISHING_ROD);

        // 斧
        for (Item item : new Item[]{AexItems.FLINT_AXE, AexItems.OBSIDIAN_AXE,
                AexItems.COPPER_AXE, AexItems.SILVER_AXE, AexItems.GOLD_AXE,
                AexItems.RUSTED_IRON_AXE, AexItems.IRON_AXE, AexItems.ANCIENT_METAL_AXE,
                AexItems.MITHRIL_AXE, AexItems.ADAMANTIUM_AXE}) {
            generateToolModel(gen, item, "tools");
        }
        // 手斧
        for (Item item : new Item[]{HatchetItems.FLINT_HATCHET, HatchetItems.OBSIDIAN_HATCHET,
                HatchetItems.COPPER_HATCHET, HatchetItems.SILVER_HATCHET, HatchetItems.GOLD_HATCHET,
                HatchetItems.RUSTED_IRON_HATCHET, HatchetItems.IRON_HATCHET,
                HatchetItems.ANCIENT_METAL_HATCHET, HatchetItems.MITHRIL_HATCHET,
                HatchetItems.ADAMANTIUM_HATCHET}) {
            generateToolModel(gen, item, "tools");
        }
        // 战斧
        for (Item item : new Item[]{BattleAxeItems.COPPER_BATTLE_AXE, BattleAxeItems.SILVER_BATTLE_AXE,
                BattleAxeItems.GOLD_BATTLE_AXE, BattleAxeItems.RUSTED_IRON_BATTLE_AXE,
                BattleAxeItems.IRON_BATTLE_AXE, BattleAxeItems.ANCIENT_METAL_BATTLE_AXE,
                BattleAxeItems.MITHRIL_BATTLE_AXE, BattleAxeItems.ADAMANTIUM_BATTLE_AXE}) {
            generateToolModel(gen, item, "tools");
        }
        // 镐
        for (Item item : new Item[]{PickaxeItems.COPPER_PICKAXE, PickaxeItems.SILVER_PICKAXE,
                PickaxeItems.GOLD_PICKAXE, PickaxeItems.RUSTED_IRON_PICKAXE,
                PickaxeItems.IRON_PICKAXE, PickaxeItems.ANCIENT_METAL_PICKAXE,
                PickaxeItems.MITHRIL_PICKAXE, PickaxeItems.ADAMANTIUM_PICKAXE}) {
            generateToolModel(gen, item, "tools");
        }
        // 战锤
        for (Item item : new Item[]{WarHammerItems.COPPER_WAR_HAMMER, WarHammerItems.SILVER_WAR_HAMMER,
                WarHammerItems.GOLD_WAR_HAMMER, WarHammerItems.RUSTED_IRON_WAR_HAMMER,
                WarHammerItems.IRON_WAR_HAMMER, WarHammerItems.ANCIENT_METAL_WAR_HAMMER,
                WarHammerItems.MITHRIL_WAR_HAMMER, WarHammerItems.ADAMANTIUM_WAR_HAMMER}) {
            generateToolModel(gen, item, "tools");
        }
        // 锹
        for (Item item : new Item[]{ShovelItems.FLINT_SHOVEL, ShovelItems.OBSIDIAN_SHOVEL,
                ShovelItems.COPPER_SHOVEL, ShovelItems.SILVER_SHOVEL, ShovelItems.GOLD_SHOVEL,
                ShovelItems.RUSTED_IRON_SHOVEL, ShovelItems.IRON_SHOVEL,
                ShovelItems.ANCIENT_METAL_SHOVEL, ShovelItems.MITHRIL_SHOVEL,
                ShovelItems.ADAMANTIUM_SHOVEL}) {
            generateToolModel(gen, item, "tools");
        }
        // 锄
        for (Item item : new Item[]{HoeItems.COPPER_HOE, HoeItems.SILVER_HOE,
                HoeItems.GOLD_HOE, HoeItems.RUSTED_IRON_HOE,
                HoeItems.IRON_HOE, HoeItems.ANCIENT_METAL_HOE,
                HoeItems.MITHRIL_HOE, HoeItems.ADAMANTIUM_HOE}) {
            generateToolModel(gen, item, "tools");
        }
        // 镰刀
        for (Item item : new Item[]{ScytheItems.COPPER_SCYTHE, ScytheItems.SILVER_SCYTHE,
                ScytheItems.GOLD_SCYTHE, ScytheItems.RUSTED_IRON_SCYTHE,
                ScytheItems.IRON_SCYTHE, ScytheItems.ANCIENT_METAL_SCYTHE,
                ScytheItems.MITHRIL_SCYTHE, ScytheItems.ADAMANTIUM_SCYTHE}) {
            generateToolModel(gen, item, "tools");
        }
        // 鹤嘴锄
        for (Item item : new Item[]{MattockItems.COPPER_MATTOCK, MattockItems.SILVER_MATTOCK,
                MattockItems.GOLD_MATTOCK, MattockItems.RUSTED_IRON_MATTOCK,
                MattockItems.IRON_MATTOCK, MattockItems.ANCIENT_METAL_MATTOCK,
                MattockItems.MITHRIL_MATTOCK, MattockItems.ADAMANTIUM_MATTOCK}) {
            generateToolModel(gen, item, "tools");
        }
        // 剪刀
        for (Item item : new Item[]{ShearsItems.COPPER_SHEARS, ShearsItems.SILVER_SHEARS,
                ShearsItems.GOLD_SHEARS, ShearsItems.RUSTED_IRON_SHEARS,
                ShearsItems.ANCIENT_METAL_SHEARS, ShearsItems.MITHRIL_SHEARS,
                ShearsItems.ADAMANTIUM_SHEARS}) {
            generateToolModel(gen, item, "tools");
        }
        // 剑
        for (Item item : new Item[]{SwordItems.COPPER_SWORD, SwordItems.GOLD_SWORD,
                SwordItems.IRON_SWORD, SwordItems.SILVER_SWORD,
                SwordItems.ANCIENT_METAL_SWORD, SwordItems.RUSTED_IRON_SWORD,
                SwordItems.MITHRIL_SWORD, SwordItems.ADAMANTIUM_SWORD}) {
            generateToolModel(gen, item, "tools");
        }
        // 短剑
        for (Item item : new Item[]{DaggerItems.COPPER_DAGGER, DaggerItems.SILVER_DAGGER,
                DaggerItems.GOLD_DAGGER, DaggerItems.RUSTED_IRON_DAGGER,
                DaggerItems.IRON_DAGGER, DaggerItems.ANCIENT_METAL_DAGGER,
                DaggerItems.MITHRIL_DAGGER, DaggerItems.ADAMANTIUM_DAGGER}) {
            generateToolModel(gen, item, "tools");
        }
        // 小刀（燧石/黑曜石）
        for (Item item : new Item[]{KnifeItems.FLINT_KNIFE, KnifeItems.OBSIDIAN_KNIFE}) {
            generateToolModel(gen, item, "tools");
        }
        // 箭（纹理在 item/arrows/ 子目录下）
        for (Item item : new Item[]{ArrowItems.FLINT_ARROW, ArrowItems.OBSIDIAN_ARROW,
                ArrowItems.COPPER_ARROW, ArrowItems.SILVER_ARROW, ArrowItems.GOLD_ARROW,
                ArrowItems.RUSTED_IRON_ARROW, ArrowItems.IRON_ARROW,
                ArrowItems.ANCIENT_METAL_ARROW, ArrowItems.MITHRIL_ARROW,
                ArrowItems.ADAMANTIUM_ARROW}) {
            generateToolModel(gen, item, "arrows");
        }

        // 材料物品（FLAT_ITEM，按 category 分目录存放模型和纹理）
        generateFlatModels(gen, "ingots",
                ModMaterials.COPPER_INGOT, ModMaterials.GOLD_INGOT,
                ModMaterials.IRON_INGOT, ModMaterials.SILVER_INGOT,
                ModMaterials.ANCIENT_METAL_INGOT, ModMaterials.MITHRIL_INGOT,
                ModMaterials.ADAMANTIUM_INGOT);
        generateFlatModels(gen, "nuggets",
                ModMaterials.COPPER_NUGGET, ModMaterials.SILVER_NUGGET,
                ModMaterials.IRON_NUGGET, ModMaterials.ANCIENT_METAL_NUGGET,
                ModMaterials.MITHRIL_NUGGET, ModMaterials.ADAMANTIUM_NUGGET);
        generateFlatModels(gen, "chains",
                ModMaterials.COPPER_CHAIN, ModMaterials.SILVER_CHAIN,
                ModMaterials.GOLDEN_CHAIN, ModMaterials.RUSTED_IRON_CHAIN,
                ModMaterials.IRON_CHAIN, ModMaterials.ANCIENT_METAL_CHAIN,
                ModMaterials.MITHRIL_CHAIN, ModMaterials.ADAMANTIUM_CHAIN);
        generateFlatModels(gen, "coins",
                ModMaterials.COPPER_COIN, ModMaterials.SILVER_COIN,
                ModMaterials.GOLDEN_COIN, ModMaterials.ANCIENT_METAL_COIN,
                ModMaterials.MITHRIL_COIN, ModMaterials.ADAMANTIUM_COIN);
        generateFlatModels(gen, "shards",
                ModMaterials.FLINT_CHIP, ModMaterials.OBSIDIAN_CHIP,
                ModMaterials.DIAMOND_CHIP, ModMaterials.EMERALD_CHIP,
                ModMaterials.GLASS_SHARD, ModMaterials.NETHER_QUARTZ_SHARD);
        // 食物材料（纹理在 item/food/ 下）
        generateFlatModels(gen, "food",
                ModMaterials.FLOUR, ManureItems.MANURE);
        // 其他材料（纹理直接在 item/ 下）
        {
            Identifier modelId = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "item/sinew");
            Identifier texId = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "item/sinew");
            ModelTemplates.FLAT_ITEM.create(modelId,
                    TextureMapping.layer0(new Material(texId)), gen.modelOutput);
            gen.itemModelOutput.accept(ModMaterials.SINEW, ItemModelUtils.plainModel(modelId));
        }
        // 唱片（纹理在 item/records/ 下）
        generateFlatModels(gen, "records",
                        ModRecordItems.RECORD_DESCENT, ModRecordItems.RECORD_LEGENDS,
                        ModRecordItems.RECORD_UNDERWORLD, ModRecordItems.RECORD_WANDERER);

        // 桶：空桶 / 水桶 / 岩浆桶 / 石桶，纹理在 item/buckets/<材质>/<内容>.png
        generateBucketModels(gen);

        // 食物：碗装食物纹理在 item/bowls/ 下，普通食物在 item/foods/ 下
        generateFoodModels(gen);

        // 护甲：纹理在 item/armor/ 下
        generateArmorModels(gen);
    }

    /**
     * 为所有护甲物品生成 FLAT_ITEM 模型。
     * 纹理路径: item/armor/{material}_{piece}.png
     * 锁链: item/armor/{material}_chainmail_{piece}.png
     */
    private void generateArmorModels(ItemModelGenerators gen) {
        String modId = MiteRecrafted.MOD_ID;
        for (Item item : ModItems.getArmors()) {
            String itemName = BuiltInRegistries.ITEM.getKey(item).getPath();
            Identifier modelId = Identifier.fromNamespaceAndPath(modId, "item/armor/" + itemName);
            // 马铠纹理在 item/armor/horse/ 子目录，文件名只用材料名
            Identifier texId;
            if (itemName.endsWith("_horse_armor")) {
                String material = itemName.substring(0, itemName.length() - "_horse_armor".length());
                texId = Identifier.fromNamespaceAndPath(modId, "item/armor/horse/" + material);
            } else {
                texId = Identifier.fromNamespaceAndPath(modId, "item/armor/" + itemName);
            }
            ModelTemplates.FLAT_ITEM.create(modelId,
                    TextureMapping.layer0(new Material(texId)), gen.modelOutput);
            gen.itemModelOutput.accept(item, ItemModelUtils.plainModel(modelId));
        }
    }

    /**
     * 为所有食物物品生成 FLAT_ITEM 模型。
     * 纹理路径：碗装食物 → {@code item/bowls/<id>.png}，普通食物 → {@code item/foods/<id>.png}。
     */
    private void generateFoodModels(ItemModelGenerators gen) {
        String modId = MiteRecrafted.MOD_ID;
        for (Item item : ModItems.getFoods()) {
            String itemName = BuiltInRegistries.ITEM.getKey(item).getPath();

            // 桶装食物（奶桶）：纹理在 item/buckets/<材质>/milk.png
            if (item instanceof ModFoodItem mfi && mfi.getFoodType().containerType() == ContainerType.BUCKET) {
                String mat = mfi.getFoodType().bucketMaterial();
                Identifier modelId = Identifier.fromNamespaceAndPath(modId, "item/buckets/" + itemName);
                Identifier texId = Identifier.fromNamespaceAndPath(modId, "item/buckets/" + mat + "/milk");
                ModelTemplates.FLAT_ITEM.create(modelId,
                        TextureMapping.layer0(new Material(texId)), gen.modelOutput);
                gen.itemModelOutput.accept(item, ItemModelUtils.plainModel(modelId));
                continue;
            }

            String texDir = "food"; // 默认（纹理在 textures/item/food/ 下）
            // 碗装食物用 bowls/ 子目录
            if (item instanceof ModFoodItem mfi && mfi.getFoodType().containerType() == ContainerType.BOWL) {
                texDir = "bowls";
            }
            Identifier modelId = Identifier.fromNamespaceAndPath(modId, "item/" + texDir + "/" + itemName);
            Identifier texId = Identifier.fromNamespaceAndPath(modId, "item/" + texDir + "/" + itemName);
            ModelTemplates.FLAT_ITEM.create(modelId,
                    TextureMapping.layer0(new Material(texId)), gen.modelOutput);
            gen.itemModelOutput.accept(item, ItemModelUtils.plainModel(modelId));
        }
    }

    /**
     * 为单个工具生成 FLAT_HANDHELD_ITEM 模型并注册物品模型引用。
     *
     * @param gen     ItemModelGenerators
     * @param tool    工具 Item 实例
     * @param texDir  纹理子目录（"tools" 或 "arrows"）
     */
    private void generateToolModel(ItemModelGenerators gen, Item tool, String texDir) {
        String itemName = BuiltInRegistries.ITEM.getKey(tool).getPath();
        String modId = MiteRecrafted.MOD_ID;
        Identifier modelId = Identifier.fromNamespaceAndPath(modId, "item/tools/" + itemName);
        Identifier texId = Identifier.fromNamespaceAndPath(modId, "item/" + texDir + "/" + itemName);
        ModelTemplates.FLAT_HANDHELD_ITEM.create(modelId,
                TextureMapping.layer0(new Material(texId)), gen.modelOutput);
        gen.itemModelOutput.accept(tool, ItemModelUtils.plainModel(modelId));
    }

    /**
     * 为材料物品生成 FLAT_ITEM 模型（parent: item/generated）。
     * 模型和纹理都在 item/<category>/ 子目录下。
     *
     * @param gen      ItemModelGenerators
     * @param category 物品分类子目录（ingots / nuggets / chains / coins / shards / records）
     * @param items    材料 Item 实例
     */
    private void generateFlatModels(ItemModelGenerators gen, String category, Item... items) {
        for (Item item : items) {
            String itemName = BuiltInRegistries.ITEM.getKey(item).getPath();
            String modId = MiteRecrafted.MOD_ID;
            Identifier modelId = Identifier.fromNamespaceAndPath(modId, "item/" + category + "/" + itemName);
            Identifier texId = Identifier.fromNamespaceAndPath(modId, "item/" + category + "/" + itemName);
            ModelTemplates.FLAT_ITEM.create(modelId,
                    TextureMapping.layer0(new Material(texId)), gen.modelOutput);
            gen.itemModelOutput.accept(item, ItemModelUtils.plainModel(modelId));
        }
    }

    /**
     * 鱼竿使用 conditional dispatch：未抛竿时显示材质特有纹理，
     * 抛竿时显示共享的 fishing_rod_cast.png（所有材质共用）。
     * 纹理在 item/fishing_rods/ 下。
     */
    private void generateFishingRodModels(ItemModelGenerators gen, Item... rods) {
        // 所有材质共用的抛竿纹理
        Identifier sharedCastTex = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "item/fishing_rods/fishing_rod_cast");
        for (Item rod : rods) {
            String itemName = BuiltInRegistries.ITEM.getKey(rod).getPath();
            String modId = MiteRecrafted.MOD_ID;

            // 未抛竿模型（杆+线，材质特有纹理）
            Identifier modelId = Identifier.fromNamespaceAndPath(modId, "item/tools/" + itemName);
            Identifier texId = Identifier.fromNamespaceAndPath(modId, "item/fishing_rods/" + itemName);
            ModelTemplates.FLAT_HANDHELD_ROD_ITEM.create(modelId,
                    TextureMapping.layer0(new Material(texId)), gen.modelOutput);

            // 抛竿模型（纯杆，所有材质共用）
            Identifier castModelId = Identifier.fromNamespaceAndPath(modId, "item/tools/" + itemName + "_cast");
            ModelTemplates.FLAT_HANDHELD_ROD_ITEM.create(castModelId,
                    TextureMapping.layer0(new Material(sharedCastTex)), gen.modelOutput);

            // conditional: 抛竿 → cast 模型，未抛 → uncast 模型
            gen.itemModelOutput.accept(rod,
                    ItemModelUtils.conditional(new FishingRodCast(),
                            ItemModelUtils.plainModel(castModelId),
                            ItemModelUtils.plainModel(modelId)));
        }
    }

    /**
     * 弓的模型使用三层 dispatch 结构（MC 26.2 SelectItemModelProperty 体系）：
     * <ol>
     *   <li>{@code conditional(using_item)} — 未使用 → standby 待机纹理</li>
     *   <li>{@code range_dispatch(use_duration, scale=0.05)} — 拉弓进度分 3 阶段（阈值 0/0.65/0.9）</li>
     *   <li>{@code select(nocked_arrow)} — 每阶段内按箭袋中的箭种选择材质（10 种箭×3 阶段=30 模型）</li>
     * </ol>
     */
    private void generateBowModels(ItemModelGenerators gen, Item... bows) {
        String modId = MiteRecrafted.MOD_ID;
        String[] arrowMaterials = {"flint","obsidian","copper","silver","gold",
                "rusted_iron","iron","ancient_metal","mithril","adamantium"};

        for (Item bow : bows) {
            String itemName = BuiltInRegistries.ITEM.getKey(bow).getPath();
            String bowMat = itemName.replace("_bow", "");

            // standby 模型：弓未使用时显示
            Identifier standbyId = Identifier.fromNamespaceAndPath(modId, "item/tools/" + itemName + "_standby");
            ModelTemplates.FLAT_ITEM.create(standbyId,
                    TextureMapping.layer0(new Material(Identifier.fromNamespaceAndPath(modId,
                            "item/bows/" + bowMat + "/standby"))), gen.modelOutput);

            // 10 箭种 × 3 拉弓阶段 = 30 个模型 ID
            Identifier[][] pullModelIds = new Identifier[3][10];
            for (int pull = 0; pull < 3; pull++)
                for (int ai = 0; ai < 10; ai++) {
                    String fn = itemName + "_" + arrowMaterials[ai] + "_" + pull;
                    Identifier mid = Identifier.fromNamespaceAndPath(modId, "item/tools/" + fn);
                    ModelTemplates.FLAT_ITEM.create(mid,
                            TextureMapping.layer0(new Material(Identifier.fromNamespaceAndPath(modId,
                                    "item/bows/" + bowMat + "/" + arrowMaterials[ai] + "_arrow_" + pull))),
                            gen.modelOutput);
                    pullModelIds[pull][ai] = mid;
                }

            // 自定义 select property：根据箭袋中箭的材质索引（0-9）选择纹理
            SelectItemModelProperty<Integer> nockedArrow = new NockedArrowProperty();

            // 为每个拉弓阶段构建 select 模型
            ItemModel.Unbaked[] selectStages = new ItemModel.Unbaked[3];
            for (int pull = 0; pull < 3; pull++) {
                List<SelectItemModel.SwitchCase<Integer>> cases = new ArrayList<>();
                for (int ai = 0; ai < 10; ai++) {
                    cases.add(ItemModelUtils.when(ai, ItemModelUtils.plainModel(pullModelIds[pull][ai])));
                }
                selectStages[pull] = ItemModelUtils.select(nockedArrow, cases);
            }

            // range_dispatch：拉弓持续时间 → 3 阶段（阈值 0.65 / 0.9，scale=0.05 换算为 0-20 tick）
            // UseDuration(false): 已过时间从 0 到 max，UseDuration(true) 是剩余时间 max→0
            ItemModel.Unbaked onTrue = ItemModelUtils.rangeSelect(
                    new UseDuration(false), 0.05f,
                    selectStages[0],  // fallback: stage 0
                    ItemModelUtils.override(selectStages[1], 0.65f),
                    ItemModelUtils.override(selectStages[2], 0.9f));

            // conditional：正在使用物品 → range_dispatch，否则 → standby
            ItemModel.Unbaked bowModel = ItemModelUtils.conditional(
                    ItemModelUtils.isUsingItem(),
                    onTrue,
                    ItemModelUtils.plainModel(standbyId));

            gen.itemModelOutput.accept(bow, bowModel);
        }
    }

    /**
     * 生成所有金属桶的物品模型。
     * 命名规则：{@code {材质}_{内容}_bucket}，其中内容为 water / lava / stone / (empty)。
     * 纹理路径：{@code item/buckets/<材质>/<内容>.png}
     */
    private void generateBucketModels(ItemModelGenerators gen) {
        String modId = MiteRecrafted.MOD_ID;
        // 四组桶：空桶、水桶、岩浆桶、石桶
        Item[][] allBucketGroups = {
                {ModBucketItems.COPPER_BUCKET, ModBucketItems.SILVER_BUCKET, ModBucketItems.GOLD_BUCKET,
                        ModBucketItems.ANCIENT_METAL_BUCKET, ModBucketItems.MITHRIL_BUCKET, ModBucketItems.ADAMANTIUM_BUCKET},
                {ModBucketItems.COPPER_WATER_BUCKET, ModBucketItems.SILVER_WATER_BUCKET,
                        ModBucketItems.GOLD_WATER_BUCKET, ModBucketItems.ANCIENT_METAL_WATER_BUCKET,
                        ModBucketItems.MITHRIL_WATER_BUCKET, ModBucketItems.ADAMANTIUM_WATER_BUCKET},
                {ModBucketItems.COPPER_LAVA_BUCKET, ModBucketItems.SILVER_LAVA_BUCKET,
                        ModBucketItems.GOLD_LAVA_BUCKET, ModBucketItems.ANCIENT_METAL_LAVA_BUCKET,
                        ModBucketItems.MITHRIL_LAVA_BUCKET, ModBucketItems.ADAMANTIUM_LAVA_BUCKET},
                {ModBucketItems.COPPER_STONE_BUCKET, ModBucketItems.SILVER_STONE_BUCKET,
                        ModBucketItems.GOLD_STONE_BUCKET, ModBucketItems.IRON_STONE_BUCKET,
                        ModBucketItems.ANCIENT_METAL_STONE_BUCKET, ModBucketItems.MITHRIL_STONE_BUCKET,
                        ModBucketItems.ADAMANTIUM_STONE_BUCKET},
        };

        for (Item[] buckets : allBucketGroups) {
            for (Item bucket : buckets) {
                String itemName = BuiltInRegistries.ITEM.getKey(bucket).getPath();
                // 解析材质名和内容类型：{mat}_{type}_bucket 或 {mat}_bucket
                String matName;
                String texSuffix;
                if (itemName.endsWith("_water_bucket")) {
                    matName = itemName.substring(0, itemName.length() - "_water_bucket".length());
                    texSuffix = "water";
                } else if (itemName.endsWith("_lava_bucket")) {
                    matName = itemName.substring(0, itemName.length() - "_lava_bucket".length());
                    texSuffix = "lava";
                } else if (itemName.endsWith("_stone_bucket")) {
                    matName = itemName.substring(0, itemName.length() - "_stone_bucket".length());
                    texSuffix = "stone";
                } else {
                    matName = itemName.substring(0, itemName.length() - "_bucket".length());
                    texSuffix = "empty";
                }

                Identifier modelId = Identifier.fromNamespaceAndPath(modId, "item/buckets/" + itemName);
                Identifier texId = Identifier.fromNamespaceAndPath(modId, "item/buckets/" + matName + "/" + texSuffix);
                ModelTemplates.FLAT_ITEM.create(modelId,
                        TextureMapping.layer0(new Material(texId)), gen.modelOutput);
                gen.itemModelOutput.accept(bucket, ItemModelUtils.plainModel(modelId));
            }
        }
    }

    /**
     * MC 26.2 的 BlockModelGenerators 会为每个方块自动生成 items/{name}.json 指向 block/ 模型。
     * 门需要 2D flat item 以正确渲染，因此通过 {@code gen.itemModelOutput.accept()} 显式注册覆盖。
     */
    private void generateDoorItemModels(ItemModelGenerators gen) {
        Block[] doors = {ModDoorBlocks.COPPER_DOOR, ModDoorBlocks.SILVER_DOOR, ModDoorBlocks.GOLD_DOOR,
                ModDoorBlocks.ANCIENT_METAL_DOOR, ModDoorBlocks.MITHRIL_DOOR, ModDoorBlocks.ADAMANTIUM_DOOR};
        String[] mats = {"copper", "silver", "gold", "ancient_metal", "mithril", "adamantium"};
        String modId = MiteRecrafted.MOD_ID;

        for (int i = 0; i < mats.length; i++) {
            String mat = mats[i];
            // 纹理在 item/doors/ 下
            Identifier tex = Identifier.fromNamespaceAndPath(modId, "item/doors/" + mat);
            // 扁平模型放在 models/item/（非 block/），对齐原版 iron_door
            Identifier modelId = Identifier.fromNamespaceAndPath(modId, "item/" + mat + "_door");

            ModelTemplates.FLAT_ITEM.create(modelId,
                    TextureMapping.layer0(new Material(tex)), gen.modelOutput);

            // itemModelOutput.accept() 优先级高于 BlockModelGenerators 自动生成
            gen.itemModelOutput.accept(doors[i].asItem(), ItemModelUtils.plainModel(modelId));
        }
    }

    /**
     * 金属门使用原版门 multipart 系统（8 变体：上下×左右×开闭）。
     * 纹理上下两半各一张图（door_{mat}_lower / door_{mat}_upper）。
     */
    private void generateDoorModels(BlockModelGenerators gen) {
        String[] mats = {"copper", "silver", "gold", "ancient_metal", "mithril", "adamantium"};
        Block[] doors = {ModDoorBlocks.COPPER_DOOR, ModDoorBlocks.SILVER_DOOR, ModDoorBlocks.GOLD_DOOR,
                ModDoorBlocks.ANCIENT_METAL_DOOR, ModDoorBlocks.MITHRIL_DOOR, ModDoorBlocks.ADAMANTIUM_DOOR};
        String modId = MiteRecrafted.MOD_ID;

        for (int i = 0; i < mats.length; i++) {
            String mat = mats[i];
            Block door = doors[i];

            // 纹理上下两半各一张图
            Identifier bottom = Identifier.fromNamespaceAndPath(modId, "block/door/door_" + mat + "_lower");
            Identifier top = Identifier.fromNamespaceAndPath(modId, "block/door/door_" + mat + "_upper");
            TextureMapping mapping = new TextureMapping()
                    .put(TextureSlot.BOTTOM, new Material(bottom))
                    .put(TextureSlot.TOP, new Material(top));

            // 8 种门的模型变体
            MultiVariant bl = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_LEFT.create(door, mapping, gen.modelOutput));
            MultiVariant blOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_LEFT_OPEN.create(door, mapping, gen.modelOutput));
            MultiVariant br = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_RIGHT.create(door, mapping, gen.modelOutput));
            MultiVariant brOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN.create(door, mapping, gen.modelOutput));
            MultiVariant tl = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_LEFT.create(door, mapping, gen.modelOutput));
            MultiVariant tlOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_LEFT_OPEN.create(door, mapping, gen.modelOutput));
            MultiVariant tr = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_RIGHT.create(door, mapping, gen.modelOutput));
            MultiVariant trOpen = BlockModelGenerators.plainVariant(ModelTemplates.DOOR_TOP_RIGHT_OPEN.create(door, mapping, gen.modelOutput));

            // multipart blockstate
            gen.blockStateOutput.accept(BlockModelGenerators.createDoor(
                    door, bl, blOpen, br, brOpen, tl, tlOpen, tr, trOpen));
        }
    }

    /**
     * 金属栅栏使用 6 个 BARS 模型变体（post_ends / post / cap / cap_alt / side / side_alt），
     * 纹理在 block/bar/<材质>_bars.png。
     * 物品模型使用 FLAT_ITEM 到 block/<材质>_bars。
     */
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
            // BARS 模板需要 BARS 和 EDGE 两个纹理槽
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

            // multipart blockstate（Fabric AW 暴露此方法）
            gen.createBars(bars, postEnds, post, cap, capAlt, side, sideAlt);

            // 物品模型：2D flat item
            Identifier itemModelId = Identifier.fromNamespaceAndPath(modId, "block/" + mat + "_bars");
            ModelTemplates.FLAT_ITEM.create(itemModelId,
                    TextureMapping.layer0(texMat), gen.modelOutput);
        }
    }

    /**
     * 金属储存块使用 cube_all 模型，纹理在 block/metal/<材质>_block.png。
     * 方块模型引用 textures/block/ 下的纹理（方块图集）。
     */
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

    /** 在 super.run() 执行期间注入的 CachedOutput，供 generateAnvilModels 直接写入 */
    private CachedOutput cachedOutput;

    /**
     * 砧模型系统。
     *
     * <p>方块模型使用 {@code minecraft:block/template_anvil} 父模板，
     * 含 body / top 纹理 + gui display 变换。
     * 裂纹分 3 级（stage 0/1/2），对应 top_damaged_0/1/2 纹理。</p>
     *
     * <p>由于 template_anvil 非标准 Multipart/Variant 模板
     * 且 MC 26.2 的 TextureSlot 没有 BODY 槽，
     * blockstate 和物品 JSON 必须手动构造。</p>
     *
     * <p>通过覆写 {@link #run(CachedOutput)} 在调用父类前注入
     * {@code cachedOutput} 引用，使 {@link #generateAnvilModels}
     * 在 {@code super.run()} 执行期间直接通过
     * {@link DataProvider#saveStable(CachedOutput, com.google.gson.JsonElement, java.nio.file.Path)}
     * 写入，确保被 CachedOutput 跟踪。</p>
     */
    private void generateAnvilModels(BlockModelGenerators gen) {
        String[] mats = {"copper", "silver", "gold", "ancient_metal", "mithril", "adamantium"};
        String modId = MiteRecrafted.MOD_ID;

        for (String mat : mats) {
            String[] tops = {"top_damaged_0", "top_damaged_1", "top_damaged_2"};
            Identifier[] modelIds = new Identifier[3];

            // 生成 3 个方块模型（stage 0/1/2），通过 gen.modelOutput.accept 注册
            for (int stage = 0; stage < 3; stage++) {
                JsonObject modelJson = new JsonObject();
                modelJson.addProperty("parent", "minecraft:block/template_anvil");
                JsonObject tex = new JsonObject();
                tex.addProperty("body", modId + ":block/anvil/" + mat + "/base");
                tex.addProperty("top", modId + ":block/anvil/" + mat + "/" + tops[stage]);
                modelJson.add("textures", tex);
                // gui display：物品栏渲染时的旋转/缩放变换
                JsonObject disp = new JsonObject();
                JsonObject gui = new JsonObject();
                JsonArray rot = new JsonArray(); rot.add(30); rot.add(45); rot.add(0);
                JsonArray trans = new JsonArray(); trans.add(0); trans.add(0); trans.add(0);
                JsonArray sc = new JsonArray(); sc.add(0.625); sc.add(0.625); sc.add(0.625);
                gui.add("rotation", rot);
                gui.add("translation", trans);
                gui.add("scale", sc);
                disp.add("gui", gui);
                modelJson.add("display", disp);
                modelIds[stage] = Identifier.fromNamespaceAndPath(modId,
                        "block/" + mat + "_anvil_stage" + stage);
                gen.modelOutput.accept(modelIds[stage], () -> modelJson);
            }

            // blockstate JSON：4 方向 × 3 裂纹等级 = 12 变体
            JsonObject variants = new JsonObject();
            String[] facings = {"north", "east", "south", "west"};
            int[] ys = {0, 90, 180, 270};
            for (int j = 0; j < 4; j++) {
                for (int stage = 0; stage < 3; stage++) {
                    String key = "facing=" + facings[j] + ",stage=" + stage;
                    JsonObject v = new JsonObject();
                    v.addProperty("model", modelIds[stage].toString());
                    if (ys[j] > 0) v.addProperty("y", ys[j]);
                    variants.add(key, v);
                }
            }
            JsonObject bs = new JsonObject();
            bs.add("variants", variants);

            // 物品 JSON：range_dispatch(minecraft:damage) 按损伤比例切换模型
            // 裂纹阈值：stage 1 → damage >= 0.25，stage 2 → damage >= 0.75
            JsonObject itemJson = new JsonObject();
            JsonObject rd = new JsonObject();
            rd.addProperty("type", "minecraft:range_dispatch");
            rd.addProperty("property", "minecraft:damage");
            JsonArray entries = new JsonArray();
            JsonObject e1 = new JsonObject(); e1.addProperty("threshold", 0.25);
            JsonObject m1 = new JsonObject(); m1.addProperty("type", "minecraft:model"); m1.addProperty("model", modelIds[1].toString());
            e1.add("model", m1); entries.add(e1);
            JsonObject e2 = new JsonObject(); e2.addProperty("threshold", 0.75);
            JsonObject m2 = new JsonObject(); m2.addProperty("type", "minecraft:model"); m2.addProperty("model", modelIds[2].toString());
            e2.add("model", m2); entries.add(e2);
            rd.add("entries", entries);
            JsonObject fallback = new JsonObject(); fallback.addProperty("type", "minecraft:model"); fallback.addProperty("model", modelIds[0].toString());
            rd.add("fallback", fallback);
            itemJson.add("model", rd);

            // 直接通过 CachedOutput 写入（在 super.run() 内部执行，保证被跟踪）
            java.nio.file.Path bsPath = dataOutput.getOutputFolder()
                    .resolve("assets").resolve(modId).resolve("blockstates")
                    .resolve(mat + "_anvil.json");
            java.nio.file.Path itemPath = dataOutput.getOutputFolder()
                    .resolve("assets").resolve(modId).resolve("items")
                    .resolve(mat + "_anvil.json");
            try {
                java.nio.file.Files.createDirectories(bsPath.getParent());
                java.nio.file.Files.createDirectories(itemPath.getParent());
                DataProvider.saveStable(cachedOutput, bs, bsPath);
                DataProvider.saveStable(cachedOutput, itemJson, itemPath);
            } catch (java.io.IOException e) {
                throw new RuntimeException("Failed to write anvil data for " + mat, e);
            }
        }
    }

    /** Generate strongbox blockstate, block model via datagen */
    private void generateStrongboxModels(BlockModelGenerators gen) {
        String modId = MiteRecrafted.MOD_ID;

        // Chest template (parent)
        JsonObject templateJson = new JsonObject();
        templateJson.addProperty("parent", "minecraft:block/chest");
        Identifier templateId = Identifier.fromNamespaceAndPath(modId, "block/chest_template");
        gen.modelOutput.accept(templateId, () -> templateJson);

        for (StrongboxType type : StrongboxType.VALUES) {
            Identifier blockModelId = Identifier.fromNamespaceAndPath(modId, "block/" + type.registryId);
            Identifier textureId = Identifier.fromNamespaceAndPath(modId, "block/" + type.registryId);

            // Block model JSON
            JsonObject modelJson = new JsonObject();
            modelJson.addProperty("parent", modId + ":block/chest_template");
            JsonObject textures = new JsonObject();
            textures.addProperty("0", textureId.toString());
            textures.addProperty("particle", "minecraft:block/stone");
            modelJson.add("textures", textures);
            gen.modelOutput.accept(blockModelId, () -> modelJson);

            // Blockstate JSON: 4 facing variants (write via saveStable)
            JsonObject blockstate = new JsonObject();
            JsonObject variants = new JsonObject();
            String[] facings = {"north", "east", "south", "west"};
            int[] rotations = {0, 90, 180, 270};
            for (int i = 0; i < 4; i++) {
                JsonObject v = new JsonObject();
                v.addProperty("model", blockModelId.toString());
                if (rotations[i] > 0) v.addProperty("y", rotations[i]);
                variants.add("facing=" + facings[i], v);
            }
            blockstate.add("variants", variants);

            if (cachedOutput == null) continue;
            java.nio.file.Path bsPath = dataOutput.getOutputFolder()
                    .resolve("assets").resolve(modId).resolve("blockstates")
                    .resolve(type.registryId + ".json");
            try {
                java.nio.file.Files.createDirectories(bsPath.getParent());
                DataProvider.saveStable(cachedOutput, blockstate, bsPath);

                // Item model JSON — use minecraft:special/minecraft:chest for 3D chest preview
                // The "texture" field omits "entity/chest/" prefix;
                // the ChestSpecialRenderer resolves it to entity/chest/<path> on the chest sheet.
                String itemTexturePath = type.texture.getPath();
                String shortTexture = itemTexturePath.startsWith("entity/chest/")
                        ? itemTexturePath.substring("entity/chest/".length())
                        : itemTexturePath;
                JsonObject itemModel = new JsonObject();
                JsonObject specialRef = new JsonObject();
                specialRef.addProperty("type", "minecraft:special");
                specialRef.addProperty("base", "minecraft:item/chest");
                JsonObject chestModel = new JsonObject();
                chestModel.addProperty("type", "minecraft:chest");
                chestModel.addProperty("texture", modId + ":" + shortTexture);
                specialRef.add("model", chestModel);
                itemModel.add("model", specialRef);
                java.nio.file.Path itemPath = dataOutput.getOutputFolder()
                        .resolve("assets").resolve(modId).resolve("items")
                        .resolve(type.registryId + ".json");
                java.nio.file.Files.createDirectories(itemPath.getParent());
                DataProvider.saveStable(cachedOutput, itemModel, itemPath);
            } catch (java.io.IOException e) {
                throw new RuntimeException("Failed to write blockstate for " + type.registryId, e);
            }
        }
    }

    /** Generate MITE furnace blockstate + block model + item model */
    private void generateFurnaceModels(BlockModelGenerators gen) {
        var modId = MiteRecrafted.MOD_ID;
        for (int i = 0; i < com.mite.recraft.block.furnace.FurnaceTier.VALUES.length; i++) {
            var tier = com.mite.recraft.block.furnace.FurnaceTier.VALUES[i];
            Block block = com.mite.recraft.block.furnace.ModFurnaceRegistry.FURNACE_BLOCKS.get(i);
            if (block == null) continue;

            // Textures are in block/furnace/<material>/{front_off,front_on,side,top}.png
            var prefix = "block/furnace/" + tier.textureBase + "/";

            // Create unlit model
            var unlitMap = TextureMapping.orientableCubeOnlyTop(block);
            unlitMap = unlitMap
                    .put(TextureSlot.FRONT, new Material(Identifier.fromNamespaceAndPath(modId, prefix + "front_off")))
                    .put(TextureSlot.SIDE, new Material(Identifier.fromNamespaceAndPath(modId, prefix + "side")))
                    .put(TextureSlot.TOP, new Material(Identifier.fromNamespaceAndPath(modId, prefix + "top")))
                    .put(TextureSlot.BOTTOM, new Material(Identifier.fromNamespaceAndPath(modId, prefix + "top")));
            var unlitModel = ModelTemplates.CUBE_ORIENTABLE.create(block, unlitMap, gen.modelOutput);

            // Create lit model (front_on replaces front_off)
            var litMap = TextureMapping.orientableCubeOnlyTop(block);
            litMap = litMap
                    .put(TextureSlot.FRONT, new Material(Identifier.fromNamespaceAndPath(modId, prefix + "front_on")))
                    .put(TextureSlot.SIDE, new Material(Identifier.fromNamespaceAndPath(modId, prefix + "side")))
                    .put(TextureSlot.TOP, new Material(Identifier.fromNamespaceAndPath(modId, prefix + "top")))
                    .put(TextureSlot.BOTTOM, new Material(Identifier.fromNamespaceAndPath(modId, prefix + "top")));
            var litModel = ModelTemplates.CUBE_ORIENTABLE.createWithSuffix(block, "_on", litMap, gen.modelOutput);

            // Blockstate: dispatch LIT (unlit/lit)
            gen.blockStateOutput.accept(
                    net.minecraft.client.data.models.blockstates.MultiVariantGenerator.dispatch(block)
                            .with(net.minecraft.client.data.models.blockstates.PropertyDispatch.initial(
                                    net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT)
                                    .generate(lit -> {
                                        var modelId = lit ? litModel : unlitModel;
                                        return new net.minecraft.client.data.models.MultiVariant(
                                                net.minecraft.util.random.WeightedList.of(
                                                        new net.minecraft.client.renderer.block.dispatch.Variant(modelId)));
                                    }))
            );

            gen.registerSimpleItemModel(block, unlitModel);
        }
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        this.cachedOutput = cachedOutput;
        try {
            return super.run(cachedOutput);
        } finally {
            this.cachedOutput = null;
        }
    }

    @Override
    @NonNull
    public String getName() {
        return MiteRecrafted.MOD_ID + " Models";
    }
}
