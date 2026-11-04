package com.mite.recraft.client.neoforge;

import com.mite.recraft.MiteRecrafted;
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
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

/**
 * NeoForge 客户端注册实现。界面与渲染器通过客户端事件登记，事件晚于模组构造触发，
 * 因此这些登记方法本身不做事，实际登记在订阅方法里完成。
 *
 * <p>{@code Dist.CLIENT} 使本类只在客户端被扫描与加载。</p>
 */
@EventBusSubscriber(modid = MiteRecrafted.MOD_ID, value = Dist.CLIENT)
public final class ClientRegistrationsImpl {

    private ClientRegistrationsImpl() {
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.WORKBENCH, ModWorkbenchScreen::new);
        for (ChestVariant variant : ChestVariant.REGISTERED) {
            event.register(ModChests.menuType(variant), VariantChestScreen::new);
        }
        for (StrongboxType type : StrongboxType.VALUES) {
            event.register(ModStrongboxes.MENU_TYPES.get(type), StrongboxScreen::new);
        }
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GhoulModel.LAYER, GhoulModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.MITE_ARROW, ModArrowRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.GHOUL, GhoulRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.LONGDEAD, LongdeadRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.REVENANT, RevenantRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.WIGHT, WightRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.BONE_LORD, BoneLordRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.ANCIENT_BONE_LORD, BoneLordRenderer::new);
        for (EntityType<? extends Spider> type : MiteSpiderRenderer.REGISTERED) {
            registerSpider(event, type);
        }
        event.registerEntityRenderer(ModEntityTypes.DIRE_WOLF, DireWolfRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.HELLHOUND, HellhoundRenderer::new);
        for (EntityType<? extends Silverfish> type : MiteSilverfishRenderer.REGISTERED) {
            registerSilverfish(event, type);
        }
        event.registerEntityRenderer(ModEntityTypes.LONGDEAD_GUARDIAN, LongdeadGuardianRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.INFERNAL_CREEPER, InfernalCreeperRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.INVISIBLE_STALKER, InvisibleStalkerRenderer::new);
        for (EntityType<? extends Slime> type : MiteSlimeRenderer.REGISTERED) {
            event.registerEntityRenderer(type, MiteSlimeRenderer.provider(type));
        }
        event.registerEntityRenderer(ModEntityTypes.VAMPIRE_BAT,
                MiteBatRenderer.provider("textures/entity/mob/vampire_bat.png"));
        event.registerEntityRenderer(ModEntityTypes.GIANT_VAMPIRE_BAT,
                MiteBatRenderer.provider("textures/entity/mob/vampire_bat.png"));
        event.registerEntityRenderer(ModEntityTypes.NIGHTWING,
                MiteBatRenderer.provider("textures/entity/mob/nightwing.png"));
        event.registerEntityRenderer(ModEntityTypes.SHADOW, ShadowRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.FIRE_ELEMENTAL, FireElementalRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.EARTH_ELEMENTAL, EarthElementalRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.CLAY_GOLEM, EarthElementalRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.GIANT_ZOMBIE, ZombieRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.WEB, ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.BRICK, ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.GELATINOUS_SPHERE, ThrownItemRenderer::new);

        for (ChestVariant variant : ChestVariant.REGISTERED) {
            registerChestRenderer(event, ModChests.storageType(variant));
        }
        for (StrongboxType type : StrongboxType.VALUES) {
            event.registerBlockEntityRenderer(
                    ModStrongboxes.BLOCK_ENTITY_TYPES.get(type), StrongboxEntityRenderer::new);
        }
    }

    /** 蠹虫系渲染器的登记入口：模型固定为原版蠹虫模型，贴图按实体 id 推导。 */
    private static void registerSilverfish(EntityRenderersEvent.RegisterRenderers event,
                                           EntityType<? extends Silverfish> type) {
        event.registerEntityRenderer(type, MiteSilverfishRenderer.provider(type));
    }

    /** 蛛形生物渲染器的登记入口：模型固定为原版蜘蛛模型，只有贴图不同。 */
    private static void registerSpider(EntityRenderersEvent.RegisterRenderers event,
                                       EntityType<? extends Spider> type) {
        event.registerEntityRenderer(type, MiteSpiderRenderer.<Spider>provider(type));
    }

    private static <T extends BlockEntity & LidBlockEntity> void registerChestRenderer(
            EntityRenderersEvent.RegisterRenderers event,
            net.minecraft.world.level.block.entity.BlockEntityType<? extends T> type) {
        event.registerBlockEntityRenderer(type, VariantChestRenderer::new);
    }

    public static void registerScreens() {
        // 由 onRegisterMenuScreens 登记
    }

    public static void registerModelLayers() {
        // 由 onRegisterLayerDefinitions 登记
    }

    public static void registerEntityRenderers() {
        // 由 onRegisterRenderers 登记
    }

    public static void registerBlockEntityRenderers() {
        // 由 onRegisterRenderers 登记
    }

    public static void registerItemModelProperties() {
        NockedArrowProperty.register();
        QuicknessDrawProperty.register();
    }

    public static void registerClientCommands() {
        ClientNutritionCommand.register();
    }
}
