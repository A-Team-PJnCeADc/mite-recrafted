package com.mite.recraft.mixin;

import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * 访问 {@link AbstractFurnaceBlockEntity} 的私有字段。
 */
@Mixin(AbstractFurnaceBlockEntity.class)
public interface AccessorFurnaceBlockEntity {

    @Accessor("cookingTimer")
    void mite$setCookingProgress(int progress);

    @Accessor("cookingTimer")
    int mite$getCookingProgress();

    @Accessor("litTimeRemaining")
    void mite$setLitTimeRemaining(int time);

    @Accessor("litTimeRemaining")
    int mite$getLitTimeRemaining();
}
