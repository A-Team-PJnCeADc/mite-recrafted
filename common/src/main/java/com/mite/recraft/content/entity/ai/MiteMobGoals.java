package com.mite.recraft.content.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.GoalSelector;

/**
 * 按 MITE 1.6.4 各生物的 AI 任务表挂目标，不做统一批发。
 */
public final class MiteMobGoals {

    private MiteMobGoals() {
    }

    /** EntityAIGetOutOfWater：狼、地狱犬、火元素等会游泳的生物。 */
    public static void waterEscape(PathfinderMob mob, GoalSelector selector) {
        selector.addGoal(1, new MiteGetOutOfWaterGoal(mob, 1.0D));
    }

    /** EntityAIGetOutOfLava：恶魔蜘蛛与地狱犬。 */
    public static void lavaEscape(PathfinderMob mob, GoalSelector selector) {
        selector.addGoal(1, new MiteGetOutOfLavaGoal(mob, 1.2D));
    }

    /** EntityAISeekShelterFromRain：火元素躲雨。 */
    public static void rainShelter(PathfinderMob mob, GoalSelector selector) {
        selector.addGoal(2, new MiteSeekShelterFromRainGoal(mob, 1.0D));
    }

    /** EntityAISeekLitTorch：暗影 40 tick 一次，无形潜伏者 200 tick 一次。 */
    public static void seekTorch(PathfinderMob mob, GoalSelector selector, int chance) {
        selector.addGoal(3, new MiteSeekLitTorchGoal(mob, 1.0D, chance, 16));
    }

    /** EntityAIFleeAttackerOrPanic：狼系被攻击后逃跑。 */
    public static void panicFlee(PathfinderMob mob, GoalSelector selector) {
        selector.addGoal(1, new MiteFleeAttackerOrPanicGoal(mob, 1.4D, 0.3F));
    }

    /** EntityAITargetIfHostileToPlayers：狼系只在自己敌对时锁玩家。 */
    public static void targetHostilePlayers(PathfinderMob mob, GoalSelector selector) {
        selector.addGoal(2, MiteTargetHostilePlayersGoal.forPlayers(mob, true));
    }

    /** EntityAIMoveToFoodItem：食尸鬼与亡魂会走向掉落食物。 */
    public static void seekFood(PathfinderMob mob, GoalSelector selector) {
        selector.addGoal(8, MiteMoveToItemGoal.food(mob, 1.0D));
    }

    /** EntityAIMoveToRepairItem：骷髅系会走向可修复的掉落物。 */
    public static void seekRepairItem(PathfinderMob mob, GoalSelector selector) {
        selector.addGoal(9, MiteMoveToItemGoal.repair(mob, 1.0D));
    }

    /** EntityAISeekFiringPosition：持弓的骷髅系与目标保持距离。 */
    public static void firingPosition(PathfinderMob mob, GoalSelector selector) {
        selector.addGoal(3, new MiteSeekFiringPositionGoal(mob, 1.1D, 6.0D, 12.0D));
    }
}
