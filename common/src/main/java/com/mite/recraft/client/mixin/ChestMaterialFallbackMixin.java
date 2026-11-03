package com.mite.recraft.client.mixin;

import com.mite.recraft.chest.ChestVariant;

import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.MaterialBaker;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 模型烘焙兜底：`ironchests` 命名空间下的贴图缺失时改用原版贴图。
 *
 * <p>平铺物品的贴图在模型烘焙阶段由 {@code MaterialBaker} 解析，缺失时不报错，只记录到
 * missingSprites 并返回缺失贴图，因此在返回值处替换。缺失判定用缺失贴图的文件标识
 * {@code minecraft:missingno}。</p>
 */
@Mixin(MaterialBaker.class)
public abstract class ChestMaterialFallbackMixin {

    private static final Identifier mite$MISSING_NAME = Identifier.withDefaultNamespace("missingno");

    @Inject(method = "get", at = @At("RETURN"), cancellable = true)
    private void mite$replaceMissing(Material material, ModelDebugName debugName,
                                     CallbackInfoReturnable<Material.Baked> cir) {
        Identifier sprite = material.sprite();
        if (!sprite.getNamespace().equals(ChestVariant.GPL_TEXTURE_NAMESPACE)) {
            return;
        }
        Identifier fallback = ChestVariant.fallbackTexture(sprite.getPath());
        if (fallback == null || !cir.getReturnValue().sprite().contents().name().equals(mite$MISSING_NAME)) {
            return;
        }
        MaterialBaker self = (MaterialBaker) (Object) this;
        cir.setReturnValue(self.get(new Material(fallback, material.forceTranslucent()), debugName));
    }
}
