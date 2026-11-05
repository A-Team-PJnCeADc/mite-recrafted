package com.mite.recraft.internal.data;

import com.mite.recraft.api.MiteData;

import net.minecraft.world.entity.Entity;

/**
 * 附加数据读写的平台入口。平台入口先调用 {@link #install} 注入实现，再走注册与读写。
 *
 * <p>{@link #register()} 必须在平台注册表冻结前调用，并早于任何 {@link #get}/{@link #set}；
 * 因此新增的 {@link MiteData} 必须在注册调用之前完成类初始化。</p>
 */
public final class EntityData {

    private static EntityDataApi implementation;

    private EntityData() {
    }

    /** 平台入口注入实现。 */
    public static void install(EntityDataApi api) {
        implementation = api;
    }

    private static EntityDataApi api() {
        if (implementation == null) {
            throw new IllegalStateException("EntityData 平台实现未安装");
        }
        return implementation;
    }

    /** 把 {@link MiteData#all()} 注册进平台附件注册表。 */
    public static void register() {
        api().register();
    }

    /** 读取数据，不存在时按描述符的初始值创建。 */
    public static <T> T get(Entity entity, MiteData<T> data) {
        return api().get(entity, data);
    }

    /** 写入数据。 */
    public static <T> void set(Entity entity, MiteData<T> data, T value) {
        api().set(entity, data, value);
    }
}
