package com.mite.recraft.component;

import com.mite.recraft.item.quality.Quality;
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

    public static void initialize() {
    }
}
