package com.mite.recraft.mixin;

import com.mite.recraft.util.CompostAccess;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 为 {@link ChestBlockEntity} 添加蠕虫堆肥进度持久化。
 *
 * <p>存储于 NBT 键 {@code mite-recraft:compost_progress}，通过 MC 26.2 的
 * {@link ValueInput}/{@link ValueOutput} API 读写。
 * 供 {@link ChestWormMixin} 使用，替代静态 HashMap。</p>
 */
@Mixin(ChestBlockEntity.class)
public class ChestCompostMixin implements CompostAccess {

    @Unique
    private static final String COMPOST_KEY = "mite-recraft:compost_progress";

    @Unique
    private float mite$compost;

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void mite$loadCompost(ValueInput input, CallbackInfo ci) {
        mite$compost = input.getFloatOr(COMPOST_KEY, 0.0F);
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void mite$saveCompost(ValueOutput output, CallbackInfo ci) {
        if (mite$compost > 0.0F) {
            output.putFloat(COMPOST_KEY, mite$compost);
        }
    }

    /** 供 {@link ChestWormMixin} 读写 compost 进度 */
    @Unique
    public float mite$getCompost() {
        return mite$compost;
    }

    @Unique
    public void mite$setCompost(float value) {
        mite$compost = value;
    }
}
