package com.mite.recraft.content.entity.mob;

import com.mite.recraft.content.combat.MiteCombat;
import com.mite.recraft.content.entity.ai.MiteMobGoals;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

/**
 * 暗影。MITE 1.6.4 EntityShadow：跟随距离 40、移动速度 0.23、攻击 5、生命 20，经验为基类两倍。
 *
 * <p>只受银质、魔法与阳光伤害；日光下当场死亡；黑暗中每 40 tick 按亮度回血；
 * 命中目标附加 10 分钟虚弱；同时会熄灭火把等光源。</p>
 */
public class ShadowEntity extends Monster {

    public ShadowEntity(EntityType<? extends ShadowEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.23)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.MAX_HEALTH, 20.0);
    }

    /** MITE EntityShadow.isImmuneTo：银质与魔法之外全部免疫；阳光用魔法通道结算，见 tick。 */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        boolean effective = MiteCombat.hasSilverAspect(source)
                || MiteCombat.hasMagicAspect(source)
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

        if (isInDaylight()) {
            // MITE 用 sunlight 伤害立即击杀；这里借魔法通道穿过自身的免疫判定
            super.hurtServer(serverLevel, serverLevel.damageSources().magic(), 1000.0F);
        } else if (tickCount % 40 == 0) {
            float brightness = getBrightness();
            int heal = (int) ((0.4F - brightness) * 10.0F);
            if (heal > 0) {
                heal(heal);
            }
        }

        if (tickCount % 20 == 0 && serverLevel.getNearestPlayer(this, 4.0D) == null) {
            MiteLightSuppression.suppress(this, serverLevel);
        }
    }

    private boolean isInDaylight() {
        if (!level().isBrightOutside()) {
            return false;
        }
        BlockPos pos = blockPosition();
        return level().canSeeSky(pos) && level().getMaxLocalRawBrightness(pos) > 11;
    }

    /** 近似 MITE 的 getBrightness：取方块光与天空光的较大值折算到 0-1。 */
    private float getBrightness() {
        BlockPos pos = blockPosition();
        int light = level().getMaxLocalRawBrightness(pos);
        return light / 15.0F;
    }

    /** MITE：命中后给目标 600 tick 虚弱。 */
    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        boolean hit = super.doHurtTarget(level, target);
        if (hit && target instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600, 0), this);
        }
        return hit;
    }

    /** MITE EntityShadow.getExperienceValue 为基类的两倍。 */
    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return super.getBaseExperienceReward(level) * 2;
    }

    @Override
    protected void registerGoals() {
        MiteMobGoals.seekTorch(this, goalSelector, 40);
        super.registerGoals();
    }
}
