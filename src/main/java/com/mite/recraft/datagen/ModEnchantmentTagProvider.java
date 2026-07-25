package com.mite.recraft.datagen;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.concurrent.CompletableFuture;

/**
 * 魔咒标签提供者 — 控制哪些魔咒出现在附魔台（non_treasure）及是否为诅咒。
 */
public class ModEnchantmentTagProvider extends FabricTagsProvider<Enchantment> {

    public ModEnchantmentTagProvider(FabricPackOutput output,
                                     CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ENCHANTMENT, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        // 所有 MITE 魔咒都出现在附魔台（non_treasure）
        // replace: false 追加到原版 non_treasure 标签
        tag(EnchantmentTags.NON_TREASURE, false)
                .add(ModEnchantments.ARROW_RECOVERY.key())
                .add(ModEnchantments.QUICKNESS.key())
                .add(ModEnchantments.POISON.key())
                .add(ModEnchantments.STUN.key())
                .add(ModEnchantments.VAMPIRIC.key())
                .add(ModEnchantments.DISARMING.key())
                .add(ModEnchantments.BUTCHERING.key())
                .add(ModEnchantments.TREE_FELLING.key())
                .add(ModEnchantments.HARVESTING.key())
                .add(ModEnchantments.FERTILITY.key())
                .add(ModEnchantments.SPEED.key())
                .add(ModEnchantments.REGENERATION.key())
                .add(ModEnchantments.FREE_ACTION.key())
                .add(ModEnchantments.ENDURANCE.key())
                .add(ModEnchantments.UNBREAKING.key())
                .add(ModEnchantments.ACCURACY.key())
                .add(ModEnchantments.SLAYING.key())
                .add(ModEnchantments.PIERCING.key())
                .add(ModEnchantments.CLEAVING.key())
                .add(ModEnchantments.TREE_FELLING.key());
    }

    @Override
    public String getName() {
        return MiteRecrafted.MOD_ID + " Enchantment Tags";
    }
}
