package com.mite.recraft.item.moditems.food;

/**
 * 碗装食物枚举（吃完后返回 Bowl）— 实现 {@link FoodType} 接口。
 *
 * <p>所有数值直接传入：hunger=鸡腿，saturation=金鸡腿（最终值，非修饰符）。</p>
 */
public enum BowlFood implements FoodType {

    VEGETABLE_SOUP("vegetable_soup", 6, 6, 0, 48000, 0, 0, 1),
    WATER_BOWL("water_bowl", 0, 0, 0, 0, 0, 0, 16),
    MILK_BOWL("milk_bowl", 1, 0, 8000, 0, 0, 0, 16)
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
        this.containerType = ContainerType.BOWL;
        this.maxStackSize = maxStackSize;
    }

    @Override public String itemId() { return itemId; }
    @Override public int hunger() { return hunger; }
    @Override public float saturationModifier() { return saturationModifier; }
    @Override public int nutrition() {
        return this == VEGETABLE_SOUP ? 6 : this == MILK_BOWL ? 1 : 0;
    }
    @Override public int protein() { return protein; }
    @Override public int essentialFats() { return essentialFats; }
    @Override public int phytonutrients() { return phytonutrients; }
    @Override public int sugar() { return sugar; }
    @Override public ContainerType containerType() { return containerType; }
    @Override public int maxStackSize() { return maxStackSize; }
    @Override public boolean clearsEffects() { return this == MILK_BOWL; }
}
