package com.mite.recraft.chest;

import com.mite.recraft.registry.ModChests;

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

/**
 * 按规格定义容量的箱子存储。
 *
 * <p>读档时把物品列表裁剪到规格容量：溢出的堆先填满空位，剩余在拿到世界后掉落到方块位置，
 * 避免旧存档或降级升级后出现越界槽位。</p>
 */
public class VariantChestStorage extends ChestBlockEntity {

    private final ChestVariant variant;
    private final NonNullList<ItemStack> overflow = NonNullList.create();

    @SuppressWarnings("this-escape")
    public VariantChestStorage(ChestVariant variant, BlockPos pos, BlockState state) {
        super(ModChests.storageType(variant), pos, state);
        this.variant = variant;
        setItems(NonNullList.withSize(variant.capacity(), ItemStack.EMPTY));
    }

    public ChestVariant variant() {
        return this.variant;
    }

    @Override
    public int getContainerSize() {
        return this.variant.capacity();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory inventory) {
        return new ChestMenu(ModChests.menuType(this.variant), this.variant, syncId, inventory, this);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, ChestItemSanitizer.sanitize(stack));
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        normalizeCapacity();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        ChestItemSanitizer.sanitizeAll(this.getItems());
        super.saveAdditional(output);
    }

    /** 把内部物品列表裁剪到规格容量，返回被移出的堆数量。 */
    public int normalizeCapacity() {
        int capacity = getContainerSize();
        NonNullList<ItemStack> stored = this.getItems();
        if (stored.size() == capacity) {
            ChestItemSanitizer.sanitizeAll(stored);
            this.setChanged();
            return 0;
        }

        NonNullList<ItemStack> trimmed = NonNullList.withSize(capacity, ItemStack.EMPTY);
        for (int slot = 0; slot < Math.min(stored.size(), capacity); slot++) {
            trimmed.set(slot, stored.get(slot));
        }

        int moved = 0;
        for (int slot = capacity; slot < stored.size(); slot++) {
            ItemStack stack = stored.get(slot);
            if (stack.isEmpty()) {
                continue;
            }
            moved++;
            absorb(trimmed, stack);
        }

        setItems(trimmed);
        ChestItemSanitizer.sanitizeAll(trimmed);
        this.setChanged();
        return moved;
    }

    private void absorb(NonNullList<ItemStack> storage, ItemStack stack) {
        for (int slot = 0; slot < storage.size(); slot++) {
            if (storage.get(slot).isEmpty()) {
                storage.set(slot, stack);
                return;
            }
        }
        this.overflow.add(stack);
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (level == null || level.isClientSide() || this.overflow.isEmpty()) {
            return;
        }
        BlockPos pos = this.getBlockPos();
        for (ItemStack stack : this.overflow) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
        }
        this.overflow.clear();
    }
}
