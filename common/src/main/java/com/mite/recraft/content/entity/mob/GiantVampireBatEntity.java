package com.mite.recraft.content.entity.mob;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/** 巨型吸血蝙蝠。MITE 1.6.4 EntityGiantVampireBat：吸血蝙蝠的翻倍体型版本（体型与生命、攻击同步翻倍）。 */
public class GiantVampireBatEntity extends VampireBatEntity {

    public GiantVampireBatEntity(EntityType<? extends GiantVampireBatEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected float scaleFactor() {
        return 2.0F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return VampireBatEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.ATTACK_DAMAGE, 2.0);
    }
}
