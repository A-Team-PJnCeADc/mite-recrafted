package com.mite.recraft.content.entity.mob;

import com.mite.recraft.content.entity.ai.MiteMobGoals;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.level.Level;

/**
 * 火元素。MITE 1.6.4 EntityFireElemental：跟随距离 40、移动速度 0.25、攻击 5、生命 20。
 *
 * <p>只受水与雪球伤害；浸水每 40 tick 掉 1 点血；在岩浆里每 30 到 50 tick 回 4 点血；
 * 命中目标点燃 6 秒。</p>
 */
public class FireElementalEntity extends Monster {

    private int nextLavaHealTick;

    public FireElementalEntity(EntityType<? extends FireElementalEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.MAX_HEALTH, 20.0);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    /** MITE EntityFireElemental.isImmuneTo：水、雪球之外全部免疫。 */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        boolean effective = source.is(DamageTypeTags.IS_FREEZING)
                || source.is(DamageTypeTags.IS_DROWNING)
                || source.getDirectEntity() instanceof Snowball
                || source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
        if (!effective) {
            return false;
        }
        return super.hurtServer(level, source, amount);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }

        if (isInWater()) {
            if (tickCount % 40 == 0) {
                super.hurtServer(serverLevel, serverLevel.damageSources().drown(), 1.0F);
            }
        } else if (isInLava()) {
            if (--nextLavaHealTick <= 0) {
                heal(4.0F);
                nextLavaHealTick = 30 + getRandom().nextInt(21);
            }
        } else {
            nextLavaHealTick = 0;
        }
    }

    /** MITE EntityFireElemental.attackEntityAsMob：命中后点燃目标 6 秒。 */
    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        boolean hit = super.doHurtTarget(level, target);
        if (hit && target instanceof LivingEntity living) {
            living.setRemainingFireTicks(120);
        }
        return hit;
    }

    @Override
    protected void registerGoals() {
        MiteMobGoals.waterEscape(this, goalSelector);
        MiteMobGoals.rainShelter(this, goalSelector);
        super.registerGoals();
    }
}
