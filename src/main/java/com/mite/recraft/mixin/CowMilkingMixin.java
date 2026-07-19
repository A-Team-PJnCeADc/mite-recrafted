package com.mite.recraft.mixin;

import com.mite.recraft.item.moditems.food.ModFoodItems;
import com.mite.recraft.item.moditems.food.NutritionSystem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.cow.AbstractCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 牛挤奶 — 支持 MITE 金属桶和碗，每日冷却（持久化）。
 *
 * <p>冷却时间通过 {@link NutritionSystem#COW_LAST_MILKED} 持久化到牛实体上。</p>
 */
@Mixin(AbstractCow.class)
public class CowMilkingMixin {

    private static final long COOLDOWN_TICKS = 24000; // 1 MC 天

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void onMobInteract(Player player, InteractionHand hand,
                               CallbackInfoReturnable<InteractionResult> cir) {
        AbstractCow cow = (AbstractCow) (Object) this;
        if (cow.isBaby()) return;

        ItemStack stack = player.getItemInHand(hand);
        if (stack.isEmpty()) return;

        // 每日冷却检查（持久化到实体附件）
        long now = cow.level().getGameTime();
        long lastMilked = cow.getAttachedOrCreate(NutritionSystem.COW_LAST_MILKED);
        if (lastMilked != -1 && now - lastMilked < COOLDOWN_TICKS) {
            if (!cow.level().isClientSide()) {
                player.sendSystemMessage(
                        Component.translatable("message.mite-recrafted.cow_already_milked"));
            }
            cir.setReturnValue(InteractionResult.FAIL);
            return;
        }

        Item held = stack.getItem();

        // —— 碗 → 牛奶碗 ——
        if (held == Items.BOWL) {
            if (cow.level().isClientSide()) {
                cir.setReturnValue(InteractionResult.SUCCESS);
                return;
            }
            cow.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            // 一碗得 4 个牛奶碗
            ItemStack result = new ItemStack(ModFoodItems.MILK_BOWL, 4);
            if (!player.getInventory().add(result)) {
                player.drop(result, false);
            }
            cow.setAttached(NutritionSystem.COW_LAST_MILKED, now);
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        // —— MITE 空桶 → 对应奶桶 ——
        String path = BuiltInRegistries.ITEM.getKey(held).getPath();
        if (path.endsWith("_bucket") && !path.equals("bucket")) {
            String material = path.substring(0, path.length() - "_bucket".length());
            Item milkBucket = findMilkBucket(material);
            if (milkBucket != null) {
                if (cow.level().isClientSide()) {
                    cir.setReturnValue(InteractionResult.SUCCESS);
                    return;
                }
                cow.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                ItemStack result = new ItemStack(milkBucket);
                if (!player.getInventory().add(result)) {
                    player.drop(result, false);
                }
                cow.setAttached(NutritionSystem.COW_LAST_MILKED, now);
                cir.setReturnValue(InteractionResult.SUCCESS);
                return;
            }
        }

        // 其他物品（原版桶等）→ 不拦截，走原版逻辑
    }

    private static Item findMilkBucket(String material) {
        Identifier id = Identifier.fromNamespaceAndPath("mite-recrafted", material + "_milk_bucket");
        Item item = BuiltInRegistries.ITEM.getValue(id);
        return item == Items.AIR ? null : item;
    }
}
