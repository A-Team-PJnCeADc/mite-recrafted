package com.mite.recraft.client.renderer;

import com.mite.recraft.api.MiteIds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.resources.Identifier;

/** 长眠者渲染器。骷髅模型（细手臂）配 MITE 贴图。 */
@Environment(EnvType.CLIENT)
public class LongdeadRenderer extends SkeletonRenderer {

    private static final Identifier TEXTURE = MiteIds.id("textures/entity/mob/longdead.png");

    public LongdeadRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Identifier getTextureLocation(SkeletonRenderState state) {
        return TEXTURE;
    }
}
