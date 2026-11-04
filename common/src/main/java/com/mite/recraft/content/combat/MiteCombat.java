package com.mite.recraft.content.combat;

import com.mite.recraft.MiteRecrafted;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * MITE 伤害模型里与来源性质相关的判定。
 *
 * <p>MITE 的 Damage 带 silver/magic 标志与 bypassesMundaneArmor 判定；26.3 没有对应字段，
 * 这里从伤害来源与攻击者手持物推导，供生物免疫与自然防御使用。</p>
 */
public final class MiteCombat {

    private MiteCombat() {
    }

    /** 伤害是否无视寻常护甲（MITE Damage.bypassesMundaneArmor）。 */
    public static boolean bypassesMundaneArmor(DamageSource source) {
        return source.is(DamageTypeTags.BYPASSES_ARMOR);
    }

    /** 魔法伤害（MITE Damage.hasMagicAspect）。 */
    public static boolean hasMagicAspect(DamageSource source) {
        return source.is(DamageTypeTags.WITCH_RESISTANT_TO);
    }

    /** 银质伤害：攻击者手持本模组的 silver_* 武器或弹药（MITE Damage.hasSilverAspect）。 */
    public static boolean hasSilverAspect(DamageSource source) {
        Entity attacker = source.getDirectEntity();
        if (attacker == null) {
            attacker = source.getEntity();
        }
        if (attacker instanceof LivingEntity living) {
            return isSilver(living.getMainHandItem());
        }
        return source.getWeaponItem() != null && isSilver(source.getWeaponItem());
    }

    public static boolean isSilver(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return MiteRecrafted.MOD_ID.equals(id.getNamespace()) && id.getPath().startsWith("silver_");
    }

    /** MITE 的平减护甲模型：伤害减去自然防御，最少保留 1 点。 */
    public static float applyNaturalDefense(float amount, float naturalDefense, DamageSource source) {
        if (amount <= 0.0F || naturalDefense <= 0.0F || bypassesMundaneArmor(source)) {
            return amount;
        }
        return Math.max(amount - naturalDefense, 1.0F);
    }
}
