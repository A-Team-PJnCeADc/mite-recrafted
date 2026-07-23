package com.mite.recraft.client.strongbox;

import com.mite.recraft.item.moditems.strongbox.StrongboxBlock;
import com.mite.recraft.item.moditems.strongbox.StrongboxBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.MultiblockChestResources;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class StrongboxEntityRenderer<T extends StrongboxBlockEntity & LidBlockEntity> extends ChestRenderer<T> {
    private final SpriteGetter sprites;
    private final MultiblockChestResources<ChestModel> models;

    public StrongboxEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.sprites = context.sprites();
        this.models = LAYERS.map(layer -> new ChestModel(context.bakeLayer(layer)));
    }

    @Override
    public ChestRenderState createRenderState() {
        return new StrongboxRenderState();
    }

    @Override
    public void extractRenderState(
            T blockEntity,
            ChestRenderState state,
            float partialTicks,
            Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        StrongboxRenderState renderState = (StrongboxRenderState) state;
        if (blockEntity.getBlockState().getBlock() instanceof StrongboxBlock strongboxBlock) {
            renderState.modSprite = new SpriteId(
                    Sheets.CHEST_SHEET,
                    strongboxBlock.getStrongboxType().texture
            );
        }
    }

    @Override
    public void submit(ChestRenderState state, PoseStack poseStack,
                       SubmitNodeCollector submitNodeCollector,
                       net.minecraft.client.renderer.state.level.CameraRenderState camera) {
        StrongboxRenderState renderState = (StrongboxRenderState) state;
        if (renderState.modSprite == null) {
            super.submit(state, poseStack, submitNodeCollector, camera);
            return;
        }

        poseStack.pushPose();
        poseStack.mulPose(modelTransformation(state.facing));
        float open = state.open;
        open = 1.0F - open;
        open = 1.0F - open * open * open;
        ChestModel model = this.models.select(state.type);
        submitNodeCollector.submitModel(
                model,
                open,
                poseStack,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                -1,
                renderState.modSprite,
                this.sprites,
                0,
                state.breakProgress
        );
        poseStack.popPose();
    }

    private static final class StrongboxRenderState extends ChestRenderState {
        @Nullable
        private SpriteId modSprite;
    }
}
