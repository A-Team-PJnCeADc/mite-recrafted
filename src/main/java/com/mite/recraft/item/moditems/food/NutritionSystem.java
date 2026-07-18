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
 * MITE 营养系统 — 基于 Fabric Data Attachment API。
 *
 * <p>计算全在服务端，通过 {@link NutritionSyncPayload} 同步到客户端。</p>
 */
public class NutritionSystem {

    // ==================== Attachment Types ====================

    /** 饱腹度 (0~20)，吃食物时先消耗此值 */
    public static final AttachmentType<Integer> SATIATION = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "nutrition_satiation"),
            builder -> builder
                    .initializer(() -> getNutrientLimit(0))
                    .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all())
                    .persistent(Codec.INT)
                    .copyOnDeath()
    );

    /** 营养值 (0~20)，饱腹度耗尽后消耗此值，影响回血速度 */
    public static final AttachmentType<Integer> NUTRITION = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "nutrition_nutrition"),
            builder -> builder
                    .initializer(() -> getNutrientLimit(0))
                    .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all())
                    .persistent(Codec.INT)
                    .copyOnDeath()
    );

    /** 蛋白质储量 (0~160000)，每 tick -1，消耗型营养 */
    public static final AttachmentType<Integer> PROTEIN = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "nutrition_protein"),
            builder -> builder
                    .initializer(() -> 160000)
                    .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all())
                    .persistent(Codec.INT)
                    .copyOnDeath()
    );

    /** 植物营养素储量 (0~160000)，每 tick -1 */
    public static final AttachmentType<Integer> PHYTONUTRIENTS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "nutrition_phytonutrients"),
            builder -> builder
                    .initializer(() -> 160000)
                    .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all())
                    .persistent(Codec.INT)
                    .copyOnDeath()
    );

    /** 必需脂肪酸储量 (0~160000)，每 tick -1 */
    public static final AttachmentType<Integer> ESSENTIAL_FATS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "nutrition_essential_fats"),
            builder -> builder
                    .initializer(() -> 160000)
                    .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all())
                    .persistent(Codec.INT)
                    .copyOnDeath()
    );

    /** 糖分含量 (0~1000)，食物中糖分的累计值，影响胰岛素反应 */
    public static final AttachmentType<Integer> SUGAR_CONTENT = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "nutrition_sugar"),
            builder -> builder
                    .initializer(() -> 0)
                    .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all())
                    .persistent(Codec.INT)
                    .copyOnDeath()
    );

    /** 胰岛素抵抗 (0~192000)，吃糖分食物累加，每 tick -1。越高中毒越严重 */
    public static final AttachmentType<Integer> INSULIN_RESISTANCE = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "nutrition_insulin"),
            builder -> builder
                    .initializer(() -> 0)
                    .syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all())
                    .persistent(Codec.INT)
                    .copyOnDeath()
    );

    // ==================== 上限计算 ====================

    /** 显式触类加载以注册 AttachmentType */
    public static void ensureLoaded() {}

    /** 营养/饱腹度上限 = max(6, min(6 + 等级/5*2, 20)) */
    public static int getNutrientLimit(int experienceLevel) {
        return Math.max(6, Math.min(6 + experienceLevel / 5 * 2, 20));
    }

    // ==================== 读取 ====================

    public static int getSatiation(Player player) {
        return player.getAttachedOrCreate(SATIATION);
    }

    public static int getNutrition(Player player) {
        return player.getAttachedOrCreate(NUTRITION);
    }

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

    public static int getInsulinResistance(Player player) {
        return player.getAttachedOrCreate(INSULIN_RESISTANCE);
    }

    /** 营养不良：protein == 0 || phytonutrients == 0 */
    public static boolean isMalnourished(Player player) {
        return getProtein(player) == 0 || getPhytonutrients(player) == 0;
    }

    /** 双重营养不良：protein == 0 && phytonutrients == 0 */
    public static boolean isDoubleMalnourished(Player player) {
        return getProtein(player) == 0 && getPhytonutrients(player) == 0;
    }

    // ==================== 写入 ====================

    public static void setSatiation(Player player, int value) {
        int level = player instanceof ServerPlayer sp ? sp.experienceLevel : player.experienceLevel;
        player.setAttached(SATIATION, Math.clamp(value, 0, getNutrientLimit(level)));
    }

    public static void addSatiation(Player player, int amount) {
        setSatiation(player, getSatiation(player) + amount);
    }

    public static void setNutrition(Player player, int value) {
        int level = player instanceof ServerPlayer sp ? sp.experienceLevel : player.experienceLevel;
        player.setAttached(NUTRITION, Math.clamp(value, 0, getNutrientLimit(level)));
    }

    public static void addNutrition(Player player, int amount) {
        setNutrition(player, getNutrition(player) + amount);
    }

    public static void setProtein(Player player, int value) {
        player.setAttached(PROTEIN, Math.clamp(value, 0, 160000));
    }

    public static void addProtein(Player player, int amount) {
        setProtein(player, getProtein(player) + amount);
    }

    public static void setPhytonutrients(Player player, int value) {
        player.setAttached(PHYTONUTRIENTS, Math.clamp(value, 0, 160000));
    }

    public static void addPhytonutrients(Player player, int amount) {
        setPhytonutrients(player, getPhytonutrients(player) + amount);
    }

    public static void setEssentialFats(Player player, int value) {
        player.setAttached(ESSENTIAL_FATS, Math.clamp(value, 0, 160000));
    }

    public static void addEssentialFats(Player player, int amount) {
        setEssentialFats(player, getEssentialFats(player) + amount);
    }

    /** 胰岛素反应 IR = sugar × 4.8 */
    public static int getInsulinResponse(Player player) {
        return (int)(getSugarContent(player) * 4.8F);
    }

    public static void setSugarContent(Player player, int value) {
        player.setAttached(SUGAR_CONTENT, Math.clamp(value, 0, 1000));
    }

    public static void addSugarContent(Player player, int amount) {
        setSugarContent(player, getSugarContent(player) + amount);
    }

    public static void setInsulinResistance(Player player, int value) {
        player.setAttached(INSULIN_RESISTANCE, Math.clamp(value, 0, 192000));
    }

    public static void addInsulinResistance(Player player, int amount) {
        setInsulinResistance(player, getInsulinResistance(player) + amount);
    }

    // ==================== Tick (服务端) ====================

    /** 每 tick 调用一次，递减营养素并同步到客户端（每秒同步一次） */
    public static void tick(ServerPlayer player) {
        if (player.isCreative()) return;

        // 递减三大营养素
        if (getProtein(player) > 0) {
            addProtein(player, -1);
        }
        if (getPhytonutrients(player) > 0) {
            addPhytonutrients(player, -1);
        }
        if (getEssentialFats(player) > 0) {
            addEssentialFats(player, -1);
        }

        // 递减胰岛素抵抗
        if (getInsulinResistance(player) > 0) {
            addInsulinResistance(player, -1);
        }

        // 每秒消耗饥饿 + 同步
        if (player.tickCount % 20 == 0) {
            var foodData = player.getFoodData();
            if (foodData.getFoodLevel() < 20) {
                if (getSatiation(player) > 0) {
                    addSatiation(player, -1);
                } else if (getNutrition(player) > 0) {
                    addNutrition(player, -1);
                }
            }
            sendSyncPacket(player);
        }
    }

    // ==================== Sync ====================

    /** 发送营养数据同步包到客户端 */
    public static void sendSyncPacket(ServerPlayer player) {
        ServerPlayNetworking.send(player, new NutritionSyncPayload(
                getSatiation(player),
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
