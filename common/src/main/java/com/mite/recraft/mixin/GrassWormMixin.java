package com.mite.recraft.mixin;

import com.mite.recraft.content.item.food.ModFoodItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 挖掘草方块时有几率掉落虫子（MITE 原版机制移植）。
 *
 * <p>规则：
 * <ul>
 *   <li>仅玩家挖掘</li>
 *   <li>寒冷生物群系（降雪地形）不掉落</li>
 *   <li>概率 = 1 / (16 - effective_fortune)</li>
 *   <li>下雨时 effective_fortune += 12</li>
 *   <li>effective_fortune 上限 14</li>
 * </ul>
 */
@Mixin(Block.class)
public class GrassWormMixin {

    @Inject(method = "playerDestroy", at = @At("TAIL"))
    private void mite$onPlayerDestroy(ServerLevel level, ServerPlayer player, BlockPos pos,
                                      BlockState state, BlockEntity blockEntity,
                                      ItemStack tool, CallbackInfo ci) {
        if (!(state.getBlock() instanceof GrassBlock)) return;
        if (level.getBiome(pos).value().coldEnoughToSnow(pos, level.getSeaLevel())) return;

        Holder<Enchantment> fortuneHolder = level.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.FORTUNE);
        int fortune = EnchantmentHelper.getItemEnchantmentLevel(fortuneHolder, tool);
        if (fortune > 3) fortune = 3;

        if (level.isRainingAt(pos.above())) {
            fortune += 12;
        }

        if (fortune > 14) fortune = 14;

        if (level.getRandom().nextInt(16 - fortune) == 0) {
            Block.popResource(level, pos, new ItemStack(ModFoodItems.WORM_RAW));
        }
    }
}
