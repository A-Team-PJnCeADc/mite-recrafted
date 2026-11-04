package com.mite.recraft.content.entity;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.cow.AbstractCow;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.monster.zombie.Zombie;

import java.util.List;
import java.util.function.Supplier;

/**
 * MITE 1.6.4 对原版生物的数值重平衡表。
 *
 * <p>MITE 只覆盖了少数几个原版生物的属性，这里集中成一张表，两端在属性注册阶段套用：
 * Fabric 走 {@code FabricDefaultAttributeRegistry.MODIFY}，NeoForge 在 {@code EntityAttributeCreationEvent} 里提交。
 * 未列出的原版生物在 MITE 源码里没有覆盖属性，保持原版数值。</p>
 */
public enum VanillaMobRebalance {

    ZOMBIE(EntityTypes.ZOMBIE, Zombie::createAttributes, List.of(
            new Mod(Attributes.ATTACK_DAMAGE, 5.0),
            new Mod(Attributes.FOLLOW_RANGE, 40.0))),

    SKELETON(EntityTypes.SKELETON, AbstractSkeleton::createAttributes, List.of(
            new Mod(Attributes.MAX_HEALTH, 6.0),
            new Mod(Attributes.ATTACK_DAMAGE, 4.0),
            new Mod(Attributes.MOVEMENT_SPEED, 0.30),
            new Mod(Attributes.FOLLOW_RANGE, 32.0))),

    SPIDER(EntityTypes.SPIDER, Spider::createAttributes, List.of(
            new Mod(Attributes.MAX_HEALTH, 12.0),
            new Mod(Attributes.ATTACK_DAMAGE, 4.0),
            new Mod(Attributes.MOVEMENT_SPEED, 1.0),
            new Mod(Attributes.FOLLOW_RANGE, 28.0))),

    WOLF(EntityTypes.WOLF, Wolf::createAttributes, List.of(
            new Mod(Attributes.MAX_HEALTH, 8.0),
            new Mod(Attributes.ATTACK_DAMAGE, 3.0),
            new Mod(Attributes.MOVEMENT_SPEED, 0.40),
            new Mod(Attributes.FOLLOW_RANGE, 16.0))),

    COW(EntityTypes.COW, AbstractCow::createAttributes, List.of(
            new Mod(Attributes.MAX_HEALTH, 20.0),
            new Mod(Attributes.MOVEMENT_SPEED, 0.2)));

    /** 单条覆盖项。 */
    public record Mod(Holder<Attribute> attribute, double value) {
    }

    private final EntityType<? extends LivingEntity> type;
    private final Supplier<AttributeSupplier.Builder> base;
    private final List<Mod> mods;

    VanillaMobRebalance(EntityType<? extends LivingEntity> type, Supplier<AttributeSupplier.Builder> base, List<Mod> mods) {
        this.type = type;
        this.base = base;
        this.mods = mods;
    }

    public EntityType<? extends LivingEntity> type() {
        return type;
    }

    /** 在给定构建器上套用覆盖值。 */
    public AttributeSupplier.Builder apply(AttributeSupplier.Builder builder) {
        for (Mod mod : mods) {
            builder.add(mod.attribute(), mod.value());
        }
        return builder;
    }

    /** 原版属性构建器的完整副本，供需要整份替换的平台使用。 */
    public AttributeSupplier supplier() {
        return apply(base.get()).build();
    }
}
