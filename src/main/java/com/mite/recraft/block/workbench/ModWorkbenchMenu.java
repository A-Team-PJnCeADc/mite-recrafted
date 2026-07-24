package com.mite.recraft.block.workbench;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.component.CraftingDifficultyHelper;
import com.mite.recraft.component.ModDataComponents;
import com.mite.recraft.item.quality.Quality;
import com.mite.recraft.network.CraftingProgressSyncPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.core.component.DataComponents;

/**
 * MITE 工作台菜单 — 含等级门槛 + 合成时间 + 品质系统。
 *
 * 品质轮转: 右键 result slot 循环品质 (POOR → FINE → EXCELLENT → ...)。
 * XP 消耗: 取出物品时扣除 = round(quality_adjusted_difficulty / 5.0)。
 * 进度通过 CraftingProgressSyncPayload (S→C) 同步。
 */
public class ModWorkbenchMenu extends CraftingMenu {

    private static final int RESULT_SLOT_INDEX = 0;

    private final WorkbenchMaterial material;
    private final ContainerLevelAccess access;
    private final Inventory playerInventory;

    // 合成进度
    private int craftingPeriod;
    private int craftingTicks;
    private boolean isCrafting;
    private int lastSentPeriod = -1;
    private int lastSentTicks = -1;

    // 品质系统
    private int qualityIndex;        // 当前品质 ordinal
    private int minQualityIdx;       // 最低可制作品质 ordinal
    private int xpCost;              // 当前品质 XP 消耗
    private int lastSentXpCost = -1;

    public ModWorkbenchMenu(int syncId, Inventory inventory, ContainerLevelAccess access, WorkbenchMaterial material) {
        super(syncId, inventory, access);
        this.access = access;
        this.material = material;
        this.playerInventory = inventory;
        this.craftingPeriod = 0;
        this.craftingTicks = 0;
        this.qualityIndex = Quality.AVERAGE.ordinal();
        this.minQualityIdx = Quality.AVERAGE.ordinal();
        this.xpCost = 0;
    }

    @Override
    public MenuType<?> getType() { return ModWorkbenchBlock.MENU_TYPE; }

    /* ========== Quality getters ========== */

    public int getQualityIndex() { return qualityIndex; }
    public int getXpCost() { return xpCost; }
    public Quality getCurrentQuality() { return Quality.byLevel(qualityIndex); }

    /* ========== Slot change → recalculate ========== */

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if (!(container instanceof CraftingContainer) && container != this.craftSlots) return;
        if (playerInventory.player.level() instanceof ServerLevel serverLevel) {
            CraftingMenu.slotChangedCraftingGrid(this, serverLevel, playerInventory.player,
                    this.craftSlots, this.resultSlots, null);
        }
        ItemStack result = getResultItem();
        if (result.isEmpty() || !canCraftItem(result)) {
            resetCrafting();
            if (!result.isEmpty()) this.resultSlots.setItem(0, ItemStack.EMPTY);
        } else if (playerInventory.player.level() instanceof ServerLevel) {
            recalcQualityBounds(result);
            updateQualityAndResult(result);
        }
    }

    /** 获取基础制作难度（stack 优先，回退查 item prototype） */
    private static Float getBaseDifficulty(ItemStack stack) {
        Float d = stack.get(ModDataComponents.CRAFTING_DIFFICULTY);
        if (d != null) return d;
        return stack.getItem().components().get(ModDataComponents.CRAFTING_DIFFICULTY);
    }

    /** 重新计算品质边界 + 同步品质到合法范围 */
    private void recalcQualityBounds(ItemStack result) {
        Float baseDiff = getBaseDifficulty(result);
        if (baseDiff == null || !hasQuality(result)) {
            qualityIndex = Quality.AVERAGE.ordinal();
            minQualityIdx = Quality.AVERAGE.ordinal();
            xpCost = 0;
            return;
        }

        Player player = playerInventory.player;
        minQualityIdx = getMinQuality(result);

        // maxQuality: 从最高品质向下找到玩家 XP 能负担的最高级别
        int maxIdx = getMaxQuality(result, baseDiff, player);
        // 如果当前超界，clamp
        if (qualityIndex > maxIdx) qualityIndex = maxIdx;
        if (qualityIndex < minQualityIdx) qualityIndex = minQualityIdx;
    }

    private void updateQualityAndResult(ItemStack result) {
        Float baseDiff = getBaseDifficulty(result);
        if (baseDiff != null && hasQuality(result)) {
            Quality quality = Quality.byLevel(qualityIndex);
            float adjDiff = CraftingDifficultyHelper.getQualityAdjustedDifficulty(baseDiff, quality);
            // AVERAGE 及以下免费（MITE 原版规则）
            this.xpCost = quality.ordinal() <= Quality.AVERAGE.ordinal()
                    ? 0 : CraftingDifficultyHelper.getXpCost(adjDiff);
            this.craftingPeriod = calculateCraftingPeriod(adjDiff);
        } else {
            this.xpCost = 0;
            this.craftingPeriod = calculateCraftingPeriod(getCraftingDifficulty());
        }
        this.craftingTicks = 0;
        this.isCrafting = false;

        applyQualityToResultItem();
    }

    /** 设置品质 component，ItemStackQualityNameMixin 接管名称显示 */
    private void applyQualityToResultItem() {
        ItemStack result = getResultItem();
        if (result.isEmpty() || !hasQuality(result)) return;
        ItemStack copy = result.copy();
        Quality quality = getCurrentQuality();
        copy.set(ModDataComponents.QUALITY, quality);
        copy.remove(DataComponents.CUSTOM_NAME);
        this.resultSlots.setItem(0, copy);
    }

    /* ========== Quality cycling ========== */

    /**
     * 右键 result slot 时调用：循环品质。
     * direction: +1 = 升级, -1 = 降级
     */
    void cycleQuality(int direction) {
        ItemStack result = getResultItem();
        if (result.isEmpty() || !hasQuality(result)) return;

        Float baseDiff = getBaseDifficulty(result);
        if (baseDiff == null) return;

        int maxIdx = getMaxQuality(result, baseDiff, playerInventory.player);
        int minIdx = this.minQualityIdx;

        int newIdx = qualityIndex + direction;
        if (newIdx > maxIdx) newIdx = minIdx;
        if (newIdx < minIdx) newIdx = maxIdx;
        qualityIndex = newIdx;

        updateQualityAndResult(result);
        syncAll();
    }

    /* ========== XP check (called from TieredResultSlot) ========== */

    boolean canAffordXp(Player player) {
        return xpCost <= 0 || player.totalExperience >= xpCost;
    }

    void deductXp(Player player) {
        if (xpCost <= 0) return;
        // giveExperiencePoints 支持负值扣除
        player.giveExperiencePoints(-xpCost);
        // 重置为 AVERAGE
        qualityIndex = Quality.AVERAGE.ordinal();
        xpCost = 0;
    }

    /* ========== Right-click intercept ========== */

    @Override
    public void clicked(int slotId, int button, ContainerInput input, Player player) {
        if (slotId == RESULT_SLOT_INDEX && button == 1) {
            if (player.level() instanceof ServerLevel) {
                cycleQuality(1);
            }
            return;
        }
        super.clicked(slotId, button, input, player);
    }

    /* ========== Quality bounds ========== */

    private static int getMinQuality(ItemStack result) {
        // 基础最低品质 = AVERAGE
        return Quality.AVERAGE.ordinal();
    }

    private static int getMaxQuality(ItemStack result, Float baseDiff, Player player) {
        Quality materialMax = getMaterialMaxQuality(result);
        if (materialMax == null) return Quality.AVERAGE.ordinal();

        // 无 XP 限制，右键随意切换，取出时再扣 XP
        return materialMax.ordinal();
    }

    /** 从物品的材料获取最高品质 */
    private static Quality getMaterialMaxQuality(ItemStack result) {
        // 通过物品路径反查 ModToolMaterial
        var key = BuiltInRegistries.ITEM.getKey(result.getItem());
        if (key == null || !key.getNamespace().equals(MiteRecrafted.MOD_ID)) return null;

        for (WorkbenchMaterial mat : WorkbenchMaterial.values()) {
            if (key.getPath().contains(mat.getName())) {
                return mat.getToolMaterial().getMaxQuality();
            }
        }
        return null;
    }

    /** 物品是否有品质系统（优先查 stack，回退查 item prototype） */
    private static boolean hasQuality(ItemStack stack) {
        if (stack.has(ModDataComponents.CRAFTING_DIFFICULTY)) return true;
        // 配方 assemble 出来的 stack 可能不带默认 component，回退查 item 原型
        return stack.getItem().components().has(ModDataComponents.CRAFTING_DIFFICULTY);
    }

    /* ========== Slot change helpers ========== */

    private void onItemTaken() {
        ItemStack result = getResultItem();
        if (result.isEmpty() || !canCraftItem(result)) {
            resetCrafting();
        } else {
            recalcQualityBounds(result);
            updateQualityAndResult(result);
        }
    }

    private void resetCrafting() {
        this.craftingPeriod = 0;
        this.craftingTicks = 0;
        this.isCrafting = false;
        this.xpCost = 0;
    }

    private ItemStack getResultItem() {
        return this.resultSlots.getItem(0);
    }

    /* ========== removed ========== */

    @Override
    public void removed(Player player) {
        if (isCraftingComplete()) {
            ItemStack result = getSlot(0).getItem().copy();
            if (!result.isEmpty()) {
                for (int i = 0; i <= 9; i++) getSlot(i).set(ItemStack.EMPTY);
                if (!player.getInventory().add(result)) player.drop(result, false);
            }
        }
        super.removed(player);
        resetCrafting();
        syncAll();
    }

    /* ========== Server tick + sync ========== */

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if (isCrafting) {
            this.craftingTicks++;
            if (this.craftingTicks >= this.craftingPeriod) {
                this.craftingTicks = this.craftingPeriod;
                this.isCrafting = false;
            }
        }
        if (craftingPeriod != lastSentPeriod || craftingTicks != lastSentTicks || xpCost != lastSentXpCost) {
            syncAll();
            lastSentPeriod = craftingPeriod;
            lastSentTicks = craftingTicks;
            lastSentXpCost = xpCost;
        }
    }

    private void syncAll() {
        if (playerInventory.player instanceof ServerPlayer serverPlayer) {
            ServerPlayNetworking.send(serverPlayer,
                    new CraftingProgressSyncPayload(craftingPeriod, craftingTicks,
                            material.getToolMaterial().getDurabilityCoefficient(),
                            qualityIndex, xpCost));
        }
    }

    /* ========== Getters (client-side reading) ========== */

    public int getCraftingPeriod() { return craftingPeriod; }
    public int getCraftingTicks() { return craftingTicks; }

    public boolean isCraftingInProgress() {
        return isCrafting && craftingPeriod > 0 && craftingTicks < craftingPeriod;
    }

    public boolean isCraftingComplete() {
        return !isCrafting && craftingPeriod > 0 && craftingTicks >= craftingPeriod;
    }

    /* ========== Period calculation ========== */

    /** 使用品质修正后的难度计算周期 */
    private int calculateCraftingPeriod(float qualityAdjustedDifficulty) {
        int basePeriod = CraftingDifficultyHelper.getBaseCraftingPeriod(qualityAdjustedDifficulty);
        float benchModifier = getBenchModifier();
        float skill = playerInventory.player.experienceLevel * 0.02f;
        return Math.max((int) ((float) basePeriod / (1.0f + skill + benchModifier)), 25);
    }

    /** 旧版难度计算（无品质物品用） */
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
        if (!(playerInventory.player.level() instanceof ServerLevel)) return true;
        float benchDur = material.getToolMaterial().getDurabilityCoefficient();
        return benchDur >= TieredResultSlot.getItemMaterialDurabilityStatic(result, this.craftSlots);
    }

    @Override
    public PostPlaceAction handlePlacement(boolean shift, boolean creative, RecipeHolder<?> recipe,
                                           ServerLevel level, Inventory inventory) {
        if (recipe.value() instanceof CraftingRecipe craftingRecipe) {
            ItemStack result = craftingRecipe.assemble(this.craftSlots.asCraftInput());
            if (!result.isEmpty() && !canCraftItem(result)) return PostPlaceAction.NOTHING;
        }
        return super.handlePlacement(shift, creative, recipe, level, inventory);
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
                         Container container, int slot, int x, int y,
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
            // XP 检查
            if (!menu.canAffordXp(player)) return false;
            // 未开始 → 点一次开始合成
            if (!menu.isCrafting && menu.craftingPeriod > 0 && menu.craftingTicks < menu.craftingPeriod) {
                menu.isCrafting = true;
                menu.craftingTicks = 0;
                return false;
            }
            if (menu.isCrafting) return false;
            return menu.craftingPeriod > 0 && menu.craftingTicks >= menu.craftingPeriod;
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            // 扣 XP
            menu.deductXp(player);
            menu.syncAll();
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
