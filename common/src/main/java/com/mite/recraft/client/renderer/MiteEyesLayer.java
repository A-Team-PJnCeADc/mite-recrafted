package com.mite.recraft.client.renderer;

import com.mite.recraft.api.MiteIds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

/**
 * MITE 的发光层。对应 RendererLivingEntity.renderModelGlowing：用全亮渲染类型再画一遍主模型，
 * 发光贴图里的不透明像素因此不受环境光影响。
 */
@Environment(EnvType.CLIENT)
@SuppressWarnings({"rawtypes", "unchecked"})
public class MiteEyesLayer extends EyesLayer {

    private RenderType renderType;

    public MiteEyesLayer(RenderLayerParent parent, Identifier texture) {
        super(parent);
        this.renderType = RenderTypes.eyes(texture);
    }

    @Override
    public RenderType renderType() {
        return renderType;
    }

    /** 切换发光贴图，用于形态会变的生物（例如土元素的熔岩形态）。 */
    public void setTexture(Identifier texture) {
        this.renderType = RenderTypes.eyes(texture);
    }

    /** 由主贴图推出同目录的 {@code <name>_glow.png}。 */
    public static Identifier glowTexture(Identifier base) {
        String path = base.getPath();
        String glowPath = path.endsWith(".png")
                ? path.substring(0, path.length() - 4) + "_glow.png"
                : path + "_glow.png";
        return MiteIds.id(glowPath);
    }
}
