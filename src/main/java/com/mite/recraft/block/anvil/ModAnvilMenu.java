package com.mite.recraft.block.anvil;

import com.mite.recraft.component.ModDataComponents;
import com.mite.recraft.mixin.AnvilMenuCostAccessor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
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
public class ModAnvilMenu extends AnvilMenu {
    private boolean canRepair;
    private int lastUsed;  // createResult 计算的消耗粒数

    public ModAnvilMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(containerId, playerInventory, access);
    }

    @Override
    public void createResult() {
        canRepair = false;
        ItemStack tool = inputSlots.getItem(0);
        ItemStack material = inputSlots.getItem(1);

        if (tool.isEmpty() || material.isEmpty()) {
            resultSlots.setItem(0, ItemStack.EMPTY);
            return;
        }

        Integer numComponents = tool.get(ModDataComponents.TOOL_COMPONENTS);
        if (numComponents == null || numComponents <= 0) {
            super.createResult();
            return;
        }

        // 检查砧等级是否 ≥ 工具等级
        Float toolTier = tool.get(ModDataComponents.TOOL_MATERIAL_TIER);
        if (toolTier == null) {
            resultSlots.setItem(0, ItemStack.EMPTY);
            return;
        }
        float anvilTier = getBlockAnvil().getMaterialTier();
        if (toolTier > anvilTier) {
            resultSlots.setItem(0, ItemStack.EMPTY);
            return;
        }

        // 检查修理材料是否正确（工具只能用对应材料粒维修）
        String tagName = tool.get(ModDataComponents.TOOL_REPAIR_TAG);
        if (tagName != null) {
            TagKey<Item> repairTag = TagKey.create(Registries.ITEM,
                    Identifier.fromNamespaceAndPath("mite-recrafted", tagName));
            if (!material.is(repairTag)) {
                resultSlots.setItem(0, ItemStack.EMPTY);
                return;
            }
        }

        int damage = tool.getDamageValue();
        if (damage <= 0) {
            resultSlots.setItem(0, ItemStack.EMPTY);
            return;
        }

        int maxNeeded = numComponents * 2;
        Integer maxDamage = tool.get(DataComponents.MAX_DAMAGE);
        if (maxDamage == null || maxDamage <= 0) {
            resultSlots.setItem(0, ItemStack.EMPTY);
            return;
        }

        int perNugget = maxDamage / maxNeeded;
        // 实际需要的粒数 = 向上取整(damage / 每粒修复量)
        int reallyNeeded = (int) Math.ceil((double) damage / perNugget);
        reallyNeeded = Math.max(reallyNeeded, 1); // 至少 1 粒
        int used = Math.min(reallyNeeded, Math.min(material.getCount(), maxNeeded));
        int newDamage = Math.max(0, damage - perNugget * used);

        ItemStack result = tool.copy();
        result.setDamageValue(newDamage);
        resultSlots.setItem(0, result);
        canRepair = true;
        lastUsed = used;  // 传递给 onTake

        // 设置 cost（用于 GUI 和经验消耗）
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
