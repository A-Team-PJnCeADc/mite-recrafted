package com.mite.recraft.content.entity.mob;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.Level;

/** 白霜蠹虫。MITE 1.6.4 EntityHoarySilverfish：蠹虫的独立变体，行为与数值沿用蠹虫，仅贴图不同。 */
public class HoarySilverfishEntity extends Silverfish {

    public HoarySilverfishEntity(EntityType<? extends HoarySilverfishEntity> type, Level level) {
        super(type, level);
    }
}
