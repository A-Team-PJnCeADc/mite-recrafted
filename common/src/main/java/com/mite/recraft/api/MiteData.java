package com.mite.recraft.api;

import com.mojang.serialization.Codec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * 实体附加数据描述符：一份描述，由平台模块分别注册为 Fabric 附件或 NeoForge attachment。
 *
 * <p>实例在类初始化时创建并记入 {@link #all()}。平台实现在模组初始化末尾按该列表统一注册，
 * 因此描述符必须在 {@code MiteRecrafted.init()} 之前完成类初始化（静态字段即可满足）。</p>
 *
 * <p>注册完成后描述符不再变化；读写通过 {@code com.mite.recraft.internal.data.EntityData}。</p>
 */
public final class MiteData<T> {

    private static final List<MiteData<?>> ALL = new ArrayList<>();

    private final Identifier id;
    private final Supplier<T> initialValue;
    private final Codec<T> codec;
    private final StreamCodec<? super RegistryFriendlyByteBuf, T> syncCodec;
    private final boolean copyOnDeath;

    private MiteData(Identifier id, Supplier<T> initialValue, Codec<T> codec,
                     StreamCodec<? super RegistryFriendlyByteBuf, T> syncCodec, boolean copyOnDeath) {
        this.id = id;
        this.initialValue = initialValue;
        this.codec = codec;
        this.syncCodec = syncCodec;
        this.copyOnDeath = copyOnDeath;
        ALL.add(this);
    }

    /**
     * @param initialValue 实体上不存在该数据时的初始值
     * @param codec        存档序列化
     * @param syncCodec    向客户端同步的编码器，null 表示不同步
     * @param copyOnDeath  玩家死亡后保留（仅对附着在玩家上的数据有意义）
     */
    public static <T> MiteData<T> create(Identifier id, Supplier<T> initialValue, Codec<T> codec,
                                         StreamCodec<? super RegistryFriendlyByteBuf, T> syncCodec,
                                         boolean copyOnDeath) {
        return new MiteData<>(id, initialValue, codec, syncCodec, copyOnDeath);
    }

    public Identifier id() {
        return id;
    }

    public Supplier<T> initialValue() {
        return initialValue;
    }

    public Codec<T> codec() {
        return codec;
    }

    public StreamCodec<? super RegistryFriendlyByteBuf, T> syncCodec() {
        return syncCodec;
    }

    public boolean copyOnDeath() {
        return copyOnDeath;
    }

    public boolean synced() {
        return syncCodec != null;
    }

    public static List<MiteData<?>> all() {
        return Collections.unmodifiableList(ALL);
    }
}
