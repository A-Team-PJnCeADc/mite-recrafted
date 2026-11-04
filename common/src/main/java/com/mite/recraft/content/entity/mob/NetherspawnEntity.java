package com.mite.recraft.content.entity.mob;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.Level;

/**
 * 下界之子。MITE 1.6.4 EntityNetherspawn：蠹虫的下界变体。
 *
 * <p>免疫火焰；遇水持续冒烟并概率受到水伤害；死亡时若不是在水中、岩浆中或死于火焰，
 * 会以威力 1 爆炸。</p>
 */
public class NetherspawnEntity extends Silverfish {

    private int fizzCooldown;

    public NetherspawnEntity(EntityType<? extends NetherspawnEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();

        if (!(level() instanceof ServerLevel serverLevel)) {
            if (isInWater()) {
                for (int i = 0; i < 4; i++) {
                    level().addParticle(ParticleTypes.LARGE_SMOKE,
                            getX() + (getRandom().nextDouble() - 0.5D) * getBbWidth(),
                            getY() + getRandom().nextDouble() * getBbHeight(),
                            getZ() + (getRandom().nextDouble() - 0.5D) * getBbWidth(),
                            0.0D, 0.0D, 0.0D);
                }
            }
            return;
        }

        if (!isInWaterOrRain()) {
            return;
        }
        if (--fizzCooldown > 0) {
            return;
        }
        fizzCooldown = getRandom().nextInt(7) + 2;
        playSound(SoundEvents.FIRE_EXTINGUISH, 0.7F, 1.6F + (getRandom().nextFloat() - getRandom().nextFloat()) * 0.4F);
        if (getRandom().nextInt(4) == 0) {
            hurtServer(serverLevel, damageSources().drown(), 1.0F);
        }
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    /** MITE：死亡时若不在水中、岩浆中且不是被火烧死，则以威力 1 爆炸。 */
    @Override
    public void die(DamageSource source) {
        if (level() instanceof ServerLevel serverLevel
                && !isInWater() && !isInLava()
                && !source.is(DamageTypeTags.IS_FIRE)) {
            serverLevel.explode(this, getX(), getY() + getBbHeight() / 4.0D, getZ(),
                    1.0F, Level.ExplosionInteraction.MOB);
        }
        super.die(source);
    }
}
