package com.mite.recraft.item.tools.toolItem;

import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * 鹤嘴锄（Mattock）—— numComponents=4，铲+锄混合工具
 */
public class MattockItems {
    public static final Item COPPER_MATTOCK       = create(ModToolMaterial.COPPER, "copper", 2.5F, -3.0F);
    public static final Item SILVER_MATTOCK       = create(ModToolMaterial.SILVER, "silver", 2.5F, -3.0F);
    public static final Item GOLD_MATTOCK         = create(ModToolMaterial.GOLD, "gold", 1.5F, -3.0F);
    public static final Item RUSTED_IRON_MATTOCK  = create(ModToolMaterial.RUSTED_IRON, "rusted_iron", 1.5F, -3.0F);
    public static final Item IRON_MATTOCK         = create(ModToolMaterial.IRON, "iron", 2.5F, -3.0F);
    public static final Item ANCIENT_METAL_MATTOCK = create(ModToolMaterial.ANCIENT_METAL, "ancient_metal", 3.5F, -3.0F);
    public static final Item MITHRIL_MATTOCK      = create(ModToolMaterial.MITHRIL, "mithril", 4.5F, -3.0F);
    public static final Item ADAMANTIUM_MATTOCK   = create(ModToolMaterial.ADAMANTIUM, "adamantium", 5.5F, -3.0F);

    private static Item create(ModToolMaterial mat, String name, float dmg, float speed) {
        ToolMaterial tm = ToolType.MATTOCK.customToolMaterial(mat);
        return new MattockItem(
                new Item.Properties()
                        .setId(ToolType.MATTOCK.itemKey(name))
                        .shovel(tm, dmg, speed));
    }

    /** 鹤嘴锄：铲挖掘 + 锄地 + 造土径 */
    public static class MattockItem extends Item {
        MattockItem(Properties properties) { super(properties); }

        @Override
        public InteractionResult useOn(UseOnContext context) {
            InteractionResult till = tryTill(context);
            if (till != InteractionResult.PASS) return till;
            return tryFlatten(context);
        }

        private static InteractionResult tryTill(UseOnContext ctx) {
            Level level = ctx.getLevel();
            BlockPos pos = ctx.getClickedPos();
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            if (ctx.getClickedFace() == Direction.DOWN) return InteractionResult.PASS;
            if (!level.getBlockState(pos.above()).isAir()) return InteractionResult.PASS;

            BlockState result = null;
            ItemStack drop = null;
            if (block == Blocks.GRASS_BLOCK || block == Blocks.DIRT_PATH || block == Blocks.DIRT)
                result = Blocks.FARMLAND.defaultBlockState();
            else if (block == Blocks.COARSE_DIRT)
                result = Blocks.DIRT.defaultBlockState();
            else if (block == Blocks.ROOTED_DIRT) {
                result = Blocks.DIRT.defaultBlockState();
                drop = new ItemStack(Items.HANGING_ROOTS);
            }
            if (result == null) return InteractionResult.PASS;

            Player player = ctx.getPlayer();
            level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!level.isClientSide()) {
                level.setBlock(pos, result, 11);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, result));
                if (drop != null) Block.popResourceFromFace(level, pos, ctx.getClickedFace(), drop);
                if (player != null) ctx.getItemInHand().hurtAndBreak(1, player, ctx.getHand().asEquipmentSlot());
            }
            return InteractionResult.SUCCESS;
        }

        private static InteractionResult tryFlatten(UseOnContext ctx) {
            Level level = ctx.getLevel();
            BlockPos pos = ctx.getClickedPos();
            BlockState state = level.getBlockState(pos);
            if (ctx.getClickedFace() == Direction.DOWN) return InteractionResult.PASS;
            if (!level.getBlockState(pos.above()).isAir()) return InteractionResult.PASS;
            if (!state.is(Blocks.GRASS_BLOCK) && !state.is(Blocks.DIRT) && !state.is(Blocks.PODZOL)
                    && !state.is(Blocks.COARSE_DIRT) && !state.is(Blocks.MYCELIUM) && !state.is(Blocks.ROOTED_DIRT))
                return InteractionResult.PASS;

            Player player = ctx.getPlayer();
            BlockState path = Blocks.DIRT_PATH.defaultBlockState();
            level.playSound(player, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!level.isClientSide()) {
                level.setBlock(pos, path, 11);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, path));
                if (player != null) ctx.getItemInHand().hurtAndBreak(1, player, ctx.getHand().asEquipmentSlot());
            }
            return InteractionResult.SUCCESS;
        }
    }
}
