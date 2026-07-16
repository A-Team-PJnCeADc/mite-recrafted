package com.mite.recraft.item.moditems.bucket;

import net.minecraft.world.item.Item;

/**
 * MITE 金属桶 — 由 {@link BucketType} 枚举驱动创建和注册。
 * peer 引用在 {@link BucketType#create} 时自动链接。
 */
public class ModBucketItems {

    // 空桶
    public static final Item COPPER_BUCKET       = BucketType.EMPTY.create("copper");
    public static final Item SILVER_BUCKET       = BucketType.EMPTY.create("silver");
    public static final Item GOLD_BUCKET         = BucketType.EMPTY.create("gold");
    public static final Item ANCIENT_METAL_BUCKET = BucketType.EMPTY.create("ancient_metal");
    public static final Item MITHRIL_BUCKET       = BucketType.EMPTY.create("mithril");
    public static final Item ADAMANTIUM_BUCKET    = BucketType.EMPTY.create("adamantium");

    // 水桶
    public static final Item COPPER_WATER_BUCKET       = BucketType.WATER.create("copper");
    public static final Item SILVER_WATER_BUCKET       = BucketType.WATER.create("silver");
    public static final Item GOLD_WATER_BUCKET         = BucketType.WATER.create("gold");
    public static final Item ANCIENT_METAL_WATER_BUCKET = BucketType.WATER.create("ancient_metal");
    public static final Item MITHRIL_WATER_BUCKET       = BucketType.WATER.create("mithril");
    public static final Item ADAMANTIUM_WATER_BUCKET    = BucketType.WATER.create("adamantium");

    // 岩浆桶
    public static final Item COPPER_LAVA_BUCKET       = BucketType.LAVA.create("copper");
    public static final Item SILVER_LAVA_BUCKET       = BucketType.LAVA.create("silver");
    public static final Item GOLD_LAVA_BUCKET         = BucketType.LAVA.create("gold");
    public static final Item ANCIENT_METAL_LAVA_BUCKET = BucketType.LAVA.create("ancient_metal");
    public static final Item MITHRIL_LAVA_BUCKET       = BucketType.LAVA.create("mithril");
    public static final Item ADAMANTIUM_LAVA_BUCKET    = BucketType.LAVA.create("adamantium");

    // 石桶
    public static final Item COPPER_STONE_BUCKET       = BucketType.STONE.create("copper");
    public static final Item SILVER_STONE_BUCKET       = BucketType.STONE.create("silver");
    public static final Item GOLD_STONE_BUCKET         = BucketType.STONE.create("gold");
    public static final Item IRON_STONE_BUCKET         = BucketType.STONE.create("iron");
    public static final Item ANCIENT_METAL_STONE_BUCKET = BucketType.STONE.create("ancient_metal");
    public static final Item MITHRIL_STONE_BUCKET       = BucketType.STONE.create("mithril");
    public static final Item ADAMANTIUM_STONE_BUCKET    = BucketType.STONE.create("adamantium");

    public static void init() {}
}
