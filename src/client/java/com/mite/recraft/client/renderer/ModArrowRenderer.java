package com.mite.recraft.client.renderer;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.entity.arrowentity.ArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;

public class ModArrowRenderer extends ArrowRenderer<ArrowEntity, ModArrowRenderer.State> {

    public ModArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Identifier getTextureLocation(State state) {
        return Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID,
                "textures/entity/arrows/" + state.material + ".png");
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(ArrowEntity entity, State state, float tickProgress) {
        super.extractRenderState(entity, state, tickProgress);
        state.material = entity.getMaterial();
    }

    public static class State extends ArrowRenderState {
        public String material = "flint";
    }
}
