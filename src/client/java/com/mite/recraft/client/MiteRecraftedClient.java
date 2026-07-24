package com.mite.recraft.client;

import com.mite.recraft.block.workbench.ModWorkbenchBlock;
import com.mite.recraft.client.command.ClientNutritionCommand;
import com.mite.recraft.client.renderer.ModArrowRenderer;
import com.mite.recraft.client.renderer.item.NockedArrowProperty;
import com.mite.recraft.client.screen.ModWorkbenchScreen;
import com.mite.recraft.client.strongbox.StrongboxClientRegistry;
import com.mite.recraft.entity.ModEntitys;
import com.mite.recraft.item.moditems.food.NutritionSystem;
import com.mite.recraft.network.CraftingProgressSyncPayload;
import com.mite.recraft.network.NutritionSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class MiteRecraftedClient implements ClientModInitializer {

    public static volatile int syncedCraftingPeriod;
    public static volatile int syncedCraftingTicks;
    public static volatile float syncedBenchCoefficient = 1.0f;
    public static volatile int syncedQualityIndex = -1;
    public static volatile int syncedXpCost = 0;

    @Override
    public void onInitializeClient() {
        EntityRenderers.register(ModEntitys.MITE_ARROW, ModArrowRenderer::new);
        MenuScreens.register(ModWorkbenchBlock.MENU_TYPE, ModWorkbenchScreen::new);

        ClientPlayNetworking.registerGlobalReceiver(CraftingProgressSyncPayload.TYPE,
                (payload, context) -> {
                    syncedCraftingPeriod = payload.period();
                    syncedCraftingTicks = payload.ticks();
                    syncedBenchCoefficient = payload.benchCoefficient();
                    syncedQualityIndex = payload.qualityIndex();
                    syncedXpCost = payload.xpCost();
                });

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

        // Strongboxes
        StrongboxClientRegistry.register();
    }
}
