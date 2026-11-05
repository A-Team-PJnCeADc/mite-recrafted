package com.mite.recraft.internal.data.fabric;

import com.mite.recraft.api.MiteData;
import com.mite.recraft.internal.data.EntityDataApi;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Fabric 平台实现：以 data attachment 承载附加数据。
 *
 * <p>附件类型在 {@link #register()} 中一次性创建并登记，之后只做表查询。实例由平台入口注入。</p>
 */
public final class EntityDataImpl implements EntityDataApi {

    private static final Map<MiteData<?>, AttachmentType<?>> TYPES = new HashMap<>();
    private static boolean registered;

    @Override
    public void register() {
        if (registered) {
            return;
        }
        registered = true;
        for (MiteData<?> data : MiteData.all()) {
            register(data);
        }
    }

    private static <T> void register(MiteData<T> data) {
        AttachmentRegistry.Builder<T> builder = AttachmentRegistry.<T>builder()
                .initializer(data.initialValue())
                .persistent(data.codec());
        if (data.copyOnDeath()) {
            builder.copyOnDeath();
        }
        if (data.synced()) {
            builder.syncWith(data.syncCodec(), AttachmentSyncPredicate.all());
        }
        TYPES.put(data, builder.buildAndRegister(data.id()));
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
        return ((AttachmentTarget) entity).getAttachedOrCreate(type(data));
    }

    @Override
    public <T> void set(Entity entity, MiteData<T> data, T value) {
        ((AttachmentTarget) entity).setAttached(type(data), value);
    }
}
