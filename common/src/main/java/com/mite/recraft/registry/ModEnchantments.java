package com.mite.recraft.registry;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.registry.ModItemTags;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jspecify.annotations.Nullable;

/**
 * MITE 自定义魔咒枚举，每个常量携带路径、支持物品分类和所有附魔台参数。
 * <p>
 * 使用 {@link #key()} 获取 ResourceKey，使用 {@link #definition(HolderGetter)} 获取 EnchantmentDefinition。
 */
public enum ModEnchantments {

    ARROW_RECOVERY("arrow_recovery", ItemCategory.BOW,      10, 5, 1, 10, 1, 15, 2, EquipmentSlotGroup.HAND),
    QUICKNESS     ("quickness",      ItemCategory.BOW,       5, 5, 10, 10, 15, 15, 5, EquipmentSlotGroup.HAND),
    POISON        ("poison",         ItemCategory.BOW,       3, 5, 10, 10, 15, 15, 5, EquipmentSlotGroup.HAND),
    ACCURACY      ("accuracy",       ItemCategory.BOW,      10, 5, 1, 10, 1, 15, 2, EquipmentSlotGroup.HAND),
    STUN      ("stun",      ItemCategory.STUN_WEAPON,       5, 5, 6, 15, 8, 15, 10, EquipmentSlotGroup.HAND),
    VAMPIRIC  ("vampiric",  ItemCategory.VAMPIRIC_WEAPON,   2, 5, 11, 20, 15, 20, 15, EquipmentSlotGroup.HAND),
    DISARMING ("disarming", ItemCategory.SWORD,             3, 4, 10, 10, 15, 15, 10, EquipmentSlotGroup.HAND),
    BUTCHERING("butchering", ItemCategory.DAGGER,           5, 3, 10, 10, 15, 15, 5,  EquipmentSlotGroup.HAND),
    SLAYING   ("slaying",    ItemCategory.BATTLE_AXE,       5, 5, 1, 10, 1, 15, 2, EquipmentSlotGroup.HAND),
    TREE_FELLING("tree_felling", ItemCategory.AXE,          5, 5, 10, 10, 15, 15, 10, EquipmentSlotGroup.HAND),
    HARVESTING  ("harvesting",  ItemCategory.FARM_TOOL,     5, 5, 10, 10, 15, 15, 10, EquipmentSlotGroup.HAND),
    FERTILITY   ("fertility",    ItemCategory.WEAPON,       5, 5, 10, 10, 15, 15, 10, EquipmentSlotGroup.HAND),
    PIERCING  ("piercing",  ItemCategory.PICKAXE,           3, 5, 10, 10, 15, 15, 5,  EquipmentSlotGroup.HAND),
    CLEAVING  ("cleaving",  ItemCategory.BATTLE_AXE,        3, 5, 10, 10, 15, 15, 5,  EquipmentSlotGroup.HAND),
    PROTECTION  ("protection",  ItemCategory.ARMOR,         10, 4, 10, 10, 15, 15, 5,  EquipmentSlotGroup.ANY),
    SPEED        ("speed",         ItemCategory.BOOTS,      3, 5, 10, 10, 15, 15, 5,  EquipmentSlotGroup.FEET),
    REGENERATION ("regeneration",  ItemCategory.CHESTPLATE, 3, 5, 10, 15, 15, 15, 10, EquipmentSlotGroup.CHEST),
    ENDURANCE    ("endurance",     ItemCategory.CHESTPLATE, 5, 4, 10, 10, 15, 15, 5,  EquipmentSlotGroup.CHEST),
    FREE_ACTION  ("free_action",   ItemCategory.LEGGINGS,   10, 4, 10, 10, 15, 15, 5,  EquipmentSlotGroup.LEGS),
    UNBREAKING("unbreaking", ItemCategory.DURABILITY, 5, 5, 5, 10, 15, 10, 2, EquipmentSlotGroup.ANY);

    /**
     * 物品分类枚举 - 每个条目关联对应的附魔支持物品标签。
     */
    public enum ItemCategory {
        BOW(ItemTags.BOW_ENCHANTABLE),
        STUN_WEAPON(ModItemTags.STUN_WEAPON),
        VAMPIRIC_WEAPON(ModItemTags.VAMPIRIC_WEAPON),
        SWORD(ItemTags.SWEEPING_ENCHANTABLE),
        DAGGER(ModItemTags.BUTCHERING_WEAPON),
        BATTLE_AXE(ModItemTags.SLAYING_WEAPON),
        AXE(ModItemTags.TREE_FELLING_TOOL),
        FARM_TOOL(ModItemTags.HARVESTING_TOOL),
        WEAPON(ItemTags.WEAPON_ENCHANTABLE),
        PICKAXE(ModItemTags.PIERCING_TOOL),
        ARMOR(ItemTags.ARMOR_ENCHANTABLE),
        DURABILITY(ItemTags.DURABILITY_ENCHANTABLE),
        BOOTS(ItemTags.FOOT_ARMOR_ENCHANTABLE),
        CHESTPLATE(ItemTags.CHEST_ARMOR_ENCHANTABLE),
        LEGGINGS(ItemTags.LEG_ARMOR_ENCHANTABLE);

        final TagKey<Item> tag;
        ItemCategory(TagKey<Item> tag) { this.tag = tag; }
        public TagKey<Item> getTag() { return tag; }
    }

    private final ResourceKey<Enchantment> key;
    private final @Nullable ItemCategory category;
    private final int weight;
    private final int maxLevel;
    private final int minCostBase;
    private final int minCostPerLevel;
    private final int maxCostBase;
    private final int maxCostPerLevel;
    private final int anvilCost;
    private final EquipmentSlotGroup slot;

    ModEnchantments(String path, @Nullable ItemCategory category,
                    int weight, int maxLevel,
                    int minCostBase, int minCostPerLevel,
                    int maxCostBase, int maxCostPerLevel,
                    int anvilCost, EquipmentSlotGroup slot) {
        Identifier id = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, path);
        this.key = ResourceKey.create(Registries.ENCHANTMENT, id);
        this.category = category;
        this.weight = weight;
        this.maxLevel = maxLevel;
        this.minCostBase = minCostBase;
        this.minCostPerLevel = minCostPerLevel;
        this.maxCostBase = maxCostBase;
        this.maxCostPerLevel = maxCostPerLevel;
        this.anvilCost = anvilCost;
        this.slot = slot;
    }

    /** ResourceKey 用于 data gen 注册和标签引用。 */
    public ResourceKey<Enchantment> key() { return key; }

    public @Nullable ItemCategory category() { return category; }

    /** 最大等级。 */
    public int maxLevel() { return maxLevel; }

    /** 在 bootstrap 阶段解析 HolderGetter 后构建 EnchantmentDefinition。 */
    public Enchantment.EnchantmentDefinition definition(HolderGetter<Item> items) {
        TagKey<Item> tag = category != null ? category.getTag() : null;
        return Enchantment.definition(
                items.getOrThrow(tag),
                weight, maxLevel,
                Enchantment.dynamicCost(minCostBase, minCostPerLevel),
                Enchantment.dynamicCost(maxCostBase, maxCostPerLevel),
                anvilCost, slot
        );
    }
}
