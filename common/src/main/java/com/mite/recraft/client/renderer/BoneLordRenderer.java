package com.mite.recraft.client.renderer;

import com.mite.recraft.api.MiteIds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.resources.Identifier;

/** 白骨领主渲染器。白骨领主与远古白骨领主共用同一贴图。 */
@Environment(EnvType.CLIENT)
public class BoneLordRenderer extends SkeletonRenderer {

    private static final Identifier TEXTURE = MiteIds.id("textures/entity/mob/bone_lord.png");

    public BoneLordRenderer(EntityRendererProvider.Context context) {
        super(context);
        addLayer(new MiteEyesLayer(this, MiteEyesLayer.glowTexture(TEXTURE)));
    }

    @Override
    public Identifier getTextureLocation(SkeletonRenderState state) {
        return TEXTURE;
    }
}
