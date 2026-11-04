package com.mite.recraft.registry.neoforge;

import com.mite.recraft.content.entity.mob.*;
import com.mite.recraft.registry.ModEntityTypes;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

/**
 * NeoForge 侧的生物属性登记。属性只能在 EntityAttributeCreationEvent 期间提交。
 */
public final class ModEntityAttributesImpl {

    private ModEntityAttributesImpl() {
    }

    /** NeoForge 无需在 common 的 register() 里做事，属性由事件回调写入。 */
    public static void register() {
    }

    public static void onAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.GHOUL, GhoulEntity.createAttributes().build());
        event.put(ModEntityTypes.LONGDEAD, LongdeadEntity.createAttributes().build());
        event.put(ModEntityTypes.REVENANT, RevenantEntity.createAttributes().build());
        event.put(ModEntityTypes.WIGHT, WightEntity.createAttributes().build());
        event.put(ModEntityTypes.BONE_LORD, BoneLordEntity.createAttributes().build());
        event.put(ModEntityTypes.ANCIENT_BONE_LORD, AncientBoneLordEntity.createAttributes().build());
        event.put(ModEntityTypes.WOOD_SPIDER, WoodSpiderEntity.createAttributes().build());
        event.put(ModEntityTypes.BLACK_WIDOW, BlackWidowSpiderEntity.createAttributes().build());
        event.put(ModEntityTypes.PHASE_SPIDER, PhaseSpiderEntity.createAttributes().build());
        event.put(ModEntityTypes.DEMON_SPIDER, DemonSpiderEntity.createAttributes().build());
        event.put(ModEntityTypes.DIRE_WOLF, DireWolfEntity.createAttributes().build());
        event.put(ModEntityTypes.HELLHOUND, HellhoundEntity.createAttributes().build());
        event.put(ModEntityTypes.COPPERSPINE, net.minecraft.world.entity.monster.Silverfish.createAttributes().build());
        event.put(ModEntityTypes.HOARY_SILVERFISH, net.minecraft.world.entity.monster.Silverfish.createAttributes().build());
        event.put(ModEntityTypes.NETHERSPAWN, net.minecraft.world.entity.monster.Silverfish.createAttributes().build());
        event.put(ModEntityTypes.LONGDEAD_GUARDIAN, LongdeadGuardianEntity.createAttributes().build());
        event.put(ModEntityTypes.INVISIBLE_STALKER, InvisibleStalkerEntity.createAttributes().build());
        event.put(ModEntityTypes.BLOB, BlobEntity.createAttributes().build());
        event.put(ModEntityTypes.OOZE, OozeEntity.createAttributes().build());
        event.put(ModEntityTypes.JELLY, JellyEntity.createAttributes().build());
        event.put(ModEntityTypes.PUDDING, PuddingEntity.createAttributes().build());
        event.put(ModEntityTypes.VAMPIRE_BAT, VampireBatEntity.createAttributes().build());
        event.put(ModEntityTypes.GIANT_VAMPIRE_BAT, GiantVampireBatEntity.createAttributes().build());
        event.put(ModEntityTypes.NIGHTWING, NightwingEntity.createAttributes().build());
        event.put(ModEntityTypes.SHADOW, ShadowEntity.createAttributes().build());
        event.put(ModEntityTypes.FIRE_ELEMENTAL, FireElementalEntity.createAttributes().build());
        event.put(ModEntityTypes.EARTH_ELEMENTAL, EarthElementalEntity.createAttributes().build());
        event.put(ModEntityTypes.CLAY_GOLEM, ClayGolemEntity.createAttributes().build());
        event.put(ModEntityTypes.GIANT_ZOMBIE, GiantZombieEntity.createAttributes().build());
        event.put(ModEntityTypes.CAVE_SPIDER, CaveSpiderEntity.createAttributes().build());
        event.put(ModEntityTypes.INFERNAL_CREEPER, net.minecraft.world.entity.monster.Creeper.createAttributes().build());

    }
}
