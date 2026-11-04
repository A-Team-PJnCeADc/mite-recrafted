package com.mite.recraft.content.entity.mob;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * 吸血蝙蝠。MITE 1.6.4 EntityVampireBat：每 20 tick 选定 32 格内的猎物，
 * 撞到目标时造成伤害并按伤害量回血；回满血后进入 1200 tick 的只喝玩家血状态。
 */
public class VampireBatEntity extends Bat {

    private static final String NBT_FEED_COOLDOWN = "feed_cooldown";

    private int attackCooldown;
    private int feedCooldown;

    public VampireBatEntity(EntityType<? extends VampireBatEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Bat.createAttributes()
                .add(Attributes.MAX_HEALTH, 3.0)
                .add(Attributes.ATTACK_DAMAGE, 1.0);
    }

    /** 巨型个体体型翻倍（MITE EntityBat.getScaleFactor）。 */
    protected float scaleFactor() {
        return 1.0F;
    }

    public boolean isGiantSized() {
        return scaleFactor() > 1.0F;
    }

    @Override
    public void tick() {
        super.tick();

        if (attackCooldown > 0) {
            --attackCooldown;
        }
        if (feedCooldown > 0) {
            if (getHealth() < getMaxHealth()) {
                feedCooldown = 0;
            } else if (--feedCooldown > 0) {
                LivingEntity target = getTarget();
                if (target != null && !preysUpon(target)) {
                    setTarget(null);
                }
            }
        }

        if (level() instanceof ServerLevel && tickCount % 20 == 0) {
            setTarget(findPrey());
        }
        tryBite();
    }

    /** 撞到目标时攻击并吸血：目标包围盒按 0.5 缩放后与自身相交即判定。 */
    private void tryBite() {
        if (attackCooldown > 0 || !(level() instanceof ServerLevel serverLevel)) {
            return;
        }
        LivingEntity target = getTarget();
        if (target == null || !target.isAlive()) {
            return;
        }
        if (getBoundingBox().intersects(target.getBoundingBox().inflate(0.5D))) {
            doHurtTarget(serverLevel, target);
        }
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        boolean hit = super.doHurtTarget(level, target);
        if (hit && target instanceof LivingEntity living) {
            heal(1.0F * scaleFactor());
            if (!isGiantSized() && getHealth() >= getMaxHealth()) {
                feedCooldown = 1200;
            }
            playSound(SoundEvents.BAT_HURT, 0.3F, 1.0F);
        }
        attackCooldown = 20;
        return hit;
    }

    /** MITE EntityVampireBat.preysUpon：进食冷却中只咬玩家，否则玩家、动物、村民都是猎物。 */
    protected boolean preysUpon(Entity entity) {
        boolean player = entity instanceof Player p && !p.isCreative() && !p.isSpectator();
        if (feedCooldown > 0) {
            return player;
        }
        return player || entity instanceof Animal || entity instanceof Villager;
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

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt(NBT_FEED_COOLDOWN, feedCooldown);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        feedCooldown = input.getIntOr(NBT_FEED_COOLDOWN, 0);
    }
}
