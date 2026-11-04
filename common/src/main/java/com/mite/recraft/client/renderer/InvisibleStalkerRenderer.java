package com.mite.recraft.client.renderer;

import com.mite.recraft.api.MiteIds;
import com.mite.recraft.client.model.GhoulModel;
import com.mite.recraft.content.entity.mob.InvisibleStalkerEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;

/**
 * 无形潜伏者渲染器。模型与食尸鬼相同（MITE 的 ModelInvisibleStalker），贴图用 wight。
 *
 * <p>MITE 让该模型以 0.05 的不透明度绘制；26.3 的渲染管线改为提交式，暂按不透明绘制。</p>
 */
@Environment(EnvType.CLIENT)
public class InvisibleStalkerRenderer
        extends HumanoidMobRenderer<InvisibleStalkerEntity, HumanoidRenderState, GhoulModel<HumanoidRenderState>> {

    private static final Identifier TEXTURE = MiteIds.id("textures/entity/mob/wight.png");

    public InvisibleStalkerRenderer(EntityRendererProvider.Context context) {
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
