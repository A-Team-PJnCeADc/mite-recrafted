package com.mite.recraft.content.entity.mob;

import com.mite.recraft.content.entity.ai.MiteMobGoals;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
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
 * 白骨领主。骷髅系，可召唤长眠者小队。
 *
 * <p>数值取自 MITE 1.6.4 EntityBoneLord：跟随距离 40、移动速度 0.26、攻击 5、生命 20，经验为基类三倍。
 * 召唤小队依赖 MITE 的刷怪计数与消失机制，暂未实现。</p>
 */
public class BoneLordEntity extends Skeleton {

    public BoneLordEntity(EntityType<? extends BoneLordEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSkeleton.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.26)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.MAX_HEALTH, 20.0);
    }

    /** 原版骷髅的随机弓由 MITE 装备表取代。 */
    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        EntitySpawnReason reason, SpawnGroupData spawnData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData);
        addRandomEquipment(level.getLevel().getLevelData().getGameTime() / 24000L);
        return data;
    }

    /** MITE EntityBoneLord.addRandomEquipment：锈铁甲 + 锈铁武器。 */
    protected void addRandomEquipment(long day) {
        MiteMobEquipment.equipArmor(this,
                "rusted_iron_helmet",
                "rusted_iron_chestplate",
                "rusted_iron_leggings",
                "rusted_iron_boots");
        if (day >= 20) {
            MiteMobEquipment.equipWeapon(this, MiteMobEquipment.weighted(
                    "rusted_iron_sword", 2, "rusted_iron_battle_axe", 1, "rusted_iron_war_hammer", 1));
        } else if (day >= 10) {
            MiteMobEquipment.equipWeapon(this, MiteMobEquipment.weighted(
                    "rusted_iron_sword", 2, "rusted_iron_battle_axe", 1));
        } else {
            MiteMobEquipment.equipWeapon(this, MiteMobEquipment.weighted("rusted_iron_sword", 2));
        }
    }

    /** MITE EntityBoneLord.getExperienceValue 为基类的三倍。 */
    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return super.getBaseExperienceReward(level) * 3;
    }

    @Override
    protected void registerGoals() {
        MiteMobGoals.seekRepairItem(this, goalSelector);
        super.registerGoals();
    }
}
