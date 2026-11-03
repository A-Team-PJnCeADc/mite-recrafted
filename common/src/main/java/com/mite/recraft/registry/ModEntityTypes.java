package com.mite.recraft.registry;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.content.entity.ArrowEntity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

/**
 * 实体类型。字段初始化只构造对象，写入注册表在 {@link #register()} 中完成。
 */
public final class ModEntityTypes {

    private static final ResourceKey<EntityType<?>> MITE_ARROW_KEY = ResourceKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "mite_arrow"));

    public static final EntityType<ArrowEntity> MITE_ARROW = EntityType.Builder
            .<ArrowEntity>of(ArrowEntity::new, MobCategory.MISC)
            .sized(0.5f, 0.5f)
            .clientTrackingRange(4)
            .updateInterval(20)
            .build(MITE_ARROW_KEY);

    private ModEntityTypes() {
    }

    /** 实体类型阶段。 */
    public static void register() {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, MITE_ARROW_KEY, MITE_ARROW);
    }
}
