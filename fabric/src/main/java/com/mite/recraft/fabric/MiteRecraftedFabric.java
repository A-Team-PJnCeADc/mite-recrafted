package com.mite.recraft.fabric;

import com.mite.recraft.MiteRecrafted;

import net.fabricmc.api.ModInitializer;

/**
 * Fabric 入口：Fabric 的注册表在模组初始化期可写，两段初始化连续执行。
 */
public final class MiteRecraftedFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        MiteRecrafted.registerContent();
        MiteRecrafted.initRuntime();
    }
}
