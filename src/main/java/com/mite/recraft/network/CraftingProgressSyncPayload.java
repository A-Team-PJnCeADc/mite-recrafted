package com.mite.recraft.network;

import com.mite.recraft.MiteRecrafted;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * S→C: 同步合成进度（周期 + 当前 tick）
 */
public record CraftingProgressSyncPayload(int period, int ticks) implements CustomPacketPayload {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "crafting_progress");

    public static final CustomPacketPayload.Type<CraftingProgressSyncPayload> TYPE =
            new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, CraftingProgressSyncPayload> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, CraftingProgressSyncPayload::period,
                    ByteBufCodecs.VAR_INT, CraftingProgressSyncPayload::ticks,
                    CraftingProgressSyncPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
