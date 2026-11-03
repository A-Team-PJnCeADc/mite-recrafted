package com.mite.recraft.fabric.client.datagen;

import com.mite.recraft.chest.ChestVariant;
import com.mite.recraft.registry.ModChests;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 方块实体类型标签生成器：登记 fastchest 兼容箱子。
 *
 * <p>标签条目用方块实体类型 id，与方块同 id；水晶箱有自己的展示逻辑，不列入。</p>
 */
public class ModBlockEntityTypeTagProvider extends FabricTagsProvider.BlockEntityTypeTagsProvider {

    /** 参与 fastchest 兼容的档位。 */
    private static final List<ChestVariant> FASTCHEST_COMPATIBLE = List.of(
            ChestVariant.CHRISTMAS, ChestVariant.COPPER, ChestVariant.DIAMOND,
            ChestVariant.EMERALD, ChestVariant.GOLD, ChestVariant.IRON,
            ChestVariant.NETHERITE, ChestVariant.OBSIDIAN);

    public ModBlockEntityTypeTagProvider(FabricPackOutput output,
                                         CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        TagAppender<BlockEntityType<?>> compatible = builder(TagKey.create(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath("fastchest", "compatible_chests")));
        for (ChestVariant variant : FASTCHEST_COMPATIBLE) {
            BlockEntityType<?> type = ModChests.storageType(variant);
            compatible.add(BuiltInRegistries.BLOCK_ENTITY_TYPE.getResourceKey(type).orElseThrow());
        }
    }
}
