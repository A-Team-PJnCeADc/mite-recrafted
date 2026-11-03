package com.mite.recraft.mixin;

import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * 读取原版箱子的开启者计数器匿名类持有的外层方块实体。
 */
@Mixin(targets = "net.minecraft.world.level.block.entity.ChestBlockEntity$1")
public interface ChestOpenCounterAccessor {

    @Accessor("this$0")
    ChestBlockEntity mite$owner();
}
