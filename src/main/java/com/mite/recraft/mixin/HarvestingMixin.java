package com.mite.recraft.mixin;

import com.mite.recraft.enchantment.Harvesting;
import com.mite.recraft.enchantment.ModEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * 收获 (Harvesting) 效果：破坏成熟作物时额外掉落。
 */
@Mixin(Block.class)
public class HarvestingMixin {

    @Inject(method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At("TAIL"))
    private static void mite$onDropResources(BlockState state, Level level, BlockPos pos, BlockEntity blockEntity,
                                             Entity entity, ItemStack tool, CallbackInfo ci) {
        if (!(state.getBlock() instanceof CropBlock crop)) return;
        if (!(entity instanceof LivingEntity living)) return;
        if (!crop.isMaxAge(state)) return;

        if (!(level instanceof ServerLevel serverLevel)) return;
        int enchLevel = EnchantmentHelper.getItemEnchantmentLevel(
                level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(ModEnchantments.HARVESTING.key()),
                living.getMainHandItem()
        );
        if (enchLevel <= 0) return;

        // 通过注册名判断工具类型
        var itemKey = BuiltInRegistries.ITEM.getKey(tool.getItem());
        if (itemKey == null) return;
        String id = itemKey.getPath();
        if (!id.contains("scythe") && !id.contains("_hoe") && !id.contains("_mattock")) return;
        String toolType = id.contains("scythe") ? "scythe" : id.contains("_hoe") ? "hoe" : "mattock";

        // 获取原本的掉落列表，然后逐个检查并施加额外掉落
        List<ItemStack> drops = Block.getDrops(state, serverLevel, pos, null, entity, tool);
        for (ItemStack drop : drops) {
            String cropId = Harvesting.getCropId(drop);
            if (cropId.isEmpty()) continue;

            if ("scythe".equals(toolType) && "wheat".equals(cropId)) {
                // 镰刀 + 小麦：MITE 公式概率使小麦加倍
                float mult = Harvesting.getMultiplier(enchLevel);
                // mult 是小数部分，作为概率判定
                if (living.getRandom().nextFloat() < mult) {
                    Block.popResource(level, pos, drop.copyWithCount(drop.getCount()));
                }
            } else if (("hoe".equals(toolType) || "mattock".equals(toolType)) && !"wheat".equals(cropId)) {
                // 锄/鹤嘴锄 + 非小麦：MITE 公式提高掉落基数
                float mult = Harvesting.getMultiplier(enchLevel);
                int extra = (int) (drop.getCount() * mult);
                if (extra > 0) {
                    Block.popResource(level, pos, drop.copyWithCount(extra));
                }
            }
        }
    }
}
