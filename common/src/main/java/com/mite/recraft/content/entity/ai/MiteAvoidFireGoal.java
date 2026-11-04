package com.mite.recraft.content.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;

/**
 * 远离火方块。MITE 1.6.4 EntityAIAvoidFire：8 格内侦测到火焰时向反方向移动。
 *
 * <p>TODO 拥有者是 {@code EntityAnimal} 与 {@code EntitySnowman}，这两类生物尚未移植；
 * 移植后由 {@link MiteMobGoals#avoidFire} 挂载。</p>
 */
public class MiteAvoidFireGoal extends Goal {

    private final PathfinderMob mob;
    private final double speed;

    public MiteAvoidFireGoal(PathfinderMob mob, double speed) {
        this.mob = mob;
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        if (mob.getRandom().nextInt(10) != 0) {
            return false;
        }
        return !mob.fireImmune() && findNearbyFire() != null;
    }

    @Override
    public void start() {
        BlockPos fire = findNearbyFire();
        if (fire == null) {
            return;
        }
        double dx = mob.getX() - (fire.getX() + 0.5D);
        double dz = mob.getZ() - (fire.getZ() + 0.5D);
        double length = Math.sqrt(dx * dx + dz * dz);
        if (length < 0.001D) {
            return;
        }
        mob.getNavigation().moveTo(mob.getX() + dx / length * 8.0D, mob.getY(), mob.getZ() + dz / length * 8.0D, speed);
    }

    private BlockPos findNearbyFire() {
        BlockPos origin = mob.blockPosition();
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        BlockPos best = null;
        double bestDistance = Double.MAX_VALUE;
        for (int dx = -8; dx <= 8; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -8; dz <= 8; dz++) {
                    cursor.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz);
                    if (mob.level().getBlockState(cursor).is(Blocks.FIRE)) {
                        double distance = origin.distSqr(cursor);
                        if (distance < bestDistance) {
                            bestDistance = distance;
                            best = cursor.immutable();
                        }
                    }
                }
            }
        }
        return best;
    }
}
