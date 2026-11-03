package com.mite.recraft.client;

import com.mite.recraft.client.state.ClientWorkbenchState;
import com.mite.recraft.content.item.food.NutritionSystem;
import com.mite.recraft.internal.data.EntityData;
import com.mite.recraft.network.CraftingProgressSyncPayload;
import com.mite.recraft.network.NutritionSyncPayload;

import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

/**
 * 客户端网络载荷处理。注册点在通用初始化（{@code ModNetwork}），方法体只在客户端执行，
 * 专用服务端不会加载本类。
 */
@Environment(EnvType.CLIENT)
public final class ClientNetworkHandlers {

    private ClientNetworkHandlers() {
    }

    public static void craftingProgress(CraftingProgressSyncPayload payload, NetworkManager.PacketContext context) {
        context.queue(() -> ClientWorkbenchState.update(payload));
    }

    /** 服务端下发营养值后写入客户端玩家。 */
    public static void nutrition(NutritionSyncPayload payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            var player = Minecraft.getInstance().player;
            if (player == null) {
                return;
            }
            EntityData.set(player, NutritionSystem.PROTEIN, payload.protein());
            EntityData.set(player, NutritionSystem.PHYTONUTRIENTS, payload.phytonutrients());
            EntityData.set(player, NutritionSystem.ESSENTIAL_FATS, payload.essentialFats());
            EntityData.set(player, NutritionSystem.SUGAR_CONTENT, payload.sugarContent());
            EntityData.set(player, NutritionSystem.INSULIN_RESISTANCE, payload.insulinResistance());
            EntityData.set(player, NutritionSystem.NUTRITION, payload.nutrition());
        });
    }
}
