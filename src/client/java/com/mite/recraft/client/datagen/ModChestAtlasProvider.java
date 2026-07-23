package com.mite.recraft.client.datagen;

import com.mite.recraft.item.moditems.strongbox.StrongboxType;
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
 * Generates {@code assets/minecraft/atlases/chests.json} for the CHEST_SHEET atlas.
 * <p>
 * Registers vanilla directory source, IronChests single sources, and MITE strongbox single sources
 * so the {@link net.minecraft.client.renderer.blockentity.ChestRenderer} can find all chest textures.
 */
public class ModChestAtlasProvider extends FabricCodecDataProvider<List<SpriteSource>> {

    public ModChestAtlasProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, PackOutput.Target.RESOURCE_PACK, "atlases", SpriteSources.FILE_CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, List<SpriteSource>> provider, HolderLookup.Provider lookup) {
        List<SpriteSource> sources = new ArrayList<>();

        // 1. Vanilla directory source: picks up minecraft:textures/entity/chest/*.png
        sources.add(new DirectoryLister("entity/chest", "entity/chest/"));

        // 2. IronChests single sources
        String[] ironChestIds = {
                "copper_chest", "iron_chest", "gold_chest", "diamond_chest",
                "emerald_chest", "crystal_chest", "obsidian_chest", "netherite_chest"
        };
        for (String id : ironChestIds) {
            sources.add(new SingleFile(
                    Identifier.fromNamespaceAndPath("ironchest", "entity/chest/" + id),
                    Optional.empty()
            ));
        }
        sources.add(new SingleFile(
                Identifier.fromNamespaceAndPath("minecraft", "entity/chest/christmas"),
                Optional.empty()
        ));

        // 3. MITE strongbox single sources
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
