package com.mite.recraft.item.material;

import com.mite.recraft.item.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;


public class ModMaterials {
    public static final Item FLINT_CHIP = registerMaterials("flint_chip", 64);
    public static final Item OBSIDIAN_CHIP = registerMaterials("obsidian_chip", 64);
    public static final Item EMERALD_CHIP = registerMaterials("emerald_chip", 64);
    public static final Item DIAMOND_CHIP = registerMaterials("diamond_chip", 64);
    public static final Item NETHER_QUARTZ_SHARD = registerMaterials("nether_quartz_shard", 64);
    public static final Item GLASS_SHARD = registerMaterials("glass_shard", 64);

    public static final Item COPPER_NUGGET = registerMaterials("copper_nugget", 64);
    public static final Item SILVER_NUGGET = registerMaterials("silver_nugget", 64);
    public static final Item IRON_NUGGET = registerMaterials("iron_nugget", 64);
    public static final Item ANCIENT_METAL_NUGGET = registerMaterials("ancient_metal_nugget", 64);
    public static final Item MITHRIL_NUGGET = registerMaterials("mithril_nugget", 64);
    public static final Item ADAMANTIUM_NUGGET = registerMaterials("adamantium_nugget", 64);

    public static final Item COPPER_INGOT = registerMaterials("copper_ingot", 8);
    public static final Item SILVER_INGOT = registerMaterials("silver_ingot", 8);
    public static final Item ANCIENT_METAL_INGOT = registerMaterials("ancient_metal_ingot", 8);
    public static final Item MITHRIL_INGOT = registerMaterials("mithril_ingot", 8);
    public static final Item ADAMANTIUM_INGOT = registerMaterials("adamantium_ingot", 8);

    public static final Item COPPER_CHAIN = registerMaterials("copper_chain", 8);
    public static final Item SILVER_CHAIN = registerMaterials("silver_chain", 8);
    public static final Item GOLDEN_CHAIN = registerMaterials("golden_chain", 8);
    public static final Item RUSTED_IRON_CHAIN = registerMaterials("rusted_iron_chain", 8);
    public static final Item IRON_CHAIN = registerMaterials("iron_chain", 8);
    public static final Item ANCIENT_METAL_CHAIN = registerMaterials("ancient_metal_chain", 8);
    public static final Item MITHRIL_CHAIN = registerMaterials("mithril_chain", 8);
    public static final Item ADAMANTIUM_CHAIN = registerMaterials("adamantium_chain", 8);

    public static final Item COPPER_COIN = registerMaterials("copper_coin", 64);
    public static final Item GOLDEN_COIN = registerMaterials("golden_coin", 64);
    public static final Item SILVER_COIN = registerMaterials("silver_coin", 64);
    public static final Item ANCIENT_METAL_COIN = registerMaterials("ancient_metal_coin", 64);
    public static final Item MITHRIL_COIN = registerMaterials("mithril_coin", 64);
    public static final Item ADAMANTIUM_COIN = registerMaterials("adamantium_coin", 64);

    // 注册物品
    private static Item registerMaterials(String path, int maxStack) {
        Identifier id = Identifier.fromNamespaceAndPath("mite-recrafted", path);
        Item item = new Item(new Item.Properties()
                .stacksTo(maxStack)
                .setId(ResourceKey.create(Registries.ITEM, id))
        );
        ModItems.addMaterial(item);
        return Registry.register(BuiltInRegistries.ITEM, id, item);
    }

    public static void init() {
    }
}
