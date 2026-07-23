package com.mite.recraft.util;

import com.mite.recraft.mixin.ChestCompostMixin;
import com.mite.recraft.mixin.ChestWormMixin;

/**
 * 蠕虫堆肥进度访问接口。
 *
 * <p>{@link ChestCompostMixin} 实现此接口注入 {@link net.minecraft.world.level.block.entity.ChestBlockEntity}。
 * {@link ChestWormMixin} 通过此接口类型安全地读写 compost 进度，避免跨 target 的 mixin 类型冲突。</p>
 */
public interface CompostAccess {

    float mite$getCompost();

    void mite$setCompost(float value);
}
