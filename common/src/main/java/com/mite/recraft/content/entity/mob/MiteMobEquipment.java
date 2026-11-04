package com.mite.recraft.content.entity.mob;

import com.mite.recraft.MiteRecrafted;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MITE 1.6.4 的装备随机化（EntityMob.addRandomEquipment / randomizeForMob）。
 *
 * <p>护甲与武器按材质给出固定组合，耐损在最大耐久的 0-50% 之间随机。</p>
 */
public final class MiteMobEquipment {

    private MiteMobEquipment() {
    }

    /** 依次穿戴头盔、胸甲、护腿、靴子。 */
    public static void equipArmor(Mob mob, String helmet, String chestplate, String leggings, String boots) {
        mob.setItemSlot(EquipmentSlot.HEAD, worn(helmet, mob.getRandom()));
        mob.setItemSlot(EquipmentSlot.CHEST, worn(chestplate, mob.getRandom()));
        mob.setItemSlot(EquipmentSlot.LEGS, worn(leggings, mob.getRandom()));
        mob.setItemSlot(EquipmentSlot.FEET, worn(boots, mob.getRandom()));
    }

    /** 按权重挑一件武器放到主手。 */
    public static void equipWeapon(Mob mob, Map<String, Integer> weightedIds) {
        int total = 0;
        for (int weight : weightedIds.values()) {
            total += weight;
        }
        if (total <= 0) {
            return;
        }
        int roll = mob.getRandom().nextInt(total);
        for (Map.Entry<String, Integer> entry : weightedIds.entrySet()) {
            roll -= entry.getValue();
            if (roll < 0) {
                mob.setItemSlot(EquipmentSlot.MAINHAND, worn(entry.getKey(), mob.getRandom()));
                return;
            }
        }
    }

    /** 权重表按顺序构建，方便调用处书写。 */
    public static Map<String, Integer> weighted(Object... idAndWeight) {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i + 1 < idAndWeight.length; i += 2) {
            map.put((String) idAndWeight[i], (Integer) idAndWeight[i + 1]);
        }
        return map;
    }

    /** 按 id 取物品并随机耐损。 */
    public static ItemStack worn(String id, RandomSource random) {
        Identifier key = Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, id);
        Item item = BuiltInRegistries.ITEM.getValue(key);
        ItemStack stack = new ItemStack(item);
        if (stack.isDamageableItem()) {
            stack.setDamageValue(random.nextInt(stack.getMaxDamage() / 2 + 1));
        }
        return stack;
    }
}
