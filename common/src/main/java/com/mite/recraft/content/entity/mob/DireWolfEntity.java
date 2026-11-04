package com.mite.recraft.content.entity.mob;

import com.mite.recraft.content.entity.ai.MiteMobGoals;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * 恐狼。MITE 1.6.4 EntityDireWolf：生命 16（驯服 24）、攻击 5，经验为基类两倍。
 *
 * <p>未驯服的成年恐狼每 tick 有 0.4% 概率把 4 格内的玩家设为目标。</p>
 */
public class DireWolfEntity extends Wolf {

    public DireWolfEntity(EntityType<? extends DireWolfEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Wolf.createAttributes()
                .add(Attributes.MAX_HEALTH, 16.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide() || isBaby() || isTame() || getRandom().nextFloat() >= 0.004F) {
            return;
        }
        Player player = level().getNearestPlayer(this, 4.0D);
        if (player != null && !player.isCreative() && !player.isSpectator()) {
            setTarget(player);
        }
    }

    /** MITE EntityDireWolf.getExperienceValue 为基类的两倍。 */
    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return super.getBaseExperienceReward(level) * 2;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        MiteMobGoals.waterEscape(this, goalSelector);
        MiteMobGoals.panicFlee(this, goalSelector);
        MiteMobGoals.targetHostilePlayers(this, goalSelector);
    }
}
