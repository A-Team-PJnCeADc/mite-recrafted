package com.mite.recraft.content.entity.mob;

import com.mite.recraft.content.entity.ai.MiteMobGoals;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * 无形潜伏者。MITE 1.6.4 EntityInvisibleStalker：跟随距离 40、移动速度 0.23、攻击 4。
 *
 * <p>附近没有玩家时会把周围的火把与南瓜灯熄掉：火把直接掉落，南瓜灯换成南瓜并掉落火把。
 * 每 20 tick 扫描一次，范围与 MITE 相同（自身所在格 ±1，向上到身高 +1）。</p>
 */
public class InvisibleStalkerEntity extends Monster {

    public InvisibleStalkerEntity(EntityType<? extends InvisibleStalkerEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.23)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.MAX_HEALTH, 20.0);
    }

    @Override
    protected void registerGoals() {
        MiteMobGoals.seekTorch(this, goalSelector, 200);
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false));
        goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1.0));
        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, false));
    }

    @Override
    public void tick() {
        super.tick();

        if (level() instanceof ServerLevel serverLevel && tickCount % 20 == 0
                && hurtTime == 0 && serverLevel.getNearestPlayer(this, 4.0D) == null) {
            disableNearbyLightSource(serverLevel);
        }
    }

    /** 对应 MITE EntityLiving.tryDisableNearbyLightSource。 */
    private void disableNearbyLightSource(ServerLevel level) {
        MiteLightSuppression.suppress(this, level);
    }
}
