package com.mite.recraft.mixin;

import com.mite.recraft.enchantment.ModEnchantments;
import com.mite.recraft.enchantment.Vampiric;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 吸血 (Vampiric) 效果：攻击后概率回复生命。
 */
@Mixin(LivingEntity.class)
public class VampiricMixin {

    @Inject(method = "hurtServer", at = @At("RETURN"))
    private void mite$onHurt(ServerLevel serverLevel, DamageSource source, float amount,
                             CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;

        LivingEntity victim = (LivingEntity) (Object) this;
        if (!(source.getEntity() instanceof LivingEntity attacker)) return;

        int level = EnchantmentHelper.getItemEnchantmentLevel(
                serverLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(ModEnchantments.VAMPIRIC.key()),
                attacker.getMainHandItem()
        );

        if (level > 0) {
            Vampiric.tryVampiric(attacker, victim, level, amount);
        }
    }
}
