package com.mite.recraft.client.renderer;

import com.mite.recraft.api.MiteIds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;

/** 亡魂渲染器，僵尸模型配 MITE 贴图。 */
@Environment(EnvType.CLIENT)
public class RevenantRenderer extends ZombieRenderer {

    private static final Identifier TEXTURE = MiteIds.id("textures/entity/mob/revenant.png");

    public RevenantRenderer(EntityRendererProvider.Context context) {
        super(context);
        addLayer(new MiteEyesLayer(this, MiteEyesLayer.glowTexture(TEXTURE)));
    }

    @Override
    public Identifier getTextureLocation(ZombieRenderState state) {
        return TEXTURE;
    }
}
