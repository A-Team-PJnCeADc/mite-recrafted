package com.mite.recraft.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 跨类共享的卡箭注册表 —— 不与任何目标类绑定的纯工具类。
 * ArrowEntity 存，LivingEntityStuckArrowsMixin 取。
 */
public final class StuckArrowTracker {

    private static final Map<UUID, List<ItemStack>> STUCK_ARROWS = new ConcurrentHashMap<>();

    private StuckArrowTracker() {}

    public static void add(LivingEntity entity, ItemStack arrow) {
        STUCK_ARROWS
                .computeIfAbsent(entity.getUUID(), k -> new ArrayList<>())
                .add(arrow.copy());
    }

    public static List<ItemStack> remove(LivingEntity entity) {
        return STUCK_ARROWS.remove(entity.getUUID());
    }
}
