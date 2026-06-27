package com.mite.recraft.item.tools.toolItem;

import com.mite.recraft.component.ModDataComponents;
import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.ToolMaterial;

/**
 * 工具类型枚举 —— 定义每种工具的属性，统一实例化。
 *
 * 使用方式：
 *   ToolType.AXE.create(ModToolMaterial.COPPER, 5.0F, -3.0F)
 */
public enum ToolType {
    //todo 配平数值
    //TODO 银质工具对亡灵怪物有伤害加成，且可攻击白色食尸鬼
    AXE         ("axe",         3, ModToolMaterial.ALL,      AxeItem::new),
    PICKAXE     ("pickaxe",     3, ModToolMaterial.METALS,   plainItem(Item.Properties::pickaxe)),
    SHOVEL      ("shovel",      1, ModToolMaterial.ALL,      ShovelItem::new),
    HOE         ("hoe",         2, ModToolMaterial.METALS,   HoeItem::new),
    SCYTHE      ("scythe",      2, ModToolMaterial.METALS,   ScytheItems::new),
    SWORD       ("sword",       2, ModToolMaterial.METALS,   plainItem(Item.Properties::sword)),
    HATCHET     ("hatchet",     1, ModToolMaterial.ALL,      AxeItem::new),
    DAGGER      ("dagger",      1, ModToolMaterial.METALS,   plainItem(Item.Properties::sword)),
    BATTLE_AXE  ("battle_axe",  4, ModToolMaterial.METALS,   AxeItem::new),
    WAR_HAMMER  ("war_hammer",  5, ModToolMaterial.METALS,   plainItem(Item.Properties::pickaxe)),
    MATTOCK     ("mattock",     4, ModToolMaterial.METALS,   plainItem(Item.Properties::shovel)),
    SHEARS      ("shears",      2, ModToolMaterial.METALS,   (tm, d, s, p) -> new ShearsItem(p.durability(tm.durability()))),
    KNIFE       ("knife",       1, ModToolMaterial.ALL,      (tm, d, s, p) -> new Item(p.sword(tm, d, s))),
    FISHING_ROD ("fishing_rod", 1, ModToolMaterial.ALL,      (tm, d, s, p) -> new FishingRodItem(p.durability(tm.durability()))),
    ;

    private final String suffix;
    private final int numComponents;
    private final ModToolMaterial[] materialSet;
    @FunctionalInterface
    public interface ItemFactory {
        Item create(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties properties);
    }
    private final ItemFactory factory;

    ToolType(String suffix, int numComponents, ModToolMaterial[] materialSet, ItemFactory factory) {
        this.suffix = suffix;
        this.numComponents = numComponents;
        this.materialSet = materialSet;
        this.factory = factory;
    }

    public String suffix() { return suffix; }
    public int numComponents() { return numComponents; }
    public ModToolMaterial[] materialSet() { return materialSet; }

    /** 创建一个材质的具体工具实例 */
    public Item create(ModToolMaterial mat, float attackDamage, float attackSpeed) {
        ToolMaterial tm = customToolMaterial(mat);
        Item.Properties props = new Item.Properties()
                .setId(itemKey(mat))
                .enchantable(tm.enchantmentValue())
                .component(ModDataComponents.TOOL_COMPONENTS, numComponents)
                .component(ModDataComponents.TOOL_MATERIAL_TIER, mat.getDurabilityCoefficient())
                .component(ModDataComponents.TOOL_REPAIR_TAG, mat.getRepairTagName());
        return factory.create(tm, attackDamage, attackSpeed, props);
    }

    /** 重载：提供 ResourceKey */
    public Item create(ModToolMaterial mat, ResourceKey<Item> key, float attackDamage, float attackSpeed) {
        ToolMaterial tm = customToolMaterial(mat);
        Item.Properties props = new Item.Properties().setId(key);
        return factory.create(tm, attackDamage, attackSpeed, props);
    }

    /** 创建带 numComponents 乘数的 ToolMaterial，公开给战锤/鹤嘴锄等自定义实例化 */
    public ToolMaterial customToolMaterial(ModToolMaterial mat) {
        ToolMaterial tm = mat.getToolMaterial();
        return new ToolMaterial(
                tm.incorrectBlocksForDrops(),
                tm.durability() * numComponents,
                tm.speed(),
                tm.attackDamageBonus(),
                tm.enchantmentValue(),
                tm.repairItems()
        );
    }

    /** 构建 ResourceKey<Item>，公开给自定义实例化 */
    public ResourceKey<Item> itemKey(ModToolMaterial mat) {
        return ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath("mite-recrafted", mat.getName() + "_" + suffix));
    }

    /** 纯 Item 工厂：需要通过 Properties 追加工具属性（pickaxe/sword/shovel 等） */
    private static ItemFactory plainItem(
            TriFunction<Item.Properties, ToolMaterial, Float, Float, Item.Properties> configurer) {
        return (tm, dmg, speed, props) -> new Item(configurer.apply(props, tm, dmg, speed));
    }

    @FunctionalInterface
    private interface TriFunction<A, B, C, D, R> {
        R apply(A a, B b, C c, D d);
    }
}
