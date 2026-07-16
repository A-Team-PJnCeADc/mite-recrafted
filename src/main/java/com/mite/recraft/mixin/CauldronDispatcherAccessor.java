package com.mite.recraft.mixin;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * 暴露 CauldronInteraction.Dispatcher.put(Item, CauldronInteraction)
 * 以供 BucketCauldronMixin 注册 MITE 桶。
 */
@Mixin(CauldronInteraction.Dispatcher.class)
public interface CauldronDispatcherAccessor {

    @Invoker("put")
    void callPut(Item item, CauldronInteraction interaction);
}
