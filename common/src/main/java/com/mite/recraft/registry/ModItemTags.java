package com.mite.recraft.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import static com.mite.recraft.MiteRecrafted.MOD_ID;

/**
 * 物品标签键。生成内容由 client 源集的 ModItemTagProvider 产出，此处只做运行期引用。
 */
public final class ModItemTags {

    private ModItemTags() {
    }

    public static final TagKey<Item> BUTCHERING_WEAPON = tag("butchering_weapon");
    public static final TagKey<Item> STUN_WEAPON = tag("stun_weapon");
    public static final TagKey<Item> VAMPIRIC_WEAPON = tag("vampiric_weapon");
    public static final TagKey<Item> SLAYING_WEAPON = tag("slaying_weapon");
    public static final TagKey<Item> HARVESTING_TOOL = tag("harvesting_tool");
    public static final TagKey<Item> PIERCING_TOOL = tag("piercing_tool");
    public static final TagKey<Item> CLEAVING_WEAPON = tag("cleaving_weapon");
    public static final TagKey<Item> TREE_FELLING_TOOL = tag("tree_felling_tool");

    private static TagKey<Item> tag(String path) {
        return TagKey.create(BuiltInRegistries.ITEM.key(),
                Identifier.fromNamespaceAndPath(MOD_ID, path));
    }
}
