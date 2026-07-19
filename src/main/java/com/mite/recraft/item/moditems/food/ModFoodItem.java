package com.mite.recraft.item.moditems.food;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

/**
 * 通用食物物品 — 原版 FoodData 驱动食物条，MITE 营养通过 {@link NutritionSystem} 附加。
 */
public class ModFoodItem extends Item {

    private final FoodType foodType;

    public ModFoodItem(FoodType foodType, Item.Properties properties) {
        super(properties);
        this.foodType = foodType;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return foodType.containerType() != ContainerType.NONE
                ? ItemUseAnimation.DRINK : ItemUseAnimation.EAT;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        // 原版 FoodData.eat() 已经处理了食物条和饱和度
        ItemStack result = super.finishUsingItem(stack, level, entity);

        if (entity instanceof Player player) {
            // MITE 营养附加值
            NutritionSystem.onEat(player,
                    foodType.nutrition(), foodType.protein(), foodType.phytonutrients(),
                    foodType.essentialFats(), foodType.sugar());

            // 容器返回
            if (foodType.containerType() != ContainerType.NONE && !player.isCreative()) {
                ItemStack container = foodType.containerType().getContainer(foodType.bucketMaterial());
                if (!container.isEmpty()) {
                    if (result.isEmpty()) {
                        result = container;
                    } else if (!player.getInventory().add(container)) {
                        player.drop(container, false);
                    }
                }
            }

            // 清除状态效果
            if (foodType.clearsEffects()) {
                player.removeAllEffects();
            }

            // 同步
            if (player instanceof ServerPlayer sp) {
                NutritionSystem.sendSyncPacket(sp);
            }
        }

        return result;
    }
}
