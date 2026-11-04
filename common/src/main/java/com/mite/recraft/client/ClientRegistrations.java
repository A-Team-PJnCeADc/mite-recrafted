package com.mite.recraft.client;

import dev.architectury.injectables.annotations.ExpectPlatform;

/**
 * 客户端注册声明。common 只声明“要注册哪些客户端内容”，具体调用哪个注册 API 由平台实现决定：
 * Fabric 直接登记，NeoForge 则把登记挂到自己的客户端事件上。
 *
 * <p>实现类位于各平台模块的 {@code com.mite.recraft.client.<platform>} 包，只在客户端调用链上出现，
 * 专用服务端不会加载。</p>
 */
public final class ClientRegistrations {

    private ClientRegistrations() {
    }

    /** 注册表写入前的客户端登记：模型层、界面与实体渲染器。模型层必须先于渲染器。 */
    public static void registerEarly() {
        registerModelLayers();
        registerScreens();
        registerEntityRenderers();
    }

    @ExpectPlatform
    public static void registerModelLayers() {
        throw new AssertionError();
    }

    /** 注册表写入后的客户端登记：模型属性、客户端命令与依赖已注册类型的方块实体渲染器。 */
    public static void registerLate() {
        registerBlockEntityRenderers();
        registerItemModelProperties();
        registerClientCommands();
    }

    @ExpectPlatform
    public static void registerScreens() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerEntityRenderers() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerBlockEntityRenderers() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerItemModelProperties() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerClientCommands() {
        throw new AssertionError();
    }
}
