package com.mite.recraft.client.renderer;

import com.mite.recraft.api.MiteIds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.resources.Identifier;

/** 恐狼渲染器。沿用原版狼模型，按驯服 / 激怒 / 平常三种状态切换贴图。 */
@Environment(EnvType.CLIENT)
public class DireWolfRenderer extends WolfRenderer {

    private static final Identifier NEUTRAL = MiteIds.id("textures/entity/mob/dire_wolf_neutral.png");
    private static final Identifier ANGRY = MiteIds.id("textures/entity/mob/dire_wolf_angry.png");
    private static final Identifier TAME = MiteIds.id("textures/entity/mob/dire_wolf_tame.png");

    public DireWolfRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Identifier getTextureLocation(WolfRenderState state) {
        if (state.collarColor != null) {
            return TAME;
        }
        return state.isAngry ? ANGRY : NEUTRAL;
    }
}
