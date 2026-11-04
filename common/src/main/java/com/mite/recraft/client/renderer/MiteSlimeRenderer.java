package com.mite.recraft.client.renderer;

import com.mite.recraft.api.MiteIds;
import com.mite.recraft.registry.ModEntityTypes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.cubemob.Slime;

import java.util.List;

/** 胶质生物渲染器。沿用原版史莱姆模型，贴图按实体 id 推导。 */
@Environment(EnvType.CLIENT)
public class MiteSlimeRenderer extends SlimeRenderer {

    private final Identifier texture;

    public MiteSlimeRenderer(EntityRendererProvider.Context context, Identifier texture) {
        super(context);
        this.texture = texture;
    }

    @Override
    public Identifier getTextureLocation(SlimeRenderState state) {
        return texture;
    }

    /** 贴图按实体 id 推导：{@code mite_recrafted:textures/entity/mob/<实体 id>.png}。 */
    public static Identifier texturePath(EntityType<?> type) {
        return MiteIds.id("textures/entity/mob/" + BuiltInRegistries.ENTITY_TYPE.getKey(type).getPath() + ".png");
    }

    public static EntityRendererProvider<Slime> provider(EntityType<?> type) {
        Identifier texture = texturePath(type);
        return context -> new MiteSlimeRenderer(context, texture);
    }

    /** 需要登记的胶质生物。 */
    public static final List<EntityType<? extends Slime>> REGISTERED = List.of(
            ModEntityTypes.BLOB,
            ModEntityTypes.OOZE,
            ModEntityTypes.JELLY,
            ModEntityTypes.PUDDING
    );
}
