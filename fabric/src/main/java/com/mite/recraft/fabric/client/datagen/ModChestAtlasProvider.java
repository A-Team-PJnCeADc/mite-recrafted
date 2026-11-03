package com.mite.recraft.fabric.client.datagen;

import com.mite.recraft.chest.ChestVariant;
import com.mite.recraft.content.block.strongbox.StrongboxType;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * 生成 {@code assets/minecraft/atlases/chests.json}，为箱子图集登记原版目录源、本模组箱子贴图与保险箱贴图。
 * <p>
 * 原版目录源收录 {@code minecraft:textures/entity/chest/*.png}，箱子与保险箱贴图逐个登记为 single 源，
 * 供箱子渲染器按 sprite 查找。
 */
public class ModChestAtlasProvider extends FabricCodecDataProvider<List<SpriteSource>> {

    public ModChestAtlasProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, PackOutput.Target.RESOURCE_PACK, "atlases", SpriteSources.FILE_CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, List<SpriteSource>> provider, HolderLookup.Provider lookup) {
        List<SpriteSource> sources = new ArrayList<>();

        // 原版贴图目录
        sources.add(new DirectoryLister("entity/chest", "entity/chest/"));

        // 2. 箱子实体贴图
        for (ChestVariant variant : ChestVariant.REGISTERED) {
            sources.add(new SingleFile(variant.texture(), Optional.empty()));
        }
        // 保险箱贴图
        for (StrongboxType type : StrongboxType.VALUES) {
            sources.add(new SingleFile(type.texture, Optional.empty()));
        }

        provider.accept(Identifier.fromNamespaceAndPath("minecraft", "chests"), sources);
    }

    @Override
    public String getName() {
        return "MITE Chest Atlas";
    }
}
