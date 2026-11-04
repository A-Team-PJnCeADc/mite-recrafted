package com.mite.recraft.content.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * 找射击位。MITE 1.6.4 EntityAISeekFiringPosition：远程攻击者与目标保持 6 到 12 格距离。
 */
public class MiteSeekFiringPositionGoal extends Goal {

    private final PathfinderMob mob;
    private final double speed;
    private final double minDistance;
    private final double maxDistance;

    public MiteSeekFiringPositionGoal(PathfinderMob mob, double speed, double minDistance, double maxDistance) {
        this.mob = mob;
        this.speed = speed;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        if (target == null) {
            return false;
        }
        double distance = mob.distanceTo(target);
        return distance < minDistance || distance > maxDistance;
    }

    @Override
    public void start() {
        LivingEntity target = mob.getTarget();
        if (target == null) {
            return;
        }
        double distance = mob.distanceTo(target);
        double dx = mob.getX() - target.getX();
        double dz = mob.getZ() - target.getZ();
        double length = Math.sqrt(dx * dx + dz * dz);
        if (length < 0.001D) {
            return;
        }
        double desired = distance < minDistance ? -6.0D : 6.0D;
        mob.getNavigation().moveTo(mob.getX() + dx / length * desired, mob.getY(), mob.getZ() + dz / length * desired, speed);
    }
}
