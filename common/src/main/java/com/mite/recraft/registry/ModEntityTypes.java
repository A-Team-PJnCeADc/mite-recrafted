package com.mite.recraft.registry;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.content.entity.ArrowEntity;
import com.mite.recraft.content.entity.mob.*;
import com.mite.recraft.content.entity.projectile.BrickEntity;
import com.mite.recraft.content.entity.projectile.GelatinousSphereEntity;
import com.mite.recraft.content.entity.projectile.WebEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

/**
 * 实体类型。字段初始化只构造对象，写入注册表在 {@link #register()} 中完成。
 */
public final class ModEntityTypes {

    private static final ResourceKey<EntityType<?>> MITE_ARROW_KEY = key("mite_arrow");
    private static final ResourceKey<EntityType<?>> GHOUL_KEY = key("ghoul");
    private static final ResourceKey<EntityType<?>> LONGDEAD_KEY = key("longdead");
    private static final ResourceKey<EntityType<?>> REVENANT_KEY = key("revenant");
    private static final ResourceKey<EntityType<?>> WIGHT_KEY = key("wight");
    private static final ResourceKey<EntityType<?>> BONE_LORD_KEY = key("bone_lord");
    private static final ResourceKey<EntityType<?>> ANCIENT_BONE_LORD_KEY = key("ancient_bone_lord");
    private static final ResourceKey<EntityType<?>> WOOD_SPIDER_KEY = key("wood_spider");
    private static final ResourceKey<EntityType<?>> BLACK_WIDOW_KEY = key("black_widow_spider");
    private static final ResourceKey<EntityType<?>> PHASE_SPIDER_KEY = key("phase_spider");
    private static final ResourceKey<EntityType<?>> DEMON_SPIDER_KEY = key("demon_spider");
    private static final ResourceKey<EntityType<?>> DIRE_WOLF_KEY = key("dire_wolf");
    private static final ResourceKey<EntityType<?>> HELLHOUND_KEY = key("hellhound");
    private static final ResourceKey<EntityType<?>> COPPERSPINE_KEY = key("copperspine");
    private static final ResourceKey<EntityType<?>> HOARY_SILVERFISH_KEY = key("hoary_silverfish");
    private static final ResourceKey<EntityType<?>> NETHERSPAWN_KEY = key("netherspawn");
    private static final ResourceKey<EntityType<?>> LONGDEAD_GUARDIAN_KEY = key("longdead_guardian");
    private static final ResourceKey<EntityType<?>> INFERNAL_CREEPER_KEY = key("infernal_creeper");
    private static final ResourceKey<EntityType<?>> INVISIBLE_STALKER_KEY = key("invisible_stalker");
    private static final ResourceKey<EntityType<?>> BLOB_KEY = key("blob");
    private static final ResourceKey<EntityType<?>> OOZE_KEY = key("ooze");
    private static final ResourceKey<EntityType<?>> JELLY_KEY = key("jelly");
    private static final ResourceKey<EntityType<?>> PUDDING_KEY = key("pudding");
    private static final ResourceKey<EntityType<?>> VAMPIRE_BAT_KEY = key("vampire_bat");
    private static final ResourceKey<EntityType<?>> GIANT_VAMPIRE_BAT_KEY = key("giant_vampire_bat");
    private static final ResourceKey<EntityType<?>> NIGHTWING_KEY = key("nightwing");
    private static final ResourceKey<EntityType<?>> WEB_KEY = key("web");
    private static final ResourceKey<EntityType<?>> BRICK_KEY = key("brick");
    private static final ResourceKey<EntityType<?>> GELATINOUS_SPHERE_KEY = key("gelatinous_sphere");
    private static final ResourceKey<EntityType<?>> SHADOW_KEY = key("shadow");
    private static final ResourceKey<EntityType<?>> FIRE_ELEMENTAL_KEY = key("fire_elemental");
    private static final ResourceKey<EntityType<?>> EARTH_ELEMENTAL_KEY = key("earth_elemental");
    private static final ResourceKey<EntityType<?>> CLAY_GOLEM_KEY = key("clay_golem");
    private static final ResourceKey<EntityType<?>> GIANT_ZOMBIE_KEY = key("giant_zombie");
    private static final ResourceKey<EntityType<?>> CAVE_SPIDER_KEY = key("cave_spider");

    public static final EntityType<ArrowEntity> MITE_ARROW = EntityType.Builder
            .<ArrowEntity>of(ArrowEntity::new, MobCategory.MISC)
            .sized(0.5f, 0.5f)
            .clientTrackingRange(4)
            .updateInterval(20)
            .build(MITE_ARROW_KEY);

    public static final EntityType<GhoulEntity> GHOUL = EntityType.Builder
            .<GhoulEntity>of(GhoulEntity::new, MobCategory.MONSTER)
            .sized(0.6f, 1.8f)
            .eyeHeight(1.62f)
            .clientTrackingRange(8)
            .build(GHOUL_KEY);

    public static final EntityType<LongdeadEntity> LONGDEAD = EntityType.Builder
            .<LongdeadEntity>of(LongdeadEntity::new, MobCategory.MONSTER)
            .sized(0.6f, 1.99f)
            .eyeHeight(1.74f)
            .clientTrackingRange(8)
            .build(LONGDEAD_KEY);

    public static final EntityType<RevenantEntity> REVENANT = EntityType.Builder
            .<RevenantEntity>of(RevenantEntity::new, MobCategory.MONSTER)
            .sized(0.6f, 1.95f)
            .eyeHeight(1.74f)
            .clientTrackingRange(8)
            .build(REVENANT_KEY);

    public static final EntityType<WightEntity> WIGHT = EntityType.Builder
            .<WightEntity>of(WightEntity::new, MobCategory.MONSTER)
            .sized(0.6f, 1.8f)
            .eyeHeight(1.62f)
            .clientTrackingRange(8)
            .build(WIGHT_KEY);

    public static final EntityType<BoneLordEntity> BONE_LORD = EntityType.Builder
            .<BoneLordEntity>of(BoneLordEntity::new, MobCategory.MONSTER)
            .sized(0.6f, 1.99f)
            .eyeHeight(1.74f)
            .clientTrackingRange(8)
            .build(BONE_LORD_KEY);

    public static final EntityType<AncientBoneLordEntity> ANCIENT_BONE_LORD = EntityType.Builder
            .<AncientBoneLordEntity>of(AncientBoneLordEntity::new, MobCategory.MONSTER)
            .sized(0.6f, 1.99f)
            .eyeHeight(1.74f)
            .clientTrackingRange(8)
            .build(ANCIENT_BONE_LORD_KEY);


    public static final EntityType<WoodSpiderEntity> WOOD_SPIDER = EntityType.Builder
            .<WoodSpiderEntity>of(WoodSpiderEntity::new, MobCategory.MONSTER)
            .sized(1.4f, 0.9f)
            .clientTrackingRange(8)
            .build(WOOD_SPIDER_KEY);

    public static final EntityType<BlackWidowSpiderEntity> BLACK_WIDOW = EntityType.Builder
            .<BlackWidowSpiderEntity>of(BlackWidowSpiderEntity::new, MobCategory.MONSTER)
            .sized(1.4f, 0.9f)
            .clientTrackingRange(8)
            .build(BLACK_WIDOW_KEY);

    public static final EntityType<PhaseSpiderEntity> PHASE_SPIDER = EntityType.Builder
            .<PhaseSpiderEntity>of(PhaseSpiderEntity::new, MobCategory.MONSTER)
            .sized(1.4f, 0.9f)
            .clientTrackingRange(8)
            .build(PHASE_SPIDER_KEY);

    public static final EntityType<DemonSpiderEntity> DEMON_SPIDER = EntityType.Builder
            .<DemonSpiderEntity>of(DemonSpiderEntity::new, MobCategory.MONSTER)
            .sized(1.4f, 0.9f)
            .clientTrackingRange(8)
            .build(DEMON_SPIDER_KEY);

    public static final EntityType<DireWolfEntity> DIRE_WOLF = EntityType.Builder
            .<DireWolfEntity>of(DireWolfEntity::new, MobCategory.MONSTER)
            .sized(0.6f, 0.8f)
            .clientTrackingRange(8)
            .build(DIRE_WOLF_KEY);

    public static final EntityType<HellhoundEntity> HELLHOUND = EntityType.Builder
            .<HellhoundEntity>of(HellhoundEntity::new, MobCategory.MONSTER)
            .sized(0.6f, 0.8f)
            .clientTrackingRange(8)
            .build(HELLHOUND_KEY);

    public static final EntityType<CopperspineEntity> COPPERSPINE = EntityType.Builder
            .<CopperspineEntity>of(CopperspineEntity::new, MobCategory.MONSTER)
            .sized(0.4f, 0.3f)
            .clientTrackingRange(8)
            .build(COPPERSPINE_KEY);

    public static final EntityType<HoarySilverfishEntity> HOARY_SILVERFISH = EntityType.Builder
            .<HoarySilverfishEntity>of(HoarySilverfishEntity::new, MobCategory.MONSTER)
            .sized(0.4f, 0.3f)
            .clientTrackingRange(8)
            .build(HOARY_SILVERFISH_KEY);

    public static final EntityType<NetherspawnEntity> NETHERSPAWN = EntityType.Builder
            .<NetherspawnEntity>of(NetherspawnEntity::new, MobCategory.MONSTER)
            .sized(0.4f, 0.3f)
            .clientTrackingRange(8)
            .build(NETHERSPAWN_KEY);

    public static final EntityType<LongdeadGuardianEntity> LONGDEAD_GUARDIAN = EntityType.Builder
            .<LongdeadGuardianEntity>of(LongdeadGuardianEntity::new, MobCategory.MONSTER)
            .sized(0.6f, 1.99f)
            .clientTrackingRange(8)
            .build(LONGDEAD_GUARDIAN_KEY);

    public static final EntityType<InfernalCreeperEntity> INFERNAL_CREEPER = EntityType.Builder
            .<InfernalCreeperEntity>of(InfernalCreeperEntity::new, MobCategory.MONSTER)
            .sized(0.6f, 1.7f)
            .clientTrackingRange(8)
            .build(INFERNAL_CREEPER_KEY);

    public static final EntityType<InvisibleStalkerEntity> INVISIBLE_STALKER = EntityType.Builder
            .<InvisibleStalkerEntity>of(InvisibleStalkerEntity::new, MobCategory.MONSTER)
            .sized(0.6f, 1.8f)
            .clientTrackingRange(8)
            .build(INVISIBLE_STALKER_KEY);

    public static final EntityType<BlobEntity> BLOB = EntityType.Builder
            .<BlobEntity>of(BlobEntity::new, MobCategory.MONSTER)
            .sized(2.04f, 2.04f)
            .clientTrackingRange(10)
            .build(BLOB_KEY);

    public static final EntityType<OozeEntity> OOZE = EntityType.Builder
            .<OozeEntity>of(OozeEntity::new, MobCategory.MONSTER)
            .sized(2.04f, 2.04f)
            .clientTrackingRange(10)
            .build(OOZE_KEY);

    public static final EntityType<JellyEntity> JELLY = EntityType.Builder
            .<JellyEntity>of(JellyEntity::new, MobCategory.MONSTER)
            .sized(2.04f, 2.04f)
            .clientTrackingRange(10)
            .build(JELLY_KEY);

    public static final EntityType<PuddingEntity> PUDDING = EntityType.Builder
            .<PuddingEntity>of(PuddingEntity::new, MobCategory.MONSTER)
            .sized(2.04f, 2.04f)
            .clientTrackingRange(10)
            .build(PUDDING_KEY);

    public static final EntityType<VampireBatEntity> VAMPIRE_BAT = EntityType.Builder
            .<VampireBatEntity>of(VampireBatEntity::new, MobCategory.AMBIENT)
            .sized(0.5f, 0.9f)
            .clientTrackingRange(8)
            .build(VAMPIRE_BAT_KEY);

    public static final EntityType<GiantVampireBatEntity> GIANT_VAMPIRE_BAT = EntityType.Builder
            .<GiantVampireBatEntity>of(GiantVampireBatEntity::new, MobCategory.AMBIENT)
            .sized(1.0f, 1.8f)
            .clientTrackingRange(8)
            .build(GIANT_VAMPIRE_BAT_KEY);

    public static final EntityType<NightwingEntity> NIGHTWING = EntityType.Builder
            .<NightwingEntity>of(NightwingEntity::new, MobCategory.AMBIENT)
            .sized(0.5f, 0.9f)
            .clientTrackingRange(8)
            .build(NIGHTWING_KEY);

    public static final EntityType<ShadowEntity> SHADOW = EntityType.Builder
            .<ShadowEntity>of(ShadowEntity::new, MobCategory.MONSTER)
            .sized(0.6f, 1.8f)
            .clientTrackingRange(8)
            .build(SHADOW_KEY);

    public static final EntityType<FireElementalEntity> FIRE_ELEMENTAL = EntityType.Builder
            .<FireElementalEntity>of(FireElementalEntity::new, MobCategory.MONSTER)
            .sized(0.6f, 1.8f)
            .clientTrackingRange(8)
            .build(FIRE_ELEMENTAL_KEY);

    public static final EntityType<EarthElementalEntity> EARTH_ELEMENTAL = EntityType.Builder
            .<EarthElementalEntity>of(EarthElementalEntity::new, MobCategory.MONSTER)
            .sized(0.8f, 1.8f)
            .clientTrackingRange(8)
            .build(EARTH_ELEMENTAL_KEY);

    public static final EntityType<ClayGolemEntity> CLAY_GOLEM = EntityType.Builder
            .<ClayGolemEntity>of(ClayGolemEntity::new, MobCategory.MONSTER)
            .sized(0.8f, 1.8f)
            .clientTrackingRange(8)
            .build(CLAY_GOLEM_KEY);

    public static final EntityType<GiantZombieEntity> GIANT_ZOMBIE = EntityType.Builder
            .<GiantZombieEntity>of(GiantZombieEntity::new, MobCategory.MONSTER)
            .sized(0.6f, 1.95f)
            .clientTrackingRange(8)
            .build(GIANT_ZOMBIE_KEY);

    public static final EntityType<CaveSpiderEntity> CAVE_SPIDER = EntityType.Builder
            .<CaveSpiderEntity>of(CaveSpiderEntity::new, MobCategory.MONSTER)
            .sized(0.7f, 0.5f)
            .clientTrackingRange(8)
            .build(CAVE_SPIDER_KEY);

    public static final EntityType<WebEntity> WEB = EntityType.Builder
            .<WebEntity>of(WebEntity::new, MobCategory.MISC)
            .sized(0.25f, 0.25f)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build(WEB_KEY);

    public static final EntityType<BrickEntity> BRICK = EntityType.Builder
            .<BrickEntity>of(BrickEntity::new, MobCategory.MISC)
            .sized(0.25f, 0.25f)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build(BRICK_KEY);

    public static final EntityType<GelatinousSphereEntity> GELATINOUS_SPHERE = EntityType.Builder
            .<GelatinousSphereEntity>of(GelatinousSphereEntity::new, MobCategory.MISC)
            .sized(0.25f, 0.25f)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build(GELATINOUS_SPHERE_KEY);
    private ModEntityTypes() {
    }

    private static ResourceKey<EntityType<?>> key(String path) {
        return ResourceKey.create(Registries.ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, path));
    }

    /** 实体类型阶段。 */
    public static void register() {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, MITE_ARROW_KEY, MITE_ARROW);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, GHOUL_KEY, GHOUL);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, LONGDEAD_KEY, LONGDEAD);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, REVENANT_KEY, REVENANT);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, WIGHT_KEY, WIGHT);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, BONE_LORD_KEY, BONE_LORD);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, ANCIENT_BONE_LORD_KEY, ANCIENT_BONE_LORD);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, WOOD_SPIDER_KEY, WOOD_SPIDER);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, BLACK_WIDOW_KEY, BLACK_WIDOW);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, PHASE_SPIDER_KEY, PHASE_SPIDER);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, DEMON_SPIDER_KEY, DEMON_SPIDER);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, DIRE_WOLF_KEY, DIRE_WOLF);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, HELLHOUND_KEY, HELLHOUND);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, COPPERSPINE_KEY, COPPERSPINE);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, HOARY_SILVERFISH_KEY, HOARY_SILVERFISH);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, NETHERSPAWN_KEY, NETHERSPAWN);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, LONGDEAD_GUARDIAN_KEY, LONGDEAD_GUARDIAN);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, INFERNAL_CREEPER_KEY, INFERNAL_CREEPER);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, INVISIBLE_STALKER_KEY, INVISIBLE_STALKER);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, BLOB_KEY, BLOB);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, OOZE_KEY, OOZE);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, JELLY_KEY, JELLY);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, PUDDING_KEY, PUDDING);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, VAMPIRE_BAT_KEY, VAMPIRE_BAT);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, GIANT_VAMPIRE_BAT_KEY, GIANT_VAMPIRE_BAT);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, NIGHTWING_KEY, NIGHTWING);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, WEB_KEY, WEB);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, BRICK_KEY, BRICK);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, GELATINOUS_SPHERE_KEY, GELATINOUS_SPHERE);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, SHADOW_KEY, SHADOW);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, FIRE_ELEMENTAL_KEY, FIRE_ELEMENTAL);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, EARTH_ELEMENTAL_KEY, EARTH_ELEMENTAL);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, CLAY_GOLEM_KEY, CLAY_GOLEM);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, GIANT_ZOMBIE_KEY, GIANT_ZOMBIE);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, CAVE_SPIDER_KEY, CAVE_SPIDER);
    }
}
