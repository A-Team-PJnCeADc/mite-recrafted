package com.mite.recraft.enchantment.effects;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

/**
 * 击晕效果 —— 每次攻击概率使目标获得缓慢效果，无粒子。
 * <p>
 * 概率 = 10% × 等级（Lv1=10%, …, Lv5=50%）。
 * 缓慢放大器 = 1 + 5 × 等级。
 * 持续 = 等级 × 2.5 秒。
 */
public record StunEffect() implements EnchantmentEntityEffect {

    public static final MapCodec<StunEffect> CODEC = MapCodec.unit(new StunEffect());

    @Override
    public void apply(ServerLevel level, int enchantLevel, EnchantedItemInUse item, Entity entity, Vec3 pos) {
        if (!(entity instanceof LivingEntity victim)) return;

        // 概率 = 10% × 等级
        float chance = 0.10f * enchantLevel;
        if (level.getRandom().nextFloat() >= chance) return;

        int amplifier = 1 + 5 * enchantLevel;
        int durationTicks = enchantLevel * 50;  // 等级 × 2.5 秒

        victim.addEffect(new MobEffectInstance(
                MobEffects.SLOWNESS, durationTicks, amplifier,
                false, false, false
        ));
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
