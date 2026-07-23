package com.mite.recraft.mixin;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 移除原版配方，改为 MITE 配方系统。
 */
@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    private static final Set<Identifier> REMOVED_RECIPES = new HashSet<>();

    static {
        REMOVED_RECIPES.add(Identifier.fromNamespaceAndPath("minecraft", "bread"));
        REMOVED_RECIPES.add(Identifier.fromNamespaceAndPath("minecraft", "cookie"));
        REMOVED_RECIPES.add(Identifier.fromNamespaceAndPath("minecraft", "pumpkin_pie"));
        REMOVED_RECIPES.add(Identifier.fromNamespaceAndPath("minecraft", "cake"));
    }

    @Inject(method = "prepare", at = @At("RETURN"), cancellable = true)
    private void mite$removeVanillaRecipes(net.minecraft.server.packs.resources.ResourceManager resourceManager,
                                           net.minecraft.util.profiling.ProfilerFiller profiler,
                                           CallbackInfoReturnable<RecipeMap> cir) {
        RecipeMap original = cir.getReturnValue();
        if (original == null) return;

        java.util.Collection<RecipeHolder<?>> values = original.values();
        java.util.List<RecipeHolder<?>> filtered = new ArrayList<>();
        boolean changed = false;
        for (RecipeHolder<?> holder : values) {
            Identifier id = holder.id().identifier();
            if (REMOVED_RECIPES.contains(id)) {
                changed = true;
            } else {
                filtered.add(holder);
            }
        }
        if (changed) {
            cir.setReturnValue(RecipeMap.create(filtered));
        }
    }
}
