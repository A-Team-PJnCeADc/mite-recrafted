package com.mite.recraft.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

/**
 * 从配方表中移除原版配方，由本模组的配方替代。
 *
 * <p>26.x 起配方作为注册表条目加载，{@link RecipeMap#create} 是其唯一构建入口。</p>
 */
@Mixin(RecipeMap.class)
public class RecipeMapMixin {

    private static final Set<Identifier> REMOVED = Set.of(
            Identifier.fromNamespaceAndPath("minecraft", "bread"),
            Identifier.fromNamespaceAndPath("minecraft", "cookie"),
            Identifier.fromNamespaceAndPath("minecraft", "pumpkin_pie"),
            Identifier.fromNamespaceAndPath("minecraft", "cake")
    );

    @Inject(method = "create", at = @At("HEAD"), cancellable = true)
    private static void mite$removeVanillaRecipes(HolderLookup<Recipe<?>> lookup,
                                                  CallbackInfoReturnable<RecipeMap> cir) {
        ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> byType = ImmutableMultimap.builder();
        ImmutableMap.Builder<ResourceKey<Recipe<?>>, RecipeHolder<?>> byKey = ImmutableMap.builder();
        lookup.listElements()
                .filter(holder -> !REMOVED.contains(holder.key().identifier()))
                .forEach(holder -> {
                    RecipeHolder<?> recipe = new RecipeHolder<>(holder.key(), holder.value());
                    byType.put(holder.value().getType(), recipe);
                    byKey.put(holder.key(), recipe);
                });
        cir.setReturnValue(RecipeMapInvoker.mite$create(byType.build(), byKey.build()));
    }
}
