package com.mite.recraft.mixin;

import com.mite.recraft.item.moditems.bucket.ModEmptyBucketItem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 金属空桶落入水中自动装水。
 *
 * <p>在 {@code ItemEntity.tick()} 末尾检测：
 * 如果物品实体在水中且物品是 MITE 空桶，则替换为对应的水桶。</p>
 */
@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Shadow
    private int age;

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void onTickInWater(CallbackInfo ci) {
        if (age < 20) return;

        ItemEntity self = (ItemEntity) (Object) this;
        if (!self.isInWater()) return;

        ItemStack stack = self.getItem();
        if (stack.isEmpty()) return;

        if (stack.getItem() instanceof ModEmptyBucketItem empty && empty.waterPeer != null) {
            self.setItem(new ItemStack(empty.waterPeer, stack.getCount()));
        }
    }
}
