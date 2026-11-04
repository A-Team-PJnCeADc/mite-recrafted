package com.mite.recraft.content.entity.mob;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.level.Level;

/**
 * 远古白骨领主。白骨领主的强化版本，召唤长眠者而非骷髅。
 *
 * <p>数值取自 MITE 1.6.4 EntityAncientBoneLord：跟随距离 40、移动速度 0.27、攻击 8、生命 24，
 * 经验在白骨领主基础上再乘二。</p>
 */
public class AncientBoneLordEntity extends BoneLordEntity {

    public AncientBoneLordEntity(EntityType<? extends AncientBoneLordEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSkeleton.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.27)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.MAX_HEALTH, 24.0);
    }

    /** MITE EntityAncientBoneLord.addRandomEquipment：远古金属甲 + 远古金属武器。 */
    @Override
    protected void addRandomEquipment(long day) {
        MiteMobEquipment.equipArmor(this,
                "ancient_metal_helmet",
                "ancient_metal_chestplate",
                "ancient_metal_leggings",
                "ancient_metal_boots");
        MiteMobEquipment.equipWeapon(this, MiteMobEquipment.weighted(
                "ancient_metal_sword", 2, "ancient_metal_battle_axe", 1, "ancient_metal_war_hammer", 1));
    }

    /** MITE EntityAncientBoneLord.getExperienceValue 在基类三倍之上再乘二。 */
    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return super.getBaseExperienceReward(level) * 2;
    }
}
