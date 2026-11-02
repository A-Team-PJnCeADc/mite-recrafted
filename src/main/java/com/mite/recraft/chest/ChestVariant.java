package com.mite.recraft.chest;

import com.mite.recraft.api.MiteIds;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jspecify.annotations.Nullable;

/**
 * 箱子规格：容量、每行格数、方块属性与实体贴图。
 *
 * <p>格数布局与 IronChests 一致：9 列配 5、6、9 行，12 列配 9 行，14 列配 9 行。
 * 所有格子一屏可见，不做分页。</p>
 *
 * <p>规格是不可变数据，注册结果存放在 {@link com.mite.recraft.registry.ModChests} 的映射里，
 * 不在规格对象上回写方块或菜单引用。</p>
 */
public record ChestVariant(String id, int capacity, int columns, int rows,
                           @Nullable Identifier entityTexture) {

    public static final ChestVariant COPPER =
            new ChestVariant("copper_chest", 45, 9, 5, null);
    public static final ChestVariant IRON =
            new ChestVariant("iron_chest", 54, 9, 6, null);
    public static final ChestVariant GOLD =
            new ChestVariant("gold_chest", 81, 9, 9, null);
    public static final ChestVariant DIAMOND =
            new ChestVariant("diamond_chest", 108, 12, 9, null);
    public static final ChestVariant EMERALD =
            new ChestVariant("emerald_chest", 108, 12, 9, null);
    public static final ChestVariant CRYSTAL =
            new ChestVariant("crystal_chest", 108, 12, 9, null);
    public static final ChestVariant OBSIDIAN =
            new ChestVariant("obsidian_chest", 108, 12, 9, null);
    public static final ChestVariant NETHERITE =
            new ChestVariant("netherite_chest", 126, 14, 9, null);
    public static final ChestVariant CHRISTMAS =
            new ChestVariant("christmas_chest", 27, 9, 3,
                    Identifier.withDefaultNamespace("entity/chest/christmas"));

    /** 参与注册的规格，顺序即创造模式物品栏顺序。 */
    public static final java.util.List<ChestVariant> REGISTERED = java.util.List.of(
            COPPER, IRON, GOLD, DIAMOND, EMERALD, CRYSTAL, OBSIDIAN, NETHERITE, CHRISTMAS);

    /** 箱子贴图所在的独立命名空间，与主项目资源分开分发。 */
    public static final String GPL_TEXTURE_NAMESPACE = "ironchests";

    /** 升级来源可用的规格，原版箱子对应 {@link #WOOD} 语义。 */
    public static final java.util.List<ChestVariant> ALL = java.util.List.of(
            COPPER, IRON, GOLD, DIAMOND, EMERALD, CRYSTAL, OBSIDIAN, NETHERITE, CHRISTMAS);

    public ChestVariant {
        if (capacity != columns * rows) {
            throw new IllegalArgumentException(id + ": 容量与行列不一致");
        }
    }

    public Identifier blockId() {
        return MiteIds.id(this.id);
    }

    /** 箱子实体贴图，需在图集 {@code minecraft:chests} 中注册。 */
    public Identifier texture() {
        if (this.entityTexture != null) {
            return this.entityTexture;
        }
        return Identifier.fromNamespaceAndPath(textureNamespace(), "entity/chest/" + this.id);
    }

    /** 贴图缺失时使用的替代贴图；不属于箱子贴图路径时返回 null。 */
    public static Identifier fallbackTexture(String path) {
        if (path.startsWith("entity/chest/")) {
            return Identifier.withDefaultNamespace("entity/chest/normal");
        }
        if (path.startsWith("item/chest/")) {
            return Identifier.withDefaultNamespace("item/netherite_upgrade_smithing_template");
        }
        return null;
    }

    /** 贴图所在命名空间：箱子贴图与主项目资源分开存放。 */
    public String textureNamespace() {
        return GPL_TEXTURE_NAMESPACE;
    }

    public BlockBehaviour.Properties blockProperties() {
        return switch (this.id) {
            case "copper_chest", "gold_chest" -> BlockBehaviour.Properties.of()
                    .strength(3.0F, 6.0F)
                    .sound(SoundType.COPPER)
                    .requiresCorrectToolForDrops();
            case "iron_chest" -> BlockBehaviour.Properties.of()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops();
            case "diamond_chest", "emerald_chest" -> BlockBehaviour.Properties.of()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops();
            case "crystal_chest" -> BlockBehaviour.Properties.of()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.AMETHYST)
                    .noOcclusion()
                    .requiresCorrectToolForDrops();
            case "obsidian_chest", "netherite_chest" -> BlockBehaviour.Properties.of()
                    .strength(50.0F, 1200.0F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops();
            default -> BlockBehaviour.Properties.of()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.WOOD);
        };
    }
}
