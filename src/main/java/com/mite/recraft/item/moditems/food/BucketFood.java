package com.mite.recraft.item.moditems.food;

/**
 * 桶装食物枚举 — 实现 {@link FoodType} 接口。
 * 吃完后返回对应材质空桶，清除状态效果。
 *
 * <p>所有数值直接传入：hunger=鸡腿，saturation=金鸡腿（最终值）。</p>
 */
public enum BucketFood implements FoodType {

    COPPER_MILK_BUCKET("copper_milk_bucket", "copper"),
    SILVER_MILK_BUCKET("silver_milk_bucket", "silver"),
    GOLD_MILK_BUCKET("gold_milk_bucket", "gold"),
    ANCIENT_METAL_MILK_BUCKET("ancient_metal_milk_bucket", "ancient_metal"),
    MITHRIL_MILK_BUCKET("mithril_milk_bucket", "mithril"),
    ADAMANTIUM_MILK_BUCKET("adamantium_milk_bucket", "adamantium"),
    ;

    private static final int HUNGER = 4;
    private static final int SATURATION = 0;
    private static final float SAT_MOD = HUNGER > 0 ? (float) SATURATION / (HUNGER * 2) : 0;
    private static final int NUTRITION = 4;
    private static final int PROTEIN = 32000;

    private final String itemId;
    private final String bucketMaterial;

    BucketFood(String itemId, String bucketMaterial) {
        this.itemId = itemId;
        this.bucketMaterial = bucketMaterial;
    }

    @Override public String itemId() { return itemId; }
    @Override public int hunger() { return HUNGER; }
    @Override public int nutrition() { return NUTRITION; }
    @Override public float saturationModifier() { return SAT_MOD; }
    @Override public int protein() { return PROTEIN; }
    @Override public int phytonutrients() { return 0; }
    @Override public int essentialFats() { return 0; }
    @Override public int sugar() { return 0; }
    @Override public ContainerType containerType() { return ContainerType.BUCKET; }
    @Override public String bucketMaterial() { return bucketMaterial; }
    @Override public int maxStackSize() { return 1; }
    @Override public boolean clearsEffects() { return true; }
}
