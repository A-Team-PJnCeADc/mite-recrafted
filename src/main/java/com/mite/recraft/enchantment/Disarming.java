package com.mite.recraft.enchantment;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

/**
 * MITE 缴械 (Disarming) — 攻击时有几率使生物持有物掉落。
 * <p>
 * 每级 20% 概率，最高 IV 级（Lv1=20%、Lv2=40%、Lv3=60%、Lv4=80%）。
 * 保留生物无敌帧，只有伤害实际生效时才可能触发射击。
 * 被缴械的物品以物品实体弹出，40 tick 内不可被任何生物捡起
 * （类似玩家丢弃物品），之后所有生物均可拾取。
 * <p>
 * 参考 MITE 1.6.4：{@code ticks_disarmed = 40}（EntityPlayer.java:1748）。
 */
public final class Disarming {

    /** 每级概率增幅 */
    private static final float CHANCE_PER_LEVEL = 0.20f;

    /** 被缴械物品的不可捡起延迟（tick），40 tick = 2 秒（20 tick/s） */
    private static final int PICKUP_DELAY = 40;

    private Disarming() {
    }

    /**
     * 尝试触发缴械效果。
     *
     * @param attacker  攻击者
     * @param victim    被攻击者
     * @param level     缴械魔咒等级（1～4）
     */
    public static void tryDisarm(LivingEntity attacker, LivingEntity victim, int level) {
        if (level <= 0) return;
        if (victim.level() instanceof ServerLevel serverLevel) {
            float chance = CHANCE_PER_LEVEL * level;
            if (attacker.getRandom().nextFloat() < chance) {
                disarm(serverLevel, victim);
            }
        }
    }

    /**
     * 执行缴械：将受害者主手物品以物品实体形式弹出，并设置拾取延迟。
     */
    private static void disarm(ServerLevel level, LivingEntity victim) {
        ItemStack held = victim.getMainHandItem();
        if (held.isEmpty()) return;

        // 从受害者手中移除物品
        victim.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);

        // 以物品实体形式弹出（带随机偏移模拟"飞出"效果）
        ItemEntity itemEntity = victim.spawnAtLocation(level, held);
        if (itemEntity != null) {
            // 设置拾取延迟（类似玩家丢弃物品）
            itemEntity.setPickUpDelay(PICKUP_DELAY);
        }
    }
}
