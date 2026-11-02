package com.mite.recraft.client.chest;

import com.mite.recraft.chest.ChestVariant;
import com.mite.recraft.registry.ModChests;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

/**
 * 箱子客户端注册：按规格登记方块实体渲染器与界面。
 */
@Environment(EnvType.CLIENT)
public final class ChestClientSetup {

    private ChestClientSetup() {
    }

    public static void register() {
        for (ChestVariant variant : ChestVariant.REGISTERED) {
            registerRenderer(ModChests.storageType(variant));
            MenuScreens.register(ModChests.menuType(variant), VariantChestScreen::new);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends ChestBlockEntity> void registerRenderer(BlockEntityType<? extends ChestBlockEntity> type) {
        BlockEntityRenderers.register((BlockEntityType<T>) type, VariantChestRenderer::new);
    }
}
