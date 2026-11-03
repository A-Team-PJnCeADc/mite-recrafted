package com.mite.recraft.client.chest;

import com.mite.recraft.chest.ChestVariant;
import com.mite.recraft.chest.CrystalChestStorage;
import com.mite.recraft.chest.VariantChestBlock;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.MultiblockChestResources;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * 箱子渲染器：模型与开合动画沿用原版，贴图按规格替换；水晶箱额外渲染箱盖上的展示物品。
 */
@Environment(EnvType.CLIENT)
public class VariantChestRenderer<T extends BlockEntity & LidBlockEntity> extends ChestRenderer<T> {

    private static final float DISPLAY_SCALE = 0.5F;
    private static final float DISPLAY_ROW_STEP = 0.5F;
    private static final double DISPLAY_ROW_START = 0.3;
    private static final double[] DISPLAY_X = {0.55, 1.4};
    private static final double[] DISPLAY_Z = {0.7, 1.4};

    private final SpriteGetter sprites;
    private final MultiblockChestResources<ChestModel> models;

    /** 贴图缺失时使用的原版木箱贴图。 */
    private static final Identifier FALLBACK_TEXTURE =
            Identifier.withDefaultNamespace("entity/chest/normal");

    /** 已判定的档位贴图；资源管理器实例变化（重载资源包）后重新判定。 */
    private static final Map<ChestVariant, Identifier> RESOLVED_TEXTURES = new HashMap<>();
    private static @Nullable ResourceManager resolvedAgainst;

    public VariantChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.sprites = context.sprites();
        this.models = LAYERS.map(layer -> new ChestModel(context.bakeLayer(layer)));
    }

    /** 取档位贴图；文件不在资源包里时改用原版木箱贴图。 */
    private static Identifier resolveTexture(ChestVariant variant) {
        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        if (manager != resolvedAgainst) {
            RESOLVED_TEXTURES.clear();
            resolvedAgainst = manager;
        }
        return RESOLVED_TEXTURES.computeIfAbsent(variant, key -> {
            Identifier texture = key.texture();
            Identifier file = Identifier.fromNamespaceAndPath(texture.getNamespace(),
                    "textures/" + texture.getPath() + ".png");
            return manager.getResource(file).isPresent() ? texture : FALLBACK_TEXTURE;
        });
    }

    @Override
    public ChestRenderState createRenderState() {
        return new VariantRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, ChestRenderState state, float partialTicks, Vec3 camera,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumbling) {
        super.extractRenderState(blockEntity, state, partialTicks, camera, crumbling);
        VariantRenderState renderState = (VariantRenderState) state;

        if (blockEntity.getBlockState().getBlock() instanceof VariantChestBlock chest) {
            ChestVariant variant = chest.variant();
            renderState.sprite = new SpriteId(Sheets.CHEST_SHEET, resolveTexture(variant));
        }
        if (blockEntity instanceof CrystalChestStorage crystal) {
            renderState.display = crystal.displayStacks();
        }
    }

    @Override
    public void submit(ChestRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
                       CameraRenderState camera) {
        VariantRenderState renderState = (VariantRenderState) state;
        if (renderState.sprite == null) {
            super.submit(state, poseStack, collector, camera);
            return;
        }

        poseStack.pushPose();
        poseStack.mulPose(modelTransformation(state.facing));

        float lid = 1.0F - state.open;
        lid = 1.0F - lid * lid * lid;

        ChestModel model = this.models.select(state.type);
        collector.submitModel(model, lid, poseStack, state.lightCoords, OverlayTexture.NO_OVERLAY, -1,
                renderState.sprite, this.sprites, 0);

        if (state.breakProgress != null) {
            collector.order(1).submitCrumblingOverlay(model, lid, poseStack,
                    renderState.sprite.renderType(model.renderType()), state.lightCoords,
                    OverlayTexture.NO_OVERLAY, -1, state.breakProgress);
        }

        if (renderState.display != null) {
            submitDisplay(poseStack, renderState.display, collector, state.lightCoords,
                    (int) state.blockPos.asLong());
        }

        poseStack.popPose();
    }

    private void submitDisplay(PoseStack poseStack, NonNullList<ItemStack> items, SubmitNodeCollector collector,
                               int light, int seed) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        if (level == null) {
            return;
        }
        float rotation = level.getGameTime() + minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);

        int index = 0;
        for (double x : DISPLAY_X) {
            for (double z : DISPLAY_Z) {
                for (int row = 0; row < 3; row++) {
                    submitDisplayItem(items.get(index++), x, DISPLAY_ROW_START + row * DISPLAY_ROW_STEP, z,
                            poseStack, collector, light, seed, minecraft, level, rotation);
                }
            }
        }
    }

    private void submitDisplayItem(ItemStack stack, double x, double y, double z, PoseStack poseStack,
                                   SubmitNodeCollector collector, int light, int seed,
                                   Minecraft minecraft, Level level, float rotation) {
        if (stack.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        poseStack.scale(DISPLAY_SCALE, DISPLAY_SCALE, DISPLAY_SCALE);
        poseStack.translate((float) x, (float) y, (float) z);
        poseStack.rotateDegrees(Axis.YP, rotation);

        ItemStackRenderState itemState = new ItemStackRenderState();
        minecraft.getItemModelResolver().updateForTopItem(itemState, stack, ItemDisplayContext.GROUND,
                level, null, seed);
        itemState.submit(poseStack, collector, light, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    private static final class VariantRenderState extends ChestRenderState {
        @Nullable
        private SpriteId sprite;
        @Nullable
        private NonNullList<ItemStack> display;
    }
}
