package com.mite.recraft.mixin;

import com.mite.recraft.item.moditems.strongbox.StrongboxScreenHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Fixes {@link ChestBlockEntity}'s {@code isOwnContainer} to recognize
 * {@link StrongboxScreenHandler} alongside vanilla {@link ChestMenu}.
 *
 * <p>Without this, {@code recheckOpeners()} always sees 0 openers for
 * strongboxes (because {@code player.containerMenu instanceof ChestMenu}
 * returns false), forcibly closing the chest every 5 ticks and breaking
 * the openers counter (causing "closes then immediately re-opens").
 */
@Mixin(targets = "net.minecraft.world.level.block.entity.ChestBlockEntity$1")
public class StrongboxOwnContainerMixin {

    @Inject(method = "isOwnContainer", at = @At("HEAD"), cancellable = true)
    private void mite$isOwnContainer(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (player.containerMenu instanceof StrongboxScreenHandler) {
            cir.setReturnValue(true);
        }
    }
}
