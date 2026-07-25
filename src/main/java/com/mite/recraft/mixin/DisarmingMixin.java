package com.mite.recraft.mixin;

import com.mite.recraft.enchantment.Disarming;
import com.mite.recraft.enchantment.ModEnchantments;
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
 * 缴械 (Disarming) 效果：攻击后概率打落目标手持物品。
 */
@Mixin(LivingEntity.class)
public class DisarmingMixin {

    @Inject(method = "hurtServer", at = @At("RETURN"))
    private void mite$onHurtServer(ServerLevel serverLevel, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        // 只有伤害实际生效时才触发（保留无敌帧）
        if (!cir.getReturnValue()) return;

        LivingEntity victim = (LivingEntity) (Object) this;
        if (!(source.getEntity() instanceof LivingEntity attacker)) return;

        // 检查攻击者武器上的缴械魔咒等级
        int level = EnchantmentHelper.getItemEnchantmentLevel(
                serverLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(ModEnchantments.DISARMING.key()),
                attacker.getMainHandItem()
        );

        if (level > 0) {
            Disarming.tryDisarm(attacker, victim, level);
        }
    }
}
