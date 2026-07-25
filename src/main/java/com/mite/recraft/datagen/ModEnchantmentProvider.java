package com.mite.recraft.datagen;

import com.mite.recraft.enchantment.Accuracy;
import com.mite.recraft.enchantment.ModEnchantments;
import com.mite.recraft.enchantment.Poison;
import com.mite.recraft.enchantment.Slaying;
import com.mite.recraft.enchantment.Unbreaking;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

/**
 * 魔咒数据生成器 — 通过 bootstrap 注册 MITE 自定义魔咒至 Enchantment registry，
 * 并通过 FabricDynamicRegistryProvider 输出 data/mite-recrafted/enchantment/*.json。
 * <p>
 * 所有定义参数在 {@link ModEnchantments} 枚举中。
 * 数据组件效果在此 switch 分发；纯 mixin 驱动效果无需注册。
 */
public class ModEnchantmentProvider extends FabricDynamicRegistryProvider {

    public ModEnchantmentProvider(FabricPackOutput output,
                                  CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(Registries.ENCHANTMENT));
    }

    @Override
    public @NonNull String getName() {
        return "MITE Recrafted Enchantments";
    }

    // ── bootstrap — 枚举驱动的一站式注册 ──
    public static void bootstrap(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);
        for (ModEnchantments ench : ModEnchantments.values()) {
            context.register(ench.key(), buildEnchantment(context, ench, items));
        }
    }

    private static Enchantment buildEnchantment(BootstrapContext<Enchantment> context,
                                                ModEnchantments ench,
                                                HolderGetter<Item> items) {
        var builder = new Enchantment.Builder(ench.definition(items));

        // 数据组件效果 —— 各子类静态方法注入
        switch (ench) {
            case UNBREAKING -> builder = Unbreaking.addEffect(builder);
            case ACCURACY   -> builder = Accuracy.addEffect(builder);
            case POISON     -> builder = Poison.addEffect(builder, MobEffects.POISON);
            case SLAYING    -> builder = Slaying.addEffect(builder);
            case STUN       -> builder = builder.withEffect(
                    net.minecraft.world.item.enchantment.EnchantmentEffectComponents.POST_ATTACK,
                    net.minecraft.world.item.enchantment.EnchantmentTarget.ATTACKER,
                    net.minecraft.world.item.enchantment.EnchantmentTarget.VICTIM,
                    new com.mite.recraft.enchantment.effects.StunEffect());
        }
        // 其余魔咒使用 mixin（无需数据组件注册）

        return builder.build(ench.key().identifier());
    }
}
