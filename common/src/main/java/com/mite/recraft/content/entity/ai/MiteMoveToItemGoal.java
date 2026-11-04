package com.mite.recraft.content.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

/**
 * 走向指定物品。MITE 1.6.4 EntityAIMoveToItem / EntityAIMoveToFoodItem / EntityAIMoveToRepairItem 的共用实现：
 * 8 格内出现符合条件的掉落物时走过去，走到身边后由调用方处理（进食、修复或吞噬）。
 */
public class MiteMoveToItemGoal extends Goal {

    private final PathfinderMob mob;
    private final double speed;
    private final Predicate<ItemStack> wanted;
    private final boolean consumeOnReach;
    private ItemEntity target;

    public MiteMoveToItemGoal(PathfinderMob mob, double speed, Predicate<ItemStack> wanted, boolean consumeOnReach) {
        this.mob = mob;
        this.speed = speed;
        this.wanted = wanted;
        this.consumeOnReach = consumeOnReach;
    }

    /** 找食物：MITE EntityAIMoveToFoodItem，饱食度低时走向可食用掉落物。 */
    public static MiteMoveToItemGoal food(PathfinderMob mob, double speed) {
        return new MiteMoveToItemGoal(mob, speed,
                stack -> stack.has(net.minecraft.core.component.DataComponents.FOOD), false);
    }

    /** 找可修复物品：MITE EntityAIMoveToRepairItem，护甲耐久有损的骷髅会捡同类护甲修复。 */
    public static MiteMoveToItemGoal repair(PathfinderMob mob, double speed) {
        return new MiteMoveToItemGoal(mob, speed, stack -> stack.isDamageableItem(), false);
    }

    @Override
    public boolean canUse() {
        if (mob.getRandom().nextInt(20) != 0) {
            return false;
        }
        target = mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(8.0D),
                item -> !item.isRemoved() && wanted.test(item.getItem()))
                .stream().findFirst().orElse(null);
        return target != null;
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && !target.isRemoved() && mob.distanceToSqr(target) > 2.25D;
    }

    @Override
    public void start() {
        if (target != null) {
            mob.getNavigation().moveTo(target, speed);
        }
    }

    @Override
    public void tick() {
        if (target == null) {
            return;
        }
        if (mob.distanceToSqr(target) <= 2.25D) {
            if (consumeOnReach) {
                target.discard();
            }
            target = null;
        } else if (mob.getNavigation().isDone()) {
            mob.getNavigation().moveTo(target, speed);
        }
    }

    @Override
    public void stop() {
        target = null;
        mob.getNavigation().stop();
    }
}
