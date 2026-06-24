package com.mite.recraft.mixin;

import com.mite.recraft.util.StuckArrowTracker;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * 生物死亡时掉落卡箭，移除时清理防止内存泄漏。
 */
@Mixin(LivingEntity.class)
public class LivingEntityStuckArrowsMixin {

    @Inject(method = "dropAllDeathLoot", at = @At("TAIL"))
    private void dropStuckArrows(ServerLevel level, DamageSource source, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        List<ItemStack> arrows = StuckArrowTracker.remove(self);
        if (arrows != null) {
            for (ItemStack arrow : arrows) {
                self.spawnAtLocation(level, arrow);
            }
        }
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void cleanupStuckArrows(Entity.RemovalReason reason, CallbackInfo ci) {
        StuckArrowTracker.remove((LivingEntity) (Object) this);
    }
}
