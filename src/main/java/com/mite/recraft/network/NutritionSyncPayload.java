package com.mite.recraft.network;

import com.mite.recraft.MiteRecrafted;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * S→C: 同步营养数据（蛋白/植营/脂肪/糖/胰岛素）。
 */
public record NutritionSyncPayload(
        int nutrition,
        int protein,
        int phytonutrients,
        int essentialFats,
        int sugarContent,
        int insulinResistance,
        int experienceLevel
) implements CustomPacketPayload {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "nutrition_sync");

    public static final CustomPacketPayload.Type<NutritionSyncPayload> TYPE =
            new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, NutritionSyncPayload> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, NutritionSyncPayload::nutrition,
                    ByteBufCodecs.VAR_INT, NutritionSyncPayload::protein,
                    ByteBufCodecs.VAR_INT, NutritionSyncPayload::phytonutrients,
                    ByteBufCodecs.VAR_INT, NutritionSyncPayload::essentialFats,
                    ByteBufCodecs.VAR_INT, NutritionSyncPayload::sugarContent,
                    ByteBufCodecs.VAR_INT, NutritionSyncPayload::insulinResistance,
                    ByteBufCodecs.VAR_INT, NutritionSyncPayload::experienceLevel,
                    NutritionSyncPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
