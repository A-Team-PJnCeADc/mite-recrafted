package com.mite.recraft.chest;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

/**
 * 箱子物品校验：修正非法的堆叠数与损伤值，丢弃未通过严格校验的堆。
 *
 * <p>只用于读档与写入槽位，避免异常数据击穿界面渲染或比较逻辑。</p>
 */
public final class ChestItemSanitizer {

    private ChestItemSanitizer() {
    }

    public static void sanitizeAll(NonNullList<ItemStack> stacks) {
        for (int slot = 0; slot < stacks.size(); slot++) {
            ItemStack original = stacks.get(slot);
            ItemStack cleaned = sanitize(original);
            if (cleaned != original) {
                stacks.set(slot, cleaned);
            }
        }
    }

    public static ItemStack sanitize(ItemStack stack) {
        if (stack.isEmpty() || stack.getCount() <= 0) {
            return ItemStack.EMPTY;
        }

        ItemStack cleaned = stack.copy();
        if (cleaned.getCount() > cleaned.getMaxStackSize()) {
            cleaned.setCount(cleaned.getMaxStackSize());
        }

        if (cleaned.has(DataComponents.DAMAGE)) {
            int damage = cleaned.getOrDefault(DataComponents.DAMAGE, 0);
            int maxDamage = cleaned.getMaxDamage();
            if (damage < 0) {
                cleaned.setDamageValue(0);
            } else if (maxDamage > 0 && damage > maxDamage) {
                cleaned.setDamageValue(maxDamage);
            }
        }

        return ItemStack.validateStrict(cleaned).isError() ? ItemStack.EMPTY : cleaned;
    }
}
