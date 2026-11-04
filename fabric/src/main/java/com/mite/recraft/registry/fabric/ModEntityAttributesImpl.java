package com.mite.recraft.registry.fabric;

import com.mite.recraft.content.entity.mob.*;
import com.mite.recraft.registry.ModEntityTypes;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

/**
 * Fabric 侧的生物属性登记。Fabric 允许在模组初始化期直接写入属性表。
 */
public final class ModEntityAttributesImpl {

    private ModEntityAttributesImpl() {
    }

    public static void register() {
        FabricDefaultAttributeRegistry.register(ModEntityTypes.GHOUL, GhoulEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.LONGDEAD, LongdeadEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.REVENANT, RevenantEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.WIGHT, WightEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.BONE_LORD, BoneLordEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.ANCIENT_BONE_LORD, AncientBoneLordEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.WOOD_SPIDER, WoodSpiderEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.BLACK_WIDOW, BlackWidowSpiderEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.PHASE_SPIDER, PhaseSpiderEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.DEMON_SPIDER, DemonSpiderEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.DIRE_WOLF, DireWolfEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.HELLHOUND, HellhoundEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.COPPERSPINE, net.minecraft.world.entity.monster.Silverfish.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.HOARY_SILVERFISH, net.minecraft.world.entity.monster.Silverfish.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.NETHERSPAWN, net.minecraft.world.entity.monster.Silverfish.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.LONGDEAD_GUARDIAN, LongdeadGuardianEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.INVISIBLE_STALKER, InvisibleStalkerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.BLOB, BlobEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.OOZE, OozeEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.JELLY, JellyEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.PUDDING, PuddingEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.VAMPIRE_BAT, VampireBatEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.GIANT_VAMPIRE_BAT, GiantVampireBatEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.NIGHTWING, NightwingEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.SHADOW, ShadowEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.FIRE_ELEMENTAL, FireElementalEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.EARTH_ELEMENTAL, EarthElementalEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.CLAY_GOLEM, ClayGolemEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.GIANT_ZOMBIE, GiantZombieEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.CAVE_SPIDER, CaveSpiderEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.INFERNAL_CREEPER, net.minecraft.world.entity.monster.Creeper.createAttributes());

    }
}
