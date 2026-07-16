package com.mite.recraft.client.mixin;

import com.mite.recraft.block.workbench.ModWorkbenchMenu;
import com.mite.recraft.block.workbench.WorkbenchMaterial;
import com.mite.recraft.client.MiteRecraftedClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 配方书视觉反馈：超过工作台等级的配方标记为不可合成。
 *
 * <p>注入 {@code selectRecipes} 末尾，用服务端同步的工作台材料系数
 * 对比配方结果物品的材料系数，超出者从 craftable 集合中移除。</p>
 */
@Mixin(RecipeCollection.class)
public class RecipeCollectionMixin {

    @Shadow
    private List<RecipeDisplayEntry> entries;

    @Shadow
    private Set<RecipeDisplayId> craftable;

    @Inject(method = "selectRecipes(Lnet/minecraft/world/entity/player/StackedItemContents;Ljava/util/function/Predicate;)V",
            at = @At("RETURN"))
    private void filterByWorkbenchTier(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        AbstractContainerMenu menu = player.containerMenu;
        if (menu == null) return;

        // 只在 MITE 工作台菜单中生效
        if (!isMiteWorkbench(menu)) return;

        float benchCoefficient = MiteRecraftedClient.syncedBenchCoefficient;
        ContextMap ctx = SlotDisplayContext.fromLevel(mc.level);
        if (ctx == null) return;

        Iterator<RecipeDisplayId> it = craftable.iterator();
        while (it.hasNext()) {
            RecipeDisplayId id = it.next();
            RecipeDisplayEntry entry = findEntry(id);
            if (entry == null) continue;

            ItemStack result = entry.display().result().resolveForFirstStack(ctx);
            if (!result.isEmpty()) {
                float required = getItemDurability(result);
                if (benchCoefficient < required) {
                    it.remove();
                }
            }
        }
    }

    private static boolean isMiteWorkbench(AbstractContainerMenu menu) {
        return menu instanceof ModWorkbenchMenu;
    }

    private RecipeDisplayEntry findEntry(RecipeDisplayId id) {
        for (RecipeDisplayEntry e : entries) {
            if (e.id().equals(id)) return e;
        }
        return null;
    }

    /** 与 TieredResultSlot.getItemDurability 逻辑一致 */
    private static float getItemDurability(ItemStack stack) {
        var key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (key == null || !key.getNamespace().equals("mite-recrafted")) return 1.0f;
        String path = key.getPath();
        for (WorkbenchMaterial mat : WorkbenchMaterial.values()) {
            if (path.contains(mat.getName()))
                return mat.getToolMaterial().getDurabilityCoefficient();
        }
        return 1.0f;
    }
}
