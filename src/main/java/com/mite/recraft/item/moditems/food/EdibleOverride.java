package com.mite.recraft.item.moditems.food;

import com.mite.recraft.MiteRecrafted;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;

/**
 * 原版不可食物品可食用覆盖 — 让不能吃的原版物品变得可吃。
 */
//todo 面包、曲奇、南瓜派和蛋糕
public enum EdibleOverride implements FoodType {

    // 草种子
    WHEAT_SEEDS(Items.WHEAT_SEEDS, 0, 1, 0, 4000, 0, 0,1.6f, 16),
    // 南瓜种子
    PUMPKIN_SEEDS(Items.PUMPKIN_SEEDS, 1, 2, 0, 0, 0, 0,1.6f, 16),
    // 烤马铃薯
    BROWN_MUSHROOM(Items.BROWN_MUSHROOM, 1, 1, 0, 0, 0, 0, 1.6f,16)
    ;

    private final Item vanillaItem;
    private final String itemId;
    private final int hunger;
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
        this.saturationModifier = hunger > 0 ? (float) saturation / (hunger * 2) : 0;
        this.protein = protein;
        this.phytonutrients = phytonutrients;
        this.essentialFats = essentialFats;
        this.sugar = sugar;
        this.containerType = ContainerType.NONE;
        this.maxStackSize = maxStackSize;
        this.consumeSeconds = consumeSeconds;
    }

    /** 为所有条目添加 CONSUMABLE + FOOD 数据组件，使物品可吃。 */
    public static void init() {
        for (EdibleOverride f : values()) {
            if (f.consumeSeconds <= 0) continue;

            FoodProperties food = new FoodProperties.Builder()
                    .nutrition(f.hunger())
                    .saturationModifier(f.saturationModifier())
                    .build();

            Consumable consumable = Consumable.builder()
                    .consumeSeconds(f.consumeSeconds)
                    .animation(ItemUseAnimation.EAT)
                    .build();

            try {
                var comps = f.vanillaItem.components();
                if (comps instanceof PatchedDataComponentMap patch) {
                    patch.set(DataComponents.CONSUMABLE, consumable);
                    patch.set(DataComponents.FOOD, food);
                    patch.set(DataComponents.MAX_STACK_SIZE, f.maxStackSize);
                } else {
                    MiteRecrafted.LOGGER.warn("Cannot make {} edible: components not mutable",
                            BuiltInRegistries.ITEM.getKey(f.vanillaItem));
                }
            } catch (NullPointerException e) {
                MiteRecrafted.LOGGER.warn("Cannot make {} edible: components not bound yet",
                        BuiltInRegistries.ITEM.getKey(f.vanillaItem));
            }
        }
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

    /** 根据原版 Item 查找对应的 EdibleOverride */
    public static EdibleOverride fromItem(Item item) {
        for (EdibleOverride f : values()) {
            if (item.equals(f.vanillaItem)) return f;
        }
        return null;
    }
}
