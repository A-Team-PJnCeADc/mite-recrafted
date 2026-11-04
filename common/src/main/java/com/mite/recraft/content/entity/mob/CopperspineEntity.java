package com.mite.recraft.content.entity.mob;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.Level;

/** 铜刺蠹虫。MITE 1.6.4 EntityCopperspine：蠹虫的独立变体，行为与数值沿用蠹虫，仅贴图不同。 */
public class CopperspineEntity extends Silverfish {

    public CopperspineEntity(EntityType<? extends CopperspineEntity> type, Level level) {
        super(type, level);
    }
}
