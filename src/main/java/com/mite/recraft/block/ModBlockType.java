package com.mite.recraft.block;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.block.anvil.ModAnvilBlock;
import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
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
 */
public enum ModBlockType {
    DOOR("_door", 1,  DoorBlock::new),
    BARS("_bars", 16, (type, props) -> new IronBarsBlock(props.noOcclusion())),
    METAL_BLOCK("_block", 4, (type, props) -> new Block(props)),
    ANVIL("_anvil",  1, (type, props) -> new ModAnvilBlock(props, 1.0F)),
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

    /** 通用创建 */
    public Block create(String material, float hardness) {
        String name = material + suffix;
        Block block = factory.create(BlockSetType.IRON, properties(name, hardness));
        STACK_SIZES.put(block, maxStackSize);
        return block;
    }

    /** ANVIL 专用创建 — 传入 ModToolMaterial*/
    public Block create(String material, float hardness, ModToolMaterial toolMat) {
        String name = material + suffix;
        Block block = factory.create(BlockSetType.IRON, properties(name, hardness));
        if (block instanceof ModAnvilBlock anvil) {
            anvil.setMaterialData(toolMat.getDurabilityCoefficient(), toolMat.getToolDurability());
        }
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

    /** 方块对应物品的最大堆叠数 */
    public static int getMaxStackSize(Block block) {
        return STACK_SIZES.getOrDefault(block, 64);
    }
}
