package com.mite.recraft.block.furnace;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

/**
 * MITE 熔炉等级 — 匹配原版 MITE 1.6.4。
 *
 * <p>所有熔炉接受热1～maxHeat 的燃料（minHeat 均为 1）。
 *
 * <ul>
 *   <li>粘土熔炉、大粘土烤炉、沙石熔炉 → max 1
 *   <li>圆石熔炉 → max 2
 *   <li>黑曜石熔炉 → max 3
 *   <li>地狱岩熔炉 → max 4
 * </ul>
 */
public enum FurnaceTier {
    CLAY("clay_oven", "clay", 1, MapColor.COLOR_LIGHT_GRAY, SoundType.STONE, 0.5F, 3.0F),
    LARGE_CLAY("large_clay_oven", "hardened_clay", 1, MapColor.TERRACOTTA_ORANGE, SoundType.STONE, 0.5F, 3.0F),
    SANDSTONE("sandstone_furnace", "sandstone", 1, MapColor.SAND, SoundType.STONE, 1.5F, 6.0F),
    STONE("stone_furnace", "cobblestone", 2, MapColor.STONE, SoundType.STONE, 3.5F, 6.0F),
    OBSIDIAN("obsidian_furnace", "obsidian", 3, MapColor.COLOR_BLACK, SoundType.STONE, 5.0F, 1200.0F),
    NETHERRACK("netherrack_furnace", "netherrack", 4, MapColor.NETHER, SoundType.NETHERRACK, 0.4F, 6.0F);

    public static final FurnaceTier[] VALUES = values();

    public final String registryId;
    public final String textureBase;
    public final int maxHeat;
    public final BlockBehaviour.Properties properties;

    FurnaceTier(String registryId, String textureBase, int maxHeat,
                MapColor mapColor, SoundType soundType,
                float hardness, float resistance) {
        this.registryId = registryId;
        this.textureBase = textureBase;
        this.maxHeat = maxHeat;
        this.properties = BlockBehaviour.Properties.of()
                .mapColor(mapColor)
                .strength(hardness, resistance)
                .sound(soundType)
                .requiresCorrectToolForDrops();
    }
}
