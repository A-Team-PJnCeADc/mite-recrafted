package com.mite.recraft.content.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * 躲雨。MITE 1.6.4 EntityAISeekShelterFromRain：下雨且头顶无遮挡时找有顶的地方。
 */
public class MiteSeekShelterFromRainGoal extends Goal {

    private final PathfinderMob mob;
    private final double speed;

    public MiteSeekShelterFromRainGoal(PathfinderMob mob, double speed) {
        this.mob = mob;
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        if (!mob.level().isRainingAt(mob.blockPosition())) {
            return false;
        }
        return mob.getRandom().nextInt(20) == 0;
    }

    @Override
    public void start() {
        BlockPos origin = mob.blockPosition();
        for (int radius = 1; radius <= 10; radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockPos ground = origin.offset(dx, 0, dz);
                    if (!mob.level().canSeeSky(ground) && mob.level().getBlockState(ground).isAir()
                            && mob.level().getBlockState(ground.below()).isSolid()) {
                        mob.getNavigation().moveTo(ground.getX() + 0.5D, ground.getY(), ground.getZ() + 0.5D, speed);
                        return;
                    }
                }
            }
        }
    }
}
