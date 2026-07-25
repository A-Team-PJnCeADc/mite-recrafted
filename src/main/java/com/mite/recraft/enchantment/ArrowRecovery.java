package com.mite.recraft.enchantment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.cubemob.Slime;

/**
 * MITE 箭矢回收 (Arrow Recovery) — 增加箭矢回收率。
 * <p>
 * 每级基于箭本身回收率提供 5% 附加回收几率。
 * 击中史莱姆、幼年动物不会回收。
 */
public final class ArrowRecovery {

    private static final float BONUS_PER_LEVEL = 0.05f;

    private ArrowRecovery() {
    }

    /**
     * 根据附魔等级计算箭矢的最终回收几率。
     *
     * @param baseChance 箭矢基础回收率（来自 ItemStack 的 RECOVERY_CHANCE）
     * @param level      Arrow Recovery 附魔等级
     * @return 最终回收几率
     */
    public static float calculateChance(float baseChance, int level) {
        if (level <= 0) return baseChance;
        // 每级提供基于基础回收率的 5% 加成
        return baseChance + baseChance * BONUS_PER_LEVEL * level;
    }

    /**
     * 判断目标是否不应回收箭矢。
     *
     * @param victim 被击中的实体
     * @return true=不回收
     */
    public static boolean shouldPreventRecovery(Entity victim) {
        // 史莱姆
        if (victim instanceof Slime) return true;
        // 幼年动物
        if (victim instanceof LivingEntity living && living.isBaby()) return true;
        return false;
    }
}
