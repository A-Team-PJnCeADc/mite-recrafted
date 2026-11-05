package com.mite.recraft;

import com.mite.recraft.client.ClientRegistrations;
import com.mite.recraft.content.item.food.NutritionSystem;
import com.mite.recraft.registry.MiteRegistrationPhases;
import com.mite.recraft.registry.ModFuels;
import com.mite.recraft.registry.ModNetwork;

import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通用入口。拆成两段：注册阶段声明由 {@link MiteRegistrationPhases} 承载，运行期初始化由 {@link #initRuntime()} 承载。
 *
 * <p>Fabric 在模组初始化时两段连续执行；NeoForge 由 RegisterEvent 逐注册表执行注册阶段，
 * 再在模组总线事件里执行运行期初始化。</p>
 */
public final class MiteRecrafted {

    /** 加载器 mod id。NeoForge 要求 ^[a-z][a-z0-9_]*(\\.[a-z][a-z0-9_]*)*$，不允许连字符。 */
    public static final String MOD_ID = "mite_recrafted";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private MiteRecrafted() {
    }

    /** 立即执行全部注册阶段。仅在 Fabric 这类即时注册的加载器上调用。 */
    public static void registerContent() {
        // 先触发附加数据描述符的类初始化，runAll 末尾要按声明表注册附件
        NutritionSystem.register();
        MiteRegistrationPhases.runAll();
        // 生物默认属性由平台入口处理：Fabric 直接写入，NeoForge 由 EntityAttributeCreationEvent 回调
        // 客户端登记不放进 runAll：专用服务端不加载任何客户端类
        EnvExecutor.runInEnv(Env.CLIENT, () -> ClientRegistrations::registerEarly);
    }

    /** 运行期初始化：网络载荷、燃料表。必须在所有注册阶段完成后调用。 */
    public static void initRuntime() {
        ModNetwork.register();
        ModFuels.register();
        // 方块实体渲染器需要已注册的方块实体类型
        EnvExecutor.runInEnv(Env.CLIENT, () -> ClientRegistrations::registerLate);
    }
}
