package com.mite.recraft.client.fabric;

import com.mite.recraft.chest.ChestVariant;
import com.mite.recraft.client.command.ClientNutritionCommand;
import com.mite.recraft.client.renderer.ModArrowRenderer;
import com.mite.recraft.client.renderer.item.NockedArrowProperty;
import com.mite.recraft.client.renderer.item.QuicknessDrawProperty;
import com.mite.recraft.client.screen.ModWorkbenchScreen;
import com.mite.recraft.client.strongbox.StrongboxEntityRenderer;
import com.mite.recraft.client.strongbox.StrongboxScreen;
import com.mite.recraft.client.chest.VariantChestRenderer;
import com.mite.recraft.client.chest.VariantChestScreen;
import com.mite.recraft.content.block.strongbox.StrongboxType;
import com.mite.recraft.registry.ModChests;
import com.mite.recraft.registry.ModEntityTypes;
import com.mite.recraft.registry.ModMenus;
import com.mite.recraft.registry.ModStrongboxes;

import dev.architectury.registry.client.gui.MenuScreenRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

/**
 * Fabric 客户端注册实现。Fabric 的客户端注册表在初始化期可写，直接登记即可。
 */
public final class ClientRegistrationsImpl {

    private ClientRegistrationsImpl() {
    }

    public static void registerScreens() {
        MenuScreenRegistry.registerScreenFactory(ModMenus.WORKBENCH, ModWorkbenchScreen::new);
        for (ChestVariant variant : ChestVariant.REGISTERED) {
            MenuScreenRegistry.registerScreenFactory(ModChests.menuType(variant), VariantChestScreen::new);
        }
        for (StrongboxType type : StrongboxType.VALUES) {
            MenuScreenRegistry.registerScreenFactory(ModStrongboxes.MENU_TYPES.get(type), StrongboxScreen::new);
        }
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(() -> ModEntityTypes.MITE_ARROW, ModArrowRenderer::new);
    }

    public static void registerBlockEntityRenderers() {
        for (ChestVariant variant : ChestVariant.REGISTERED) {
            registerChestRenderer(ModChests.storageType(variant));
        }
        for (StrongboxType type : StrongboxType.VALUES) {
            BlockEntityRendererRegistry.register(
                    ModStrongboxes.BLOCK_ENTITY_TYPES.get(type), StrongboxEntityRenderer::new);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends ChestBlockEntity> void registerChestRenderer(BlockEntityType<? extends ChestBlockEntity> type) {
        BlockEntityRendererRegistry.register((BlockEntityType<T>) type, VariantChestRenderer::new);
    }

    public static void registerItemModelProperties() {
        NockedArrowProperty.register();
        QuicknessDrawProperty.register();
    }

    public static void registerClientCommands() {
        ClientNutritionCommand.register();
    }
}
