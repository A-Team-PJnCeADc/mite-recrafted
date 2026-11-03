package com.mite.recraft.registry;

import com.mite.recraft.client.ClientNetworkHandlers;
import com.mite.recraft.content.item.food.NutritionSystem;
import com.mite.recraft.network.CraftingProgressSyncPayload;
import com.mite.recraft.network.NutritionSyncPayload;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;

/**
 * 网络载荷注册与服务端连接钩子。
 *
 * <p>{@link NetworkManager#registerS2C} 同时完成载荷类型注册与客户端接收端绑定，两件事不能拆开调用：
 * 逐个注册类型后再调 registerReceiver 会在 Fabric 侧重复注册同一 id。</p>
 */
public final class ModNetwork {

    private ModNetwork() {
    }

    public static void register() {
        // 用方法体内的间接调用而非方法引用：专用服务端上注册接收端时不得解析客户端类
        NetworkManager.registerS2C(CraftingProgressSyncPayload.TYPE, CraftingProgressSyncPayload.CODEC,
                (payload, context) -> ClientNetworkHandlers.craftingProgress(payload, context));
        NetworkManager.registerS2C(NutritionSyncPayload.TYPE, NutritionSyncPayload.CODEC,
                (payload, context) -> ClientNetworkHandlers.nutrition(payload, context));

        // 登录时下发当前营养数据，客户端数据在此之前是初始值
        PlayerEvent.PLAYER_JOIN.register(NutritionSystem::sendSyncPacket);
    }
}
