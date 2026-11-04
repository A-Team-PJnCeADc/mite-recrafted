package com.mite.recraft.client.renderer;

import com.mite.recraft.api.MiteIds;
import com.mite.recraft.registry.ModEntityTypes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SilverfishRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Silverfish;

import java.util.List;

/** 蠹虫系渲染器。沿用原版蠹虫模型，按实体 id 推导贴图。 */
@Environment(EnvType.CLIENT)
public class MiteSilverfishRenderer extends SilverfishRenderer {

    private final Identifier texture;

    public MiteSilverfishRenderer(EntityRendererProvider.Context context, Identifier texture) {
        super(context);
        this.texture = texture;
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return texture;
    }

    /** 贴图按实体 id 推导：{@code mite_recrafted:textures/entity/mob/<实体 id>.png}。 */
    public static Identifier texturePath(EntityType<?> type) {
        return MiteIds.id("textures/entity/mob/" + BuiltInRegistries.ENTITY_TYPE.getKey(type).getPath() + ".png");
    }

    /** 渲染器工厂：模型固定为原版蠹虫模型，贴图由实体 id 推导。 */
    public static EntityRendererProvider<Silverfish> provider(EntityType<?> type) {
        Identifier texture = texturePath(type);
        return context -> new MiteSilverfishRenderer(context, texture);
    }

    /** 需要登记的蠹虫系生物。 */
    public static final List<EntityType<? extends Silverfish>> REGISTERED = List.of(
            ModEntityTypes.COPPERSPINE,
            ModEntityTypes.HOARY_SILVERFISH,
            ModEntityTypes.NETHERSPAWN
    );
}
