package com.mite.recraft.content.entity.mob;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/**
 * 木蜘蛛。MITE 1.6.4 EntityWoodSpider：生命 6、移动速度 0.8、攻击 1。
 * 保留基类的吐网与爬墙。
 */
public class WoodSpiderEntity extends ArachnidEntity {

    public WoodSpiderEntity(EntityType<? extends WoodSpiderEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ArachnidEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.MOVEMENT_SPEED, 0.8)
                .add(Attributes.ATTACK_DAMAGE, 1.0);
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return super.getBaseExperienceReward(level);
    }
}
