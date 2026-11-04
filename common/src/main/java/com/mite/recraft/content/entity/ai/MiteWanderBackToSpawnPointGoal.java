package com.mite.recraft.content.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * 回到出生点。MITE 1.6.4 EntityAIWanderBackToSpawnPoint：离出生点超过 16 格时慢慢走回去。
 *
 * <p>TODO 拥有者是 {@code EntityWitch}，女巫尚未移植；移植后由
 * {@link MiteMobGoals#wanderBackToSpawn} 挂载。</p>
 */
public class MiteWanderBackToSpawnPointGoal extends Goal {

    private final PathfinderMob mob;
    private final double speed;
    private BlockPos home;

    public MiteWanderBackToSpawnPointGoal(PathfinderMob mob, double speed) {
        this.mob = mob;
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        if (home == null) {
            home = mob.blockPosition();
            return false;
        }
        return mob.blockPosition().distSqr(home) > 256.0D && mob.getRandom().nextInt(40) == 0;
    }

    @Override
    public void start() {
        if (home != null) {
            mob.getNavigation().moveTo(home.getX() + 0.5D, home.getY(), home.getZ() + 0.5D, speed);
        }
    }
}
