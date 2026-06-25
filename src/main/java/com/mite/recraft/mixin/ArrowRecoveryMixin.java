package com.mite.recraft.mixin;

import com.mite.recraft.component.ModDataComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class ArrowRecoveryMixin {

    @Unique
    private static final java.util.Random mite_recovery$RANDOM = new java.util.Random();

    @Inject(method = "spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;F)Lnet/minecraft/world/entity/item/ItemEntity;",
            at = @At("HEAD"), cancellable = true)
    private void filterRecovery(ServerLevel level, ItemStack stack, float f,
                                CallbackInfoReturnable<ItemEntity> cir) {
        if (stack.has(DataComponents.INTANGIBLE_PROJECTILE)) {
            cir.setReturnValue(null);
            return;
        }
        Float chance = stack.get(ModDataComponents.RECOVERY_CHANCE);
        if (chance != null && mite_recovery$RANDOM.nextFloat() >= chance) {
            cir.setReturnValue(null);
        }
    }
}
