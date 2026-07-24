package com.mite.recraft.item.moditems.armor;

import com.mite.recraft.item.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.equipment.EquipmentAsset;

import static com.mite.recraft.MiteRecrafted.MOD_ID;

/**
 * MITE 马铠 — 铜/银/远古金属/秘银/艾德曼。
 * 防御值: 铜+4/银+4/远古金属+5/秘银+6/艾德曼+7。
 * 装备纹理: assets/mite-recrafted/equipment/<material>_horse.json
 *           → textures/entity/equipment/horse_body/<material>_horse.png
 */
public class ModHorseArmorItems {
    public static final Item COPPER_HORSE_ARMOR = create("copper_horse_armor", 4.0f, ModEquipmentAssets.COPPER_HORSE);
    public static final Item SILVER_HORSE_ARMOR = create("silver_horse_armor", 4.0f, ModEquipmentAssets.SILVER_HORSE);
    public static final Item ANCIENT_METAL_HORSE_ARMOR = create("ancient_metal_horse_armor", 5.0f, ModEquipmentAssets.ANCIENT_METAL_HORSE);
    public static final Item MITHRIL_HORSE_ARMOR = create("mithril_horse_armor", 6.0f, ModEquipmentAssets.MITHRIL_HORSE);
    public static final Item ADAMANTIUM_HORSE_ARMOR = create("adamantium_horse_armor", 7.0f, ModEquipmentAssets.ADAMANTIUM_HORSE);

    public static final Item[] ALL = {
            COPPER_HORSE_ARMOR, SILVER_HORSE_ARMOR, ANCIENT_METAL_HORSE_ARMOR,
            MITHRIL_HORSE_ARMOR, ADAMANTIUM_HORSE_ARMOR
    };

    static {
        for (Item item : ALL) {
            ModItems.addArmor(item);
        }
    }

    private static Item create(String id, float protection, ResourceKey<EquipmentAsset> asset) {
        Identifier identifier = Identifier.fromNamespaceAndPath(MOD_ID, id);
        ItemAttributeModifiers armorAttrs = ItemAttributeModifiers.builder()
                .add(Attributes.ARMOR,
                        new AttributeModifier(
                                Identifier.fromNamespaceAndPath(MOD_ID, "armor." + id),
                                protection,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.BODY)
                .build();

        Item item = new Item(new Item.Properties()
                .setId(ResourceKey.create(BuiltInRegistries.ITEM.key(), identifier))
                .stacksTo(1)
                .attributes(armorAttrs)
                .component(DataComponents.EQUIPPABLE,
                        Equippable.builder(EquipmentSlot.BODY)
                                .setEquipSound(SoundEvents.HORSE_ARMOR)
                                .setAsset(asset)
                                .build()));
        Registry.register(BuiltInRegistries.ITEM, identifier, item);
        return item;
    }

    public static void init() {}
}
