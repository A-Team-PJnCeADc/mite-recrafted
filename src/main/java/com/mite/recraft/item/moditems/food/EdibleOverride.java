package com.mite.recraft.item.moditems.food;

import com.mite.recraft.mixin.EdibleOverrideUseMixin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

/**
 * 原版不可食物品可食用
 */
public enum EdibleOverride implements FoodType {

    // 草种子 //todo AppleSkin
    WHEAT_SEEDS(Items.WHEAT_SEEDS, 0, 1, 0, 4000, 0, 0,1.6f, 16),
    // 南瓜种子
    PUMPKIN_SEEDS(Items.PUMPKIN_SEEDS, 1, 2, 0, 0, 0, 0,1.6f, 16),
    // 棕色蘑菇
    BROWN_MUSHROOM(Items.BROWN_MUSHROOM, 1, 1, 0, 0, 0, 0, 1.6f,16),
    ;

    private final Item vanillaItem;
    private final String itemId;
    private final int hunger;
    private final int rawSaturation;
    private final float saturationModifier;
    private final int protein;
    private final int phytonutrients;
    private final int essentialFats;
    private final int sugar;
    private final ContainerType containerType;
    private final int maxStackSize;
    private final float consumeSeconds;

    EdibleOverride(Item vanillaItem, int hunger, int saturation,
                   int protein, int phytonutrients, int essentialFats, int sugar,
                   float consumeSeconds, int maxStackSize) {
        this.vanillaItem = vanillaItem;
        this.itemId = BuiltInRegistries.ITEM.getKey(vanillaItem).getPath();
        this.hunger = hunger;
        this.rawSaturation = saturation;
        this.saturationModifier = hunger > 0 ? (float) saturation / (hunger * 2) : 0;
        this.protein = protein;
        this.phytonutrients = phytonutrients;
        this.essentialFats = essentialFats;
        this.sugar = sugar;
        this.containerType = ContainerType.NONE;
        this.maxStackSize = maxStackSize;
        this.consumeSeconds = consumeSeconds;
    }

    /** MC 26.2 物品组件不可变，此方法不再有效。由 {@link EdibleOverrideUseMixin} 替代。 */
    public static void init() {
    }

    @Override public String itemId() { return itemId; }
    @Override public int hunger() { return hunger; }
    @Override public float saturationModifier() { return saturationModifier; }
    @Override public int protein() { return protein; }
    @Override public int phytonutrients() { return phytonutrients; }
    @Override public int essentialFats() { return essentialFats; }
    @Override public int sugar() { return sugar; }
    @Override public ContainerType containerType() { return containerType; }
    @Override public int maxStackSize() { return maxStackSize; }

    public float consumeSeconds() { return consumeSeconds; }

    public int rawSaturation() { return rawSaturation; }

    /** 根据原版 Item 查找对应的 EdibleOverride */
    public static EdibleOverride fromItem(Item item) {
        for (EdibleOverride f : values()) {
            if (item.equals(f.vanillaItem)) return f;
        }
        return null;
    }
}
