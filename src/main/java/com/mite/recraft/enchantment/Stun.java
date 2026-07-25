package com.mite.recraft.enchantment;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * MITE 击晕 (Stun) — 每次攻击有概率使生物短暂硬直。
 * <p>
 * 概率 = 10% × 等级（Lv1=10%, …, Lv5=50%）。
 * 效果：缓慢，放大器 = 1 + 5 × 等级，持续 = 等级 × 2.5 秒。
 * 已击晕的怪物再次击晕将刷新时长。
 * 不显示药水粒子。
 */
public final class Stun {

    private static final float CHANCE_PER_LEVEL = 0.10f;

    private Stun() {
    }

    /**
     * 尝试触发击晕效果。
     *
     * @param attacker 攻击者
     * @param victim   被攻击者
     * @param level    击晕魔咒等级（1～5）
     */
    public static void tryStun(LivingEntity attacker, LivingEntity victim, int level) {
        if (level <= 0) return;
        if (!(victim.level() instanceof ServerLevel)) return;

        // 概率 = 10% × 等级
        if (attacker.getRandom().nextFloat() >= CHANCE_PER_LEVEL * level) return;

        // 放大器 = 1 + 5 × 等级
        int amplifier = 1 + 5 * level;
        // 持续 = 等级 × 2.5 秒 = 等级 × 50 tick
        int durationTicks = level * 50;

        victim.addEffect(new MobEffectInstance(
                MobEffects.SLOWNESS,
                durationTicks,
                amplifier,
                false,    // ambient
                false,    // show particles
                false     // show icon
        ));
    }
}
