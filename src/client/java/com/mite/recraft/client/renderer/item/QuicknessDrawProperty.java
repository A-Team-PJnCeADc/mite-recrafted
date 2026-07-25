package com.mite.recraft.client.renderer.item;

import com.mite.recraft.client.mixin.RangeSelectItemModelPropertiesAccessor;
import com.mite.recraft.enchantment.ModEnchantments;
import com.mite.recraft.enchantment.Quickness;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/**
 * 弓的蓄力动画属性 —— 返回被 Quickness 加速后的 use_duration 值。
 * <p>
 * 原版动画阈值（scale=0.05）：bow_pulling_0 (1+)、bow_pulling_1 (13+)、bow_pulling_2 (18+) ticks。
 * Quickness Lv5 (×2) 后：pulling_2 在 9+ ticks 触发，与 12 tick 满蓄同步。
 */
public record QuicknessDrawProperty() implements RangeSelectItemModelProperty {

    public static final MapCodec<QuicknessDrawProperty> MAP_CODEC = MapCodec.unit(new QuicknessDrawProperty());
    public static final Identifier ID = Identifier.fromNamespaceAndPath("mite-recrafted", "quickness_draw");

    @Override
    public float get(ItemStack stack, ClientLevel level, ItemOwner itemOwner, int seed) {
        if (!(itemOwner instanceof LivingEntity living)) return 0;
        if (level == null) return 0;
        if (living.getUseItem() != stack) return 0;

        // 原版 use_duration 返回已蓄力 ticks（getUseDuration - remaining）
        int chargeTicks = stack.getUseDuration(living) - living.getUseItemRemainingTicks();

        // 查 Quickness 附魔等级
        int ql = EnchantmentHelper.getItemEnchantmentLevel(
                level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(ModEnchantments.QUICKNESS.key()),
                stack
        );
        if (ql <= 0) return chargeTicks;

        // 返回加速后的等效 ticks（平滑 t² 渐进，与 gameaply 和手部动画一致）
        float multiplier = Quickness.multiplier(ql);
        float t = Math.min(chargeTicks / 20.0f, 1.0f);
        float accel = 1.0f + (multiplier - 1.0f) * t * t;
        return chargeTicks * accel;
    }

    @Override
    public MapCodec<? extends RangeSelectItemModelProperty> type() {
        return MAP_CODEC;
    }

    /** 注册到 RangeSelectItemModelProperties ID_MAPPER */
    public static void register() {
        RangeSelectItemModelPropertiesAccessor.getIdMapper().put(ID, MAP_CODEC);
    }
}
