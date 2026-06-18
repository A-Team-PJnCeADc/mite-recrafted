package com.mite.recraft.block.workbench;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

//todo 1. 自定义网络包传输点击/松手事件
//     2. broadcastChanges() 推进进度 tick
//     3. CraftingScreen Mixin 渲染进度条
//     4. dataSlot 同步进度值

/**
 * MITE 工作台菜单
 *
 * 门槛逻辑：通过 TieredResultSlot.mayPickup() 阻止取出
 * 配方产物会在结果槽中显示，但等级不够时无法拾取（与 MITE 原版一致）
 */
public class ModWorkbenchMenu extends CraftingMenu {

    private final WorkbenchMaterial material;
    private final ContainerLevelAccess access;

    public ModWorkbenchMenu(int syncId, Inventory inventory, ContainerLevelAccess access, WorkbenchMaterial material) {
        super(syncId, inventory, access);
        this.access = access;
        this.material = material;
    }

    /**
     * 重写有效性检查：用当前工作台实例替代原版 Blocks.CRAFTING_TABLE
     * 否则 GUI 会因为检查到方块不匹配而瞬间关闭
     */
    @Override
    public boolean stillValid(Player player) {
        return access.evaluate(
                (world, pos) -> {
                    BlockPos blockPos = pos.immutable();
                    return world.getBlockState(blockPos).getBlock() instanceof ModWorkbenchBlock
                            && player.distanceToSqr(
                                    (double) blockPos.getX() + 0.5,
                                    (double) blockPos.getY() + 0.5,
                                    (double) blockPos.getZ() + 0.5
                            ) <= 64.0;
                },
                true
        );
    }

    public WorkbenchMaterial getWorkbenchMaterial() {
        return material;
    }

    /**
     * 检查配方是否可以在当前工作台上合成
     */
    public boolean canCraftItem(ItemStack result) {
        if (result.isEmpty()) return true;
        float benchDurability = material.getToolMaterial().getDurabilityCoefficient();
        float requiredDurability = getRequiredDurabilityForResult(result);
        return benchDurability >= requiredDurability;
    }

    private float getRequiredDurabilityForResult(ItemStack result) {
        return TieredResultSlot.getItemMaterialDurabilityStatic(result, this.craftSlots);
    }

    @Override
    protected Slot addResultSlot(Player player, int x, int y) {
        var slot = new TieredResultSlot(player, this.craftSlots, this.resultSlots, 0, x, y, this);
        this.addSlot(slot);
        return slot;
    }

    /**
     * 自定义合成结果槽：取出物品前检查工作台等级
     */
    static class TieredResultSlot extends ResultSlot {
        private final ModWorkbenchMenu menu;

        public TieredResultSlot(Player player, CraftingContainer craftSlots,
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
            // 工作台等级不够 → 结果可见但不能拿（等价于 MITE "需要更好的工具"）
            return menu.canCraftItem(result);
        }

        /**
         * 计算配方所需的材料耐久系数最大值
         * 遍历产物与所有原料，取其中最高的材料耐久值
         */
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

        /**
         * 从物品 ID 推断材料耐久系数
         * 匹配 mite-recrafted 物品名称中的材料前缀
         */
        private static float getItemDurability(ItemStack stack) {
            var key = stack.getItem().builtInRegistryHolder().key();
            if (key == null || !key.identifier().getNamespace().equals("mite-recrafted")) {
                return 1.0f; // 原版物品 → 最低等级
            }
            String path = key.identifier().getPath();
            for (WorkbenchMaterial mat : WorkbenchMaterial.values()) {
                if (path.contains(mat.getName())) {
                    return mat.getToolMaterial().getDurabilityCoefficient();
                }
            }
            return 1.0f;
        }
    }
}
