package com.mite.recraft.content.entity.mob;

import com.mite.recraft.content.combat.MiteCombat;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * 尸妖。除火焰、岩浆与魔法外免疫伤害，命中玩家时按概率吸取经验。
 *
 * <p>数值取自 MITE 1.6.4 EntityWight：跟随距离 40、移动速度 0.25、攻击 5，经验为基类两倍。
 * 命中玩家后 40% 概率吸走 {@code max((等级 + 1) * 10, 20)} 点经验。</p>
 */
public class WightEntity extends Monster {

    public WightEntity(EntityType<? extends WightEntity> type, Level level) {
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
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false));
        goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1.0));
        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, false));
    }

    /** MITE EntityWight.isImmuneTo：只有火焰、岩浆与魔法能造成伤害。 */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        boolean effective = source.is(DamageTypeTags.IS_FIRE)
                || MiteCombat.hasMagicAspect(source)
                || MiteCombat.hasSilverAspect(source);
        if (!effective) {
            return false;
        }
        return super.hurtServer(level, source, amount);
    }

    /** MITE EntityWight.attackEntityAsMob：命中玩家时有 40% 概率吸取经验。 */
    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        boolean hit = super.doHurtTarget(level, target);
        if (hit && target instanceof Player player && getRandom().nextFloat() < 0.4F) {
            int drain = Math.max((player.experienceLevel + 1) * 10, 20);
            player.giveExperiencePoints(-drain);
        }
        return hit;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    /** MITE EntityWight.getExperienceValue 为基类的两倍。 */
    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return super.getBaseExperienceReward(level) * 2;
    }
}
