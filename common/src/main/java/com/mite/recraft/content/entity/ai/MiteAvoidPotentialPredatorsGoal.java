package com.mite.recraft.content.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.function.Predicate;

/**
 * 躲避捕食者。MITE 1.6.4 EntityAIAvoidPotentialPredators：12 格内出现捕食者时反向逃跑。
 *
 * <p>TODO 拥有者是 {@code EntityHorse}、{@code EntityLivestock}、{@code EntityVillager}，
 * 这三类生物尚未移植；移植后由 {@link MiteMobGoals#avoidPredators} 挂载。</p>
 */
public class MiteAvoidPotentialPredatorsGoal extends Goal {

    private final PathfinderMob mob;
    private final double speed;
    private final Predicate<LivingEntity> predator;
    private LivingEntity current;

    public MiteAvoidPotentialPredatorsGoal(PathfinderMob mob, double speed, Predicate<LivingEntity> predator) {
        this.mob = mob;
        this.speed = speed;
        this.predator = predator;
    }

    @Override
    public boolean canUse() {
        if (mob.getRandom().nextInt(20) != 0) {
            return false;
        }
        current = mob.level().getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(12.0D), predator)
                .stream().findFirst().orElse(null);
        return current != null;
    }

    @Override
    public void start() {
        if (current == null) {
            return;
        }
        double dx = mob.getX() - current.getX();
        double dz = mob.getZ() - current.getZ();
        double length = Math.sqrt(dx * dx + dz * dz);
        if (length < 0.001D) {
            return;
        }
        mob.getNavigation().moveTo(mob.getX() + dx / length * 12.0D, mob.getY(), mob.getZ() + dz / length * 12.0D, speed);
    }
}
