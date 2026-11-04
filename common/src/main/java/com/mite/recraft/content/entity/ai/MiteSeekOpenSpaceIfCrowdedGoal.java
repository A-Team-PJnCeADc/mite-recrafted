package com.mite.recraft.content.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * 拥挤时散开。MITE 1.6.4 EntityAISeekOpenSpaceIfCrowded：同类过密时向人少的方向移动。
 *
 * <p>TODO 拥有者是 {@code EntityLivestock}，家畜尚未移植；移植后由
 * {@link MiteMobGoals#seekOpenSpace} 挂载。</p>
 */
public class MiteSeekOpenSpaceIfCrowdedGoal extends Goal {

    private final PathfinderMob mob;
    private final double speed;
    private final int crowdThreshold;

    public MiteSeekOpenSpaceIfCrowdedGoal(PathfinderMob mob, double speed, int crowdThreshold) {
        this.mob = mob;
        this.speed = speed;
        this.crowdThreshold = crowdThreshold;
    }

    @Override
    public boolean canUse() {
        return mob.getRandom().nextInt(40) == 0 && countNeighbours() > crowdThreshold;
    }

    @Override
    public void start() {
        var candidates = mob.level().getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(12.0D),
                entity -> entity != mob);
        if (candidates.isEmpty()) {
            return;
        }
        LivingEntity center = candidates.get(mob.getRandom().nextInt(candidates.size()));
        double dx = mob.getX() - center.getX();
        double dz = mob.getZ() - center.getZ();
        double length = Math.sqrt(dx * dx + dz * dz);
        if (length < 0.001D) {
            dx = mob.getRandom().nextDouble() - 0.5D;
            dz = mob.getRandom().nextDouble() - 0.5D;
            length = Math.sqrt(dx * dx + dz * dz);
        }
        mob.getNavigation().moveTo(mob.getX() + dx / length * 8.0D, mob.getY(), mob.getZ() + dz / length * 8.0D, speed);
    }

    private int countNeighbours() {
        return mob.level().getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(4.0D),
                entity -> entity != mob && entity.getType() == mob.getType()).size();
    }
}
