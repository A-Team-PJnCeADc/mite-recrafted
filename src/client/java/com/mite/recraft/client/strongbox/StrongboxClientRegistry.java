package com.mite.recraft.client.strongbox;

import com.mite.recraft.content.block.strongbox.StrongboxType;
import com.mite.recraft.registry.ModStrongboxes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

@Environment(EnvType.CLIENT)
public class StrongboxClientRegistry {
    public static void register() {
        // Register screen for each strongbox menu type
        for (StrongboxType type : StrongboxType.VALUES) {
            MenuScreens.register(
                    ModStrongboxes.MENU_TYPES.get(type),
                    StrongboxScreen::new
            );
        }

        // Register entity renderer for each strongbox block entity type
        for (StrongboxType type : StrongboxType.VALUES) {
            BlockEntityRenderers.register(
                    ModStrongboxes.BLOCK_ENTITY_TYPES.get(type),
                    StrongboxEntityRenderer::new
            );
        }
    }
}
