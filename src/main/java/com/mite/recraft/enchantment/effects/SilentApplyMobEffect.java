package com.mite.recraft.enchantment.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.ApplyMobEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

/**
 * 不显示粒子的 ApplyMobEffect
 * <p>
 * 与 {@link ApplyMobEffect} 功能相同，
 * 但 {@link MobEffectInstance} 构造时设置 visible=false。
 */
public record SilentApplyMobEffect(
        HolderSet<MobEffect> toApply,
        LevelBasedValue minDuration,
        LevelBasedValue maxDuration,
        LevelBasedValue minAmplifier,
        LevelBasedValue maxAmplifier
) implements EnchantmentEntityEffect {

    public static final MapCodec<SilentApplyMobEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    RegistryCodecs.homogeneousList(Registries.MOB_EFFECT).fieldOf("to_apply").forGetter(SilentApplyMobEffect::toApply),
                    LevelBasedValue.CODEC.fieldOf("min_duration").forGetter(SilentApplyMobEffect::minDuration),
                    LevelBasedValue.CODEC.fieldOf("max_duration").forGetter(SilentApplyMobEffect::maxDuration),
                    LevelBasedValue.CODEC.fieldOf("min_amplifier").forGetter(SilentApplyMobEffect::minAmplifier),
                    LevelBasedValue.CODEC.fieldOf("max_amplifier").forGetter(SilentApplyMobEffect::maxAmplifier)
            ).apply(instance, SilentApplyMobEffect::new)
    );

    @Override
    public void apply(ServerLevel level, int enchantLevel, EnchantedItemInUse item, Entity entity, Vec3 pos) {
        if (!(entity instanceof LivingEntity living)) return;
        RandomSource random = living.getRandom();
        var opt = toApply.getRandomElement(random);
        if (opt.isEmpty()) return;

        int duration = Math.round(Mth.randomBetween(random, minDuration.calculate(enchantLevel), maxDuration.calculate(enchantLevel)) * 20.0f);
        int amplifier = Math.max(0, Math.round(Mth.randomBetween(random, minAmplifier.calculate(enchantLevel), maxAmplifier.calculate(enchantLevel))));

        // 关键区别：visible=false，不显示粒子
        living.addEffect(new MobEffectInstance(opt.get(), duration, amplifier, false, false, true));
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
