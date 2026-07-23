package com.mite.recraft.mixin;

import com.mite.recraft.item.moditems.food.ModFoodItems;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 僵尸村民击杀概率掉落洋葱，女巫击杀有概率掉落洋葱。
 */
@Mixin(LivingEntity.class)
public class EntityOnionDropMixin {

    @Inject(method = "die", at = @At("HEAD"))
    private void mite$onDeath(DamageSource source, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self.level() instanceof ServerLevel serverLevel)) return;

        // 非玩家击杀不处理
        if (!(source.getEntity() instanceof Player player)) return;

        // 获取抢夺等级
        Holder<Enchantment> lootingHolder = serverLevel.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.LOOTING);
        int looting = EnchantmentHelper.getItemEnchantmentLevel(lootingHolder, player.getMainHandItem());

        // 僵尸村民：rand.nextInt(50) < 5 + looting * 2
        var entityId = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getKey(self.getType());
        if (entityId != null && entityId.getPath().equals("zombie_villager")
                && self.getRandom().nextInt(50) < 5 + looting * 2) {
            if (self.getRandom().nextInt(6) == 5) {
                Block.popResource(self.level(), self.blockPosition(), new ItemStack(ModFoodItems.ONION));
            }
        }

        // 女巫：rand.nextInt(5 + looting) + 1 次掉落，1/18 概率洋葱
        if (self instanceof Witch) {
            int numDrops = self.getRandom().nextInt(5 + looting) + 1;
            for (int i = 0; i < numDrops; i++) {
                if (self.getRandom().nextInt(18) == 14) {
                    Block.popResource(self.level(), self.blockPosition(), new ItemStack(ModFoodItems.ONION));
                }
            }
        }
    }
}