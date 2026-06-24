package com.mite.recraft.component;

import com.mite.recraft.item.quality.Quality;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import static com.mite.recraft.MiteRecrafted.MOD_ID;

public class ModDataComponents {
    public static final DataComponentType<Quality> QUALITY = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(MOD_ID, "quality"),
            DataComponentType.<Quality>builder()
                    .persistent(Quality.CODEC)
                    .build()
    );

    /** 箭矢回收概率 (0.0 ~ 1.0) */
    public static final DataComponentType<Float> RECOVERY_CHANCE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(MOD_ID, "recovery_chance"),
            DataComponentType.<Float>builder()
                    .persistent(Codec.floatRange(0.0f, 1.0f))
                    .build()
    );

    /** 箭矢基础伤害 */
    public static final DataComponentType<Float> BASE_DAMAGE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(MOD_ID, "base_damage"),
            DataComponentType.<Float>builder()
                    .persistent(Codec.FLOAT)
                    .build()
    );

    public static void initialize() {
    }
}
