package com.mite.recraft.item.moditems.food;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

/**
 * 普通食物枚举（吃完后无容器返回）— 实现 {@link FoodType} 接口。
 *
 * <p>所有数值直接传入：hunger=鸡腿，saturation=金鸡腿。</p>
 */
public enum CommonFood implements FoodType {

    // 洋葱
    ONION("onion", 1, 1, 0, 8000, 0, 0, 16),
    // 香蕉
    BANANA("banana", 2, 1, 0, 8000, 0, 4800, 16),
    // 橘子
    ORANGE("orange", 2, 1, 0, 8000, 0, 4800, 16),
    // 蓝莓
    BLUEBERRIES("blueberries", 1, 1, 0, 8000, 0, 4800, 16),
    // 奶酪
    CHEESE("cheese", 3, 3, 24000, 0, 0, 0, 16),
    // 面团
    DOUGH("dough", 2, 6, 0, 0, 0, 0, 16),
    // 巧克力
    CHOCOLATE("chocolate", 3, 3, 0, 0, 0, 4800, 16),
    // 生虫子
    WORM_RAW("worm_raw", 1, 0, 8000, 0, 0, 0, 16),
    // 熟虫子
    WORM_COOKED("worm_cooked", 1, 1, 8000, 0, 0, 0, 16),

    //原版食物 MITE 重制（不注册为独立物品，通过 mixin 覆盖原版）
    // 土豆
    POTATO(Items.POTATO, 3, 1, 0, 0, 0, 0, 16),
    // 胡萝卜
    CARROT(Items.CARROT, 1, 2, 0, 16000, 0, 0, 16),
    // 熟鸡肉
    COOKED_CHICKEN(Items.COOKED_CHICKEN, 6, 6, 48000, 0, 0, 0, 16),
    // 生鸡肉
    CHICKEN(Items.CHICKEN, 3, 3, 24000, 0, 0, 0, 16),
    // 熟牛肉
    COOKED_BEEF(Items.COOKED_BEEF, 10, 10, 80000, 0, 0, 0, 16),
    // 生牛肉
    BEEF(Items.BEEF, 5, 5, 40000, 0, 0, 0, 16),
    // 生羊肉
    MUTTON(Items.MUTTON, 3, 3, 24000, 0, 0, 0, 16),
    // 生鲑鱼
    SALMON(Items.SALMON, 5, 5, 40000, 0, 0, 0, 16),
    // 熟鲑鱼
    COOKED_SALMON(Items.COOKED_SALMON, 10, 10, 80000, 0, 0, 0, 16),
    // 熟羊排
    COOKED_MUTTON(Items.COOKED_MUTTON, 6, 6, 48000, 0, 0, 0, 16),
    // 烤马铃薯
    BAKED_POTATO(Items.BAKED_POTATO, 6, 2, 0, 0, 0, 0, 16),
    ;

    private final String itemId;
    private final int hunger;
    private final float saturationModifier;
    private final int protein;
    private final int phytonutrients;
    private final int essentialFats;
    private final int sugar;
    private final ContainerType containerType;
    private final int maxStackSize;
    private final Item vanillaItem;  // null = 自定义食物；非 null = 原版食物覆盖

    CommonFood(String itemId, int hunger, int saturation,
               int protein, int phytonutrients, int essentialFats, int sugar,
               int maxStackSize) {
        this.itemId = itemId;
        this.hunger = hunger;
        this.saturationModifier = hunger > 0 ? (float) saturation / (hunger * 2) : 0;
        this.protein = protein;
        this.phytonutrients = phytonutrients;
        this.essentialFats = essentialFats;
        this.sugar = sugar;
        this.containerType = ContainerType.NONE;
        this.maxStackSize = maxStackSize;
        this.vanillaItem = null;
    }

    /** 重载构造函数 — 用于覆盖原版食物时传入 Items.POTATO 等 */
    CommonFood(Item vanillaItem, int hunger, int saturation,
               int protein, int phytonutrients, int essentialFats, int sugar,
               int maxStackSize) {
        this.itemId = BuiltInRegistries.ITEM.getKey(vanillaItem).getPath();
        this.hunger = hunger;
        this.saturationModifier = hunger > 0 ? (float) saturation / (hunger * 2) : 0;
        this.protein = protein;
        this.phytonutrients = phytonutrients;
        this.essentialFats = essentialFats;
        this.sugar = sugar;
        this.containerType = ContainerType.NONE;
        this.maxStackSize = maxStackSize;
        this.vanillaItem = vanillaItem;  // 直接存储引用
    }

    /** 根据原版 Item 查找对应的 CommonFood */
    public static CommonFood fromItem(Item item) {
        for (CommonFood f : values()) {
            if (item.equals(f.vanillaItem)) return f;
        }
        return null;
    }

    @Override public String itemId() { return itemId; }
    @Override public int hunger() { return hunger; }
    @Override public float saturationModifier() { return saturationModifier; }
    @Override public int protein() { return protein; }
    @Override public int phytonutrients() { return phytonutrients; }
    @Override public int essentialFats() { return essentialFats; }
    @Override public int sugar() { return sugar; }
    @Override public ContainerType containerType() { return containerType; }
    @Override public int maxStackSize() { return maxStackSize; }
}
