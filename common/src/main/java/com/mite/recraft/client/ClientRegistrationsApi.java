package com.mite.recraft.client;

/**
 * 客户端注册的平台实现。实现类由平台入口通过 {@link ClientRegistrations#install} 注入，
 * 只在客户端安装，专用服务端不会加载。
 *
 * <p>NeoForge 的界面、模型层与实体渲染器由客户端事件完成，对应方法为空实现；
 * AppleSkin 这类可选兼容在 {@link #registerCompat()} 里处理。</p>
 */
public interface ClientRegistrationsApi {

    void registerModelLayers();

    void registerScreens();

    void registerEntityRenderers();

    void registerBlockEntityRenderers();

    void registerItemModelProperties();

    void registerClientCommands();

    /** 可选模组兼容（AppleSkin 等），在客户端注册末尾调用。 */
    void registerCompat();
}
