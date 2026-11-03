package com.mite.recraft.registry;

import com.mite.recraft.api.MiteIds;
import com.mite.recraft.content.enchantment.effects.SilentApplyMobEffect;
import com.mite.recraft.content.enchantment.effects.StunEffect;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

/**
 * 自定义附魔实体效果类型的编解码器注册。
 */
public final class ModEnchantmentEffects {

    private ModEnchantmentEffects() {
    }

    public static void register() {
        Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE,
                MiteIds.id("silent_apply_mob_effect"), SilentApplyMobEffect.CODEC);
        Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE,
                MiteIds.id("stun_effect"), StunEffect.CODEC);
    }
}
