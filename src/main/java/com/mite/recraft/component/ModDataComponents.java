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

    /** 工具部件数（numComponents），用于砧修理计算 */
    public static final DataComponentType<Integer> TOOL_COMPONENTS = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(MOD_ID, "tool_components"),
            DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .build()
    );

    /** 工具材料等级（durabilityCoefficient），用于砧修理等级检查 */
    public static final DataComponentType<Float> TOOL_MATERIAL_TIER = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(MOD_ID, "tool_material_tier"),
            DataComponentType.<Float>builder()
                    .persistent(Codec.FLOAT)
                    .build()
    );

    /** 工具修理标签路径（如 repairs_copper），用于砧材料匹配 */
    public static final DataComponentType<String> TOOL_REPAIR_TAG = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(MOD_ID, "tool_repair_tag"),
            DataComponentType.<String>builder()
                    .persistent(Codec.STRING)
                    .build()
    );

    public static void init() {}
}
