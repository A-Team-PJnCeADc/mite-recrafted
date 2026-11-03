package com.mite.recraft.client.mixin;

import com.mite.recraft.chest.ChestVariant;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 图集取图兜底：`ironchests` 命名空间下的贴图缺失时改用原版贴图。
 *
 * <p>方块实体与特殊模型在渲染期通过 {@code AtlasManager.get} 取 sprite，缺失时返回缺失贴图，
 * 因此在返回处替换。缺失判定用缺失贴图的文件标识 {@code minecraft:missingno}。</p>
 */
@Mixin(AtlasManager.class)
public abstract class ChestSpriteFallbackMixin {

    private static final Identifier mite$MISSING_NAME = Identifier.withDefaultNamespace("missingno");

    @Inject(method = "get", at = @At("RETURN"), cancellable = true)
    private void mite$replaceMissing(SpriteId id, CallbackInfoReturnable<TextureAtlasSprite> cir) {
        if (!id.texture().getNamespace().equals(ChestVariant.GPL_TEXTURE_NAMESPACE)) {
            return;
        }
        Identifier fallback = ChestVariant.fallbackTexture(id.texture().getPath());
        if (fallback == null || !cir.getReturnValue().contents().name().equals(mite$MISSING_NAME)) {
            return;
        }
        AtlasManager self = (AtlasManager) (Object) this;
        TextureAtlasSprite replacement = self.get(new SpriteId(id.atlasLocation(), fallback));
        if (!replacement.contents().name().equals(mite$MISSING_NAME)) {
            cir.setReturnValue(replacement);
        }
    }
}
