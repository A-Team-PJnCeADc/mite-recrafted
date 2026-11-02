package com.mite.recraft.content.item.food;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.api.MiteEvents;
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
 * MITE 营养系统 - 纯营养附件管理。
 *
 * <p>食物条由原版 FoodData 驱动（食品仅通过 FoodProperties 提供数值），
 * 此系统只管理 MITE 特有的蛋白质/植物营养素/必需脂肪酸/糖分/胰岛素抵抗。</p>
 */
public class NutritionSystem {


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


    /** 糖分 -> 胰岛素响应倍率（原版 MITE 常数）。 */
    public static final float SUGAR_CONTENT_TO_INSULIN_RESPONSE = 4.8F;

    /** 牛的最后挤奶时间（game time）。-1 = 从未挤过。 */
    public static final AttachmentType<Long> COW_LAST_MILKED = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "cow_last_milked"),
            builder -> builder
                    .initializer(() -> -1L)
                    .persistent(Codec.LONG)
    );

    /** 蛋白质 / 植物营养素 / 必需脂肪酸储量上限。 */
    public static final int MACRONUTRIENT_MAX = 160000;

    /** 糖分含量上限。 */
    public static final int SUGAR_MAX = 1000;

    /** 胰岛素抵抗上限。 */
    public static final int INSULIN_RESISTANCE_MAX = 192000;

    /** 营养值（次级储备）上限。 */
    public static final int NUTRITION_MAX = 20;

    /**
     * 营养项：附件类型 + 取值上限。子模组通过 api.MiteNutrition 以枚举读写。
     */
    public enum Nutrient {
        PROTEIN(NutritionSystem.PROTEIN, MACRONUTRIENT_MAX),
        PHYTONUTRIENTS(NutritionSystem.PHYTONUTRIENTS, MACRONUTRIENT_MAX),
        ESSENTIAL_FATS(NutritionSystem.ESSENTIAL_FATS, MACRONUTRIENT_MAX),
        SUGAR(NutritionSystem.SUGAR_CONTENT, SUGAR_MAX),
        INSULIN_RESISTANCE(NutritionSystem.INSULIN_RESISTANCE, INSULIN_RESISTANCE_MAX),
        NUTRITION(NutritionSystem.NUTRITION, NUTRITION_MAX);

        private final AttachmentType<Integer> attachment;
        private final int max;

        Nutrient(AttachmentType<Integer> attachment, int max) {
            this.attachment = attachment;
            this.max = max;
        }

        public AttachmentType<Integer> attachment() {
            return attachment;
        }

        public int max() {
            return max;
        }
    }


    public static void register() {}


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


    /** 按食物定义进食，并在写入后触发 {@link MiteEvents#NUTRITION_CHANGED}。 */
    public static void onEat(Player player, FoodType food) {
        onEat(player, food.nutrition(), food.protein(), food.phytonutrients(),
                food.essentialFats(), food.sugar());
        MiteEvents.NUTRITION_CHANGED.invoker().onNutritionChanged(player, food);
    }

    /** 写入原始营养数值，不触发事件。 */
    public static void onEat(Player player, int nutritionAmount, int protein, int phytonutrients,
                             int essentialFats, int sugar) {
        // MITE 次级储备
        int newNut = Math.clamp(getNutrition(player) + nutritionAmount, 0, NUTRITION_MAX);
        player.setAttached(NUTRITION, newNut);
        // 营养附件
        player.setAttached(PROTEIN, Math.clamp(getProtein(player) + protein, 0, MACRONUTRIENT_MAX));
        player.setAttached(PHYTONUTRIENTS, Math.clamp(getPhytonutrients(player) + phytonutrients, 0, MACRONUTRIENT_MAX));
        player.setAttached(ESSENTIAL_FATS, Math.clamp(getEssentialFats(player) + essentialFats, 0, MACRONUTRIENT_MAX));
        player.setAttached(SUGAR_CONTENT, Math.clamp(getSugarContent(player) + sugar, 0, SUGAR_MAX));
        player.setAttached(INSULIN_RESISTANCE,
                Math.clamp(getInsulinResistance(player) + (int) (sugar * SUGAR_CONTENT_TO_INSULIN_RESPONSE), 0, INSULIN_RESISTANCE_MAX));
    }


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
