package com.mite.recraft.mixin;

import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

/**
 * {@link RecipeMap} 私有构造器的调用入口，供 {@link RecipeMapMixin} 构建过滤后的配方表。
 */
@Mixin(RecipeMap.class)
public interface RecipeMapInvoker {

    @Invoker("<init>")
    static RecipeMap mite$create(Multimap<RecipeType<?>, RecipeHolder<?>> byType,
                                 Map<ResourceKey<Recipe<?>>, RecipeHolder<?>> byKey) {
        throw new AssertionError();
    }
}
