package com.mite.recraft.chest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

/**
 * 水晶箱：把最早入库的若干物品展示在箱盖上方，并在内容变化时同步给客户端。
 */
public class CrystalChestStorage extends VariantChestStorage {

    /** 箱盖上方展示的物品数量上限。 */
    public static final int DISPLAY_LIMIT = 12;

    public CrystalChestStorage(BlockPos pos, BlockState state) {
        super(ChestVariant.CRYSTAL, pos, state);
    }

    /** 展示用物品列表，长度固定为 {@link #DISPLAY_LIMIT}，不足补空。 */
    public NonNullList<ItemStack> displayStacks() {
        NonNullList<ItemStack> display = NonNullList.withSize(DISPLAY_LIMIT, ItemStack.EMPTY);
        int cursor = 0;
        for (ItemStack stack : this.getItems()) {
            if (cursor >= DISPLAY_LIMIT) {
                break;
            }
            if (!stack.isEmpty()) {
                display.set(cursor++, stack.copy());
            }
        }
        return display;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().blockChanged(this.worldPosition);
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }
}
