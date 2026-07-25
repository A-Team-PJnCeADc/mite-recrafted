package com.mite.recraft.mixin;

import com.mite.recraft.enchantment.ModEnchantments;
import com.mite.recraft.enchantment.TreeFelling;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 砍伐 (Tree Felling) 效果：破坏原木时上方原木一同掉落。
 * <p>
 * 注入 {@link Block#playerDestroy}（玩家破坏方块时触发），
 * 该路径在 MC 26.2 中可靠触发且不会出现递归问题：
 * {@link TreeFelling#fell} 内部使用 {@code serverLevel.destroyBlock} 移除上方原木，
 * 不会再次调用 {@code playerDestroy}（后者仅由玩家操作触发）。
 */
@Mixin(Block.class)
public class TreeFellingMixin {

    @Inject(method = "playerDestroy", at = @At("TAIL"))
    private void mite$onPlayerDestroy(Level level, Player player, BlockPos pos,
                                      BlockState state, BlockEntity blockEntity, ItemStack tool,
                                      CallbackInfo ci) {
        if (!state.is(BlockTags.LOGS)) return;
        if (level.isClientSide()) return;
        if (!(level instanceof ServerLevel serverLevel)) return;

        int enchLevel = EnchantmentHelper.getItemEnchantmentLevel(
                serverLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(ModEnchantments.TREE_FELLING.key()),
                player.getMainHandItem()
        );
        if (enchLevel <= 0) return;

        TreeFelling.fell(enchLevel, serverLevel, pos, state, tool, player);
    }
}
