package com.mite.recraft.registry;

import com.mite.recraft.content.block.furnace.MiteFuels;
import com.mite.recraft.content.item.bucket.ModBucketItems;
import com.mite.recraft.content.item.tool.WoodenItems;

/**
 * 燃料注册。原版物品的 MITE 覆盖值在 {@link MiteFuels#registerDefaults()}，此处只登记本模组物品。
 */
public final class ModFuels {

    private static final int WOODEN_TOOL_BURN_TICKS = 200;
    private static final int LAVA_BUCKET_BURN_TICKS = 3200;
    private static final int LAVA_BUCKET_HEAT = 3;

    private ModFuels() {
    }

    public static void register() {
        MiteFuels.registerDefaults();

        MiteFuels.register(WoodenItems.CLUB, WOODEN_TOOL_BURN_TICKS, 1);
        MiteFuels.register(WoodenItems.CUDGEL, WOODEN_TOOL_BURN_TICKS, 1);

        MiteFuels.register(ModBucketItems.COPPER_LAVA_BUCKET, LAVA_BUCKET_BURN_TICKS, LAVA_BUCKET_HEAT);
        MiteFuels.register(ModBucketItems.SILVER_LAVA_BUCKET, LAVA_BUCKET_BURN_TICKS, LAVA_BUCKET_HEAT);
        MiteFuels.register(ModBucketItems.GOLD_LAVA_BUCKET, LAVA_BUCKET_BURN_TICKS, LAVA_BUCKET_HEAT);
        MiteFuels.register(ModBucketItems.ANCIENT_METAL_LAVA_BUCKET, LAVA_BUCKET_BURN_TICKS, LAVA_BUCKET_HEAT);
        MiteFuels.register(ModBucketItems.MITHRIL_LAVA_BUCKET, LAVA_BUCKET_BURN_TICKS, LAVA_BUCKET_HEAT);
        MiteFuels.register(ModBucketItems.ADAMANTIUM_LAVA_BUCKET, LAVA_BUCKET_BURN_TICKS, LAVA_BUCKET_HEAT);
    }
}
