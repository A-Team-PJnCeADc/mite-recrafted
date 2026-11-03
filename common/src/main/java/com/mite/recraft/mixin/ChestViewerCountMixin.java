package com.mite.recraft.mixin;

import com.mite.recraft.chest.ChestMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 让原版箱子的“是否为本人容器”判断识别 {@link ChestMenu}。
 *
 * <p>原版只认 {@code minecraft:chest} 菜单，自定义箱子被打开时不会计入开启者数量，
 * 箱盖开合与多人共享判定都会失效，因此补上本模组菜单的匹配。</p>
 */
@Mixin(targets = "net.minecraft.world.level.block.entity.ChestBlockEntity$1")
public abstract class ChestViewerCountMixin {

    @Inject(method = "isOwnContainer", at = @At("RETURN"), cancellable = true)
    private void mite$acceptCustomChestMenu(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() || !(player.containerMenu instanceof ChestMenu menu)) {
            return;
        }

        ChestBlockEntity chest = ((ChestOpenCounterAccessor) this).mite$owner();
        if (menu.storage() == chest) {
            cir.setReturnValue(true);
            return;
        }

        BlockPos pos = menu.blockPos();
        if (pos != null && pos.equals(chest.getBlockPos())) {
            cir.setReturnValue(true);
        }
    }
}
