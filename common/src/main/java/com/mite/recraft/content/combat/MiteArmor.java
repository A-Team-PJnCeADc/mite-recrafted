package com.mite.recraft.content.combat;

import com.mite.recraft.content.item.armor.ModArmorItem;
import com.mite.recraft.registry.ModEnchantments;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/**
 * MITE 1.6.4 的平减护甲：护甲值直接抵扣伤害，最少保留 1 点。
 *
 * <p>单件护甲值 = numComponents * protection / 24；再乘耐久系数（玩家按当前耐久线性衰减，
 * 非玩家固定 0.5）。保护附魔每级提升 12.5%。</p>
 */
public final class MiteArmor {

    private MiteArmor() {
    }

    /** 该实体穿戴的本模组护甲提供的总护甲值。 */
    public static float protection(LivingEntity entity, DamageSource source) {
        if (MiteCombat.bypassesMundaneArmor(source)) {
            return 0.0F;
        }
        float total = 0.0F;
        for (EquipmentSlot slot : EquipmentSlot.VALUES) {
            if (!slot.isArmor()) {
                continue;
            }
            ItemStack stack = entity.getItemBySlot(slot);
            if (stack.isEmpty() || !(stack.getItem() instanceof ModArmorItem armor)) {
                continue;
            }
            float piece = armor.numComponents() * armor.protection() / 24.0F;
            total += piece * damageFactor(stack, entity) * protectionBonus(stack, entity);
        }
        return total;
    }

    /** MITE ItemArmor.getDamageFactor：玩家随耐久线性衰减，其他生物固定 0.5。 */
    private static float damageFactor(ItemStack stack, LivingEntity entity) {
        if (!(entity instanceof Player)) {
            return 0.5F;
        }
        if (stack.getMaxDamage() > 1 && stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            return 0.0F;
        }
        float factor = 2.0F - (float) stack.getDamageValue() / (float) stack.getMaxDamage() * 2.0F;
        return Math.min(factor, 1.0F);
    }

    /** 保护附魔：每级 12.5%。 */
    private static float protectionBonus(ItemStack stack, LivingEntity entity) {
        var lookup = entity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        int level = EnchantmentHelper.getItemEnchantmentLevel(lookup.getOrThrow(ModEnchantments.PROTECTION.key()), stack);
        return level <= 0 ? 1.0F : 1.0F + 0.125F * level;
    }

    /** 平减后伤害，最少 1 点。 */
    public static float apply(float amount, LivingEntity entity, DamageSource source) {
        if (amount <= 0.0F) {
            return amount;
        }
        float protection = protection(entity, source);
        if (protection <= 0.0F) {
            return amount;
        }
        return Math.max(amount - protection, 1.0F);
    }
}
