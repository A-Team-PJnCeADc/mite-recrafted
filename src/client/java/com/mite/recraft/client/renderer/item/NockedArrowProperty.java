package com.mite.recraft.client.renderer.item;

import com.mite.recraft.item.tools.toolItem.ArrowItems;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

/**
 * 弓模型 Select 属性：返回箭矢材质索引 (0-9)，供 select 模型分发纹理
 */
public record NockedArrowProperty() implements SelectItemModelProperty<Integer> {

    public static final Codec<Integer> VALUE_CODEC = Codec.INT;
    public static final SelectItemModelProperty.Type<NockedArrowProperty, Integer> TYPE =
            SelectItemModelProperty.Type.create(MapCodec.unit(new NockedArrowProperty()), VALUE_CODEC);

    private static final Map<Item, Integer> ARROW_INDEX = Map.ofEntries(
            Map.entry(ArrowItems.FLINT_ARROW, 0),
            Map.entry(ArrowItems.OBSIDIAN_ARROW, 1),
            Map.entry(ArrowItems.COPPER_ARROW, 2),
            Map.entry(ArrowItems.SILVER_ARROW, 3),
            Map.entry(ArrowItems.GOLD_ARROW, 4),
            Map.entry(ArrowItems.RUSTED_IRON_ARROW, 5),
            Map.entry(ArrowItems.IRON_ARROW, 6),
            Map.entry(ArrowItems.ANCIENT_METAL_ARROW, 7),
            Map.entry(ArrowItems.MITHRIL_ARROW, 8),
            Map.entry(ArrowItems.ADAMANTIUM_ARROW, 9)
    );

    @Override
    public Integer get(ItemStack bowStack, ClientLevel level, LivingEntity entity, int seed,
                       ItemDisplayContext displayContext) {
        if (entity instanceof Player player) {
            ItemStack proj = player.getProjectile(bowStack);
            if (!proj.isEmpty()) {
                return ARROW_INDEX.getOrDefault(proj.getItem(), 0);
            }
        }
        return 0;
    }

    @Override
    public Codec<Integer> valueCodec() { return VALUE_CODEC; }

    @Override
    public Type<? extends SelectItemModelProperty<Integer>, Integer> type() { return TYPE; }

    /** 需要在 ClientModInitializer 中调用以注册到 ID_MAPPER */
    public static void register() {
        SelectItemModelProperties.ID_MAPPER.put(
                Identifier.fromNamespaceAndPath("mite-recrafted", "nocked_arrow"), TYPE);
    }
}
