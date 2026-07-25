package com.mite.recraft.enchantment;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * MITE 砍伐 (Tree Felling) — 砍树时上方原木一同掉落。
 * <p>
 * 每级额外掉落上方 1 块原木，工具根据额外掉落数量减少耐久。
 */
public final class TreeFelling {

    private TreeFelling() {
    }

    /**
     * 执行砍伐效果：破坏上方原木。
     *
     * @param level      附魔等级
     * @param serverLevel 世界
     * @param pos         被破坏的原木位置
     * @param state       被破坏的原木状态（破坏前）
     * @param tool        使用的工具
     * @param miner       挖掘者
     */
    public static void fell(int level, ServerLevel serverLevel, BlockPos pos, BlockState state,
                            ItemStack tool, LivingEntity miner) {
        if (level <= 0) return;
        if (!state.is(BlockTags.LOGS)) return;

        int felled = 0;
        // 向上检查，每级最多额外破坏 1 块
        for (int i = 1; i <= level; i++) {
            BlockPos abovePos = pos.above(i);
            BlockState aboveState = serverLevel.getBlockState(abovePos);
            if (!aboveState.is(BlockTags.LOGS)) break;  // 遇到非原木停止

            // 破坏原木并掉落
            Block.dropResources(aboveState, serverLevel, abovePos, null, miner, tool);
            serverLevel.destroyBlock(abovePos, false, miner);
            felled++;
        }

        // 工具耐久损耗：每额外破坏 1 块原木消耗 1 点耐久
        if (felled > 0 && tool.isDamageableItem() && miner instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            tool.hurtAndBreak(felled, serverLevel, serverPlayer, item -> {});
        }
    }
}
