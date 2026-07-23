package com.mite.recraft.mixin;

import com.mite.recraft.item.moditems.ManureItems;
import com.mite.recraft.item.moditems.food.ModFoodItems;
import com.mite.recraft.util.CompostAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

/**
 * 箱子蠕虫堆肥系统 — 复刻 MITE 1.6.4 机制。
 *
 * <p>通过 {@link ChestBlock#getTicker} 添加服务端 ticker。</p>
 *
 * <p>堆肥进度存储于 {@code  ConcurrentHashMap}，线程安全。
 * 重启后进度丢失（非关键数据，仅影响当前 compost 积累）。</p>
 */
@Mixin(ChestBlock.class)
public class ChestWormMixin {

    @Unique
    private static int tickCounter = 0;

    @Inject(method = "getTicker", at = @At("RETURN"), cancellable = true)
    private <T extends BlockEntity> void mite$serverTicker(
            Level level, BlockState state,
            net.minecraft.world.level.block.entity.BlockEntityType<T> type,
            CallbackInfoReturnable<BlockEntityTicker<T>> cir) {
        if (level.isClientSide()) return;
        cir.setReturnValue(
                (BlockEntityTicker<T>) (BlockEntityTicker<ChestBlockEntity>) ChestWormMixin::serverTick
        );
    }

    @Unique
    private static void serverTick(Level level, BlockPos pos,
                                    BlockState state, ChestBlockEntity chest) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        tickCounter++;
        if (tickCounter % 20 != 0) return;

        RandomSource random = serverLevel.getRandom();
        Item wormRaw = ModFoodItems.WORM_RAW;
        int limit = chest.getContainerSize();

        int numWorms = 0;
        for (int i = 0; i < limit; i++) {
            ItemStack st = chest.getItem(i);
            if (!st.isEmpty() && st.is(wormRaw)) numWorms += st.getCount();
        }
        if (numWorms < 1) return;

        for (int w = 0; w < Math.min(numWorms, 10); w++) {
            List<Integer> foodSlots = new ArrayList<>();
            for (int i = 0; i < limit; i++) {
                ItemStack st = chest.getItem(i);
                if (!st.isEmpty() && !st.is(wormRaw) && canCompost(st)) {
                    foodSlots.add(i);
                }
            }
            if (foodSlots.isEmpty()) return;

            int srcIdx = foodSlots.get(random.nextInt(foodSlots.size()));
            ItemStack food = chest.getItem(srcIdx);
            float compostVal = getCompostVal(food);

//            // MITE 原版概率：1 / (int)(100 * compostVal)
//            int chance = (int) (100.0F * compostVal);
//            if (chance <= 0 || random.nextInt(chance) != 0) continue;

            float eatChance = Math.min(1.0F, 1.0F / (100.0F * compostVal));
            if (random.nextFloat() >= eatChance) continue;


            if (isLeaves(food)) {
                int count = food.getCount();
                if (roomFor(chest, ManureItems.MANURE, count, limit)) {
                    food.shrink(count);
                    addToChest(chest, ManureItems.MANURE, count, limit);
                    chest.setChanged();
                }
                continue;
            }

            if (food.is(Blocks.PUMPKIN.asItem())) {
                if (roomFor(chest, ManureItems.MANURE, 2, limit)
                        && roomFor(chest, Items.PUMPKIN_SEEDS, 1, limit)) {
                    food.shrink(1);
                    addToChest(chest, ManureItems.MANURE, 2, limit);
                    addToChest(chest, Items.PUMPKIN_SEEDS, 1, limit);
                    chest.setChanged();
                }
                continue;
            }

            food.shrink(1);
            float prog = ((CompostAccess) chest).mite$getCompost() + compostVal;
            int produced = 0;

            while (prog >= 1.0F && roomFor(chest, ManureItems.MANURE, 1, limit)) {
                addToChest(chest, ManureItems.MANURE, 1, limit);
                prog -= 1.0F;
                produced++;
            }

            // 只有实际有变化时才保存
            if (produced > 0 || prog != ((CompostAccess) chest).mite$getCompost()) {
                ((CompostAccess) chest).mite$setCompost(Math.max(0, prog));
                chest.setChanged();
            }
        }
    }

    @Unique
    private static boolean canCompost(ItemStack st) {
        if (st.is(ModFoodItems.WORM_RAW)) return false;
        if (st.is(ModFoodItems.WORM_COOKED)) return true;
        if (isLeaves(st)) return true;
        if (st.is(Blocks.PUMPKIN.asItem())) return true;
        return st.has(net.minecraft.core.component.DataComponents.FOOD);
    }

    @Unique
    private static float getCompostVal(ItemStack st) {
        if (isLeaves(st)) return 1.0F;
        if (st.is(Blocks.PUMPKIN.asItem())) return 1.0F;
        var food = st.get(net.minecraft.core.component.DataComponents.FOOD);
        return food != null ? food.nutrition() * 0.1F : 0.1F;
    }

    @Unique
    private static boolean isLeaves(ItemStack st) {
        return st.is(ItemTags.LEAVES);
    }

    @Unique
    private static boolean roomFor(ChestBlockEntity chest, Item item, int count, int limit) {
        int remaining = count;
        int maxStack = item.getDefaultMaxStackSize();

        // 先计算已有物品的剩余空间
        for (int i = 0; i < limit && remaining > 0; i++) {
            ItemStack st = chest.getItem(i);
            if (st.is(item)) {
                int space = st.getMaxStackSize() - st.getCount();
                if (space > 0) {
                    remaining -= Math.min(remaining, space);
                }
            }
        }

        // 再计算空槽位
        if (remaining > 0) {
            for (int i = 0; i < limit && remaining > 0; i++) {
                if (chest.getItem(i).isEmpty()) {
                    remaining -= Math.min(remaining, maxStack);
                }
            }
        }

        return remaining <= 0;
    }

    @Unique
    private static void addToChest(ChestBlockEntity chest, Item item, int count, int limit) {
        int rem = count;
        for (int i = 0; i < limit && rem > 0; i++) {
            ItemStack st = chest.getItem(i);
            if (st.is(item) && st.getCount() < st.getMaxStackSize()) {
                int add = Math.min(rem, st.getMaxStackSize() - st.getCount());
                st.grow(add);
                rem -= add;
            }
        }
        for (int i = 0; i < limit && rem > 0; i++) {
            if (chest.getItem(i).isEmpty()) {
                int add = Math.min(rem, item.getDefaultMaxStackSize());
                chest.setItem(i, new ItemStack(item, add));
                rem -= add;
            }
        }
    }
}
