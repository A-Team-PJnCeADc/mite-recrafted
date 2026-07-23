package com.mite.recraft.item.moditems.strongbox;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.ArrayList;
import java.util.List;

public class StrongboxBlockEntity extends ChestBlockEntity {
    private final StrongboxType type;
    private final List<ItemStack> pendingOverflow = new ArrayList<>();

    @SuppressWarnings("this-escape")
    public StrongboxBlockEntity(StrongboxType type, BlockPos pos, BlockState state) {
        super(StrongboxRegistry.BLOCK_ENTITY_TYPES.get(type), pos, state);
        this.type = type;
        setItems(NonNullList.withSize(type.size, ItemStack.EMPTY));
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory inventory) {
        return new StrongboxScreenHandler(
                StrongboxRegistry.MENU_TYPES.get(this.type),
                this.type,
                syncId,
                inventory,
                this
        );
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public int getContainerSize() {
        return this.type.size;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.clampInventoryToCapacity();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
    }

    public void clampInventoryToCapacity() {
        int capacity = this.getContainerSize();
        NonNullList<ItemStack> items = this.getItems();
        if (items.size() == capacity) {
            this.setChanged();
            return;
        }

        NonNullList<ItemStack> clamped = NonNullList.withSize(capacity, ItemStack.EMPTY);
        for (int slot = 0; slot < Math.min(items.size(), capacity); slot++) {
            clamped.set(slot, items.get(slot));
        }

        Level level = this.getLevel();
        BlockPos pos = this.getBlockPos();
        for (int slot = capacity; slot < items.size(); slot++) {
            ItemStack overflow = items.get(slot);
            if (!overflow.isEmpty()) {
                this.placeOverflow(clamped, overflow, capacity, level, pos);
            }
        }

        this.setItems(clamped);
        this.setChanged();
    }

    private void placeOverflow(NonNullList<ItemStack> inventory, ItemStack stack, int capacity, Level level, BlockPos pos) {
        for (int slot = 0; slot < capacity; slot++) {
            if (inventory.get(slot).isEmpty()) {
                inventory.set(slot, stack);
                return;
            }
        }
        if (level != null) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
        } else {
            this.pendingOverflow.add(stack);
        }
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (level == null || level.isClientSide() || this.pendingOverflow.isEmpty()) {
            return;
        }
        BlockPos pos = this.getBlockPos();
        for (ItemStack stack : this.pendingOverflow) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
        }
        this.pendingOverflow.clear();
    }
}
