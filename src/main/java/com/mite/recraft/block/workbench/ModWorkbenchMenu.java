package com.mite.recraft.block.workbench;

import com.mite.recraft.network.CraftingProgressSyncPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

/**
 * MITE 工作台菜单 — 含等级门槛 + 合成时间系统。
 * 进度通过 CraftingProgressSyncPayload (S→C) 同步。
 */
public class ModWorkbenchMenu extends CraftingMenu {

    private final WorkbenchMaterial material;
    private final ContainerLevelAccess access;
    private final Inventory playerInventory;

    private int craftingPeriod;
    private int craftingTicks;
    private boolean isCrafting;   // true = 玩家点击后开始计时
    private int lastSentPeriod = -1;
    private int lastSentTicks = -1;

    public ModWorkbenchMenu(int syncId, Inventory inventory, ContainerLevelAccess access, WorkbenchMaterial material) {
        super(syncId, inventory, access);
        this.access = access;
        this.material = material;
        this.playerInventory = inventory;
        this.craftingPeriod = 0;
        this.craftingTicks = 0;
    }

    @Override
    public MenuType<?> getType() {
        return ModWorkbenchBlock.MENU_TYPE;
    }

    /* ========== Getters ========== */

    public int getCraftingPeriod() { return craftingPeriod; }
    public int getCraftingTicks() { return craftingTicks; }

    public boolean isCraftingInProgress() {
        return isCrafting && craftingPeriod > 0 && craftingTicks < craftingPeriod;
    }

    public boolean isCraftingComplete() {
        return !isCrafting && craftingPeriod > 0 && craftingTicks >= craftingPeriod;
    }

    /* ========== Slot change → recalculate ========== */

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if (!(container instanceof CraftingContainer) && container != this.craftSlots) return;
        if (playerInventory.player.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            CraftingMenu.slotChangedCraftingGrid(this, serverLevel, playerInventory.player,
                    this.craftSlots, this.resultSlots, null);
        }
        ItemStack result = getResultItem();
        if (result.isEmpty() || !canCraftItem(result)) {
            resetCrafting();
        } else if (playerInventory.player.level() instanceof net.minecraft.server.level.ServerLevel) {
            this.craftingPeriod = calculateCraftingPeriod(result);
            this.craftingTicks = 0;
            this.isCrafting = false;
        }
    }

    private void onItemTaken() {
        ItemStack result = getResultItem();
        if (result.isEmpty() || !canCraftItem(result)) {
            resetCrafting();
        } else {
            this.craftingPeriod = calculateCraftingPeriod(result);
            this.craftingTicks = 0;
        }
    }

    private void resetCrafting() {
        this.craftingPeriod = 0;
        this.craftingTicks = 0;
        this.isCrafting = false;
    }

    private ItemStack getResultItem() {
        return this.resultSlots.getItem(0);
    }

    /** 关闭 GUI 或切换容器时重置合成状态 */
    @Override
    public void removed(Player player) {
        super.removed(player);
        resetCrafting();
        sendProgressToClient();
    }

    /* ========== Server tick + sync ========== */

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if (isCrafting) {
            this.craftingTicks++;
            if (this.craftingTicks >= this.craftingPeriod) {
                this.craftingTicks = this.craftingPeriod;
                this.isCrafting = false; // 完成
            }
        }
        if (craftingPeriod != lastSentPeriod || craftingTicks != lastSentTicks) {
            sendProgressToClient();
            lastSentPeriod = craftingPeriod;
            lastSentTicks = craftingTicks;
        }
    }

    private void sendProgressToClient() {
        if (playerInventory.player instanceof ServerPlayer serverPlayer) {
            ServerPlayNetworking.send(serverPlayer,
                    new CraftingProgressSyncPayload(craftingPeriod, craftingTicks));
        }
    }

    /* ========== Period calculation ========== */

    private int calculateCraftingPeriod(ItemStack result) {
        float difficulty = getCraftingDifficulty();
        int basePeriod = calcBasePeriod(difficulty);
        float benchModifier = getBenchModifier();
        float skill = playerInventory.player.experienceLevel * 0.02f;
        return Math.max((int) ((float) basePeriod / (1.0f + skill + benchModifier)), 25);
    }

    static int calcBasePeriod(float difficulty) {
        int raw = Math.round(difficulty * 70.0f);
        return Math.clamp(raw, 25, 200);
    }

    float getCraftingDifficulty() {
        float max = 1.0f;
        for (int i = 0; i < this.craftSlots.getContainerSize(); i++) {
            ItemStack stack = this.craftSlots.getItem(i);
            if (!stack.isEmpty()) {
                float d = TieredResultSlot.getItemDurability(stack);
                if (d > max) max = d;
            }
        }
        return max;
    }

    float getBenchModifier() {
        return switch (material) {
            case FLINT, OBSIDIAN -> 0.2f;
            case COPPER, SILVER, GOLD -> 0.3f;
            case RUSTED_IRON, IRON -> 0.4f;
            case ANCIENT_METAL -> 0.5f;
            case MITHRIL -> 0.6f;
            case ADAMANTIUM -> 0.7f;
        };
    }

    /* ========== StillValid ========== */

    @Override
    public boolean stillValid(Player player) {
        return access.evaluate((world, pos) ->
                world.getBlockState(pos.immutable()).getBlock() instanceof ModWorkbenchBlock
                        && player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0,
                true
        );
    }

    public WorkbenchMaterial getWorkbenchMaterial() { return material; }

    public boolean canCraftItem(ItemStack result) {
        if (result.isEmpty()) return true;
        float benchDur = material.getToolMaterial().getDurabilityCoefficient();
        return benchDur >= TieredResultSlot.getItemMaterialDurabilityStatic(result, this.craftSlots);
    }

    /* ========== TieredResultSlot ========== */

    @Override
    protected Slot addResultSlot(Player player, int x, int y) {
        var slot = new TieredResultSlot(player, this.craftSlots, this.resultSlots, 0, x, y, this);
        this.addSlot(slot);
        return slot;
    }

    static class TieredResultSlot extends ResultSlot {
        private final ModWorkbenchMenu menu;

        TieredResultSlot(Player player, CraftingContainer craftSlots,
                         net.minecraft.world.Container container, int slot, int x, int y,
                         ModWorkbenchMenu menu) {
            super(player, craftSlots, container, slot, x, y);
            this.menu = menu;
        }

        @Override
        public boolean mayPickup(Player player) {
            if (!super.mayPickup(player)) return false;
            ItemStack result = this.getItem();
            if (result.isEmpty()) return true;
            if (!menu.canCraftItem(result)) return false;
            // 未开始 → 点一次开始合成
            if (!menu.isCrafting && menu.craftingPeriod > 0 && menu.craftingTicks < menu.craftingPeriod) {
                menu.isCrafting = true;
                menu.craftingTicks = 0;
                return false;
            }
            // 合成中 → 不能拿
            if (menu.isCrafting) return false;
            // 合成完成 → 可以拿
            return menu.craftingPeriod > 0 && menu.craftingTicks >= menu.craftingPeriod;
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            super.onTake(player, stack);
            menu.onItemTaken();
        }

        static float getItemMaterialDurabilityStatic(ItemStack result, CraftingContainer craftSlots) {
            float max = getItemDurability(result);
            for (int i = 0; i < craftSlots.getContainerSize(); i++) {
                ItemStack ing = craftSlots.getItem(i);
                if (!ing.isEmpty()) {
                    float d = getItemDurability(ing);
                    if (d > max) max = d;
                }
            }
            return Math.max(max, 1.0f);
        }

        static float getItemDurability(ItemStack stack) {
            var key = BuiltInRegistries.ITEM.getKey(stack.getItem());
            if (key == null || !key.getNamespace().equals("mite-recrafted")) return 1.0f;
            String path = key.getPath();
            for (WorkbenchMaterial mat : WorkbenchMaterial.values()) {
                if (path.contains(mat.getName())) return mat.getToolMaterial().getDurabilityCoefficient();
            }
            return 1.0f;
        }
    }
}
