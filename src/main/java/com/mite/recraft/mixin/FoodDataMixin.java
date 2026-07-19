package com.mite.recraft.mixin;

import com.mite.recraft.item.moditems.food.NutritionSystem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * MITE 食物系统
 *
 * <p>MITE 原版 FoodStats.onUpdate：
 * <ol>
 *   <li>每 tick 累积 hunger (0.002/tick)，hunger ≥ 4 时消耗 1 点 satiation</li>
 *   <li>hunger_for_nutrition_only 累积到 4 时开始消耗 nutrition 而非 satiation</li>
 *   <li>回血公式：heal_progress += (0.0004 + nutrition×0.00002) × 修正</li>
 *   <li>heal_progress ≥ 1.0 → heal(1HP)，消耗 1 hunger</li>
 * </ol>
 * </p>
 */
@Mixin(FoodData.class)
public class FoodDataMixin {

    @Unique
    private static final float HEAL_NUTRITION_FACTOR = 0.00002F;
    @Unique
    private static final float HEAL_BASE = 0.0004F;

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(ServerPlayer player, CallbackInfo ci) {
        FoodData self = (FoodData) (Object) this;
        if (player.isCreative()) return;

        int nutrition = player.getAttachedOrCreate(NutritionSystem.NUTRITION);
        int foodLevel = self.getFoodLevel();
        int limit = getNutrientLimit(player.experienceLevel);

        // 食物条 < 上限 且 nutrition > 0 → 从 nutrition 补充到食物条
        if (foodLevel < limit && nutrition > 0) {
            int transfer = Math.min(limit - foodLevel, nutrition);
            self.setFoodLevel(foodLevel + transfer);
            player.setAttached(NutritionSystem.NUTRITION, nutrition - transfer);
            nutrition -= transfer;
        }

        //todo
        // MITE 回血
        if (foodLevel > 0 && !player.isDeadOrDying()) {
            float hp = player.getAttachedOrCreate(NutritionSystem.HEAL_PROGRESS);
            // MITE 公式: heal_progress += (0.0004 + nutrition×0.00002) × malnourish × inBed
            float increment = (HEAL_BASE + nutrition * HEAL_NUTRITION_FACTOR)
                    * (NutritionSystem.isMalnourished(player) ? 0.25F : 1.0F)
                    * (player.isSleeping() ? 4.0F : 1.0F);
            hp += increment;
            player.setAttached(NutritionSystem.HEAL_PROGRESS, hp);

            if (hp >= 1.0F) {
                player.heal(1.0F);
                hp -= 1.0F;
                player.setAttached(NutritionSystem.HEAL_PROGRESS, hp);
                // MITE 原版: 回血消耗 1 hunger（由 FoodData 自然处理）
            }
        }
    }

    @Unique
    private static int getNutrientLimit(int level) {
        return Math.max(6, Math.min(6 + level / 5 * 2, 20));
    }
}
