package com.mite.recraft.item.moditems.bucket;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.item.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.HashMap;
import java.util.Map;

/**
 * 桶类型枚举 — 定义 suffix / maxStackSize / fluid / 创建逻辑。
 * 创建满桶时通过材质名查找同材质空桶完成 peer 链接。
 */
//todo 手持桶右击水或熔岩可以获得装满水的铜桶或装满岩浆的铜桶，但是不能转移液体；按住 Ctrl 右击液体可以将其转移。盛熔岩时有16%的概率会损坏。
public enum BucketType {

    EMPTY ("_bucket",       8, null,          BucketType::createEmpty),
    WATER ("_water_bucket", 1, Fluids.WATER,  BucketType::createFilled),
    LAVA  ("_lava_bucket",  1, Fluids.LAVA,   BucketType::createFilled),
    STONE ("_stone_bucket", 1, null,          BucketType::createStorage);

    /** 材质名 → 空桶，创建满桶时查找 peer */
    private static final Map<String, Item> EMPTY_BUCKETS = new HashMap<>();

    final String suffix;
    final int maxStackSize;
    final Fluid fluid;
    final Factory factory;

    BucketType(String suffix, int maxStackSize, Fluid fluid, Factory factory) {
        this.suffix = suffix;
        this.maxStackSize = maxStackSize;
        this.fluid = fluid;
        this.factory = factory;
    }

    @FunctionalInterface
    interface Factory {
        Item create(String material, Item.Properties props, BucketType self);
    }

    /** 创建并注册桶物品 */
    public Item create(String material) {
        String name = material + suffix;
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, name));
        Item.Properties props = new Item.Properties().setId(key).stacksTo(maxStackSize);
        Item item = factory.create(material, props, this);

        ModItems.addBucket(item);
        Registry.register(BuiltInRegistries.ITEM, key, item);
        return item;
    }

    /* ===== Factory methods ===== */

    private static Item createEmpty(String material, Item.Properties props, BucketType self) {
        Item item = new ModEmptyBucketItem(props);
        EMPTY_BUCKETS.put(material, item);
        return item;
    }

    private static Item createFilled(String material, Item.Properties props, BucketType self) {
        ModFilledBucketItem item = new ModFilledBucketItem(self.fluid, props);
        Item empty = EMPTY_BUCKETS.get(material);
        if (empty instanceof ModEmptyBucketItem e) {
            if (self == WATER) {
                e.waterPeer = item;
            } else if (self == LAVA) {
                e.lavaPeer = item;
            }
        }
        item.emptyPeer = empty;
        return item;
    }

    private static Item createStorage(String material, Item.Properties props, BucketType self) {
        return new Item(props);
    }
}
