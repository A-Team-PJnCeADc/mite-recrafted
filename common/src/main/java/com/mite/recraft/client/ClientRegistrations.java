package com.mite.recraft.client;

/**
 * 客户端注册声明。common 只决定“要注册哪些内容、在哪个阶段注册”，
 * 具体调用哪个注册 API 由 {@link ClientRegistrationsApi} 的实现决定。
 */
public final class ClientRegistrations {

    private static ClientRegistrationsApi implementation;

    private ClientRegistrations() {
    }

    /** 平台入口在客户端注入实现。 */
    public static void install(ClientRegistrationsApi api) {
        implementation = api;
    }

    private static ClientRegistrationsApi api() {
        if (implementation == null) {
            throw new IllegalStateException("ClientRegistrations 平台实现未安装");
        }
        return implementation;
    }

    /** 注册表写入前的客户端登记：模型层、界面与实体渲染器。模型层必须先于渲染器。 */
    public static void registerEarly() {
        api().registerModelLayers();
        api().registerScreens();
        api().registerEntityRenderers();
    }

    /** 注册表写入后的客户端登记：模型属性、客户端命令与依赖已注册类型的方块实体渲染器。 */
    public static void registerLate() {
        api().registerBlockEntityRenderers();
        api().registerItemModelProperties();
        api().registerClientCommands();
        api().registerCompat();
    }
}
