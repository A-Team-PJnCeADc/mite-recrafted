package com.mite.recraft.client.strongbox;

import com.mite.recraft.item.moditems.strongbox.StrongboxRegistry;
import com.mite.recraft.item.moditems.strongbox.StrongboxType;
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
                    StrongboxRegistry.MENU_TYPES.get(type),
                    StrongboxScreen::new
            );
        }

        // Register entity renderer for each strongbox block entity type
        for (StrongboxType type : StrongboxType.VALUES) {
            BlockEntityRenderers.register(
                    StrongboxRegistry.BLOCK_ENTITY_TYPES.get(type),
                    StrongboxEntityRenderer::new
            );
        }
    }
}
