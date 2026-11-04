package com.mite.recraft.registry;

import com.mite.recraft.api.ItemCategory;
import com.mite.recraft.api.MiteRegistration;
import com.mite.recraft.content.entity.mob.EarthElementalEntity;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.TypedEntityData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 生物刷怪蛋。刷怪蛋通过 {@code ENTITY_DATA} 组件绑定实体类型，与 26.3 原版刷怪蛋一致。
 *
 * <p>物品阶段早于实体阶段，这里只保存实体类型引用；实体类型对象在类初始化时已构造完成，
 * 注册顺序不影响引用。</p>
 */
public final class ModSpawnEggs {

    private static final List<Item> EGGS = new ArrayList<>();

    private ModSpawnEggs() {
    }

    /** 物品阶段。 */
    public static void register() {
        register("ghoul", ModEntityTypes.GHOUL);
        register("longdead", ModEntityTypes.LONGDEAD);
        register("revenant", ModEntityTypes.REVENANT);
        register("wight", ModEntityTypes.WIGHT);
        register("bone_lord", ModEntityTypes.BONE_LORD);
        register("ancient_bone_lord", ModEntityTypes.ANCIENT_BONE_LORD);
        register("wood_spider", ModEntityTypes.WOOD_SPIDER);
        register("black_widow_spider", ModEntityTypes.BLACK_WIDOW);
        register("phase_spider", ModEntityTypes.PHASE_SPIDER);
        register("demon_spider", ModEntityTypes.DEMON_SPIDER);
        register("dire_wolf", ModEntityTypes.DIRE_WOLF);
        register("hellhound", ModEntityTypes.HELLHOUND);
        register("copperspine", ModEntityTypes.COPPERSPINE);
        register("hoary_silverfish", ModEntityTypes.HOARY_SILVERFISH);
        register("netherspawn", ModEntityTypes.NETHERSPAWN);
        register("infernal_creeper", ModEntityTypes.INFERNAL_CREEPER);
        register("invisible_stalker", ModEntityTypes.INVISIBLE_STALKER);
        register("blob", ModEntityTypes.BLOB);
        register("ooze", ModEntityTypes.OOZE);
        register("jelly", ModEntityTypes.JELLY);
        register("pudding", ModEntityTypes.PUDDING);
        register("shadow", ModEntityTypes.SHADOW);
        register("fire_elemental", ModEntityTypes.FIRE_ELEMENTAL);
        register("earth_elemental", ModEntityTypes.EARTH_ELEMENTAL, elemental(EarthElementalEntity.TYPE_STONE));
        register("earth_elemental_obsidian", ModEntityTypes.EARTH_ELEMENTAL, elemental(EarthElementalEntity.TYPE_OBSIDIAN));
        register("earth_elemental_netherrack", ModEntityTypes.EARTH_ELEMENTAL, elemental(EarthElementalEntity.TYPE_NETHERRACK));
        register("earth_elemental_end_stone", ModEntityTypes.EARTH_ELEMENTAL, elemental(EarthElementalEntity.TYPE_END_STONE));
        register("clay_golem", ModEntityTypes.CLAY_GOLEM, elemental(EarthElementalEntity.TYPE_CLAY));
        register("clay_golem_hardened", ModEntityTypes.CLAY_GOLEM, hardenedClay());
    }

    private static void register(String path, EntityType<? extends Mob> type) {
        register(path, type, new CompoundTag());
    }

    /** 带初始 NBT 的刷怪蛋，用于土元素这类按形态区分的生物。 */
    private static void register(String path, EntityType<? extends Mob> type, CompoundTag data) {
        Item item = MiteRegistration.registerItem(ItemCategory.SPAWN_EGG, path + "_spawn_egg",
                props -> new SpawnEggItem(props.component(DataComponents.ENTITY_DATA,
                        TypedEntityData.of(type, data))));
        EGGS.add(item);
    }

    /** 土元素形态 NBT：elemental_type 选择石/黑曜石/地狱岩/末地石。 */
    private static CompoundTag elemental(int type) {
        CompoundTag data = new CompoundTag();
        data.putInt("elemental_type", type);
        return data;
    }

    /** 硬化黏土傀儡：elemental_type 为黏土，另加硬化标记。 */
    private static CompoundTag hardenedClay() {
        CompoundTag data = new CompoundTag();
        data.putInt("elemental_type", EarthElementalEntity.TYPE_CLAY);
        data.putBoolean("hardened", true);
        return data;
    }

    public static List<Item> all() {
        return Collections.unmodifiableList(EGGS);
    }
}
