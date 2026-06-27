package com.mite.recraft.block;

import com.mite.recraft.MiteRecrafted;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 方块类型枚举 —— 定义每种方块的属性，统一实例化。
 *
 * 使用方式：
 *   ModBlockType.DOOR.create("copper", 5.0F)
 *   ModBlockType.BARS.create("copper", 5.0F)
 *   ModBlockType.METAL_BLOCK.create("copper", 5.0F)
 */
public enum ModBlockType {
    // 暂定硬度
    DOOR("_door", 1,  DoorBlock::new),
    BARS("_bars", 16, (type, props) -> new IronBarsBlock(props.noOcclusion())),
    METAL_BLOCK("_block", 4, (type, props) -> new Block(props)),
    ;

    private static final Map<Block, Integer> STACK_SIZES = new LinkedHashMap<>();

    private final String suffix;
    private final int maxStackSize;
    private final BlockFactory factory;

    @FunctionalInterface
    public interface BlockFactory {
        Block create(BlockSetType blockSetType, Block.Properties properties);
    }

    ModBlockType(String suffix, int maxStackSize, BlockFactory factory) {
        this.suffix = suffix;
        this.maxStackSize = maxStackSize;
        this.factory = factory;
    }

    public String suffix() { return suffix; }
    public int maxStackSize() { return maxStackSize; }

    /** 创建一个材质的具体方块实例 */
    public Block create(String material, float hardness) {
        String name = material + suffix;
        Block block = factory.create(BlockSetType.IRON, properties(name, hardness));
        STACK_SIZES.put(block, maxStackSize);
        return block;
    }

    /** 金属方块通用 Properties */
    private static Block.Properties properties(String name, float hardness) {
        return Block.Properties.of().strength(hardness, hardness)
                .requiresCorrectToolForDrops().sound(SoundType.METAL)
                .setId(ResourceKey.create(Registries.BLOCK,
                        Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, name)));
    }

    /** 方块对应物品的最大堆叠数，未记录时返回默认 64 */
    public static int getMaxStackSize(Block block) {
        return STACK_SIZES.getOrDefault(block, 64);
    }
}
