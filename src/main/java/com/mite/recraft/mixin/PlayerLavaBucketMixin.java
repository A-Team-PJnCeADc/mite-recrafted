package com.mite.recraft.mixin;

import com.mite.recraft.item.moditems.bucket.ModFilledBucketItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 玩家完全浸入水中时，物品栏中的装满岩浆的金属桶变为石头桶。
 */
@Mixin(Player.class)
public class PlayerLavaBucketMixin {

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        Player self = (Player) (Object) this;
        if (self.level().isClientSide()) return;
        if (self.tickCount % 20 != 0) return;   // 每秒检查一次
        if (!self.isUnderWater()) return;

        var inv = self.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;
            if (!(stack.getItem() instanceof ModFilledBucketItem filled)) continue;
            if (!filled.fluid.isSame(Fluids.LAVA)) continue;

            // lava 桶 → stone 桶，同材质
            Item stoneBucket = findStoneBucket(stack.getItem());
            if (stoneBucket != null) {
                inv.setItem(i, new ItemStack(stoneBucket, stack.getCount()));
            }
        }
    }

    /** 查找同材质的石头桶 */
    private static Item findStoneBucket(Item lavaBucket) {
        Identifier id = BuiltInRegistries.ITEM.getKey(lavaBucket);
        String path = id.getPath();
        if (!path.endsWith("_lava_bucket")) return null;
        String stonePath = path.substring(0, path.length() - "_lava_bucket".length()) + "_stone_bucket";
        Identifier stoneId = Identifier.fromNamespaceAndPath(id.getNamespace(), stonePath);
        return BuiltInRegistries.ITEM.getValue(stoneId);
    }
}
