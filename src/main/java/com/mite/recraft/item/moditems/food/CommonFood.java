package com.mite.recraft.item.moditems.food;

/**
 * 普通食物枚举（吃完后无容器返回）— 实现 {@link FoodType} 接口。
 *
 * <p>所有数值直接传入：hunger=鸡腿，saturation=金鸡腿（最终值）。</p>
 */
public enum CommonFood implements FoodType {

    ONION("onion", 1, 1, 0, 8000, 0, 0, 16),

    //原版食物 MITE 重制（不注册为独立物品，通过 mixin 覆盖原版）
    POTATO("potato", 3, 1, 0, 0, 0, 0, 16),
    CARROT("carrot", 1, 2, 0, 16000, 0, 0, 16)
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
