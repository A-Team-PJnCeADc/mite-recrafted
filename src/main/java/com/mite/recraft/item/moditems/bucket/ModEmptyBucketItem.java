package com.mite.recraft.item.moditems.bucket;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * MITE 金属空桶 — 匹配原版 BucketItem 的装液逻辑。
 * 通过 BucketPickup 接口处理水源消耗，并将原版桶物品转换为同材质 MITE 桶。
 * 炼药锅交互直接在 use() 中处理。
 */
public class ModEmptyBucketItem extends Item {

    public Item waterPeer;
    public Item lavaPeer;

    public ModEmptyBucketItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (hit.getType() == HitResult.Type.MISS)
            return InteractionResult.PASS;
        if (hit.getType() != HitResult.Type.BLOCK)
            return InteractionResult.PASS;

        var clickedPos = hit.getBlockPos();
        var face = hit.getDirection();
        var placePos = clickedPos.relative(face);

        if (!level.mayInteract(player, clickedPos))
            return InteractionResult.FAIL;
        if (!player.mayUseItemAt(placePos, face, held))
            return InteractionResult.FAIL;

        // 炼药锅取水/岩浆 → handleCauldron 只改方块+音效，物品交换走下方统一逻辑
        ItemStack cauldronResult = handleCauldron(level, player, clickedPos, held);
        if (cauldronResult != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            ItemStack result = ItemUtils.createFilledResult(held, player, cauldronResult);
            return InteractionResult.SUCCESS.heldItemTransformedTo(result);
        }

        // 1) 尝试 BucketPickup —— 处理水源(LiquidBlock)等，会自动消耗方块
        BlockState state = level.getBlockState(clickedPos);
        Block block = state.getBlock();
        if (block instanceof BucketPickup pickup) {
            ItemStack pickedUp = pickup.pickupBlock(player, level, clickedPos, state);
            if (!pickedUp.isEmpty()) {
                return handlePickup(player, level, held, pickedUp, clickedPos, pickup);
            }
        }

        // 2) 回退：直接检查流体状态（某些自定义流体方块可能未实现 BucketPickup）
        FluidState fluidState = level.getFluidState(clickedPos);
        Item peer = null;
        if (fluidState.isSourceOfType(Fluids.WATER)) {
            peer = waterPeer;
        } else if (fluidState.isSourceOfType(Fluids.LAVA)) {
            peer = lavaPeer;
        }

        if (peer != null) {
            // 手动移除流体源
            if (!level.isClientSide()) {
                level.destroyBlock(clickedPos, false);
            }
            level.gameEvent(player, GameEvent.FLUID_PICKUP, clickedPos);
            if (peer == waterPeer) {
                player.playSound(SoundEvents.BUCKET_FILL, 1.0F, 1.0F);
            } else {
                player.playSound(SoundEvents.BUCKET_FILL_LAVA, 1.0F, 1.0F);
            }
            return finishPickup(player, level, held, new ItemStack(peer), clickedPos);
        }

        return InteractionResult.PASS;
    }

    /**
     * 尝试从炼药锅取水/岩浆。
     * 只处理方块变化+音效，返回获取到的 MITE 桶物品栈（或 null 表示不处理）。
     * 物品栏修改由调用方 use() 统一处理。
     */
    private ItemStack handleCauldron(Level level, Player player, BlockPos pos, ItemStack held) {
        BlockState state = level.getBlockState(pos);

        if (state.is(Blocks.WATER_CAULDRON) && waterPeer != null) {
            if (level.isClientSide()) return new ItemStack(waterPeer);
            player.awardStat(Stats.USE_CAULDRON);
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
            level.setBlock(pos, Blocks.CAULDRON.defaultBlockState(), 3);
            return new ItemStack(waterPeer);

        } else if (state.is(Blocks.LAVA_CAULDRON) && lavaPeer != null) {
            if (level.isClientSide()) return new ItemStack(lavaPeer);
            player.awardStat(Stats.USE_CAULDRON);
            level.playSound(null, pos, SoundEvents.BUCKET_FILL_LAVA, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
            level.setBlock(pos, Blocks.CAULDRON.defaultBlockState(), 3);
            return new ItemStack(lavaPeer);
        }

        return null;
    }

    /** 处理 BucketPickup 结果，翻译原版桶为 MITE 桶 */
    private InteractionResult handlePickup(Player player, Level level, ItemStack held,
                                           ItemStack pickedUp, BlockPos pos, BucketPickup pickup) {
        // 将原版桶翻译为同材质 MITE 桶
        if (pickedUp.is(Items.WATER_BUCKET) && waterPeer != null) {
            pickedUp = new ItemStack(waterPeer);
        } else if (pickedUp.is(Items.LAVA_BUCKET) && lavaPeer != null) {
            pickedUp = new ItemStack(lavaPeer);
        }

        // 播放装液体音效
        pickup.getPickupSound().ifPresent(sound ->
            player.playSound(sound, 1.0F, 1.0F));

        return finishPickup(player, level, held, pickedUp, pos);
    }

    /** 成功装液后的公共逻辑：统计、GameEvent、物品栏处理 */
    private InteractionResult finishPickup(Player player, Level level, ItemStack held,
                                           ItemStack pickedUp, BlockPos pos) {
        player.awardStat(Stats.ITEM_USED.get(this));
        level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);

        ItemStack result = ItemUtils.createFilledResult(held, player, pickedUp);
        return InteractionResult.SUCCESS.heldItemTransformedTo(result);
    }
}
