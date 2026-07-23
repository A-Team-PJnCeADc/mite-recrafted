package com.mite.recraft.mixin;

import com.mite.recraft.item.moditems.food.EdibleOverride;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 使 {@link EdibleOverride} 中的原版不可食物品可食用。
 *
 * <p>不修改 ItemStack 组件，完全绕过标准食用流程，
 * 手动控制动画和食物值应用，彻底避免渲染马赛克。</p>
 */
@Mixin(Item.class)
public class EdibleOverrideUseMixin {

    @Inject(method = "getUseAnimation", at = @At("HEAD"), cancellable = true)
    private void mite$getUseAnimation(ItemStack stack,
                                      CallbackInfoReturnable<ItemUseAnimation> cir) {
        EdibleOverride edible = EdibleOverride.fromItem(stack.getItem());
        if (edible == null) return;
        cir.setReturnValue(ItemUseAnimation.EAT);
    }

    @Inject(method = "onUseTick", at = @At("HEAD"))
    private void mite$onUseTick(Level level, LivingEntity entity, ItemStack stack,
                                int remaining, CallbackInfo ci) {
        EdibleOverride edible = EdibleOverride.fromItem(stack.getItem());
        if (edible == null) return;
        // 每 4 tick 播一次粒子和音效（匹配原版 Consumable.emitParticlesAndSounds）
        if (remaining % 4 == 0 && remaining > 2) {
            entity.spawnItemParticles(stack, 2);
            if (!level.isClientSide()) {
                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        SoundEvents.GENERIC_EAT, entity.getSoundSource(), 0.5F + 0.5F * level.getRandom().nextFloat(), level.getRandom().nextFloat() * 0.25F + 0.75F);
            }
        }
    }

    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
    private void mite$getUseDuration(ItemStack stack, LivingEntity entity,
                                     CallbackInfoReturnable<Integer> cir) {
        EdibleOverride edible = EdibleOverride.fromItem(stack.getItem());
        if (edible == null) return;
        // 返回食用动画时长（ticks = 秒 × 20）
        cir.setReturnValue((int) (edible.consumeSeconds() * 20));
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void mite$startEating(Level level, Player player, InteractionHand hand,
                                  CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = player.getItemInHand(hand);
        EdibleOverride edible = EdibleOverride.fromItem(stack.getItem());
        if (edible == null) return;

        // 刚开始吃时播放粒子和音效
        player.spawnItemParticles(stack, 4);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.GENERIC_EAT, player.getSoundSource(), 0.5F, 1.0F);

        player.startUsingItem(hand);
        cir.setReturnValue(InteractionResult.CONSUME);
    }

    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    private void mite$finishEating(ItemStack stack, Level level, LivingEntity entity,
                                    CallbackInfoReturnable<ItemStack> cir) {
        EdibleOverride edible = EdibleOverride.fromItem(stack.getItem());
        if (edible == null) return;
        if (!(entity instanceof Player player)) return;

        // 吃完时播放粒子和音效（匹配原版 onConsume）
        entity.spawnItemParticles(stack, 16);
        if (!level.isClientSide()) {
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.GENERIC_EAT, entity.getSoundSource(), 1.0F, 1.0F);
        }

        // 应用食物值 — nutrition=0 时绕过 FoodData.eat 公式，直接加原始饱和度
        var foodData = player.getFoodData();
        int hunger = edible.hunger();
        int newFood = Math.min(foodData.getFoodLevel() + hunger, 20);
        float saturationToAdd;
        if (hunger > 0) {
            saturationToAdd = hunger * edible.saturationModifier() * 2;
        } else {
            saturationToAdd = edible.rawSaturation();
        }
        float newSat = Math.min(foodData.getSaturationLevel() + saturationToAdd, newFood);
        foodData.setFoodLevel(newFood);
        foodData.setSaturation(newSat);

        // 红色蘑菇负面效果
        if (edible == EdibleOverride.RED_MUSHROOM) {
            player.addEffect(new MobEffectInstance(MobEffects.POISON, 10 * 20, 0)); // 中毒I 10秒
            player.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 60 * 20, 0)); // 反胃 60秒
        }

        stack.shrink(1);
        cir.setReturnValue(stack);
    }
}
