package com.mite.recraft.content.item.throwable;

import com.mite.recraft.api.ItemCategory;
import com.mite.recraft.api.MiteRegistration;

import net.minecraft.world.item.Item;

/** 投掷物的物品注册。 */
public final class ModThrowables {

    /** 粘液球：MITE 的 subtype 0。 */
    public static final Item GELATINOUS_SPHERE = MiteRegistration.registerItem(ItemCategory.MATERIAL,
            "gelatinous_sphere", props -> new GelatinousSphereItem(props, 0));

    /** 软泥球：MITE 的 subtype 1。 */
    public static final Item ACIDIC_GELATINOUS_SPHERE = MiteRegistration.registerItem(ItemCategory.MATERIAL,
            "acidic_gelatinous_sphere", props -> new GelatinousSphereItem(props, 1));

    public static final Item BRICK = MiteRegistration.registerItem(ItemCategory.MATERIAL,
            "brick", BrickItem::new);

    public static void init() {
    }

    private ModThrowables() {
    }
}
