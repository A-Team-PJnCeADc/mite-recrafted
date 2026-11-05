package com.mite.recraft.internal.data.neoforge;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.api.MiteData;
import com.mite.recraft.internal.data.EntityDataApi;

import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.Map;

/**
 * NeoForge 平台实现：以 attachment 承载附加数据。
 *
 * <p>描述符对象在 {@link #register(IEventBus)} 里即时构造并存入查找表，注册动作交给
 * DeferredRegister，由 NeoForge 在自己的注册窗口内写表。存档格式为 {{@code "value": <编码值>}}。</p>
 */
public final class EntityDataImpl implements EntityDataApi {

    private static final Map<MiteData<?>, AttachmentType<?>> TYPES = new HashMap<>();
    private static boolean registered;

    /** 无参注册由 common 调用；NeoForge 的附件表在 {@link #register(IEventBus)} 里写。 */
    @Override
    public void register() {
        // NeoForge 的附件注册表只在 DeferredRegister 窗口内可写，无参入口不做事
    }

    public static void register(IEventBus modBus) {
        if (registered) {
            return;
        }
        registered = true;
        DeferredRegister<AttachmentType<?>> deferred =
                DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MiteRecrafted.MOD_ID);
        for (MiteData<?> data : MiteData.all()) {
            declare(data, deferred);
        }
        deferred.register(modBus);
    }

    private static <T> void declare(MiteData<T> data, DeferredRegister<AttachmentType<?>> deferred) {
        AttachmentType.Builder<T> builder = AttachmentType.builder(data.initialValue())
                .serialize(data.codec().fieldOf("value"));
        if (data.copyOnDeath()) {
            builder.copyOnDeath();
        }
        // 不使用 builder.sync：客户端数值由本模组自己的 NutritionSyncPayload 下发（登录与数值变化时）
        AttachmentType<T> type = builder.build();
        TYPES.put(data, type);
        deferred.register(data.id().getPath(), () -> type);
    }

    @SuppressWarnings("unchecked")
    private static <T> AttachmentType<T> type(MiteData<T> data) {
        AttachmentType<T> type = (AttachmentType<T>) TYPES.get(data);
        if (type == null) {
            throw new IllegalStateException("附加数据未注册: " + data.id());
        }
        return type;
    }

    @Override
    public <T> T get(Entity entity, MiteData<T> data) {
        return ((IAttachmentHolder) entity).getData(type(data));
    }

    @Override
    public <T> void set(Entity entity, MiteData<T> data, T value) {
        ((IAttachmentHolder) entity).setData(type(data), value);
    }
}
