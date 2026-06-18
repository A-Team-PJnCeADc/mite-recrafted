package com.mite.recraft.item.tools.modtoolmaterials;

import com.mite.recraft.item.quality.Quality;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;


/**
 * 工具材料枚举
 * 每个枚举值包含耐久系数、最高品质、是否可用于工具/盔甲，以及原版 ToolMaterial 实例
 */
public enum ModToolMaterial {
    // ===== 基础材料 =====
    WOOD("wood", 0.5f, Quality.FINE, true, false,
            BlockTags.INCORRECT_FOR_WOODEN_TOOL,
            200, 1.0f, 0.0f, 1,
            repairsTag("repairs_wood")),

    FLINT("flint", 1.0f, Quality.FINE, true, false,
            BlockTags.INCORRECT_FOR_WOODEN_TOOL,
            400, 1.25f, 0.0f, 1,
            repairsTag("repairs_flint")),

    OBSIDIAN("obsidian", 2.0f, Quality.FINE, true, false,
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            800, 1.5f, 0.0f, 8,
            repairsTag("repairs_obsidian")),

    GLASS("glass", 2.0f, Quality.AVERAGE, false, false,
            BlockTags.INCORRECT_FOR_WOODEN_TOOL,
            800, 0.0001f, 0.0f, 1,
            repairsTag("repairs_glass")),

    NETHERRACK("netherrack", 4.0f, Quality.AVERAGE, false, false,
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            1600, 0.0001f, 0.0f, 1,
            repairsTag("repairs_netherrack")),

    QUARTZ("quartz", 4.0f, Quality.FINE, false, false,
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            1600, 0.0001f, 0.0f, 10,
            repairsTag("repairs_quartz")),

    // ===== 金属材料 =====
    COPPER("copper", 4.0f, Quality.EXCELLENT, true, true,
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            1600, 1.75f, 0.0f, 12,
            repairsTag("repairs_copper")),

    SILVER("silver", 4.0f, Quality.EXCELLENT, true, true,
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            1600, 1.75f, 0.0f, 10,
            repairsTag("repairs_silver")),

    GOLD("gold", 4.0f, Quality.SUPERB, true, true,
            BlockTags.INCORRECT_FOR_GOLD_TOOL,
            1600, 1.75f, 0.0f, 22,
            repairsTag("repairs_gold")),

    RUSTED_IRON("rusted_iron", 4.0f, Quality.POOR, true, true,
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            1600, 1.25f, 0.0f, 8,
            repairsTag("repairs_rusted_iron")),

    IRON("iron", 8.0f, Quality.MASTERWORK, true, true,
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            3200, 2.0f, 0.0f, 14,
            repairsTag("repairs_iron")),

    EMERALD("emerald", 8.0f, Quality.EXCELLENT, false, false,
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            3200, 0.0001f, 0.0f, 20,
            repairsTag("repairs_emerald")),

    DIAMOND("diamond", 16.0f, Quality.SUPERB, true, true,
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            6400, 2.5f, 0.0f, 10,
            repairsTag("repairs_diamond")),

    ANCIENT_METAL("ancient_metal", 16.0f, Quality.MASTERWORK, true, true,
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            6400, 2.0f, 0.0f, 18,
            repairsTag("repairs_ancient_metal")),

    MITHRIL("mithril", 64.0f, Quality.LEGENDARY, true, true,
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            25600, 2.5f, 0.0f, 24,
            repairsTag("repairs_mithril")),

    ADAMANTIUM("adamantium", 256.0f, Quality.LEGENDARY, true, true,
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            102400, 3.0f, 0.0f, 30,
            repairsTag("repairs_adamantium"));

    // ===== 字段 =====
    private final String name;
    private final float durabilityCoefficient;   // 耐久系数（material.durability）
    private final Quality maxQuality;            // 该材料最高可达品质
    private final boolean canTool;               // 是否可用于工具
    private final boolean canArmor;              // 是否可用于盔甲
    private final ToolMaterial toolMaterial;     // 原版 ToolMaterial 实例（用于工具/武器）

    // 构造函数接收 ToolMaterial 所需的所有参数
    ModToolMaterial(String name,
                    float durabilityCoefficient,
                    Quality maxQuality,
                    boolean canTool,
                    boolean canArmor,
                    TagKey<Block> incorrectBlocks,
                    int toolDurability,
                    float miningSpeed,
                    float attackDamage,
                    int enchantability,
                    TagKey<Item> repairIngredient) {
        this.name = name;
        this.durabilityCoefficient = durabilityCoefficient;
        this.maxQuality = maxQuality;
        this.canTool = canTool;
        this.canArmor = canArmor;
        // 只有可用于工具的材料才创建有效的 ToolMaterial，否则创建占位（但实际不会使用）
        this.toolMaterial = canTool ? new ToolMaterial(
                incorrectBlocks,
                toolDurability,
                miningSpeed,
                attackDamage,
                enchantability,
                repairIngredient
        ) : null;
    }

    // ===== Getter 方法 =====
    public String getName() { return name; }
    public float getDurabilityCoefficient() { return durabilityCoefficient; }
    public Quality getMaxQuality() { return maxQuality; }
    public boolean canTool() { return canTool; }
    public boolean canArmor() { return canArmor; }
    public ToolMaterial getToolMaterial() { return toolMaterial; }

    /**
     * 根据名称获取枚举（用于反序列化）
     */
    public static ModToolMaterial byName(String name) {
        for (ModToolMaterial m : values()) {
            if (m.name.equals(name)) return m;
        }
        return IRON;
    }

    // 创建修复物品的标签
    private static TagKey<Item> repairsTag(String path) {
        return TagKey.create(
                BuiltInRegistries.ITEM.key(),
                Identifier.fromNamespaceAndPath("mite-recraft", path)
        );
    }
}