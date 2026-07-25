package com.mite.recraft.mixin;

import com.mite.recraft.enchantment.ModEnchantments;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 保护附魔 tooltip — 在原版护甲数值后追加绿色加成值。
 */
@Mixin(ItemStack.class)
public class ItemStackTooltipMixin {

    @Inject(method = "getTooltipLines", at = @At("RETURN"))
    private void mite$appendProtectionTooltip(Item.TooltipContext context, Player player, TooltipFlag flag,
                                              CallbackInfoReturnable<List<Component>> cir) {
        if (player == null) return;

        ItemStack stack = (ItemStack) (Object) this;
        int level = EnchantmentHelper.getItemEnchantmentLevel(
                player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(ModEnchantments.PROTECTION.key()),
                stack
        );
        if (level <= 0) return;

        var modifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
        double base = 0;
        for (var entry : modifiers.modifiers()) {
            if (entry.attribute() == Attributes.ARMOR) {
                base = entry.modifier().amount();
                break;
            }
        }
        if (base <= 0) return;

        double boosted = base * (1.0 + 0.125 * level);
        DecimalFormat fmt = ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT;
        String suffix = " +" + fmt.format(boosted);

        List<Component> lines = cir.getReturnValue();
        for (int i = 0; i < lines.size(); i++) {
            String text = lines.get(i).getString();
            if (text.contains("盔甲") || text.contains("护甲") || text.contains("Armor")) {
                MutableComponent modified = lines.get(i).copy()
                        .append(Component.literal(suffix).withStyle(ChatFormatting.DARK_GREEN));
                lines.set(i, modified);
                break;
            }
        }
    }
}
