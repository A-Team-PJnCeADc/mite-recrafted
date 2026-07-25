package com.mite.recraft.mixin;

import com.mite.recraft.enchantment.ModEnchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 灵活移动 — 蜘蛛网减速抵消。
 * 使用递归标志避免 @Inject(cancel)+递归调用导致的 StackOverflow。
 */
@Mixin(Entity.class)
public class FreeActionWebMixin {

    @Unique
    private static final ThreadLocal<Boolean> mite$reentrant = ThreadLocal.withInitial(() -> false);

    @Inject(method = "makeStuckInBlock", at = @At("HEAD"), cancellable = true)
    private void mite$reduceWebStuck(BlockState state, Vec3 speedMultiplier, CallbackInfo ci) {
        if (!state.is(Blocks.COBWEB)) return;
        if (!(((Entity) (Object) this) instanceof LivingEntity self)) return;
        if (mite$reentrant.get()) return; // 防止递归

        int freeLevel = EnchantmentHelper.getItemEnchantmentLevel(
                self.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(ModEnchantments.FREE_ACTION.key()),
                self.getItemBySlot(EquipmentSlot.LEGS)
        );
        if (freeLevel <= 0) return;

        double reduction = 1.0 - 0.20 * freeLevel;
        reduction = Math.max(reduction, 0.0);
        Vec3 reduced = new Vec3(
                speedMultiplier.x + (1.0 - speedMultiplier.x) * (1.0 - reduction),
                speedMultiplier.y + (1.0 - speedMultiplier.y) * (1.0 - reduction),
                speedMultiplier.z + (1.0 - speedMultiplier.z) * (1.0 - reduction)
        );

        ci.cancel();
        mite$reentrant.set(true);
        try {
            self.makeStuckInBlock(state, reduced);
        } finally {
            mite$reentrant.set(false);
        }
    }
}
