package com.mite.recraft.fabric;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.client.ClientRegistrations;
import com.mite.recraft.client.fabric.ClientRegistrationsImpl;
import com.mite.recraft.internal.data.EntityData;
import com.mite.recraft.internal.data.fabric.EntityDataImpl;
import com.mite.recraft.registry.fabric.ModEntityAttributesImpl;

import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.fabricmc.api.ModInitializer;

/**
 * Fabric 入口：Fabric 的注册表在模组初始化期可写，两段初始化连续执行。
 * 平台实现在这里注入，源码中对实现类有直接引用。
 */
public final class MiteRecraftedFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        EntityData.install(new EntityDataImpl());
        EnvExecutor.runInEnv(Env.CLIENT, () -> () -> ClientRegistrations.install(new ClientRegistrationsImpl()));
        MiteRecrafted.registerContent();
        ModEntityAttributesImpl.register();
        MiteRecrafted.initRuntime();
    }
}
