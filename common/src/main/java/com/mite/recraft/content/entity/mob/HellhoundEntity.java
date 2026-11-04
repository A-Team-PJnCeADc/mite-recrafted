package com.mite.recraft.content.entity.mob;

import com.mite.recraft.content.entity.ai.MiteMobGoals;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * 狱犬。MITE 1.6.4 EntityHellhound：生命 20、攻击 4、移动速度 0.4，经验为基类三倍。
 *
 * <p>敌对且不可驯服；免疫火焰；命中后 40% 概率点燃目标 1～8 秒。</p>
 */
public class HellhoundEntity extends Wolf {

    public HellhoundEntity(EntityType<? extends HellhoundEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Wolf.createAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ATTACK_DAMAGE, 4.0);
    }

    /** MITE 清空了狼的驯服向目标集，换成敌对玩家的目标集。 */
    @Override
    protected void registerGoals() {
        MiteMobGoals.waterEscape(this, goalSelector);
        MiteMobGoals.lavaEscape(this, goalSelector);
        MiteMobGoals.panicFlee(this, goalSelector);
        MiteMobGoals.targetHostilePlayers(this, goalSelector);
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.4F));
        goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, true));
        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Animal.class, 20, true, false, null));
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    /** 不能驯服。 */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        boolean hit = super.doHurtTarget(level, target);
        if (hit && target instanceof LivingEntity living && getRandom().nextFloat() < 0.4F) {
            living.setRemainingFireTicks((1 + getRandom().nextInt(8)) * 20);
        }
        return hit;
    }

    /** MITE EntityHellhound.getExperienceValue 为基类的三倍。 */
    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return super.getBaseExperienceReward(level) * 3;
    }
}
