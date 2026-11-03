package com.mite.recraft.internal.data;

import com.mite.recraft.api.MiteData;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.Entity;

/**
 * 附加数据读写的平台入口。Fabric 实现为 data attachment，NeoForge 实现为 attachment。
 *
 * <p>{@link #register()} 必须在平台注册表冻结前调用，并早于任何 {@link #get}/{@link #set}；
 * 平台入口在 {@code MiteRecrafted.init()} 末尾调用，因此新增的 {@link MiteData} 必须在
 * 该调用之前完成类初始化。</p>
 */
public final class EntityData {

    private EntityData() {
    }

    /** 把 {@link MiteData#all()} 注册进平台附件注册表。 */
    @ExpectPlatform
    public static void register() {
        throw new AssertionError();
    }

    /** 读取数据，不存在时按描述符的初始值创建。 */
    @ExpectPlatform
    public static <T> T get(Entity entity, MiteData<T> data) {
        throw new AssertionError();
    }

    /** 写入数据。 */
    @ExpectPlatform
    public static <T> void set(Entity entity, MiteData<T> data, T value) {
        throw new AssertionError();
    }
}
