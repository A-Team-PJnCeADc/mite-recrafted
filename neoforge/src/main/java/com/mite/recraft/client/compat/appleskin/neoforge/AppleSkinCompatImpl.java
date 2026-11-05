package com.mite.recraft.client.compat.appleskin.neoforge;

import com.mite.recraft.client.compat.appleskin.AppleSkinCompat;

import net.minecraft.world.food.FoodProperties;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import squeek.appleskin.api.event.FoodValuesEvent;

/**
 * NeoForge 侧 AppleSkin 注册。NeoForge 版的 {@code FoodValuesEvent} 继承
 * {@code net.neoforged.bus.api.Event}，挂到事件总线；字段为
 * {@code defaultFoodProperties} / {@code modifiedFoodProperties}。
 */
public final class AppleSkinCompatImpl {

    private AppleSkinCompatImpl() {
    }

    public static void init() {
        if (!ModList.get().isLoaded("appleskin")) {
            return;
        }
        register();
    }

    /** 只在 AppleSkin 存在时调用，避免未安装时触发类加载。 */
    private static void register() {
        NeoForge.EVENT_BUS.addListener((FoodValuesEvent event) -> {
            FoodProperties preview = AppleSkinCompat.preview(event.itemStack, event.player);
            if (preview != null) {
                event.defaultFoodProperties = preview;
                event.modifiedFoodProperties = preview;
            }
        });
    }
}
