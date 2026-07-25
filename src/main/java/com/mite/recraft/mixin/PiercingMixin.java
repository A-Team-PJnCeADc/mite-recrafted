package com.mite.recraft.mixin;

import com.mite.recraft.enchantment.ModEnchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 穿透 (Piercing) + 劈裂 (Cleaving) 效果：无视目标部分护甲。
 * <p>
 * MC 26.2 的护甲计算发生在 actuallyHurt 内部，
 * 因此在 hurtServer HEAD 保存护甲前伤害，在 getDamageAfterArmorAbsorb 后计算穿透 bonus。
 */
@Mixin(LivingEntity.class)
public class PiercingMixin {

    @Unique
    private static final ThreadLocal<Integer> mite_piercing$level = new ThreadLocal<>();
    @Unique
    private static final ThreadLocal<Float> mite_piercing$preArmor = new ThreadLocal<>();

    @Inject(method = "hurtServer", at = @At("HEAD"))
    private void mite$capturePiercing(ServerLevel level, DamageSource source, float amount,
                                      CallbackInfoReturnable<Boolean> cir) {
        mite_piercing$preArmor.remove();
        mite_piercing$level.remove();
        if (!(source.getEntity() instanceof LivingEntity attacker)) return;

        mite_piercing$preArmor.set(amount);

        ItemStack weapon = attacker.getMainHandItem();
        int enchLevel = EnchantmentHelper.getItemEnchantmentLevel(
                level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(ModEnchantments.PIERCING.key()),
                weapon
        );
        if (enchLevel > 0) {
            mite_piercing$level.set(enchLevel);
            return;
        }
        enchLevel = EnchantmentHelper.getItemEnchantmentLevel(
                level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(ModEnchantments.CLEAVING.key()),
                weapon
        );
        if (enchLevel > 0) {
            mite_piercing$level.set(enchLevel);
        }
    }

    @Inject(method = "hurtServer", at = @At("RETURN"))
    private void mite$clearPiercing(ServerLevel level, DamageSource source, float amount,
                                    CallbackInfoReturnable<Boolean> cir) {
        mite_piercing$level.remove();
        mite_piercing$preArmor.remove();
    }

    /**
     * 注入到 getDamageAfterArmorAbsorb 返回赋值之后，此时 damage 已是护甲减免后的值。
     */
    @ModifyVariable(
            method = "actuallyHurt",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"
            ),
            index = 3
    )
    private float mite$applyPiercing(float damage, ServerLevel level, DamageSource source) {
        Integer levelVal = mite_piercing$level.get();
        if (levelVal == null || levelVal <= 0) return damage;

        int armorValue = ((LivingEntity) (Object) this).getArmorValue();
        if (armorValue <= 0) return damage;

        Float preArmor = mite_piercing$preArmor.get();
        if (preArmor == null) return damage;

        float absorbed = preArmor - damage;
        if (absorbed <= 0) return damage;

        int piercing = Math.min(levelVal, armorValue);
        float perArmorAbsorb = absorbed / armorValue;
        float bonus = perArmorAbsorb * piercing;
        bonus = Math.min(bonus, absorbed);
        return damage + bonus;
    }
}
