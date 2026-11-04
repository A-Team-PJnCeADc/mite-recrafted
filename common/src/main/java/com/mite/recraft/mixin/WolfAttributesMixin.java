package com.mite.recraft.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mite.recraft.content.entity.VanillaMobRebalance;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.wolf.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** 狼属性交给 VanillaMobRebalance 表，mixin 只做转发。 */
@Mixin(Wolf.class)
public class WolfAttributesMixin {

    @ModifyReturnValue(method = "createAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder mite$rebalance(AttributeSupplier.Builder builder) {
        return VanillaMobRebalance.WOLF.apply(builder);
    }
}
