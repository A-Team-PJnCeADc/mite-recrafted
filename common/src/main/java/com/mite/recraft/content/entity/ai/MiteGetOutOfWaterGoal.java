package com.mite.recraft.content.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;

/**
 * 离开水面。MITE 1.6.4 EntityAIGetOutOfWater：需要呼吸空气的生物落水后寻找最近的干燥陆地。
 */
public class MiteGetOutOfWaterGoal extends Goal {

    private final PathfinderMob mob;
    private final double speed;

    public MiteGetOutOfWaterGoal(PathfinderMob mob, double speed) {
        this.mob = mob;
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        if (!mob.isInWater() || mob.canBreatheUnderwater()) {
            return false;
        }
        return mob.getRandom().nextInt(16) == 0 || mob.getAirSupply() < mob.getMaxAirSupply() - 20;
    }

    @Override
    public void start() {
        BlockPos target = findDryLand();
        if (target != null) {
            mob.getNavigation().moveTo(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D, speed);
        }
    }

    private BlockPos findDryLand() {
        BlockPos origin = mob.blockPosition();
        BlockPos best = null;
        double bestDistance = Double.MAX_VALUE;
        for (int radius = 2; radius <= 12; radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (Math.abs(dx) != radius && Math.abs(dz) != radius) {
                        continue;
                    }
                    BlockPos ground = origin.offset(dx, 0, dz);
                    if (!mob.level().getBlockState(ground).is(Blocks.WATER)
                            && mob.level().getBlockState(ground).isSolid()
                            && mob.level().getBlockState(ground.above()).isAir()) {
                        double distance = origin.distSqr(ground);
                        if (distance < bestDistance) {
                            bestDistance = distance;
                            best = ground.above();
                        }
                    }
                }
            }
            if (best != null) {
                return best;
            }
        }
        return null;
    }
}
