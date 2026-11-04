package com.mite.recraft.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mite.recraft.content.combat.MiteArmor;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * 平减护甲入口：伤害在进入 LivingEntity.hurtServer 前先减去本模组护甲值。
 *
 * <p>本模组护甲不再提供原版 ARMOR 属性点，因此不会与原版百分比减伤叠加。</p>
 */
@Mixin(LivingEntity.class)
public class FlatArmorMixin {

    @ModifyVariable(
            method = "hurtServer",
            at = @At("HEAD"),
            argsOnly = true
    )
    private float mite$applyFlatArmor(float amount,
                                     ServerLevel level,
                                     @Local(argsOnly = true) DamageSource source) {
        return MiteArmor.apply(amount, (LivingEntity) (Object) this, source);
    }
}
