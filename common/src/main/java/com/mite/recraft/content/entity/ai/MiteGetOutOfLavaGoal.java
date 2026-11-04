package com.mite.recraft.content.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * 离开岩浆。MITE 1.6.4 EntityAIGetOutOfLava：怕岩浆的生物踩到岩浆后向最近的非岩浆位置移动。
 */
public class MiteGetOutOfLavaGoal extends Goal {

    private final PathfinderMob mob;
    private final double speed;

    public MiteGetOutOfLavaGoal(PathfinderMob mob, double speed) {
        this.mob = mob;
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        return !mob.fireImmune() && mob.isInLava();
    }

    @Override
    public void start() {
        BlockPos origin = mob.blockPosition();
        for (int radius = 2; radius <= 8; radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockPos candidate = origin.offset(dx, 0, dz);
                    if (!mob.level().getBlockState(candidate).is(net.minecraft.world.level.block.Blocks.LAVA)
                            && mob.level().getBlockState(candidate).isSolid()) {
                        mob.getNavigation().moveTo(candidate.getX() + 0.5D, candidate.getY() + 1, candidate.getZ() + 0.5D, speed);
                        return;
                    }
                }
            }
        }
    }
}
