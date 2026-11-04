package com.mite.recraft.client.renderer;

import com.mite.recraft.api.MiteIds;
import com.mite.recraft.client.model.GhoulModel;
import com.mite.recraft.content.entity.mob.WightEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;

/**
 * 尸妖渲染器。与食尸鬼同为 MITE 的 ModelInvisibleStalker 姿态，复用 64x32 的 {@link GhoulModel}，
 * 只有贴图不同。
 */
@Environment(EnvType.CLIENT)
public class WightRenderer
        extends HumanoidMobRenderer<WightEntity, HumanoidRenderState, GhoulModel<HumanoidRenderState>> {

    private static final Identifier TEXTURE = MiteIds.id("textures/entity/mob/wight.png");

    public WightRenderer(EntityRendererProvider.Context context) {
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
