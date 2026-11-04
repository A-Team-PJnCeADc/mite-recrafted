package com.mite.recraft.content.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;

/**
 * 靠近光源。MITE 1.6.4 EntityAISeekLitTorch：暗影每 40 tick、其它生物每 200 tick 尝试找火把或南瓜灯。
 */
public class MiteSeekLitTorchGoal extends Goal {

    private final PathfinderMob mob;
    private final double speed;
    private final int chance;
    private final int maxDistance;

    public MiteSeekLitTorchGoal(PathfinderMob mob, double speed, int chance, int maxDistance) {
        this.mob = mob;
        this.speed = speed;
        this.chance = chance;
        this.maxDistance = maxDistance;
    }

    @Override
    public boolean canUse() {
        return mob.getRandom().nextInt(chance) == 0 && findTorch() != null;
    }

    @Override
    public void start() {
        BlockPos torch = findTorch();
        if (torch != null) {
            mob.getNavigation().moveTo(torch.getX() + 0.5D, torch.getY(), torch.getZ() + 0.5D, speed);
        }
    }

    private BlockPos findTorch() {
        BlockPos origin = mob.blockPosition();
        BlockPos best = null;
        double bestDistance = Double.MAX_VALUE;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int dx = -maxDistance; dx <= maxDistance; dx++) {
            for (int dy = -4; dy <= 4; dy++) {
                for (int dz = -maxDistance; dz <= maxDistance; dz++) {
                    cursor.set(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz);
                    var state = mob.level().getBlockState(cursor);
                    if (state.is(Blocks.TORCH) || state.is(Blocks.WALL_TORCH)
                            || state.is(Blocks.REDSTONE_TORCH) || state.is(Blocks.REDSTONE_WALL_TORCH)
                            || state.is(Blocks.JACK_O_LANTERN)) {
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
