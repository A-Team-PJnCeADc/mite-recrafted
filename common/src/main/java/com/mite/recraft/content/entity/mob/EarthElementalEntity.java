package com.mite.recraft.content.entity.mob;

import com.mite.recraft.content.combat.MiteCombat;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * 土元素。MITE 1.6.4 EntityEarthElemental：跟随距离 20、移动速度 0.2、攻击 12、生命 30，自然防御 4。
 *
 * <p>按所处方块区分石、黑曜石、地狱岩、末地石四种形态；在岩浆或火里积热，热量到 256 转为熔岩形态，
 * 熔岩形态命中点燃目标 6 秒；入水或受冻会降温并退回普通形态。</p>
 */
public class EarthElementalEntity extends Monster {

    public static final int TYPE_STONE = 0;
    public static final int TYPE_OBSIDIAN = 1;
    public static final int TYPE_NETHERRACK = 2;
    public static final int TYPE_END_STONE = 3;
    public static final int TYPE_CLAY = 4;

    /** MITE 的 MAGMA_BIT：热量达到该值转为熔岩形态。 */
    private static final int MAGMA_THRESHOLD = 256;

    private static final EntityDataAccessor<Integer> DATA_TYPE =
            SynchedEntityData.defineId(EarthElementalEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_MAGMA =
            SynchedEntityData.defineId(EarthElementalEntity.class, EntityDataSerializers.BOOLEAN);

    private int heat;

    public EarthElementalEntity(EntityType<? extends EarthElementalEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 12.0)
                .add(Attributes.MAX_HEALTH, 30.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TYPE, TYPE_STONE);
        builder.define(DATA_MAGMA, false);
    }

    public int getElementalType() {
        return entityData.get(DATA_TYPE);
    }

    public void setElementalType(int type) {
        entityData.set(DATA_TYPE, type);
    }

    public boolean isMagma() {
        return entityData.get(DATA_MAGMA);
    }

    private void setMagma(boolean magma) {
        entityData.set(DATA_MAGMA, magma);
    }

    /** MITE getNaturalDefense：寻常伤害减 4，无视护甲的伤害不减。 */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return super.hurtServer(level, source, MiteCombat.applyNaturalDefense(amount, naturalDefense(), source));
    }

    /** NBT 里没有形态字段时的默认形态。 */
    protected int defaultElementalType() {
        return TYPE_STONE;
    }

    /** 是否随环境降温。黏土傀儡的硬化形态不降温。 */
    protected boolean coolsDown() {
        return true;
    }

    protected float naturalDefense() {
        return 4.0F;
    }

    @Override
    public boolean fireImmune() {
        return isMagma();
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!(level() instanceof ServerLevel)) {
            return;
        }

        if (isInWaterOrRain()) {
            heat = 0;
            if (isMagma()) {
                setMagma(false);
            }
        } else if (isInLava() || isOnFire()) {
            heat = Math.min(MAGMA_THRESHOLD, heat + 4);
            if (heat >= MAGMA_THRESHOLD && !isMagma()) {
                setMagma(true);
            }
        } else if (heat > 0 && coolsDown()) {
            int cooling = 1 + (isInWaterOrRain() ? 1 : 0);
            heat = Math.max(0, heat - cooling);
            if (heat < MAGMA_THRESHOLD && isMagma()) {
                setMagma(false);
            }
        }
    }

    /** 熔岩形态命中点燃目标 6 秒。 */
    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        boolean hit = super.doHurtTarget(level, target);
        if (hit && isMagma() && target instanceof LivingEntity living) {
            living.setRemainingFireTicks(120);
        }
        return hit;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("elemental_type", getElementalType());
        output.putInt("heat", heat);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setElementalType(input.getIntOr("elemental_type", defaultElementalType()));
        heat = input.getIntOr("heat", 0);
        setMagma(heat >= MAGMA_THRESHOLD);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }
}
