package com.mite.recraft.content.entity.mob;

import com.mite.recraft.content.combat.MiteCombat;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * 夜翼。MITE 1.6.4 EntityNightwing：吸血蝙蝠的近亲，每 20 tick 选定 32 格内的猎物。
 *
 * <p>MITE 里它只受银质、魔法与阳光伤害；本端尚无银质武器体系，这里放行魔法与火焰（火对应日晒）。</p>
 */
public class NightwingEntity extends Bat {

    private int attackCooldown;

    public NightwingEntity(EntityType<? extends NightwingEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Bat.createAttributes()
                .add(Attributes.MAX_HEALTH, 3.0)
                .add(Attributes.ATTACK_DAMAGE, 1.0);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        boolean effective = MiteCombat.hasMagicAspect(source)
                || MiteCombat.hasSilverAspect(source)
                || source.is(DamageTypeTags.IS_FIRE);
        if (!effective) {
            return false;
        }
        return super.hurtServer(level, source, amount);
    }

    @Override
    public void tick() {
        super.tick();

        if (attackCooldown > 0) {
            --attackCooldown;
        }
        if (level() instanceof ServerLevel && tickCount % 20 == 0) {
            setTarget(findPrey());
        }
        if (attackCooldown <= 0 && level() instanceof ServerLevel serverLevel) {
            LivingEntity target = getTarget();
            if (target != null && target.isAlive()
                    && getBoundingBox().intersects(target.getBoundingBox().inflate(0.5D))) {
                doHurtTarget(serverLevel, target);
            }
        }
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        if (attackCooldown > 0) {
            return false;
        }
        boolean hit = super.doHurtTarget(level, target);
        attackCooldown = 20;
        if (hit) {
            playSound(SoundEvents.BAT_HURT, 0.3F, 1.0F);
        }
        return hit;
    }

    private boolean preysUpon(Entity entity) {
        return (entity instanceof Player p && !p.isCreative() && !p.isSpectator())
                || entity instanceof Animal || entity instanceof Villager;
    }

    private LivingEntity findPrey() {
        LivingEntity best = null;
        double bestDistance = 32.0D * 32.0D;
        for (LivingEntity candidate : level().getEntitiesOfClass(LivingEntity.class,
                getBoundingBox().inflate(32.0D), this::preysUpon)) {
            if (candidate == this || !candidate.isAlive()) {
                continue;
            }
            double distance = distanceToSqr(candidate);
            if (distance < bestDistance) {
                bestDistance = distance;
                best = candidate;
            }
        }
        return best;
    }
}
