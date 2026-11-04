package com.mite.recraft.content.entity.mob;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 对应 MITE EntityLiving.tryDisableNearbyLightSource：附近没有玩家时熄灭火把与南瓜灯。
 *
 * <p>火把直接掉落本体，南瓜灯换成南瓜并掉落火把；每次扫描最多处理一处。</p>
 */
public final class MiteLightSuppression {

    private MiteLightSuppression() {
    }

    /** 调用方负责判断间隔与玩家距离。 */
    public static void suppress(LivingEntity entity, ServerLevel level) {
        BlockPos origin = entity.blockPosition();
        int height = (int) Math.ceil(entity.getBbHeight());
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= height + 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos pos = origin.offset(dx, dy, dz);
                    BlockState state = level.getBlockState(pos);
                    if (state.is(Blocks.JACK_O_LANTERN)) {
                        level.setBlockAndUpdate(pos, Blocks.CARVED_PUMPKIN.defaultBlockState());
                        Block.popResource(level, pos, new ItemStack(Items.TORCH));
                        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.HOSTILE, 0.05F, 2.0F);
                        return;
                    }
                    if (state.is(Blocks.TORCH) || state.is(Blocks.WALL_TORCH)
                            || state.is(Blocks.REDSTONE_TORCH) || state.is(Blocks.REDSTONE_WALL_TORCH)) {
                        Block block = state.getBlock();
                        level.removeBlock(pos, false);
                        Block.popResource(level, pos, new ItemStack(block.asItem()));
                        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.HOSTILE, 0.05F, 2.0F);
                        return;
                    }
                }
            }
        }
    }
}
