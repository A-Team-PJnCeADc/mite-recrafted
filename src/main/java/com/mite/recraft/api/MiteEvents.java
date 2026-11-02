package com.mite.recraft.api;

import com.mite.recraft.content.item.food.FoodType;
import com.mite.recraft.content.item.quality.Quality;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * 子模组事件。事件由本模组内部在对应时机触发，外部只注册监听器。
 */
public final class MiteEvents {

    private MiteEvents() {
    }

    /** 玩家进食并写入营养值之后触发。 */
    @FunctionalInterface
    public interface NutritionChanged {
        void onNutritionChanged(Player player, FoodType food);
    }

    public static final Event<NutritionChanged> NUTRITION_CHANGED = EventFactory.createArrayBacked(
            NutritionChanged.class,
            listeners -> (player, food) -> {
                for (NutritionChanged listener : listeners) {
                    listener.onNutritionChanged(player, food);
                }
            }
    );

    /** 从 MITE 工作台取出成品之后触发，xpCost 为本次实际扣除的经验。 */
    @FunctionalInterface
    public interface CraftCompleted {
        void onCraftCompleted(ServerPlayer player, ItemStack result, Quality quality, int xpCost);
    }

    public static final Event<CraftCompleted> CRAFT_COMPLETED = EventFactory.createArrayBacked(
            CraftCompleted.class,
            listeners -> (player, result, quality, xpCost) -> {
                for (CraftCompleted listener : listeners) {
                    listener.onCraftCompleted(player, result, quality, xpCost);
                }
            }
    );
}
