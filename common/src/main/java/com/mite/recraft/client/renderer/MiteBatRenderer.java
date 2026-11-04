package com.mite.recraft.client.renderer;

import com.mite.recraft.api.MiteIds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.renderer.entity.BatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.BatRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ambient.Bat;

/** 蝙蝠系渲染器。沿用原版蝙蝠模型，贴图由调用方给出。 */
@Environment(EnvType.CLIENT)
public class MiteBatRenderer extends BatRenderer {

    private final Identifier texture;

    public MiteBatRenderer(EntityRendererProvider.Context context, String texturePath) {
        super(context);
        this.texture = MiteIds.id(texturePath);
        addLayer(new MiteEyesLayer(this, MiteEyesLayer.glowTexture(this.texture)));
    }

    @Override
    public Identifier getTextureLocation(BatRenderState state) {
        return texture;
    }

    public static EntityRendererProvider<Bat> provider(String texturePath) {
        return context -> new MiteBatRenderer(context, texturePath);
    }
}
