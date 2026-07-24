package com.mite.recraft.network;

import com.mite.recraft.MiteRecrafted;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * S→C: 同步合成进度（周期 + 当前 tick + 工作台材料系数 + 品质 + XP消耗）。
 *
 * qualityIndex: 当前品质 ordinal (-1 表示无品质物品)
 * xpCost:      当前品质所需 XP 消耗 (0 表示免费)
 */
public record CraftingProgressSyncPayload(
        int period, int ticks, float benchCoefficient,
        int qualityIndex, int xpCost
) implements CustomPacketPayload {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "crafting_progress");

    public static final CustomPacketPayload.Type<CraftingProgressSyncPayload> TYPE =
            new CustomPacketPayload.Type<>(ID);

    /** 无品质信息时 qualityIndex=-1, xpCost=0 */
    public CraftingProgressSyncPayload(int period, int ticks, float benchCoefficient) {
        this(period, ticks, benchCoefficient, -1, 0);
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, CraftingProgressSyncPayload> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, CraftingProgressSyncPayload::period,
                    ByteBufCodecs.VAR_INT, CraftingProgressSyncPayload::ticks,
                    ByteBufCodecs.FLOAT, CraftingProgressSyncPayload::benchCoefficient,
                    ByteBufCodecs.VAR_INT, CraftingProgressSyncPayload::qualityIndex,
                    ByteBufCodecs.VAR_INT, CraftingProgressSyncPayload::xpCost,
                    CraftingProgressSyncPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
