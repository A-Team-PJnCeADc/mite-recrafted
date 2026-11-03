package com.mite.recraft.registry;

import com.mite.recraft.content.item.quality.Quality;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import static com.mite.recraft.MiteRecrafted.MOD_ID;

public class ModDataComponents {

    /** 声明表：字段初始化只构造对象，写入注册表在 register() 中完成。 */
    private static final java.util.Map<String, DataComponentType<?>> DECLARED =
            new java.util.LinkedHashMap<>();
    public static final DataComponentType<Quality> QUALITY = declare("quality",
            DataComponentType.<Quality>builder()
                    .persistent(Quality.CODEC)
                    .build()
    );

    /** 箭矢回收概率 (0.0 ~ 1.0) */
    public static final DataComponentType<Float> RECOVERY_CHANCE = declare("recovery_chance",
            DataComponentType.<Float>builder()
                    .persistent(Codec.floatRange(0.0f, 1.0f))
                    .build()
    );

    /** 箭矢基础伤害 */
    public static final DataComponentType<Float> BASE_DAMAGE = declare("base_damage",
            DataComponentType.<Float>builder()
                    .persistent(Codec.FLOAT)
                    .build()
    );

    /** 工具部件数（numComponents），用于砧修理计算 */
    public static final DataComponentType<Integer> TOOL_COMPONENTS = declare("tool_components",
            DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .build()
    );

    public static final DataComponentType<Float> TOOL_MATERIAL_TIER = declare("tool_material_tier",
            DataComponentType.<Float>builder()
                    .persistent(Codec.FLOAT)
                    .build()
    );

    /** 工具修理标签路径（如 repairs_copper），用于砧材料匹配 */
    public static final DataComponentType<String> TOOL_REPAIR_TAG = declare("tool_repair_tag",
            DataComponentType.<String>builder()
                    .persistent(Codec.STRING)
                    .build()
    );

    /** 基础制作难度 = material.durability × 100.0 × numComponents，用于品质调整和 XP 消耗计算 */
    public static final DataComponentType<Float> CRAFTING_DIFFICULTY = declare("crafting_difficulty",
            DataComponentType.<Float>builder()
                    .persistent(Codec.FLOAT)
                    .build()
    );


    private static <T> DataComponentType<T> declare(String path, DataComponentType<T> type) {
        DECLARED.put(path, type);
        return type;
    }

    /** 数据组件类型阶段。 */
    public static void register() {
        for (java.util.Map.Entry<String, DataComponentType<?>> entry : DECLARED.entrySet()) {
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                    Identifier.fromNamespaceAndPath(MOD_ID, entry.getKey()), entry.getValue());
        }
    }
}
