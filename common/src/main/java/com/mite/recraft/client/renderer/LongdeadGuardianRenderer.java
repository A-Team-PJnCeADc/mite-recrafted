package com.mite.recraft.client.renderer;

import com.mite.recraft.api.MiteIds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.resources.Identifier;

/** 长眠者守卫渲染器。骷髅模型配守卫贴图。 */
@Environment(EnvType.CLIENT)
public class LongdeadGuardianRenderer extends SkeletonRenderer {

    private static final Identifier TEXTURE = MiteIds.id("textures/entity/mob/longdead_guardian.png");

    public LongdeadGuardianRenderer(EntityRendererProvider.Context context) {
        super(context);
        addLayer(new MiteEyesLayer(this, MiteEyesLayer.glowTexture(TEXTURE)));
    }

    @Override
    public Identifier getTextureLocation(SkeletonRenderState state) {
        return TEXTURE;
    }
}
