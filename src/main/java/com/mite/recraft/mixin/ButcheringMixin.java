package com.mite.recraft.mixin;

import com.mite.recraft.enchantment.Butchering;
import com.mite.recraft.enchantment.ModEnchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 屠宰 (Butchering) 效果：奖励表掉落后额外掉落肉类。
 */
@Mixin(LivingEntity.class)
public class ButcheringMixin {

    @Inject(method = "dropFromLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;Z)V", at = @At("RETURN"))
    private void mite$afterDropLoot(ServerLevel serverLevel, DamageSource source, boolean playerKilled, CallbackInfo ci) {
        LivingEntity victim = (LivingEntity) (Object) this;

        // 获取攻击者武器上的屠宰魔咒等级
        if (source.getEntity() instanceof LivingEntity attacker) {
            int level = EnchantmentHelper.getItemEnchantmentLevel(
                    serverLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                            .getOrThrow(ModEnchantments.BUTCHERING.key()),
                    attacker.getMainHandItem()
            );
            Butchering.tryExtraMeat(level, victim, serverLevel);
        }
    }
}
