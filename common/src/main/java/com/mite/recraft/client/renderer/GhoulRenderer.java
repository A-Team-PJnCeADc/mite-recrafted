package com.mite.recraft.client.renderer;

import com.mite.recraft.api.MiteIds;
import com.mite.recraft.client.model.GhoulModel;
import com.mite.recraft.content.entity.mob.GhoulEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;

/**
 * 食尸鬼渲染器。模型走自定义层定义（64x32），因此直接使用 MITE 原始贴图；
 * 层定义在 {@link GhoulModel} 中按 MITE 的 ModelInvisibleStalker 姿态构建。
 */
@Environment(EnvType.CLIENT)
public class GhoulRenderer
        extends HumanoidMobRenderer<GhoulEntity, HumanoidRenderState, GhoulModel<HumanoidRenderState>> {

    private static final Identifier TEXTURE = MiteIds.id("textures/entity/mob/ghoul.png");

    public GhoulRenderer(EntityRendererProvider.Context context) {
        super(context, new GhoulModel<HumanoidRenderState>(context.bakeLayer(GhoulModel.LAYER)), 0.5F);
    }

    @Override
    public HumanoidRenderState createRenderState() {
        return new HumanoidRenderState();
    }

    @Override
    public Identifier getTextureLocation(HumanoidRenderState state) {
        return TEXTURE;
    }
}
