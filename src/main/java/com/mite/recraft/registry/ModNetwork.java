package com.mite.recraft.registry;

import com.mite.recraft.content.item.food.NutritionSystem;
import com.mite.recraft.network.CraftingProgressSyncPayload;
import com.mite.recraft.network.NutritionSyncPayload;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

/**
 * 网络载荷注册与服务端连接钩子。客户端接收端在 MiteRecraftedClient 中注册。
 */
public final class ModNetwork {

    private ModNetwork() {
    }

    public static void register() {
        PayloadTypeRegistry.clientboundPlay().register(CraftingProgressSyncPayload.TYPE, CraftingProgressSyncPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(NutritionSyncPayload.TYPE, NutritionSyncPayload.CODEC);

        // 登录时下发当前营养数据，客户端附件在此之前是初始值
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                NutritionSystem.sendSyncPacket(handler.getPlayer()));
    }
}
