package com.mite.recraft.content.block.strongbox;

import com.mite.recraft.chest.ChestPanelLayout;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.jspecify.annotations.Nullable;

public final class StrongboxScreenHandler extends AbstractContainerMenu {
    private final Container container;
    private final StrongboxType strongboxType;
    private final @Nullable BlockPos strongboxBlockPos;

    public StrongboxScreenHandler(MenuType<?> menuType, StrongboxType strongboxType,
                                  int syncId, Inventory playerInventory, Container container) {
        super(menuType, syncId);
        checkContainerSize(container, strongboxType.size);
        this.strongboxType = strongboxType;
        this.container = container;
        this.strongboxBlockPos = container instanceof ChestBlockEntity blockEntity
                ? blockEntity.getBlockPos() : null;
        container.startOpen(playerInventory.player);

        int columns = strongboxType.rowLength;
        int rows = strongboxType.getRowCount();
        ChestPanelLayout layout = new ChestPanelLayout(columns, rows);
        int slotIndex = 0;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                this.addSlot(new Slot(
                        container,
                        slotIndex++,
                        layout.chestSlotX(column),
                        layout.chestSlotY(row)
                ));
            }
        }

        int playerInventoryX = layout.playerInventoryX();
        int playerInventoryY = layout.playerInventoryY();
        for (int row = 0; row < ChestPanelLayout.PLAYER_ROWS; row++) {
            for (int column = 0; column < ChestPanelLayout.PLAYER_COLUMNS; column++) {
                this.addSlot(new Slot(
                        playerInventory,
                        column + row * ChestPanelLayout.PLAYER_COLUMNS + ChestPanelLayout.PLAYER_COLUMNS,
                        playerInventoryX + column * ChestPanelLayout.SLOT_STEP,
                        playerInventoryY + row * ChestPanelLayout.SLOT_STEP
                ));
            }
        }
        for (int column = 0; column < ChestPanelLayout.PLAYER_COLUMNS; column++) {
            this.addSlot(new Slot(
                    playerInventory,
                    column,
                    playerInventoryX + column * ChestPanelLayout.SLOT_STEP,
                    layout.hotbarY()
            ));
        }
    }

    public int getChestRows() {
        return strongboxType.getRowCount();
    }

    public int getChestColumns() {
        return strongboxType.rowLength;
    }

    public Container getBlockInventory() {
        return container;
    }

    public @Nullable BlockPos getStrongboxBlockPos() {
        return strongboxBlockPos;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack movedStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            movedStack = stackInSlot.copy();
            int chestSlots = strongboxType.size;
            if (slotIndex < chestSlots) {
                if (!this.moveItemStackTo(stackInSlot, chestSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stackInSlot, 0, chestSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return movedStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        container.stopOpen(player);
    }

    public static Container createClientContainer(StrongboxType type) {
        return new SimpleContainer(type.size);
    }
}
