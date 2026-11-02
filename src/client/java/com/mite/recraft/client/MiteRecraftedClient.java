package com.mite.recraft.client;

import com.mite.recraft.client.chest.ChestClientSetup;
import com.mite.recraft.client.command.ClientNutritionCommand;
import com.mite.recraft.client.renderer.ModArrowRenderer;
import com.mite.recraft.client.renderer.item.NockedArrowProperty;
import com.mite.recraft.client.renderer.item.QuicknessDrawProperty;
import com.mite.recraft.client.screen.ModWorkbenchScreen;
import com.mite.recraft.client.state.ClientWorkbenchState;
import com.mite.recraft.client.strongbox.StrongboxClientRegistry;
import com.mite.recraft.content.item.food.NutritionSystem;
import com.mite.recraft.network.CraftingProgressSyncPayload;
import com.mite.recraft.network.NutritionSyncPayload;
import com.mite.recraft.registry.ModEntityTypes;
import com.mite.recraft.registry.ModMenus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class MiteRecraftedClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        EntityRenderers.register(ModEntityTypes.MITE_ARROW, ModArrowRenderer::new);
        MenuScreens.register(ModMenus.WORKBENCH, ModWorkbenchScreen::new);

        ClientPlayNetworking.registerGlobalReceiver(CraftingProgressSyncPayload.TYPE,
                (payload, context) -> ClientWorkbenchState.update(payload));

        // 营养同步：将服务端数据附加到客户端玩家
        ClientPlayNetworking.registerGlobalReceiver(NutritionSyncPayload.TYPE,
                (payload, context) -> context.client().execute(() -> {
                    var player = context.client().player;
                    if (player == null) return;
                    player.setAttached(NutritionSystem.PROTEIN, payload.protein());
                    player.setAttached(NutritionSystem.PHYTONUTRIENTS, payload.phytonutrients());
                    player.setAttached(NutritionSystem.ESSENTIAL_FATS, payload.essentialFats());
                    player.setAttached(NutritionSystem.SUGAR_CONTENT, payload.sugarContent());
                    player.setAttached(NutritionSystem.INSULIN_RESISTANCE, payload.insulinResistance());
                    player.setAttached(NutritionSystem.NUTRITION, payload.nutrition());
                }));

        // 客户端营养调试命令
        ClientNutritionCommand.register();

        // 弓的箭矢材质 Select 属性注册
        NockedArrowProperty.register();
        QuicknessDrawProperty.register();

        StrongboxClientRegistry.register();
        ChestClientSetup.register();
    }
}
