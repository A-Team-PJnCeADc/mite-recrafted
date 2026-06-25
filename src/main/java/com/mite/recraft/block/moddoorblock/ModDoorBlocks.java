package com.mite.recraft.block.moddoorblock;

import com.mite.recraft.MiteRecrafted;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;

/**
 * 金属门 —— 同铁门，仅红石可开
 */
//todo硬度暂定
public class ModDoorBlocks {
    public static final Block COPPER_DOOR = create("copper_door", 5.0F);
    public static final Block SILVER_DOOR = create("silver_door", 5.0F);
    public static final Block GOLD_DOOR = create("gold_door", 3.0F);
    public static final Block ANCIENT_METAL_DOOR = create("ancient_metal_door", 5.0F);
    public static final Block MITHRIL_DOOR = create("mithril_door", 8.0F);
    public static final Block ADAMANTIUM_DOOR = create("adamantium_door", 12.0F);

    private static Block create(String name, float hardness) {
        return new DoorBlock(BlockSetType.IRON,
                Block.Properties.of().strength(hardness, hardness)
                        .requiresCorrectToolForDrops().sound(SoundType.METAL)
                        .setId(ResourceKey.create(Registries.BLOCK,
                                Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, name))));
    }
}
