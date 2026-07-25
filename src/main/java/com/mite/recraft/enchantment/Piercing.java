package com.mite.recraft.enchantment;

/**
 * MITE 穿透 (Piercing) — 攻击时无视部分护甲。
 * <p>
 * 每级提供 1 点护甲穿透，最终伤害近似修正为：
 * {@code damage_after_armor + min(piercing, original_armor)}。
 * 穿透大于护甲时完全穿透但不额外增伤。
 */
public final class Piercing {

    private Piercing() {
    }

    /**
     * 计算穿透附魔应额外造成的伤害。
     *
     * @param level           穿透等级
     * @param originalDamage  原始伤害（护甲减免前）
     * @param damageAfterArmor 护甲减免后的伤害
     * @param targetArmor     目标护甲值
     * @return 额外伤害
     */
    public static float getBonusDamage(int level, float originalDamage, float damageAfterArmor, int targetArmor) {
        if (level <= 0) return 0;

        // 穿透不超过护甲值（完全穿透即止）
        int piercing = Math.min(level, targetArmor);
        if (piercing <= 0) return 0;

        // 被护甲减免的伤害 = 原始伤害 - 护甲后伤害
        float absorbed = originalDamage - damageAfterArmor;
        if (absorbed <= 0) return 0;

        // 护甲每点吸收的伤害比例
        float perArmorAbsorb = absorbed / targetArmor;
        // 恢复被穿透部分护甲吸收的伤害
        float bonus = perArmorAbsorb * piercing;

        // 不超过原始伤害
        return Math.min(bonus, originalDamage - damageAfterArmor);
    }
}
