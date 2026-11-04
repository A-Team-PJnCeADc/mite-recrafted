package com.mite.recraft.content.entity.mob;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;

/**
 * 巨型僵尸。MITE 1.6.4 EntityGiantZombie：体型为普通僵尸的六倍，生命 100、攻击 50、移动速度 0.5。
 *
 * <p>放大走 SCALE 属性，碰撞箱与渲染同步放大。</p>
 */
public class GiantZombieEntity extends Zombie {

    public GiantZombieEntity(EntityType<? extends GiantZombieEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 100.0)
                .add(Attributes.MOVEMENT_SPEED, 0.5)
                .add(Attributes.ATTACK_DAMAGE, 50.0)
                .add(Attributes.SCALE, 6.0);
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }
}
