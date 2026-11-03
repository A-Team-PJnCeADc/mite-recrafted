package com.mite.recraft.mixin;

import com.mite.recraft.content.block.anvil.ModAnvilMenu;

import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 把 AnvilMenu.createResult 的计算替换为 {@link ModAnvilMenu#mite$createResult()}。
 *
 * <p>NeoForge 把 createResult 声明为 final，子类无法覆盖；两端统一改为 mixin 注入。
 * 子类内部仍会调用 super.createResult() 走原版附魔书合并，用静态标志避免重入。</p>
 */
@Mixin(AnvilMenu.class)
public abstract class AnvilMenuCreateResultMixin {

    @Unique
    private static boolean mite$inside;

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void mite$replaceCreateResult(CallbackInfo ci) {
        if (mite$inside) {
            return;
        }
        if ((Object) this instanceof ModAnvilMenu menu) {
            mite$inside = true;
            try {
                menu.mite$createResult();
            } finally {
                mite$inside = false;
            }
            ci.cancel();
        }
    }
}
