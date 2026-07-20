package com.mite.recraft.item.moditems.food;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.network.NutritionSyncPayload;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * MITE 营养系统 — 纯营养附件管理。
 *
 * <p>食物条由原版 FoodData 驱动（食品仅通过 FoodProperties 提供数值），
 * 此系统只管理 MITE 特有的蛋白质/植物营养素/必需脂肪酸/糖分/胰岛素抵抗。</p>
 */
//todo 初始饱食度3个
public class NutritionSystem {

    // ==================== Attachment Types ====================

    /** 蛋白质储量 (0~160000)，每 tick -1。 */
    public static final AttachmentType<Integer> PROTEIN = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "nutrition_protein"),
            builder -> builder
                    .initializer(() -> 160000)
                    .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all())
                    .persistent(Codec.INT)
                    .copyOnDeath()
    );

    /** 植物营养素储量 (0~160000)，每 tick -1。 */
    public static final AttachmentType<Integer> PHYTONUTRIENTS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "nutrition_phytonutrients"),
            builder -> builder
                    .initializer(() -> 160000)
                    .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all())
                    .persistent(Codec.INT)
                    .copyOnDeath()
    );

    /** 必需脂肪酸储量 (0~160000)，每 tick -1。 */
    public static final AttachmentType<Integer> ESSENTIAL_FATS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "nutrition_essential_fats"),
            builder -> builder
                    .initializer(() -> 160000)
                    .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all())
                    .persistent(Codec.INT)
                    .copyOnDeath()
    );

    /** 糖分含量 (0~1000)。 */
    public static final AttachmentType<Integer> SUGAR_CONTENT = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "nutrition_sugar"),
            builder -> builder
                    .initializer(() -> 0)
                    .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all())
                    .persistent(Codec.INT)
                    .copyOnDeath()
    );

    /** 胰岛素抵抗 (0~192000)，每 tick -1。 */
    public static final AttachmentType<Integer> INSULIN_RESISTANCE = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "nutrition_insulin"),
            builder -> builder
                    .initializer(() -> 0)
                    .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all())
                    .persistent(Codec.INT)
                    .copyOnDeath()
    );

    /** 营养值（次级储备）(0~20)，MITE: 食物条耗尽后消耗，影响回血速度。 */
    public static final AttachmentType<Integer> NUTRITION = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "nutrition_nutrition"),
            builder -> builder
                    .initializer(() -> 6)
                    .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all())
                    .persistent(Codec.INT)
                    .copyOnDeath()
    );

    /** MITE 回血进度 (0.0~1.0)，累积满 1.0 回 1 HP。 */
    public static final AttachmentType<Float> HEAL_PROGRESS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "nutrition_heal_progress"),
            builder -> builder
                    .initializer(() -> 0.0F)
                    .persistent(Codec.FLOAT)
    );

    // ==================== 常量 ====================

    /** 糖分 → 胰岛素响应倍率（原版 MITE 常数）。 */
    public static final float SUGAR_CONTENT_TO_INSULIN_RESPONSE = 4.8F;

    /** 牛的最后挤奶时间（game time）。-1 = 从未挤过。 */
    public static final AttachmentType<Long> COW_LAST_MILKED = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "cow_last_milked"),
            builder -> builder
                    .initializer(() -> -1L)
                    .persistent(Codec.LONG)
    );

    // ==================== 注册 ====================

    public static void ensureLoaded() {}

    // ==================== 读取 ====================

    public static int getProtein(Player player) {
        return player.getAttachedOrCreate(PROTEIN);
    }

    public static int getPhytonutrients(Player player) {
        return player.getAttachedOrCreate(PHYTONUTRIENTS);
    }

    public static int getEssentialFats(Player player) {
        return player.getAttachedOrCreate(ESSENTIAL_FATS);
    }

    public static int getSugarContent(Player player) {
        return player.getAttachedOrCreate(SUGAR_CONTENT);
    }

    public static int getNutrition(Player player) {
        return player.getAttachedOrCreate(NUTRITION);
    }

    public static int getInsulinResistance(Player player) {
        return player.getAttachedOrCreate(INSULIN_RESISTANCE);
    }

    public static boolean isMalnourished(Player player) {
        return getProtein(player) == 0 || getPhytonutrients(player) == 0;
    }

    // ==================== 吃食物时写入 ====================

    public static void onEat(Player player, int nutritionAmount, int protein, int phytonutrients,
                             int essentialFats, int sugar) {
        // MITE 次级储备
        int newNut = Math.clamp(getNutrition(player) + nutritionAmount, 0, 20);
        player.setAttached(NUTRITION, newNut);
        // 营养附件
        player.setAttached(PROTEIN, Math.clamp(getProtein(player) + protein, 0, 160000));
        player.setAttached(PHYTONUTRIENTS, Math.clamp(getPhytonutrients(player) + phytonutrients, 0, 160000));
        player.setAttached(ESSENTIAL_FATS, Math.clamp(getEssentialFats(player) + essentialFats, 0, 160000));
        player.setAttached(SUGAR_CONTENT, Math.clamp(getSugarContent(player) + sugar, 0, 1000));
        player.setAttached(INSULIN_RESISTANCE,
                Math.clamp(getInsulinResistance(player) + (int)(sugar * SUGAR_CONTENT_TO_INSULIN_RESPONSE), 0, 192000));
    }

    // ==================== Tick（服务端） ====================

    /** 每 tick 调用一次，递减营养素并同步到客户端（每秒同步一次） */
    public static void tick(ServerPlayer player) {
        if (player.isCreative()) return;

        // 递减三大营养素 + 胰岛素
        decrement(player, PROTEIN);
        decrement(player, PHYTONUTRIENTS);
        decrement(player, ESSENTIAL_FATS);
        decrement(player, INSULIN_RESISTANCE);
    }

    private static void decrement(ServerPlayer player, AttachmentType<Integer> type) {
        int val = player.getAttachedOrCreate(type);
        if (val > 0) {
            player.setAttached(type, val - 1);
        }
    }

    // ==================== Sync ====================

    public static void sendSyncPacket(ServerPlayer player) {
        ServerPlayNetworking.send(player, new NutritionSyncPayload(
                getNutrition(player),
                getProtein(player),
                getPhytonutrients(player),
                getEssentialFats(player),
                getSugarContent(player),
                getInsulinResistance(player),
                player.experienceLevel
        ));
    }
}
