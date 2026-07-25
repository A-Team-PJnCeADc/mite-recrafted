package com.mite.recraft.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mite.recraft.enchantment.ModEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/**
 * 保护 (Protection) — 百分比增加被附魔装备的盔甲值，每级 12.5%，最高 IV 级。
 */
@Mixin(LivingEntity.class)
public class ProtectionMixin {

    @ModifyReturnValue(method = "getArmorValue", at = @At("RETURN"))
    private int mite$applyProtection(int originalArmor) {
        LivingEntity self = (LivingEntity) (Object) this;
        var lookup = self.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

        double totalEffective = 0.0;
        double totalBase = 0.0;
        for (EquipmentSlot slot : EquipmentSlot.VALUES) {
            if (!slot.isArmor()) continue;
            ItemStack stack = self.getItemBySlot(slot);
            if (stack.isEmpty()) continue;

            int level = EnchantmentHelper.getItemEnchantmentLevel(
                    lookup.getOrThrow(ModEnchantments.PROTECTION.key()),
                    stack
            );
            if (level <= 0) continue;

            double baseArmor = getArmorModifierTotal(stack, slot);
            if (baseArmor <= 0) continue;

            totalBase += baseArmor;
            totalEffective += baseArmor * (1.0 + 0.125 * level);
        }

        int bonus = (int) Math.floor(totalEffective) - (int) Math.floor(totalBase);
        return originalArmor + bonus;
    }

    @Unique
    private static double getArmorModifierTotal(ItemStack stack, EquipmentSlot slot) {
        double[] total = {0.0};
        stack.forEachModifier(slot, (Holder<Attribute> attr, AttributeModifier mod) -> {
            if (attr == Attributes.ARMOR) {
                total[0] += mod.amount();
            }
        });
        return total[0];
    }
}
