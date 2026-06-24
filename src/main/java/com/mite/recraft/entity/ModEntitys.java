package com.mite.recraft.entity;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.entity.arrowentity.ArrowEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntitys {

    // 箭矢实体
    public static final EntityType<ArrowEntity> MITE_ARROW = register(
            "mite_arrow",
            EntityType.Builder.<ArrowEntity>of(ArrowEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(4)
                    .updateInterval(20)
    );

    //注册实体
    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(
                Registries.ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, name)
        );
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void init() {
    }
}
