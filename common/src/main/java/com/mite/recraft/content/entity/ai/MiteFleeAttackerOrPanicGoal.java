package com.mite.recraft.content.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * 受击逃跑或惊慌。MITE 1.6.4 EntityAIFleeAttackerOrPanic：被攻击后向远离攻击者的方向逃 3 秒。
 */
public class MiteFleeAttackerOrPanicGoal extends Goal {

    private final PathfinderMob mob;
    private final double speed;
    private final float panicHealthFraction;
    private LivingEntity attacker;
    private int panicTicks;

    public MiteFleeAttackerOrPanicGoal(PathfinderMob mob, double speed, float panicHealthFraction) {
        this.mob = mob;
        this.speed = speed;
        this.panicHealthFraction = panicHealthFraction;
    }

    @Override
    public boolean canUse() {
        if (mob.getLastHurtByMob() != null && mob.tickCount - mob.getLastHurtByMobTimestamp() < 40) {
            attacker = mob.getLastHurtByMob();
            return true;
        }
        return mob.getHealth() < mob.getMaxHealth() * panicHealthFraction && mob.getRandom().nextInt(20) == 0;
    }

    @Override
    public void start() {
        panicTicks = 60;
        flee();
    }

    @Override
    public boolean canContinueToUse() {
        return panicTicks > 0;
    }

    @Override
    public void tick() {
        --panicTicks;
        if (panicTicks % 10 == 0) {
            flee();
        }
    }

    private void flee() {
        if (attacker == null || !attacker.isAlive()) {
            return;
        }
        double dx = mob.getX() - attacker.getX();
        double dz = mob.getZ() - attacker.getZ();
        double length = Math.sqrt(dx * dx + dz * dz);
        if (length < 0.001D) {
            return;
        }
        mob.getNavigation().moveTo(mob.getX() + dx / length * 10.0D, mob.getY(), mob.getZ() + dz / length * 10.0D, speed);
    }
}
