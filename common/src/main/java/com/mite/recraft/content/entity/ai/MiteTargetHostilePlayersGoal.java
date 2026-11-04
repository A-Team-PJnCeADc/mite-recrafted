package com.mite.recraft.content.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

/**
 * 对玩家敌对时才锁定玩家。MITE 1.6.4 EntityAITargetIfHostileToPlayers：
 * 生物处于敌对状态（被激怒或本身主动攻击）时才把玩家加入目标。
 */
public class MiteTargetHostilePlayersGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public MiteTargetHostilePlayersGoal(Mob mob, Class<T> targetType, boolean mustSee) {
        super(mob, targetType, mustSee);
    }

    /** 便捷入口：直接锁定玩家。 */
    public static MiteTargetHostilePlayersGoal<Player> forPlayers(Mob mob, boolean mustSee) {
        return new MiteTargetHostilePlayersGoal<>(mob, Player.class, mustSee);
    }
}
