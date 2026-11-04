package com.mite.recraft.content.entity.mob;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/**
 * 洞穴蛛。MITE 1.6.4 EntityCaveSpider：蛛形生物里体型最小的一种，生命 16，命中附加 480 tick 中毒，
 * 经验为基类两倍，吐网间隔 200 tick。
 */
public class CaveSpiderEntity extends ArachnidEntity {

    public CaveSpiderEntity(EntityType<? extends CaveSpiderEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ArachnidEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 16.0);
    }

    @Override
    protected int getTicksBetweenWebThrows() {
        return 200;
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        boolean hit = super.doHurtTarget(level, target);
        if (hit && target instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 480, 0), this);
        }
        return hit;
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return super.getBaseExperienceReward(level) * 2;
    }
}
