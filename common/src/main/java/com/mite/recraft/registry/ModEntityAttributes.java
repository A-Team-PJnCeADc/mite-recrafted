package com.mite.recraft.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;

/**
 * 生物默认属性登记。各加载器把登记挂到自己的入口：Fabric 直接写入属性表，
 * NeoForge 在 EntityAttributeCreationEvent 里提交。
 */
public final class ModEntityAttributes {

    private ModEntityAttributes() {
    }

    @ExpectPlatform
    public static void register() {
        throw new AssertionError();
    }
}
