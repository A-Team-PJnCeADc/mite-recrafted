package com.mite.recraft.content.entity.mob;

import com.mite.recraft.content.combat.MiteCombat;
import com.mite.recraft.mixin.CreeperExplosionRadiusAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;

/**
 * 炼狱苦力怕。MITE 1.6.4 EntityInfernalCreeper：免疫火焰与岩浆，经验为基类三倍。
 *
 * <p>MITE 把爆炸半径翻倍，通过 {@link CreeperExplosionRadiusAccessor} 改写原版私有字段。</p>
 */
public class InfernalCreeperEntity extends Creeper {

    /** 原版苦力怕的默认爆炸半径。 */
    private static final int DEFAULT_EXPLOSION_RADIUS = 3;

    public InfernalCreeperEntity(EntityType<? extends InfernalCreeperEntity> type, Level level) {
        super(type, level);
        // MITE EntityInfernalCreeper：explosionRadius 翻倍
        CreeperExplosionRadiusAccessor accessor = (CreeperExplosionRadiusAccessor) this;
        accessor.mite$setExplosionRadius(accessor.mite$getExplosionRadius() * 2);
    }

    /** NBT 载入会把半径写回存档值，这里对未翻倍的存档值再翻一倍，已翻倍的值保持不变。 */
    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        CreeperExplosionRadiusAccessor accessor = (CreeperExplosionRadiusAccessor) this;
        int radius = accessor.mite$getExplosionRadius();
        if (radius <= DEFAULT_EXPLOSION_RADIUS) {
            accessor.mite$setExplosionRadius(radius * 2);
        }
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    /** MITE EntityInfernalCreeper.getNaturalDefense：寻常伤害减 2。 */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return super.hurtServer(level, source, MiteCombat.applyNaturalDefense(amount, 2.0F, source));
    }

    /** MITE EntityInfernalCreeper.getExperienceValue 为基类的三倍。 */
    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return super.getBaseExperienceReward(level) * 3;
    }
}
