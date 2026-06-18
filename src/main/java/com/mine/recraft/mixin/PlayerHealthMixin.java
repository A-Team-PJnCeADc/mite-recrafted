package com.mine.recraft.mixin;

import com.mine.recraft.PlayerManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerHealthMixin {

    @Inject(method = "giveExperienceLevels", at = @At("RETURN"))
    private void onExperienceLevelChange(int levels, CallbackInfo ci){
        Player player = (Player) (Object) this;
        if (player.level().isClientSide) return;
        PlayerManager.applyMaxHealth((ServerPlayer)player);
    }
}
