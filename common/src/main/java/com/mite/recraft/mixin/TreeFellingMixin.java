package com.mite.recraft.mixin;

import com.mite.recraft.content.enchantment.TreeFelling;
import com.mite.recraft.registry.ModEnchantments;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
 * 注入 {@link Block#playerDestroy}；{@link TreeFelling#fell} 用 {@code destroyBlock} 移除上方原木，
 * 不经过玩家破坏路径，不产生递归。
 */
@Mixin(Block.class)
public class TreeFellingMixin {

    @Inject(method = "playerDestroy", at = @At("TAIL"))
    private void mite$onPlayerDestroy(ServerLevel level, ServerPlayer player, BlockPos pos,
                                      BlockState state, BlockEntity blockEntity, ItemStack tool,
                                      CallbackInfo ci) {
        if (!state.is(BlockTags.LOGS)) return;

        int enchLevel = EnchantmentHelper.getItemEnchantmentLevel(
                level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(ModEnchantments.TREE_FELLING.key()),
                player.getMainHandItem()
        );
        if (enchLevel <= 0) return;

        TreeFelling.fell(enchLevel, level, pos, state, tool, player);
    }
}
