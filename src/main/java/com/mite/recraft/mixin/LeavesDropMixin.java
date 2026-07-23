package com.mite.recraft.mixin;

import com.mite.recraft.item.moditems.food.ModFoodItems;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 树叶破坏时额外掉落香蕉/橘子。
 * <ul>
 *   <li>丛林树叶 → 1/16 概率掉香蕉</li>
 *   <li>丛林生物群系的橡树树叶 → 1/24 概率掉橘子</li>
 * </ul>
 */
@Mixin(Block.class)
public class LeavesDropMixin {

    @Inject(method = "playerDestroy", at = @At("TAIL"))
    private void mite$onLeafDestroyed(Level level, Player player, BlockPos pos,
                                      BlockState state, BlockEntity blockEntity, ItemStack tool,
                                      CallbackInfo ci) {
        if (level.isClientSide()) return;

        // 只在丛林生物群系掉落 — 用 Identifier 比较
        var biomeKey = level.getBiome(pos).unwrapKey();
        if (biomeKey.isEmpty()) return;
        if (!biomeKey.get().identifier().getPath().contains("jungle")) return;

        Identifier leafId = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(state.getBlock());
        var random = level.getRandom();

        if (leafId.getPath().equals("jungle_leaves") && random.nextFloat() < 0.005F) {
            Block.popResource(level, pos, new ItemStack(ModFoodItems.BANANA));
        } else if (leafId.getPath().equals("oak_leaves")
                && random.nextFloat() < 0.005F) {
            Block.popResource(level, pos, new ItemStack(ModFoodItems.ORANGE));
        }
    }
}
