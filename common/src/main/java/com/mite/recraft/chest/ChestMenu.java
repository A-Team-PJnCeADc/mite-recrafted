package com.mite.recraft.chest;

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

/**
 * 箱子菜单：全部格子一屏可见，槽位直接绑定真实容器下标。
 *
 * <p>第 {@code row × 列数 + column} 个槽位对应容器同一下标，因此原版点击、拖拽、
 * shift-click 与同步逻辑无需改动；也不下发菜单数据或按钮包。</p>
 */
public final class ChestMenu extends AbstractContainerMenu {

    private final ChestVariant variant;
    private final ChestPanelLayout layout;
    private final Container storage;
    private final @Nullable BlockPos blockPos;

    public ChestMenu(MenuType<?> menuType, ChestVariant variant, int syncId, Inventory inventory,
                     Container storage) {
        super(menuType, syncId);
        checkContainerSize(storage, variant.capacity());
        this.variant = variant;
        this.layout = new ChestPanelLayout(variant.columns(), variant.rows());
        this.storage = storage;
        this.blockPos = storage instanceof ChestBlockEntity entity ? entity.getBlockPos() : null;

        storage.startOpen(inventory.player);
        buildSlots(inventory);
    }

    public ChestVariant variant() {
        return this.variant;
    }

    public ChestPanelLayout layout() {
        return this.layout;
    }

    /** 真实容器，供开启者计数与方块位置比对。 */
    public Container storage() {
        return this.storage;
    }

    public @Nullable BlockPos blockPos() {
        return this.blockPos;
    }

    private void buildSlots(Inventory inventory) {
        int columns = this.variant.columns();
        for (int slot = 0; slot < this.variant.capacity(); slot++) {
            int row = slot / columns;
            int column = slot % columns;
            addSlot(new Slot(this.storage, slot,
                    this.layout.chestSlotX(column), this.layout.chestSlotY(row)));
        }

        int playerX = this.layout.playerInventoryX();
        int playerY = this.layout.playerInventoryY();
        for (int row = 0; row < ChestPanelLayout.PLAYER_ROWS; row++) {
            for (int column = 0; column < ChestPanelLayout.PLAYER_COLUMNS; column++) {
                addSlot(new Slot(inventory, column + row * ChestPanelLayout.PLAYER_COLUMNS
                        + ChestPanelLayout.PLAYER_COLUMNS,
                        playerX + column * ChestPanelLayout.SLOT_STEP,
                        playerY + row * ChestPanelLayout.SLOT_STEP));
            }
        }
        for (int column = 0; column < ChestPanelLayout.PLAYER_COLUMNS; column++) {
            addSlot(new Slot(inventory, column,
                    playerX + column * ChestPanelLayout.SLOT_STEP,
                    this.layout.hotbarY()));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        Slot slot = this.slots.get(slotIndex);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack moved = stack.copy();
        int storageSlots = this.variant.capacity();

        boolean handled = slotIndex < storageSlots
                ? this.moveItemStackTo(stack, storageSlots, this.slots.size(), true)
                : this.moveItemStackTo(stack, 0, storageSlots, false);
        if (!handled) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return moved;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.storage.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.storage.stopOpen(player);
    }

    /** 客户端占位容器，容量与真实容器一致。 */
    public static Container clientStorage(ChestVariant variant) {
        return new SimpleContainer(variant.capacity());
    }
}
