package com.mite.recraft.content.item.food;

import com.mite.recraft.content.item.bucket.ModBucketItems;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public enum ContainerType {
    NONE,
    BOWL,
    BUCKET;

    public ItemStack getContainer(String bucketMaterial) {
        return switch (this) {
            case BOWL -> new ItemStack(Items.BOWL);
            case BUCKET -> {
                if (bucketMaterial == null) yield ItemStack.EMPTY;
                yield switch (bucketMaterial) {
                    case "copper" -> new ItemStack(ModBucketItems.COPPER_BUCKET);
                    case "silver" -> new ItemStack(ModBucketItems.SILVER_BUCKET);
                    case "gold" -> new ItemStack(ModBucketItems.GOLD_BUCKET);
                    case "ancient_metal" -> new ItemStack(ModBucketItems.ANCIENT_METAL_BUCKET);
                    case "mithril" -> new ItemStack(ModBucketItems.MITHRIL_BUCKET);
                    case "adamantium" -> new ItemStack(ModBucketItems.ADAMANTIUM_BUCKET);
                    default -> ItemStack.EMPTY;
                };
            }
            default -> ItemStack.EMPTY;
        };
    }
}
