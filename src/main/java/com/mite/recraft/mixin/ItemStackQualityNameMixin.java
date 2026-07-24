package com.mite.recraft.mixin;

import com.mite.recraft.component.ModDataComponents;
import com.mite.recraft.item.quality.Quality;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin ItemStack.getHoverName() 自动为有 QUALITY 组件的物品添加品质前缀。
 * <p>
 * 品质为 AVERAGE 时不显示前缀，其余品质（POOR～LEGENDARY）均显示。
 * 对已铁砧重命名的物品（有 CUSTOM_NAME）同样有效，品质前缀附加在原自定义名前。
 * </p>
 */
@Mixin(ItemStack.class)
public abstract class ItemStackQualityNameMixin {

    @Inject(method = "getHoverName", at = @At("RETURN"), cancellable = true)
    private void miteRecrafted$prependQuality(CallbackInfoReturnable<Component> cir) {
        ItemStack self = (ItemStack) (Object) this;
        Quality quality = self.get(ModDataComponents.QUALITY);
        if (quality == null) return;
        if (quality == Quality.AVERAGE) return;

        Component qualityName = Component.translatable("quality.mite-recraft." + quality.getSerializedName());
        Component original = cir.getReturnValue();
        Component fullName = Component.translatable("gui.mite-recraft.workbench.quality_prefix",
                qualityName, original)
                .withStyle(style -> style.withItalic(false));
        cir.setReturnValue(fullName);
    }
}
