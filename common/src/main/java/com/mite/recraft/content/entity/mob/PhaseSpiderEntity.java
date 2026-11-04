package com.mite.recraft.content.entity.mob;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * 相位蜘蛛。MITE 1.6.4 EntityPhaseSpider：攻击 3，追击时沿路径瞬移。
 *
 * <p>MITE 的实现是沿寻路结果反复闪现（每 10 tick 有 1/3 概率前跳最多 4 个路径点）；
 * 这里按同一节奏朝目标方向闪现 2.5 格，落点要求无碰撞。</p>
 */
public class PhaseSpiderEntity extends WoodSpiderEntity {

    public PhaseSpiderEntity(EntityType<? extends PhaseSpiderEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return WoodSpiderEntity.createAttributes()
                .add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Override
    public void tick() {
        super.tick();

        if (!(level() instanceof ServerLevel)) {
            return;
        }
        if (tickCount % 10 != 0 || getRandom().nextInt(3) != 0) {
            return;
        }

        LivingEntity target = getTarget();
        if (target == null) {
            return;
        }

        Vec3 dir = target.position().subtract(position());
        if (dir.lengthSqr() < 1.0E-4D) {
            return;
        }
        Vec3 step = dir.normalize().scale(2.5D);
        if (level().noCollision(this, getBoundingBox().move(step))) {
            teleportTo(getX() + step.x, getY() + step.y, getZ() + step.z);
        }
    }

    /** MITE EntityPhaseSpider.getExperienceValue 为基类的两倍。 */
    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return super.getBaseExperienceReward(level) * 2;
    }
}
