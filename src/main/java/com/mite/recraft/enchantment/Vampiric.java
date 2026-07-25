package com.mite.recraft.enchantment;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.monster.EnderMan;

/**
 * MITE 吸血 (Vampiric) — 攻击时概率根据伤害回复生命。
 * <p>
 * 概率 = 10% × 等级，回复 = 伤害的 5%~50%（均匀随机）。
 * 对亡灵生物、铁傀儡、末影人、雪傀儡无效。
 * 秘银和银质武器不可拥有此附魔。
 */
public final class Vampiric {

    private static final float CHANCE_PER_LEVEL = 0.10f;
    private static final float MIN_HEAL_RATIO = 0.05f;
    private static final float MAX_HEAL_RATIO = 0.50f;

    private Vampiric() {
    }

    /**
     * 尝试触发吸血。
     * @return 实际回复的生命值（0 表示未触发）
     */
    public static float tryVampiric(LivingEntity attacker, LivingEntity victim, int level, float damage) {
        if (level <= 0 || damage <= 0) return 0;
        if (!(attacker.level() instanceof ServerLevel)) return 0;

        if (isInvalidTarget(victim)) return 0;

        // 概率 = 10% × 等级
        if (attacker.getRandom().nextFloat() >= CHANCE_PER_LEVEL * level) return 0;

        // 回复 = 伤害 × (5% ~ 50%)
        float healRatio = MIN_HEAL_RATIO + attacker.getRandom().nextFloat() * (MAX_HEAL_RATIO - MIN_HEAL_RATIO);
        float healAmount = damage * healRatio;
        attacker.heal(healAmount);
        return healAmount;
    }
    private static boolean isInvalidTarget(Entity entity) {
        // 亡灵生物 — 使用原版 EntityTypeTags.UNDEAD 标签
        if (entity.typeHolder().is(EntityTypeTags.UNDEAD)) return true;
        // 铁傀儡、末影人、雪傀儡
        if (entity instanceof IronGolem || entity instanceof EnderMan || entity instanceof SnowGolem) {
            return true;
        }
        return false;
    }
}
