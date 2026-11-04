package com.mite.recraft.content.entity.mob;

import com.mite.recraft.content.combat.MiteCombat;
import com.mite.recraft.content.entity.ai.MiteMobGoals;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * 长眠者。骷髅系亡灵，装备远古金属链甲与武器。
 *
 * <p>数值取自 MITE 1.6.4 EntityLongdead：跟随距离 40、移动速度 0.29，
 * 生命 12（守护者 24）、攻击 6（守护者 8）。基类取骷髅，模型与贴图布局一致。</p>
 */
public class LongdeadEntity extends Skeleton {

    public LongdeadEntity(EntityType<? extends LongdeadEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSkeleton.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.29)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.MAX_HEALTH, 12.0);
    }

    /** 原版骷髅的随机弓由 MITE 装备表取代。 */
    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
    }

    /** MITE EntityLongdead.addRandomEquipment：远古金属链甲，守护者持剑、其余持弓。 */
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        EntitySpawnReason reason, SpawnGroupData spawnData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData);
        MiteMobEquipment.equipArmor(this,
                "ancient_metal_chainmail_helmet",
                "ancient_metal_chainmail_chestplate",
                "ancient_metal_chainmail_leggings",
                "ancient_metal_chainmail_boots");
        MiteMobEquipment.equipWeapon(this, MiteMobEquipment.weighted(
                isGuardian() ? "ancient_metal_sword" : "ancient_metal_bow", 1));
        return data;
    }

    public boolean isGuardian() {
        return false;
    }

    /** MITE EntityLongdead.getNaturalDefense：寻常伤害减 1（守护者 2）。 */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return super.hurtServer(level, source,
                MiteCombat.applyNaturalDefense(amount, isGuardian() ? 2.0F : 1.0F, source));
    }

    /** MITE EntityLongdead.getExperienceValue 为基类的三倍。 */
    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return super.getBaseExperienceReward(level) * 3;
    }

    @Override
    protected void registerGoals() {
        MiteMobGoals.seekRepairItem(this, goalSelector);
        MiteMobGoals.firingPosition(this, goalSelector);
        super.registerGoals();
    }
}
