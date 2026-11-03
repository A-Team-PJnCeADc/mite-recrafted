package com.mite.recraft.client.mixin;

import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * 暴露 SelectItemModelProperties.ID_MAPPER：原版该字段为 private，Fabric 侧由 Fabric API 的
 * 访问放宽变为 public，NeoForge 侧保持 private，因此统一走访问器注册自定义 select 属性。
 */
@Mixin(SelectItemModelProperties.class)
public interface SelectItemModelPropertiesAccessor {

    @Accessor("ID_MAPPER")
    static ExtraCodecs.LateBoundIdMapper<Identifier, SelectItemModelProperty.Type<?, ?>> getIdMapper() {
        throw new AssertionError();
    }
}
