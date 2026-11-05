package com.mite.recraft.internal.data;

import com.mite.recraft.api.MiteData;

import net.minecraft.world.entity.Entity;

/**
 * 附加数据读写的平台实现。实现类由平台入口通过 {@link EntityData#install} 注入，
 * 源码里对实现类有直接引用，IDE 可以直接跳转。
 */
public interface EntityDataApi {

    /** 把 {@link MiteData#all()} 注册进平台附件注册表。 */
    void register();

    <T> T get(Entity entity, MiteData<T> data);

    <T> void set(Entity entity, MiteData<T> data, T value);
}
