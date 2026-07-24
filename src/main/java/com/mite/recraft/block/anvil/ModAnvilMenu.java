package com.mite.recraft.block.anvil;

import com.mite.recraft.component.ModDataComponents;
import com.mite.recraft.item.moditems.armor.ModArmorItem;
import com.mite.recraft.mixin.AnvilMenuCostAccessor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * MITE 砧 — numComponents×2 粒修理 + 砧耐久扣除
 */
//todo 砧惩罚机制
public class ModAnvilMenu extends AnvilMenu {
    private boolean canRepair;
    private int lastUsed;  // createResult 计算的消耗粒数
    private ItemStack lastResult = ItemStack.EMPTY;  // createResult 的结果，用于 shift-click 时取回

    public ModAnvilMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(containerId, playerInventory, access);
    }

    @Override
    public void createResult() {
        canRepair = false;
        ItemStack target = inputSlots.getItem(0);
        ItemStack material = inputSlots.getItem(1);

        if (target.isEmpty() || material.isEmpty()) {
            resultSlots.setItem(0, ItemStack.EMPTY);
            return;
        }

        // 部件数：工具读 TOOL_COMPONENTS，护甲读 ModArmorItem.numComponents()
        int numComponents;
        String repairTagName = null;
        Float toolTier = null;

        Integer nc = target.get(ModDataComponents.TOOL_COMPONENTS);
        if (nc != null && nc > 0) {
            numComponents = nc;
            repairTagName = target.get(ModDataComponents.TOOL_REPAIR_TAG);
            toolTier = target.get(ModDataComponents.TOOL_MATERIAL_TIER);
        } else if (target.getItem() instanceof ModArmorItem armor) {
            numComponents = armor.numComponents();
            repairTagName = armor.material().repairTagName();
            toolTier = armor.material().durabilityCoefficient();
        } else {
            super.createResult();
            return;
        }

        // 相同物品修复（简单耐久叠加，无增益）
        if (target.getItem() == material.getItem()) {
            int maxDamage = target.getMaxDamage();
            int targetDamage = target.getDamageValue();
            if (targetDamage <= 0) {
                resultSlots.setItem(0, ItemStack.EMPTY);
                return;
            }
            int matRemaining = maxDamage - material.getDamageValue();
            if (matRemaining <= 0) {
                resultSlots.setItem(0, ItemStack.EMPTY);
                return;
            }
            int newDamage = Math.max(0, targetDamage - matRemaining);
            ItemStack result = target.copyWithCount(1);
            result.setDamageValue(newDamage);
            resultSlots.setItem(0, result);
            canRepair = true;
            lastResult = result.copy();
            lastUsed = 1;
            ((AnvilMenuCostAccessor) this).getCost().set(1);
            return;
        }

        // 需要检查砧等级
        if (toolTier != null) {
            float anvilTier = getBlockAnvil().getMaterialTier();
            if (toolTier > anvilTier) {
                resultSlots.setItem(0, ItemStack.EMPTY);
                return;
            }
        }

        // 检查修理材料匹配
        if (repairTagName != null) {
            TagKey<Item> repairTag = TagKey.create(Registries.ITEM,
                    Identifier.fromNamespaceAndPath("mite-recrafted", repairTagName));
            if (!material.is(repairTag)) {
                resultSlots.setItem(0, ItemStack.EMPTY);
                return;
            }
        }

        int damage = target.getDamageValue();
        if (damage <= 0) {
            resultSlots.setItem(0, ItemStack.EMPTY);
            return;
        }

        int maxNeeded = numComponents * 2;
        Integer maxDamage = target.get(DataComponents.MAX_DAMAGE);
        if (maxDamage == null || maxDamage <= 0) {
            resultSlots.setItem(0, ItemStack.EMPTY);
            return;
        }

        int perNugget = maxDamage / maxNeeded;
        int reallyNeeded = (int) Math.ceil((double) damage / perNugget);
        reallyNeeded = Math.max(reallyNeeded, 1);
        int used = Math.min(reallyNeeded, Math.min(material.getCount(), maxNeeded));
        int newDamage = Math.max(0, damage - perNugget * used);

        ItemStack result = target.copy();
        result.setDamageValue(newDamage);
        resultSlots.setItem(0, result);
        canRepair = true;
        lastResult = result.copy();
        lastUsed = used;

        ((AnvilMenuCostAccessor) this).getCost().set(used);
    }

    @Override
    protected boolean mayPickup(Player player, boolean present) {
        if (!canRepair) return false;
        int cost = ((AnvilMenuCostAccessor) this).getCost().get();
        return player.hasInfiniteMaterials() || player.experienceLevel >= cost;
    }

    @Override
    protected void onTake(Player player, ItemStack stack) {
        int cost = ((AnvilMenuCostAccessor) this).getCost().get();
        int used = lastUsed;  // 使用 createResult 计算的粒数
        lastUsed = 0;

        if (used > 0 && !player.getAbilities().instabuild) {
            player.giveExperienceLevels(-cost);
        }

        inputSlots.getItem(1).shrink(used);
        inputSlots.setItem(0, ItemStack.EMPTY);
        ((AnvilMenuCostAccessor) this).getCost().set(0);

        if (used > 0) {
            access.execute((level, pos) -> {
                if (level.getBlockEntity(pos) instanceof ModAnvilBlockEntity be) {
                    boolean willBreak = be.wouldBreak(used);

                    if (willBreak && !level.isClientSide()) {
                        // shift-click: stack 已被 moveItemStackTo 清空，用 lastResult 取回
                        // 普通点击: stack 还在，setCount(0) 让光标变空
                        ItemStack dropStack = stack.isEmpty() ? lastResult : stack.copy();
                        if (!stack.isEmpty()) {
                            stack.setCount(0);  // 普通点击 → 清空光标
                        } else if (!lastResult.isEmpty()) {
                            // shift-click → 从背包移除已移入的物品
                            player.getInventory().removeItem(lastResult);
                        }
                        lastResult = ItemStack.EMPTY;
                        // 修理物掉落在地面
                        if (!dropStack.isEmpty()) {
                            level.addFreshEntity(new ItemEntity(level,
                                    pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                                    dropStack));
                        }
                        // 砧碎裂音效+销毁（与原版 AnvilBlock.damage 返回 null 时同逻辑）
                        level.levelEvent(1029, pos, 0);
                        level.removeBlock(pos, false);
                    }
                    be.addDamage(used, level);
                }
            });
        }
    }

    @Override
    protected boolean isValidBlock(BlockState state) {
        return state.getBlock() instanceof ModAnvilBlock;
    }

    private int getNuggetCount(ItemStack tool) {
        Integer nc = tool.get(ModDataComponents.TOOL_COMPONENTS);
        return (nc != null && nc > 0) ? nc * 2 : 0;
    }

    private ModAnvilBlock getBlockAnvil() {
        var ref = new Object() { ModAnvilBlock block = null; };
        access.execute((level, pos) -> {
            if (level.getBlockState(pos).getBlock() instanceof ModAnvilBlock b) {
                ref.block = b;
            }
        });
        return ref.block != null ? ref.block : null;
    }
}
