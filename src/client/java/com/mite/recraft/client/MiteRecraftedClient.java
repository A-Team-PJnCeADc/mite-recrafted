package com.mite.recraft.client;

import com.mite.recraft.client.renderer.ModArrowRenderer;
import com.mite.recraft.entity.ModEntitys;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class MiteRecraftedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRenderers.register(ModEntitys.MITE_ARROW, ModArrowRenderer::new);
    }
}
