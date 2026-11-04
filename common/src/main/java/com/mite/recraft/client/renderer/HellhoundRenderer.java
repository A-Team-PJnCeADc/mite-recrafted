package com.mite.recraft.client.renderer;

import com.mite.recraft.api.MiteIds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.resources.Identifier;

/** 狱犬渲染器。沿用原版狼模型，固定使用狱犬贴图。 */
@Environment(EnvType.CLIENT)
public class HellhoundRenderer extends WolfRenderer {

    private static final Identifier TEXTURE = MiteIds.id("textures/entity/mob/hellhound.png");

    public HellhoundRenderer(EntityRendererProvider.Context context) {
        super(context);
        addLayer(new MiteEyesLayer(this, MiteEyesLayer.glowTexture(TEXTURE)));
    }

    @Override
    public Identifier getTextureLocation(WolfRenderState state) {
        return TEXTURE;
    }
}
