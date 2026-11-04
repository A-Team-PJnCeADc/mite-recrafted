package com.mite.recraft.content.entity.mob;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;

/**
 * 黑寡妇蜘蛛。木蜘蛛的强化个体，经验为基类的 8/5。
 */
public class BlackWidowSpiderEntity extends WoodSpiderEntity {

    public BlackWidowSpiderEntity(EntityType<? extends BlackWidowSpiderEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return WoodSpiderEntity.createAttributes();
    }

    /** MITE EntityBlackWidowSpider.getExperienceValue：基类 × 8 / 5。 */
    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return super.getBaseExperienceReward(level) * 8 / 5;
    }
}
