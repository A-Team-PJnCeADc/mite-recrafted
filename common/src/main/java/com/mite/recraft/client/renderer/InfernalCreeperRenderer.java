package com.mite.recraft.client.renderer;

import com.mite.recraft.api.MiteIds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.CreeperRenderState;
import net.minecraft.resources.Identifier;

/** 炼狱苦力怕渲染器。原版苦力怕模型配炼狱贴图。 */
@Environment(EnvType.CLIENT)
public class InfernalCreeperRenderer extends CreeperRenderer {

    private static final Identifier TEXTURE = MiteIds.id("textures/entity/mob/infernal_creeper.png");

    public InfernalCreeperRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Identifier getTextureLocation(CreeperRenderState state) {
        return TEXTURE;
    }
}
