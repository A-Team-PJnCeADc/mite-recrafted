package com.mite.recraft.content.combat;

import com.mite.recraft.MiteRecrafted;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

/**
 * MITE 1.6.4 的溶解规则（Block.getDissolvePeriod 与 Material 的 pepsin/acid 标记）。
 *
 * <p>返回值单位是 tick：0 表示接触即毁，正数表示需要累积的溶解时间，-1 表示不溶解。</p>
 */
public final class MiteDissolve {

    /** 溶解类型：pepsin 对应粘液/凝胶，acid 对应软泥/布丁。 */
    public enum Type {
        PEPSIN,
        ACID
    }

    private static final int IMMEDIATE = 0;
    private static final int SLOW = 400;
    private static final int NEVER = -1;

    /** 缓慢溶解的方块：门、箱子、压力板、玻璃板、栏杆、梯子、床、活塞等。 */
            private static final Set<Block> SLOW_BLOCKS = resolve(
            "iron_door", "oak_door", "spruce_door", "birch_door", "jungle_door", "acacia_door",
            "dark_oak_door", "cherry_door", "mangrove_door", "bamboo_door", "crimson_door", "warped_door",
            "chest", "trapped_chest", "ender_chest", "barrel", "light_weighted_pressure_plate", "heavy_weighted_pressure_plate",
            "stone_pressure_plate", "oak_pressure_plate", "spruce_pressure_plate", "birch_pressure_plate", "jungle_pressure_plate", "acacia_pressure_plate",
            "dark_oak_pressure_plate", "cherry_pressure_plate", "mangrove_pressure_plate", "bamboo_pressure_plate", "crimson_pressure_plate", "warped_pressure_plate",
            "polished_blackstone_pressure_plate", "glass_pane", "iron_bars", "rail", "powered_rail", "detector_rail",
            "activator_rail", "lever", "stone_button", "oak_button", "spruce_button", "birch_button",
            "jungle_button", "acacia_button", "dark_oak_button", "crimson_button", "warped_button", "bamboo_button",
            "oak_sign", "oak_wall_sign", "spruce_sign", "birch_sign", "jungle_sign", "acacia_sign",
            "dark_oak_sign", "cherry_sign", "ladder", "scaffolding", "piston", "sticky_piston",
            "piston_head", "moving_piston", "cactus", "melon", "pumpkin", "carved_pumpkin",
            "jack_o_lantern", "repeater", "comparator", "daylight_detector", "enchanting_table", "skeleton_skull",
            "skeleton_wall_skull", "hopper", "hay_block", "cauldron", "water_cauldron", "lava_cauldron",
            "powder_snow_cauldron", "oak_fence", "oak_fence_gate", "cocoa", "tripwire", "iron_block",
            "copper_block", "anvil", "chipped_anvil", "damaged_anvil");

    /** 不溶于酸的材料方块：金、秘银、精金、雪。 */
    private static final Set<Block> ACID_IMMUNE_BLOCKS = Set.<Block>of(
            Blocks.GOLD_BLOCK, Blocks.SNOW, Blocks.SNOW_BLOCK, Blocks.POWDER_SNOW);

    /** 遇酸立即溶解的方块：羊毛、地毯、树叶、睡莲、蛋糕、作物与花草。 */
    private static final Set<Block> ACID_IMMEDIATE_BLOCKS = Set.<Block>of(
            Blocks.LILY_PAD, Blocks.CAKE, Blocks.REDSTONE_WIRE);

    private MiteDissolve() {
    }

    /** 把原版方块常量名批量解析成 Block，找不到时跳过。 */
    private static Set<Block> resolve(String... names) {
        Set<Block> blocks = new java.util.HashSet<>();
        for (String name : names) {
            Block block = BuiltInRegistries.BLOCK.getValue(
                    Identifier.withDefaultNamespace(name.toLowerCase(java.util.Locale.ROOT)));
            if (block != null) {
                blocks.add(block);
            }
        }
        return Set.copyOf(blocks);
    }

    /** 方块在该溶解类型下的溶解时间（tick），-1 表示不溶解。 */
    public static int period(BlockState state, Type type) {
        if (state.isAir()) {
            return NEVER;
        }
        Block block = state.getBlock();

        if (type == Type.PEPSIN) {
            if (state.is(BlockTags.WOOL) || state.is(BlockTags.WOOL_CARPETS) || block == Blocks.CAKE) {
                return SLOW;
            }
            return NEVER;
        }

        if (state.is(BlockTags.WOOL) || state.is(BlockTags.WOOL_CARPETS) || state.is(BlockTags.LEAVES)) {
            return IMMEDIATE;
        }
        if (ACID_IMMEDIATE_BLOCKS.contains(block)) {
            return IMMEDIATE;
        }
        if (state.is(BlockTags.CROPS) || state.is(BlockTags.FLOWERS) || state.is(BlockTags.SAPLINGS)
                || state.is(BlockTags.REPLACEABLE_BY_TREES)) {
            return IMMEDIATE;
        }
        if (ACID_IMMUNE_BLOCKS.contains(block) || block == Blocks.STONE_BUTTON) {
            return NEVER;
        }
        if (isMetalStorage(state)) {
            return SLOW;
        }
        if (SLOW_BLOCKS.contains(block) || state.is(BlockTags.WOODEN_DOORS)
                || state.is(BlockTags.WOODEN_FENCES) || state.is(BlockTags.FENCE_GATES)
                || state.is(BlockTags.ALL_SIGNS) || state.is(BlockTags.BUTTONS)
                || state.is(BlockTags.PRESSURE_PLATES) || state.is(BlockTags.RAILS)
                || state.is(BlockTags.WOODEN_PRESSURE_PLATES) || state.is(BlockTags.BEDS)) {
            return SLOW;
        }
        // 其余固体方块（石、土、木、砖等）缓慢溶解，与 MITE 的默认材料标记一致。
        return state.isSolidRender() ? SLOW : IMMEDIATE;
    }

    /** 本模组的金属块按材料名识别，金、秘银、精金不溶于酸。 */
    private static boolean isMetalStorage(BlockState state) {
        Identifier id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        if (!MiteRecrafted.MOD_ID.equals(id.getNamespace())) {
            return false;
        }
        String path = id.getPath();
        if (!path.endsWith("_block")) {
            return false;
        }
        return !path.startsWith("gold") && !path.startsWith("mithril") && !path.startsWith("adamantium");
    }

    /** 物品是否被该溶解类型侵蚀：pepsin 侵蚀食物、皮革、布料、羽毛；acid 侵蚀金属与石材制品。 */
    public static boolean harmsItem(ItemStack stack, Type type) {
        if (stack.isEmpty()) {
            return false;
        }
        Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String path = id.getPath();

        if (type == Type.PEPSIN) {
            return stack.has(DataComponents.FOOD) || path.contains("leather") || path.contains("wool")
                    || path.contains("cloth") || path.contains("string") || path.contains("feather")
                    || path.contains("cake") || path.contains("bowl");
        }

        if (path.startsWith("gold") || path.startsWith("mithril") || path.startsWith("adamantium")) {
            return false;
        }
        return path.contains("iron") || path.contains("copper") || path.contains("silver")
                || path.contains("ancient_metal") || path.contains("anvil")
                || path.contains("chain") || path.contains("minecart") || path.contains("cauldron");
    }
}
