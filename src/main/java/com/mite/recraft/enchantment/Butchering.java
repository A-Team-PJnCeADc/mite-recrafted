package com.mite.recraft.enchantment;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.polarbear.PolarBear;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * MITE 屠宰 (Butchering) — 增加杀死动物时肉的掉落量。
 * <p>
 * 在 {@code dropFromLootTable} 触发后额外掉落肉类。
 * MITE 公式：{@code extra = random.nextInt(1 + level)}。
 * <p>
 * 仅适用于短剑（Dagger），由 {@code butchering_weapon} 标签界定。
 */
public final class Butchering {

    private Butchering() {
    }

    /**
     * 在正常奖励表掉落后，根据 Butchering 等级额外掉落肉类。
     *
     * @param level       Butchering 魔咒等级
     * @param victim      被杀动物
     * @param serverLevel 服务端世界
     */
    public static void tryExtraMeat(int level, LivingEntity victim, ServerLevel serverLevel) {
        if (level <= 0) return;
        if (!(victim instanceof Animal)) return;

        ItemStack meat = getMeatFor(victim);
        if (meat.isEmpty()) return;

        // MITE 公式: extra = random.nextInt(1 + level)
        int extra = victim.getRandom().nextInt(1 + level);
        for (int i = 0; i < extra; i++) {
            victim.spawnAtLocation(serverLevel, meat.copy());
        }
    }

    private static ItemStack getMeatFor(LivingEntity entity) {
        if (entity instanceof Cow)           return new ItemStack(Items.BEEF);
        if (entity instanceof Sheep)         return new ItemStack(Items.MUTTON);
        if (entity instanceof Pig)           return new ItemStack(Items.PORKCHOP);
        if (entity instanceof Chicken)       return new ItemStack(Items.CHICKEN);
        if (entity instanceof Rabbit)        return new ItemStack(Items.RABBIT);
        if (entity instanceof Horse)         return new ItemStack(Items.BEEF);
        if (entity instanceof Goat)          return new ItemStack(Items.MUTTON);
        if (entity instanceof PolarBear)     return new ItemStack(Items.COD);
        return ItemStack.EMPTY;
    }
}
