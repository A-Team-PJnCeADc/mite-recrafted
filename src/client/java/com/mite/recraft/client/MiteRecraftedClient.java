package com.mite.recraft.client;

import com.mite.recraft.block.workbench.ModWorkbenchBlock;
import com.mite.recraft.client.renderer.ModArrowRenderer;
import com.mite.recraft.client.screen.ModWorkbenchScreen;
import com.mite.recraft.entity.ModEntitys;
import com.mite.recraft.network.CraftingProgressSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class MiteRecraftedClient implements ClientModInitializer {

    public static volatile int syncedCraftingPeriod;
    public static volatile int syncedCraftingTicks;

    @Override
    public void onInitializeClient() {
        EntityRenderers.register(ModEntitys.MITE_ARROW, ModArrowRenderer::new);

        MenuScreens.register(ModWorkbenchBlock.MENU_TYPE, ModWorkbenchScreen::new);


        ClientPlayNetworking.registerGlobalReceiver(CraftingProgressSyncPayload.TYPE,
                (payload, context) -> {
                    syncedCraftingPeriod = payload.period();
                    syncedCraftingTicks = payload.ticks();
                });
    }
}
