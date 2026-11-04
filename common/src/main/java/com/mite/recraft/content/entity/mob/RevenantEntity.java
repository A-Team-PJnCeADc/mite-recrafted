package com.mite.recraft.content.entity.mob;

import com.mite.recraft.content.entity.ai.MiteMobGoals;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * 亡魂。锈铁装备的僵尸系亡灵。
 *
 * <p>数值取自 MITE 1.6.4 EntityRevenant：跟随距离 40、移动速度 0.26、攻击 7、生命 30，
 * 另有 0～0.1 的击退抗性随机加成。</p>
 */
public class RevenantEntity extends Zombie {

    public RevenantEntity(EntityType<? extends RevenantEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.26)
                .add(Attributes.ATTACK_DAMAGE, 7.0)
                .add(Attributes.MAX_HEALTH, 30.0);
    }

    /** MITE 的僵尸系生物在 EntityAnimalWatcher 之上实现，没有日晒燃烧，这里关掉原版行为。 */
    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    /** 原版僵尸的随机装备由 MITE 装备表取代。 */
    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficulty) {
    }

    /** MITE EntityRevenant.addRandomEquipment：锈铁甲 + 锈铁武器（剑，第 10 天起战斧，第 20 天起战锤）。 */
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

    /** 击退抗性按 MITE 的 applyEntityAttributes 做 0～0.1 的随机加成。 */
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        EntitySpawnReason reason, SpawnGroupData spawnData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData);
        var knockback = getAttribute(Attributes.KNOCKBACK_RESISTANCE);
        if (knockback != null) {
            knockback.setBaseValue(knockback.getBaseValue() + getRandom().nextDouble() * 0.1D);
        }
        addRandomEquipment(level.getLevel().getLevelData().getGameTime() / 24000L);
        return data;
    }

    @Override
    protected void registerGoals() {
        MiteMobGoals.seekFood(this, goalSelector);
        super.registerGoals();
    }
}
