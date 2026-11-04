package com.mite.recraft.content.entity.mob;

import com.mite.recraft.content.entity.ai.MiteMobGoals;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/**
 * 恶魔蜘蛛。MITE 1.6.4 EntityDemonSpider：生命 18、攻击 5，经验为基类三倍。
 *
 * <p>吐网间隔 200 tick；MITE 让投出的网带火，命中后点燃目标，这里在落网时给目标点火 10 秒。</p>
 */
public class DemonSpiderEntity extends ArachnidEntity {

    public DemonSpiderEntity(EntityType<? extends DemonSpiderEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ArachnidEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 18.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    @Override
    protected int getTicksBetweenWebThrows() {
        return 200;
    }

    @Override
    protected void throwWeb(ServerLevel level, LivingEntity target) {
        super.throwWeb(level, target);
        target.setRemainingFireTicks(200);
    }

    /** MITE EntityDemonSpider.getExperienceValue 为基类的三倍。 */
    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return super.getBaseExperienceReward(level) * 3;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        MiteMobGoals.lavaEscape(this, goalSelector);
    }
}
