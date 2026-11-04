package com.mite.recraft.client.renderer;

import com.mite.recraft.api.MiteIds;
import com.mite.recraft.registry.ModEntityTypes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.spider.Spider;

import java.util.List;

/**
 * 蛛形生物渲染器。沿用原版蜘蛛模型（层定义本身就是 64x32），只替换贴图。
 */
@Environment(EnvType.CLIENT)
public class MiteSpiderRenderer<T extends Spider> extends SpiderRenderer<T> {

    private final Identifier texture;

    public MiteSpiderRenderer(EntityRendererProvider.Context context, Identifier texture) {
        super(context);
        this.texture = texture;
        addLayer(new MiteEyesLayer(this, MiteEyesLayer.glowTexture(texture)));
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return texture;
    }

    /** 贴图按实体 id 推导：{@code mite_recrafted:textures/entity/mob/<实体 id>.png}。 */
    public static Identifier texturePath(EntityType<?> type) {
        return MiteIds.id("textures/entity/mob/" + BuiltInRegistries.ENTITY_TYPE.getKey(type).getPath() + ".png");
    }

    /** 渲染器工厂：模型固定为原版蜘蛛模型，贴图由实体 id 推导。 */
    public static <T extends Spider> EntityRendererProvider<T> provider(EntityType<?> type) {
        Identifier texture = texturePath(type);
        return context -> new MiteSpiderRenderer<>(context, texture);
    }

    /** 需要登记的蛛形生物。贴图按实体 id 推导，两端平台按这张表登记。 */
    public static final List<EntityType<? extends Spider>> REGISTERED = List.of(
            ModEntityTypes.WOOD_SPIDER,
            ModEntityTypes.BLACK_WIDOW,
            ModEntityTypes.PHASE_SPIDER,
            ModEntityTypes.DEMON_SPIDER,
            ModEntityTypes.CAVE_SPIDER
    );
}
