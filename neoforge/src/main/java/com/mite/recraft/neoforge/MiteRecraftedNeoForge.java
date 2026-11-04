package com.mite.recraft.neoforge;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.content.item.food.NutritionSystem;
import com.mite.recraft.internal.data.neoforge.EntityDataImpl;
import com.mite.recraft.registry.MiteRegistrationPhases;
import com.mite.recraft.registry.neoforge.ModEntityAttributesImpl;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * NeoForge 入口。注册阶段由 RegisterEvent 按注册表回调，运行期初始化在 ModSetup 之后执行。
 *
 * <p>NeoForge 只在自己开窗的 RegisterEvent 期间解冻目标注册表，因此这里不做任何解冻或冻结操作，
 * 只把注册表键交给 {@link MiteRegistrationPhases}；附加数据走 DeferredRegister。</p>
 */
@Mod(MiteRecrafted.MOD_ID)
public final class MiteRecraftedNeoForge {

    public MiteRecraftedNeoForge(IEventBus modBus) {
        // 触发附加数据描述符的类初始化，再交给 DeferredRegister 写表
        NutritionSystem.register();
        EntityDataImpl.register(modBus);

        modBus.addListener(MiteRecraftedNeoForge::onRegister);
        modBus.addListener(MiteRecraftedNeoForge::onCommonSetup);
        modBus.addListener(ModEntityAttributesImpl::onAttributeCreation);
    }

    private static void onRegister(RegisterEvent event) {
        ResourceKey<? extends Registry<?>> key = event.getRegistryKey();
        MiteRegistrationPhases.run(key);
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(MiteRecrafted::initRuntime);
    }
}
