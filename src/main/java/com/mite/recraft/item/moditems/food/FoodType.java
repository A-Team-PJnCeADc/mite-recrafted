package com.mite.recraft.item.moditems.food;

/**
 * 食物类型基接口 — 所有食物枚举都实现此接口。
 *
 * <p>三个实现：{@link CommonFood}（普通食物）、{@link BowlFood}（碗装食物）、{@link BucketFood}（桶装食物）。</p>
 */
public interface FoodType {

    /** 物品注册 ID（如 "vegetable_soup"） */
    String itemId();

    /** 饥饿值（半鸡腿数，如 6 = 3 鸡腿） */
    int hunger();

    /** MITE 营养值 — 次级储备（对应 MITE setFoodValue 第二参数） */
    default int nutrition() { return 0; }

    /** 饱和度修饰符（浮点数，乘到饥饿上得到实际饱和度） */
    float saturationModifier();

    /** 蛋白质（MITE 营养系统） */
    int protein();

    /** 植物营养素（MITE 营养系统） */
    int phytonutrients();

    /** 必需脂肪酸（MITE 营养系统） */
    int essentialFats();

    /** 糖分（MITE 营养系统，影响胰岛素抵抗） */
    int sugar();

    /** 容器返回类型 */
    ContainerType containerType();

    /** 桶材质名（仅 {@code containerType() == BUCKET} 时需要） */
    default String bucketMaterial() { return null; }

    /** 最大堆叠数，由实例化时传入 */
    int maxStackSize();

    /** 食用后是否清除状态效果（如牛奶桶） */
    default boolean clearsEffects() { return false; }
}
