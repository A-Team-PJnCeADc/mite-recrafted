package com.mite.recraft.item.moditems.food;

/**
 * 碗装食物枚举（吃完后返回 Bowl）— 实现 {@link FoodType} 接口。
 *
 * <p>所有数值直接传入：hunger=鸡腿，saturation=金鸡腿（最终值，非修饰符）。</p>
 */
public enum BowlFood implements FoodType {

    // 蔬菜汤
    VEGETABLE_SOUP("vegetable_soup", 6, 6, 0, 48000, 0, 0, 1),
    // 水碗
    WATER_BOWL("water_bowl", 0, 0, 0, 0, 0, 0, 16),
    // 牛奶碗
    MILK_BOWL("milk_bowl", 1, 0, 8000, 0, 0, 0, 16),
    // 沙拉
    SALAD("salad", 1, 1, 0, 8000, 0, 0, 4),
    // 蓝莓粥
    BLUEBERRY_PORRIDGE("blueberry_porridge", 2, 4, 0, 16000, 0, 9600, 4),
    // 南瓜汤
    PUMPKIN_SOUP("pumpkin_soup", 1, 2, 0, 16000, 0, 0, 4),
    // 奶油蘑菇汤
    CREAM_OF_MUSHROOM_SOUP("cream_of_mushroom_soup", 5, 3, 40000, 0, 0, 0, 4),
    // 奶油蔬菜汤
    CREAM_OF_VEGETABLE_SOUP("cream_of_vegetable_soup", 7, 7, 56000, 56000, 0, 0, 4),
    // 鸡汤
    CHICKEN_SOUP("chicken_soup", 10, 10, 80000, 80000, 0, 0, 4),
    // 麦片粥
    CEREAL("cereal", 4, 2, 16000, 0, 0, 4800, 4),
    // 牛肉羹
    BEEF_STEW("beef_stew", 16, 16, 128000, 128000, 0, 0, 4),
    // 果汁雪糕
    SORBET("sorbet", 4, 2, 0, 16000, 0, 9600, 4),
    // 土豆泥（烤马铃薯 + 奶酪 + 牛奶碗）
    MASHED_POTATO("mashed_potato", 12, 8, 64000, 0, 0, 0, 4),
    // 冰淇淋（雪球 + 可可豆 + 糖 + 牛奶碗）
    ICE_CREAM("ice_cream", 5, 4, 32000, 0, 0, 4800, 4)
    ;

    private final String itemId;
    private final int hunger;
    private final float saturationModifier;
    private final int protein;
    private final int phytonutrients;
    private final int essentialFats;
    private final int sugar;
    private final int nutrition;
    private final ContainerType containerType;
    private final int maxStackSize;

    BowlFood(String itemId, int hunger, int saturation,
             int protein, int phytonutrients, int essentialFats, int sugar,
             int maxStackSize) {
        this.itemId = itemId;
        this.hunger = hunger;
        // 饱和度修饰符 = 最终饱和度 ÷ (饥饿值 × 2)
        // MC 26.2 公式: actual_sat = hunger × satMod × 2
        this.saturationModifier = hunger > 0 ? (float) saturation / (hunger * 2) : 0;
        this.protein = protein;
        this.phytonutrients = phytonutrients;
        this.essentialFats = essentialFats;
        this.sugar = sugar;
        this.nutrition = hunger;
        this.containerType = ContainerType.BOWL;
        this.maxStackSize = maxStackSize;
    }

    @Override public String itemId() { return itemId; }
    @Override public int hunger() { return hunger; }
    @Override public float saturationModifier() { return saturationModifier; }
    @Override public int nutrition() { return nutrition; }
    @Override public int protein() { return protein; }
    @Override public int essentialFats() { return essentialFats; }
    @Override public int phytonutrients() { return phytonutrients; }
    @Override public int sugar() { return sugar; }
    @Override public ContainerType containerType() { return containerType; }
    @Override public int maxStackSize() { return maxStackSize; }
    @Override public boolean clearsEffects() { return this == MILK_BOWL; }
}
