package com.mite.recraft.mixin;

import com.mite.recraft.block.furnace.MiteFurnaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * MITE 熔炉机制 Mixin：
 * 1. 配方按热量过滤（粘土仅食物/粘土，大粘土/沙石+原木等）
 * 2. 流水熄灭（熔炉前方有水源时熄灭）
 * 3. 前方方块阻挡（熔炉前方有实心方块时熄灭并阻止 GUI）
 */
@Mixin(AbstractFurnaceBlockEntity.class)
public class MiteFurnaceRecipeFilterMixin {

    // ======================== 配方过滤 ========================

    @Inject(method = "serverTick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;setRecipeUsed(Lnet/minecraft/world/item/crafting/RecipeHolder;)V",
            shift = At.Shift.BEFORE), cancellable = true)
    private static void mite$filterRecipeByHeat(ServerLevel level, BlockPos pos, BlockState state,
                                                 AbstractFurnaceBlockEntity be, CallbackInfo ci) {
        if (!(be instanceof MiteFurnaceBlockEntity miteFurnace)) return;
        if (level.isClientSide()) return;

        int maxHeat = miteFurnace.getTier().maxHeat;
        ItemStack input = be.getItem(0);
        if (input.isEmpty()) return;

        // ======= 沙→砂岩/玻璃 热量区分 =======
        if (input.is(Items.SAND)) {
            int heat = com.mite.recraft.block.furnace.FuelHeatHelper.getHeatLevel(be.getItem(1));
            if (heat == 1) {
                // 热1: 4沙→1砂岩（手动处理，跳过配方）
                ItemStack output = be.getItem(2);
                if (output.isEmpty() || output.is(Items.SANDSTONE)) {
                    if (input.getCount() >= 4) {
                        if (output.isEmpty()) {
                            be.setItem(2, new ItemStack(Items.SANDSTONE));
                        } else if (output.getCount() < output.getMaxStackSize()) {
                            output.grow(1);
                        }
                        input.shrink(4);
                    }
                }
                ((AccessorFurnaceBlockEntity) be).mite$setCookingProgress(0);
                ci.cancel();
                return;
            }
            // 热2+: 由 vanilla 玻璃配方处理
        }

        // ======= 通用配方按热量过滤 =======
        if (!isAllowedForTier(input, maxHeat)) {
            ((AccessorFurnaceBlockEntity) be).mite$setCookingProgress(0);
            ci.cancel();
        }
    }

    // ======================== 流水熄灭 + 前方阻挡 ========================

    @Inject(method = "serverTick", at = @At("HEAD"))
    private static void mite$checkExtinguish(ServerLevel level, BlockPos pos, BlockState state,
                                              AbstractFurnaceBlockEntity be, CallbackInfo ci) {
        if (!(be instanceof MiteFurnaceBlockEntity)) return;
        if (level.isClientSide()) return;

        var accessor = (AccessorFurnaceBlockEntity) be;
        if (accessor.mite$getLitTimeRemaining() <= 0 && accessor.mite$getCookingProgress() <= 0) return;

        Direction facing = state.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING);

        if (isFlooded(level, pos, facing)) {
            accessor.mite$setLitTimeRemaining(0);
            accessor.mite$setCookingProgress(0);
            level.setBlock(pos, state.setValue(AbstractFurnaceBlock.LIT, false), 3);
            return;
        }

        if (isSmothered(level, pos, facing)) {
            accessor.mite$setLitTimeRemaining(0);
            accessor.mite$setCookingProgress(0);
            level.setBlock(pos, state.setValue(AbstractFurnaceBlock.LIT, false), 3);
        }
    }

    private static boolean isFlooded(ServerLevel level, BlockPos pos, Direction facing) {
        BlockPos frontPos = pos.relative(facing);
        FluidState fluid = level.getFluidState(frontPos);
        return fluid.is(Fluids.WATER) || fluid.is(Fluids.FLOWING_WATER);
    }

    private static boolean isSmothered(ServerLevel level, BlockPos pos, Direction facing) {
        BlockPos frontPos = pos.relative(facing);
        BlockState frontState = level.getBlockState(frontPos);
        return frontState.isFaceSturdy(level, frontPos, facing.getOpposite());
    }

    // ======================== 配方白名单 ========================

    private static boolean isAllowedForTier(ItemStack input, int maxHeat) {
        if (maxHeat == 1) {
            if (input.has(net.minecraft.core.component.DataComponents.FOOD)) return true;
            if (input.is(Items.CLAY)) return true;
            return false;
        }
        if (maxHeat <= 1 && !input.is(Items.CLAY)) {
            if (input.is(Items.OAK_LOG) || input.is(Items.SPRUCE_LOG) || input.is(Items.BIRCH_LOG) ||
                input.is(Items.JUNGLE_LOG) || input.is(Items.ACACIA_LOG) || input.is(Items.DARK_OAK_LOG) ||
                input.is(Items.MANGROVE_LOG) || input.is(Items.CHERRY_LOG) ||
                input.is(Items.CACTUS) || input.is(Items.NETHERRACK))
                return true;
            return false;
        }
        return true;
    }
}
