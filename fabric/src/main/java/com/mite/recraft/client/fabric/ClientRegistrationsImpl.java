package com.mite.recraft.client.fabric;

import com.mite.recraft.chest.ChestVariant;
import com.mite.recraft.client.chest.VariantChestRenderer;
import com.mite.recraft.client.chest.VariantChestScreen;
import com.mite.recraft.client.command.ClientNutritionCommand;
import com.mite.recraft.client.model.GhoulModel;
import com.mite.recraft.client.renderer.*;
import com.mite.recraft.client.renderer.item.NockedArrowProperty;
import com.mite.recraft.client.renderer.item.QuicknessDrawProperty;
import com.mite.recraft.client.screen.ModWorkbenchScreen;
import com.mite.recraft.client.strongbox.StrongboxEntityRenderer;
import com.mite.recraft.client.strongbox.StrongboxScreen;
import com.mite.recraft.content.block.strongbox.StrongboxType;
import com.mite.recraft.registry.ModChests;
import com.mite.recraft.registry.ModEntityTypes;
import com.mite.recraft.registry.ModMenus;
import com.mite.recraft.registry.ModStrongboxes;
import dev.architectury.registry.client.gui.MenuScreenRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.LidBlockEntity;

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

    public static void registerModelLayers() {
        ModelLayerRegistry.registerModelLayer(GhoulModel.LAYER, GhoulModel::createBodyLayer);
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(() -> ModEntityTypes.MITE_ARROW, ModArrowRenderer::new);
        EntityRendererRegistry.register(() -> ModEntityTypes.GHOUL, GhoulRenderer::new);
        EntityRendererRegistry.register(() -> ModEntityTypes.LONGDEAD, LongdeadRenderer::new);
        EntityRendererRegistry.register(() -> ModEntityTypes.REVENANT, RevenantRenderer::new);
        EntityRendererRegistry.register(() -> ModEntityTypes.WIGHT, WightRenderer::new);
        EntityRendererRegistry.register(() -> ModEntityTypes.BONE_LORD, BoneLordRenderer::new);
        EntityRendererRegistry.register(() -> ModEntityTypes.ANCIENT_BONE_LORD, BoneLordRenderer::new);
        for (EntityType<? extends Spider> type : MiteSpiderRenderer.REGISTERED) { registerSpider(type); }
        EntityRendererRegistry.register(() -> ModEntityTypes.DIRE_WOLF, DireWolfRenderer::new);
        EntityRendererRegistry.register(() -> ModEntityTypes.HELLHOUND, HellhoundRenderer::new);
        for (EntityType<? extends Silverfish> type : MiteSilverfishRenderer.REGISTERED) { registerSilverfish(type); }
        EntityRendererRegistry.register(() -> ModEntityTypes.LONGDEAD_GUARDIAN, LongdeadGuardianRenderer::new);
        EntityRendererRegistry.register(() -> ModEntityTypes.INFERNAL_CREEPER, InfernalCreeperRenderer::new);
        EntityRendererRegistry.register(() -> ModEntityTypes.INVISIBLE_STALKER, InvisibleStalkerRenderer::new);
        for (EntityType<? extends Slime> type : MiteSlimeRenderer.REGISTERED) { registerSlime(type); }
        EntityRendererRegistry.register(() -> ModEntityTypes.VAMPIRE_BAT,
                MiteBatRenderer.provider("textures/entity/mob/vampire_bat.png"));
        EntityRendererRegistry.register(() -> ModEntityTypes.GIANT_VAMPIRE_BAT,
                MiteBatRenderer.provider("textures/entity/mob/vampire_bat.png"));
        EntityRendererRegistry.register(() -> ModEntityTypes.NIGHTWING,
                MiteBatRenderer.provider("textures/entity/mob/nightwing.png"));
        EntityRendererRegistry.register(() -> ModEntityTypes.SHADOW, ShadowRenderer::new);
        EntityRendererRegistry.register(() -> ModEntityTypes.FIRE_ELEMENTAL, FireElementalRenderer::new);
        EntityRendererRegistry.register(() -> ModEntityTypes.EARTH_ELEMENTAL, EarthElementalRenderer::new);
        EntityRendererRegistry.register(() -> ModEntityTypes.CLAY_GOLEM, EarthElementalRenderer::new);
        EntityRendererRegistry.register(() -> ModEntityTypes.GIANT_ZOMBIE, ZombieRenderer::new);
        EntityRendererRegistry.register(() -> ModEntityTypes.WEB, ThrownItemRenderer::new);
        EntityRendererRegistry.register(() -> ModEntityTypes.BRICK, ThrownItemRenderer::new);
        EntityRendererRegistry.register(() -> ModEntityTypes.GELATINOUS_SPHERE, ThrownItemRenderer::new);
    }

    /** 蛛形生物渲染器的登记入口：模型固定为原版蜘蛛模型，只有贴图不同。 */
    private static void registerSpider(EntityType<? extends Spider> type) {
        EntityRendererRegistry.register(() -> type, MiteSpiderRenderer.<Spider>provider(type));
    }

    private static void registerSilverfish(EntityType<? extends Silverfish> type) {
        EntityRendererRegistry.register(() -> type, MiteSilverfishRenderer.provider(type));
    }

    private static void registerSlime(EntityType<? extends Slime> type) {
        EntityRendererRegistry.register(() -> type, MiteSlimeRenderer.provider(type));
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

    private static <T extends BlockEntity & LidBlockEntity> void registerChestRenderer(BlockEntityType<T> type) {
        BlockEntityRendererRegistry.register(type, VariantChestRenderer::new);
    }

    public static void registerItemModelProperties() {
        NockedArrowProperty.register();
        QuicknessDrawProperty.register();
    }

    public static void registerClientCommands() {
        ClientNutritionCommand.register();
    }
}
