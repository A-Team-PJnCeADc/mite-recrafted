package com.mite.recraft.mixin;

import com.mite.recraft.item.ModItems;
import com.mite.recraft.item.moditems.bucket.ModEmptyBucketItem;
import com.mite.recraft.item.moditems.bucket.ModFilledBucketItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

/**
 * 在 CauldronInteractions.bootStrap() 末尾注册 MITE 金属桶。
 */
@Mixin(targets = "net.minecraft.core.cauldron.CauldronInteractions")
public class BucketCauldronMixin {

    @Shadow @Final private static CauldronInteraction.Dispatcher EMPTY;
    @Shadow @Final private static CauldronInteraction.Dispatcher WATER;
    @Shadow @Final private static CauldronInteraction.Dispatcher LAVA;

    @Inject(method = "bootStrap", at = @At("TAIL"))
    private static void onBootStrap(CallbackInfo ci) {
        for (var miteBucket : ModItems.getBuckets()) {
            boolean isFilled = false;

            if (miteBucket instanceof ModFilledBucketItem filled) {
                isFilled = true;
                if (filled.fluid != null) {
                    CauldronInteraction fillHandler;
                    if (filled.fluid.is(FluidTags.WATER)) {
                        fillHandler = BucketCauldronMixin::fillWaterCauldron;
                    } else if (filled.fluid.is(FluidTags.LAVA)) {
                        fillHandler = BucketCauldronMixin::fillLavaCauldron;
                    } else {
                        continue;
                    }
                    put(EMPTY, miteBucket, fillHandler);
                    put(WATER, miteBucket, fillHandler);
                    put(LAVA, miteBucket, fillHandler);
                }
            }

            if (!isFilled && miteBucket instanceof ModEmptyBucketItem emptyBucket) {
                ItemStack waterResult = emptyBucket.waterPeer != null
                    ? new ItemStack(emptyBucket.waterPeer) : new ItemStack(Items.WATER_BUCKET);
                ItemStack lavaResult = emptyBucket.lavaPeer != null
                    ? new ItemStack(emptyBucket.lavaPeer) : new ItemStack(Items.LAVA_BUCKET);

                CauldronInteraction takeWater = (state, level, pos, player, hand, stack) ->
                    takeFromCauldron(state, level, pos, player, hand, stack,
                        s -> s.is(Blocks.WATER_CAULDRON), waterResult, SoundEvents.BUCKET_FILL);
                CauldronInteraction takeLava = (state, level, pos, player, hand, stack) ->
                    takeFromCauldron(state, level, pos, player, hand, stack,
                        s -> s.is(Blocks.LAVA_CAULDRON), lavaResult, SoundEvents.BUCKET_FILL_LAVA);

                put(WATER, miteBucket, takeWater);
                put(LAVA, miteBucket, takeLava);
            }
        }
    }

    /** 通过 accessor 调用 Dispatcher.put (package-private) */
    private static void put(CauldronInteraction.Dispatcher dispatcher, net.minecraft.world.item.Item item,
                            CauldronInteraction handler) {
        ((CauldronDispatcherAccessor)(Object)dispatcher).callPut(item, handler);
    }

    // ====== 交互处理器 ======

    private static InteractionResult fillWaterCauldron(BlockState state, Level level, BlockPos pos,
                                                       Player player, InteractionHand hand, ItemStack stack) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (state.is(Blocks.WATER_CAULDRON)) {
            int lvl = state.getValue(LayeredCauldronBlock.LEVEL);
            if (lvl >= LayeredCauldronBlock.MAX_FILL_LEVEL) return InteractionResult.PASS;
            level.setBlock(pos, state.setValue(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MAX_FILL_LEVEL), 3);
        } else if (state.is(Blocks.CAULDRON)) {
            level.setBlock(pos, Blocks.WATER_CAULDRON.defaultBlockState()
                .setValue(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MAX_FILL_LEVEL), 3);
        } else {
            return InteractionResult.PASS;
        }
        player.awardStat(Stats.FILL_CAULDRON);
        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
        ItemStack emptyResult = stack.getItem() instanceof ModFilledBucketItem filled && filled.emptyPeer != null
            ? new ItemStack(filled.emptyPeer) : new ItemStack(Items.BUCKET);
        ItemUtils.createFilledResult(stack, player, emptyResult);
        return InteractionResult.SUCCESS;
    }

    private static InteractionResult fillLavaCauldron(BlockState state, Level level, BlockPos pos,
                                                      Player player, InteractionHand hand, ItemStack stack) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (state.is(Blocks.CAULDRON) || state.is(Blocks.LAVA_CAULDRON)) {
            level.setBlock(pos, Blocks.LAVA_CAULDRON.defaultBlockState(), 3);
        } else {
            return InteractionResult.PASS;
        }
        player.awardStat(Stats.FILL_CAULDRON);
        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        level.playSound(null, pos, SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
        ItemStack emptyResult = stack.getItem() instanceof ModFilledBucketItem filled && filled.emptyPeer != null
            ? new ItemStack(filled.emptyPeer) : new ItemStack(Items.BUCKET);
        ItemUtils.createFilledResult(stack, player, emptyResult);
        return InteractionResult.SUCCESS;
    }

    private static InteractionResult takeFromCauldron(BlockState state, Level level, BlockPos pos,
                                                       Player player, InteractionHand hand, ItemStack stack,
                                                       Predicate<BlockState> canPickup, ItemStack result, SoundEvent sound) {
        if (!canPickup.test(state)) return InteractionResult.PASS;
        if (!level.isClientSide()) {
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, result));
            player.awardStat(Stats.USE_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
            level.setBlock(pos, Blocks.CAULDRON.defaultBlockState(), 3);
        }
        return InteractionResult.SUCCESS;
    }
}
