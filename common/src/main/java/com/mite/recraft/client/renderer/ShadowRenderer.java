package com.mite.recraft.client.renderer;

import com.mite.recraft.api.MiteIds;
import com.mite.recraft.client.model.GhoulModel;
import com.mite.recraft.content.entity.mob.ShadowEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;

/** 暗影渲染器。模型与无形潜伏者同源（MITE 的 ModelInvisibleStalker），只换贴图。 */
@Environment(EnvType.CLIENT)
public class ShadowRenderer
        extends HumanoidMobRenderer<ShadowEntity, HumanoidRenderState, GhoulModel<HumanoidRenderState>> {

    private static final Identifier TEXTURE = MiteIds.id("textures/entity/mob/shadow.png");

    public ShadowRenderer(EntityRendererProvider.Context context) {
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
