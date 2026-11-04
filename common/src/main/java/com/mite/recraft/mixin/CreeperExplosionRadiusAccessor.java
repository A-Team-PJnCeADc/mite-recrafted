package com.mite.recraft.mixin;

import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** 苦力怕爆炸半径的读写入口：原版字段为私有，MITE 的炼狱苦力怕需要把它翻倍。 */
@Mixin(Creeper.class)
public interface CreeperExplosionRadiusAccessor {

    @Accessor("explosionRadius")
    int mite$getExplosionRadius();

    @Accessor("explosionRadius")
    void mite$setExplosionRadius(int radius);
}
