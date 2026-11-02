package com.mite.recraft.api;

import com.mite.recraft.content.item.food.FoodType;
import com.mite.recraft.content.item.food.NutritionSystem;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * 营养数值读写。所有数值以整数储量表示，取值范围见 {@link NutritionSystem.Nutrient}。
 */
public final class MiteNutrition {

    private MiteNutrition() {
    }

    public static int get(Player player, NutritionSystem.Nutrient nutrient) {
        return player.getAttachedOrCreate(nutrient.attachment());
    }

    /** 写入并把数值夹取到该营养项的上限。 */
    public static void set(Player player, NutritionSystem.Nutrient nutrient, int value) {
        player.setAttached(nutrient.attachment(), Math.clamp(value, 0, nutrient.max()));
    }

    /** 立即同步到客户端，仅服务端调用有效。 */
    public static void sync(ServerPlayer player) {
        NutritionSystem.sendSyncPacket(player);
    }

    /** 按食物定义写入全部营养项，语义与食用该食物一致。 */
    public static void feed(Player player, FoodType food) {
        NutritionSystem.onEat(player, food);
    }

    /** 蛋白质或植物营养素耗尽。 */
    public static boolean isMalnourished(Player player) {
        return NutritionSystem.isMalnourished(player);
    }
}
