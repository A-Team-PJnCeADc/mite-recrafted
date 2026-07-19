package com.mite.recraft.item.moditems.food;

import com.mite.recraft.item.moditems.bucket.ModBucketItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * 食物容器返回类型 — 吃完后返还什么容器。
 *
 * <p>注意：{@link ItemStack} 不能在类加载时创建（注册表未就绪），
 * 因此通过 {@link #getContainer(String)} 延迟创建。</p>
 */
public enum ContainerType {
    /** 普通食物，无容器返回 */
    NONE,
    /** 碗装食物，返回 Bowl */
    BOWL,
    /** 桶装食物（奶桶），返回对应材质的空桶 */
    BUCKET;

    /**
     * 获取吃完后返还的容器 ItemStack。
     *
     * @param bucketMaterial BUCKET 类型时需要传入材质名（如 "copper"）
     */
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
