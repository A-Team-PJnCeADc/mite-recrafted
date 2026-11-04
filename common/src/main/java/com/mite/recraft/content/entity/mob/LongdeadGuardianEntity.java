package com.mite.recraft.content.entity.mob;

import com.mite.recraft.content.entity.ai.MiteMobGoals;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.level.Level;

/**
 * 长眠者守卫。MITE 1.6.4 EntityLongdeadGuardian：长眠者的强化个体，
 * 生命 24、攻击 8，经验为基类的五倍（长眠者本身三倍）。
 */
public class LongdeadGuardianEntity extends LongdeadEntity {

    public LongdeadGuardianEntity(EntityType<? extends LongdeadGuardianEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSkeleton.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.29)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.MAX_HEALTH, 24.0);
    }

    @Override
    public boolean isGuardian() {
        return true;
    }

    /** MITE EntityLongdead.getExperienceValue 对守卫取五倍。 */
    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return super.getBaseExperienceReward(level) * 5 / 3;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        MiteMobGoals.seekRepairItem(this, goalSelector);
    }
}
