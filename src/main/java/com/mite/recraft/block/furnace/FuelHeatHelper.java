package com.mite.recraft.block.furnace;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * MITE 燃料热量等级查询 — 匹配原版 MITE 1.6.4。
 *
 * <p>热量等级：
 * <ul>
 *   <li>热1: 木质品、纸制品、粪便、木炭、煤炭块
 *   <li>热2: 煤炭
 *   <li>热3: 岩浆桶
 *   <li>热4: 烈焰棒
 * </ul>
 */
public class FuelHeatHelper {
    public static int getHeatLevel(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;

        // 烈焰棒 → 4
        if (stack.is(Items.BLAZE_ROD)) return 4;

        // 岩浆桶 → 3
        if (stack.is(Items.LAVA_BUCKET)) return 3;

        // 煤炭 (damage 0) → 2，木炭 (damage 1) → 1
        if (stack.is(Items.COAL)) {
            return stack.getDamageValue() == 0 ? 2 : 1;
        }

        // 其他所有可燃烧物品 → 1
        return 1;
    }
}
