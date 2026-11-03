package com.mite.recraft.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractArrow.class)
public interface AbstractArrowInvoker {
    @Invoker("<init>")
    static AbstractArrow create(EntityType<? extends AbstractArrow> type, LivingEntity owner,
                                Level level, ItemStack pickupItem, ItemStack weapon) {
        throw new AssertionError();
    }
}
