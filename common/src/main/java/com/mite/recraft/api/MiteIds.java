package com.mite.recraft.api;

import com.mite.recraft.MiteRecrafted;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * 资源 ID 与注册键工厂。无 namespace 参数的重载使用 {@link MiteRecrafted#MOD_ID}。
 */
public final class MiteIds {

    private MiteIds() {
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, path);
    }

    public static Identifier id(String namespace, String path) {
        return Identifier.fromNamespaceAndPath(namespace, path);
    }

    public static ResourceKey<Item> itemKey(String path) {
        return ResourceKey.create(Registries.ITEM, id(path));
    }

    public static ResourceKey<Item> itemKey(Identifier id) {
        return ResourceKey.create(Registries.ITEM, id);
    }

    public static ResourceKey<Block> blockKey(String path) {
        return ResourceKey.create(Registries.BLOCK, id(path));
    }

    public static ResourceKey<Block> blockKey(Identifier id) {
        return ResourceKey.create(Registries.BLOCK, id);
    }

    public static TagKey<Item> itemTag(String path) {
        return TagKey.create(BuiltInRegistries.ITEM.key(), id(path));
    }

    public static TagKey<Item> itemTag(Identifier id) {
        return TagKey.create(BuiltInRegistries.ITEM.key(), id);
    }

    public static TagKey<Block> blockTag(String path) {
        return TagKey.create(BuiltInRegistries.BLOCK.key(), id(path));
    }

    public static TagKey<Block> blockTag(Identifier id) {
        return TagKey.create(BuiltInRegistries.BLOCK.key(), id);
    }
}
