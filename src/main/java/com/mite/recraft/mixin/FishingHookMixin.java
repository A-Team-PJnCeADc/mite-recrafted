package com.mite.recraft.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * 修复自定义鱼竿的浮漂被立即移除的问题。
 *
 * 原版 FishingHook.shouldStopFishing() 用 ItemStack.is(Items.FISHING_ROD)
 * 精确匹配原版物品，导致任何模组鱼竿的浮漂在 tick 第一帧就被 discard()。
 * 改为 instanceof FishingRodItem 检查，兼容所有自定义鱼竿。
 */
@Mixin(FishingHook.class)
public class FishingHookMixin {

    /**
     * @author mite-recrafted
     * @reason 原版硬编码检查 Items.FISHING_ROD，不支持模组鱼竿
     */
    @Overwrite
    private boolean shouldStopFishing(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        boolean holdsFishingRod = mainHand.getItem() instanceof FishingRodItem
                || offHand.getItem() instanceof FishingRodItem;
        boolean mainEmpty = mainHand.isEmpty();
        boolean offEmpty = offHand.isEmpty();

        // 玩家没拿鱼竿（或拿的不是鱼竿），停止钓鱼
        if (!holdsFishingRod || (mainEmpty && offEmpty)) {
            ((FishingHook) (Object) this).discard();
            return true;
        }

        // 距离检查
        if (((FishingHook) (Object) this).distanceToSqr(player) > 1024.0D) {
            ((FishingHook) (Object) this).discard();
            return true;
        }

        return false;
    }
}
