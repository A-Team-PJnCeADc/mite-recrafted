package com.mite.recraft.item.tools.toolItem;

import com.mite.recraft.component.ModDataComponents;
import com.mite.recraft.enchantment.ModEnchantments;
import com.mite.recraft.enchantment.Quickness;
import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * 弓（Bow）—— wood(32), ancient_metal(64), mithril(128)
 * <p>
 * 自身继承 BowItem，覆盖 releaseUsing 支持 Quickness 平滑加速，
 * 覆盖 shoot 处理 Accuracy 散射。
 */
public class BowItems extends BowItem {

    public static final Item WOOD_BOW = new BowItems(new Item.Properties()
            .setId(id("wood_bow"))
            .durability(32));

    public static final Item ANCIENT_METAL_BOW = new BowItems(new Item.Properties()
            .setId(id("ancient_metal_bow"))
            .durability(64)
            .component(ModDataComponents.TOOL_COMPONENTS, 2)
            .component(ModDataComponents.TOOL_MATERIAL_TIER, ModToolMaterial.ANCIENT_METAL.getDurabilityCoefficient())
            .component(ModDataComponents.TOOL_REPAIR_TAG, ModToolMaterial.ANCIENT_METAL.getRepairTagName()));

    public static final Item MITHRIL_BOW = new BowItems(new Item.Properties()
            .setId(id("mithril_bow"))
            .durability(128)
            .component(ModDataComponents.TOOL_COMPONENTS, 2)
            .component(ModDataComponents.TOOL_MATERIAL_TIER, ModToolMaterial.MITHRIL.getDurabilityCoefficient())
            .component(ModDataComponents.TOOL_REPAIR_TAG, ModToolMaterial.MITHRIL.getRepairTagName()));

    private BowItems(Properties properties) {
        super(properties);
    }

    private static ResourceKey<Item> id(String name) {
        return ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath("mite-recrafted", name));
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity user, int useTicks) {
        // ── Quickness：调整蓄力 ticks ──
        int charge = this.getUseDuration(stack, user) - useTicks;
        int quicknessLevel = 0;
        if (level instanceof ServerLevel serverLevel) {
            quicknessLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    serverLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                            .getOrThrow(ModEnchantments.QUICKNESS.key()),
                    stack
            );
        }
        if (quicknessLevel > 0) {
            float multiplier = Quickness.multiplier(quicknessLevel);
            float t = Math.min(charge / 20.0f, 1.0f);
            float accel = 1.0f + (multiplier - 1.0f) * t * t;
            int boostedCharge = Math.round(charge * accel);
            int adjustedTicks = this.getUseDuration(stack, user) - boostedCharge;
            if (adjustedTicks < 0) adjustedTicks = 0;
            useTicks = adjustedTicks;
        }
        return super.releaseUsing(stack, level, user, useTicks);
    }

    @Override
    protected void shoot(ServerLevel level, LivingEntity shooter, InteractionHand hand,
                         ItemStack weapon, List<ItemStack> projectiles,
                         float velocity, float inaccuracy, boolean critical,
                         LivingEntity target) {
        // Accuracy：通过 PROJECTILE_SPREAD 组件处理 inaccuracy
        float processedInaccuracy = EnchantmentHelper.processProjectileSpread(level, weapon, shooter, inaccuracy);
        super.shoot(level, shooter, hand, weapon, projectiles, velocity, processedInaccuracy, critical, target);
    }
}
