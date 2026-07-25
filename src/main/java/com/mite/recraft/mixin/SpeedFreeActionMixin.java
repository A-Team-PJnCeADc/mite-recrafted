package com.mite.recraft.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mite.recraft.enchantment.ModEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/**
 * 灵活移动 (Free Action) — 削弱缓慢效果。
 * 蜘蛛网部分在 FreeActionWebMixin 中处理（Entity 级别的注入）。
 * Speed 附魔也在此合并处理避免链式冲突。
 */
@Mixin(LivingEntity.class)
public class SpeedFreeActionMixin {

    @ModifyReturnValue(method = "getAttributeValue(Lnet/minecraft/core/Holder;)D", at = @At("RETURN"))
    private double mite$applyFreeAction(double originalValue, Holder<Attribute> attribute) {
        if (attribute != Attributes.MOVEMENT_SPEED) return originalValue;
        LivingEntity self = (LivingEntity) (Object) this;

        double result = originalValue;

        // 抵消缓慢效果减速
        if (self.hasEffect(MobEffects.SLOWNESS)) {
            int freeLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    self.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                            .getOrThrow(ModEnchantments.FREE_ACTION.key()),
                    self.getItemBySlot(EquipmentSlot.LEGS)
            );
            if (freeLevel > 0) {
                double reduction = 0.20 * freeLevel;
                double noSlownessSpeed = result / (1.0 - mite$getSlownessFactor(self));
                result = result + (noSlownessSpeed - result) * Math.min(reduction, 1.0);
            }
        }

        // Speed 附魔
        int speedLevel = EnchantmentHelper.getItemEnchantmentLevel(
                self.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(ModEnchantments.SPEED.key()),
                self.getItemBySlot(EquipmentSlot.FEET)
        );
        if (speedLevel > 0) {
            float multiplier = com.mite.recraft.enchantment.Speed.getMultiplier(speedLevel);
            result = result * multiplier;
        }

        return result;
    }

    @Unique
    private static double mite$getSlownessFactor(LivingEntity entity) {
        var effect = entity.getEffect(MobEffects.SLOWNESS);
        if (effect == null) return 0;
        return 0.15 * (effect.getAmplifier() + 1);
    }
}
