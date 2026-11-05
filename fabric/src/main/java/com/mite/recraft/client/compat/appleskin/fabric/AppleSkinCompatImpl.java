package com.mite.recraft.client.compat.appleskin.fabric;

import com.mite.recraft.client.compat.appleskin.AppleSkinCompat;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.food.FoodProperties;
import squeek.appleskin.api.event.FoodValuesEvent;

/**
 * Fabric 侧 AppleSkin 注册。Fabric 版的 {@code FoodValuesEvent} 自带
 * {@code EVENT}（Fabric API 事件），字段为 {@code defaultFoodComponent} / {@code modifiedFoodComponent}。
 */
public final class AppleSkinCompatImpl {

    private AppleSkinCompatImpl() {
    }

    public static void init() {
        if (!FabricLoader.getInstance().isModLoaded("appleskin")) {
            return;
        }
        register();
    }

    /** 只在 AppleSkin 存在时调用，避免未安装时触发类加载。 */
    private static void register() {
        FoodValuesEvent.EVENT.register(event -> {
            FoodProperties preview = AppleSkinCompat.preview(event.itemStack, event.player);
            if (preview != null) {
                event.defaultFoodComponent = preview;
                event.modifiedFoodComponent = preview;
            }
        });
    }
}
