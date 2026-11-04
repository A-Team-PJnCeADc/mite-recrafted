package com.mite.recraft.client.renderer;

import com.mite.recraft.api.MiteIds;
import com.mite.recraft.client.model.GhoulModel;
import com.mite.recraft.content.entity.mob.EarthElementalEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;

/** 土元素渲染器。模型与无形潜伏者同源，贴图按形态（石/黑曜石/地狱岩/末地石/黏土）与熔岩状态切换。 */
@Environment(EnvType.CLIENT)
public class EarthElementalRenderer
        extends HumanoidMobRenderer<EarthElementalEntity, EarthElementalRenderer.State, GhoulModel<EarthElementalRenderer.State>> {

    /** 渲染状态里带上本轮要用的贴图名。 */
    public static class State extends HumanoidRenderState {
        public String texture = "earth_elemental_stone";
    }

    private static final Identifier NORMAL_GLOW = MiteIds.id("textures/entity/mob/earth_elemental_glow.png");
    private static final Identifier MAGMA_GLOW = MiteIds.id("textures/entity/mob/earth_elemental_magma_glow.png");

    private final MiteEyesLayer eyesLayer;

    public EarthElementalRenderer(EntityRendererProvider.Context context) {
        super(context, new GhoulModel<State>(context.bakeLayer(GhoulModel.LAYER)), 0.5F);
        this.eyesLayer = new MiteEyesLayer(this, NORMAL_GLOW);
        addLayer(this.eyesLayer);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(EarthElementalEntity entity, State state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.texture = textureFor(entity.getElementalType(), entity.isMagma());
        eyesLayer.setTexture(entity.isMagma() ? MAGMA_GLOW : NORMAL_GLOW);
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return MiteIds.id("textures/entity/mob/" + state.texture + ".png");
    }

    /** 黏土形态用 hardened 后缀而不是 magma。 */
    public static String textureFor(int type, boolean magma) {
        return switch (type) {
            case EarthElementalEntity.TYPE_OBSIDIAN -> magma ? "earth_elemental_obsidian_magma" : "earth_elemental_obsidian";
            case EarthElementalEntity.TYPE_NETHERRACK -> magma ? "earth_elemental_netherrack_magma" : "earth_elemental_netherrack";
            case EarthElementalEntity.TYPE_END_STONE -> magma ? "earth_elemental_end_stone_magma" : "earth_elemental_end_stone";
            case EarthElementalEntity.TYPE_CLAY -> magma ? "earth_elemental_clay_hardened" : "earth_elemental_clay";
            default -> magma ? "earth_elemental_stone_magma" : "earth_elemental_stone";
        };
    }
}
