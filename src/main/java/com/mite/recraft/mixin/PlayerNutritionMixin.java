package com.mite.recraft.mixin;

import com.mite.recraft.item.moditems.food.NutritionSystem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 玩家 tick 注入 — 服务端营养消耗。
 */
@Mixin(Player.class)
public class PlayerNutritionMixin {

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        Player self = (Player) (Object) this;
        if (!(self instanceof ServerPlayer serverPlayer)) return;
        NutritionSystem.tick(serverPlayer);
    }
}
