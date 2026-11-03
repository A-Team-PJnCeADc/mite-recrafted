package com.mite.recraft.registry;

import com.mite.recraft.api.MiteRegistration;
import com.mite.recraft.content.block.anvil.ModAnvilBlock;
import com.mite.recraft.internal.data.EntityData;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 注册阶段声明层：common 只声明“有哪些注册阶段、按什么顺序、每阶段写哪些内容”，
 * 不持有任何加载器的注册表句柄。
 *
 * <p>Fabric 在模组初始化时按 {@link #order()} 逐阶段立即执行；NeoForge 在自己的
 * RegisterEvent 回调里按注册表键调用 {@link #run(ResourceKey)}，由加载器负责开窗与冻结。
 * 同一阶段只执行一次。</p>
 */
public final class MiteRegistrationPhases {

    private static final List<ResourceKey<? extends Registry<?>>> ORDER = List.of(
            Registries.SOUND_EVENT,
            Registries.DATA_COMPONENT_TYPE,
            Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE,
            Registries.BLOCK,
            Registries.BLOCK_ENTITY_TYPE,
            Registries.ITEM,
            Registries.MENU,
            Registries.ENTITY_TYPE,
            Registries.CREATIVE_MODE_TAB
    );

    private static final Set<ResourceKey<? extends Registry<?>>> DONE = new LinkedHashSet<>();

    private MiteRegistrationPhases() {
    }

    /** 阶段顺序即声明顺序，也是 Fabric 侧的执行顺序。 */
    public static List<ResourceKey<? extends Registry<?>>> order() {
        return ORDER;
    }

    /**
     * 执行某个注册表对应的阶段。
     *
     * @return 本次是否真正执行（未声明或已执行过返回 false）
     */
    public static boolean run(ResourceKey<? extends Registry<?>> key) {
        if (!ORDER.contains(key) || !DONE.add(key)) {
            return false;
        }
        if (key.equals(Registries.SOUND_EVENT)) {
            ModSounds.register();
        } else if (key.equals(Registries.DATA_COMPONENT_TYPE)) {
            ModDataComponents.register();
        } else if (key.equals(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE)) {
            ModEnchantmentEffects.register();
        } else if (key.equals(Registries.BLOCK)) {
            ModBlockRegister.registerBlocks();
            ModFurnaces.registerBlocks();
            ModStrongboxes.registerBlocks();
            ModChests.registerBlocks();
        } else if (key.equals(Registries.BLOCK_ENTITY_TYPE)) {
            ModAnvilBlock.registerBlockEntityType();
            ModFurnaces.registerBlockEntityType();
            ModStrongboxes.registerBlockEntityTypes();
            ModChests.registerBlockEntityTypes();
        } else if (key.equals(Registries.ITEM)) {
            ModItems.register();
            MiteRegistration.flushDeferredItems();
            ModChests.registerUpgradeItems();
        } else if (key.equals(Registries.MENU)) {
            ModMenus.register();
            ModStrongboxes.registerMenus();
            ModChests.registerMenus();
        } else if (key.equals(Registries.ENTITY_TYPE)) {
            ModEntityTypes.register();
        } else if (key.equals(Registries.CREATIVE_MODE_TAB)) {
            ModCreativeTabs.register();
        }
        return true;
    }

    /** 立即执行全部阶段：Fabric 侧入口使用。 */
    public static void runAll() {
        for (ResourceKey<? extends Registry<?>> key : ORDER) {
            run(key);
        }
        // 附加数据不是原版注册表，Fabric 在初始化阶段仍可写入
        EntityData.register();
    }
}
