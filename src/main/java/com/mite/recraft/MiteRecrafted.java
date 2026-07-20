package com.mite.recraft;

import com.mite.recraft.block.ModBlocks;
import com.mite.recraft.block.workbench.ModWorkbenchBlock;
import com.mite.recraft.component.ModDataComponents;
import com.mite.recraft.entity.ModEntitys;
import com.mite.recraft.item.ModCreativeTabs;
import com.mite.recraft.item.ModItems;
import com.mite.recraft.item.moditems.food.CommonFood;
import com.mite.recraft.item.moditems.food.EdibleOverride;
import com.mite.recraft.item.moditems.food.NutritionSystem;
import com.mite.recraft.item.moditems.bucket.ModBucketItems;
import com.mite.recraft.item.tools.toolItem.WoodenItems;
import com.mite.recraft.network.CraftingProgressSyncPayload;
import com.mite.recraft.network.NutritionSyncPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.registry.FuelValueEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiteRecrafted implements ModInitializer {
    public static final String MOD_ID = "mite-recrafted";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");

        registerSoundEvents();
        ModDataComponents.init();
        ModItems.init();
        ModCreativeTabs.init();
        ModBlocks.init();
        ModEntitys.init();

        // 为非食物物品添加食用组件
        EdibleOverride.init();

        //注册 AttachmentType
        NutritionSystem.ensureLoaded();

        // MenuType
        Registry.register(
                BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(MOD_ID, "workbench"),
                ModWorkbenchBlock.MENU_TYPE
        );

        PayloadTypeRegistry.clientboundPlay().register(CraftingProgressSyncPayload.TYPE, CraftingProgressSyncPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(NutritionSyncPayload.TYPE, NutritionSyncPayload.CODEC);

        // 燃料
        FuelValueEvents.BUILD.register((builder, ctx) -> {
            builder.add(WoodenItems.CLUB, 200);
            builder.add(WoodenItems.CUDGEL, 200);

            // MITE 岩浆桶：可烧制 16 个物品（3200 tick）
            builder.add(ModBucketItems.COPPER_LAVA_BUCKET, 3200);
            builder.add(ModBucketItems.SILVER_LAVA_BUCKET, 3200);
            builder.add(ModBucketItems.GOLD_LAVA_BUCKET, 3200);
            builder.add(ModBucketItems.ANCIENT_METAL_LAVA_BUCKET, 3200);
            builder.add(ModBucketItems.MITHRIL_LAVA_BUCKET, 3200);
            builder.add(ModBucketItems.ADAMANTIUM_LAVA_BUCKET, 3200);
        });

        // 玩家登录时发送初始营养数据
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                NutritionSystem.sendSyncPacket(handler.getPlayer()));
    }

    private static void registerSoundEvents() {
        String[] names = {"music_disc.descent", "music_disc.legends",
                "music_disc.underworld", "music_disc.wanderer"};
        for (String name : names) {
            Registry.register(BuiltInRegistries.SOUND_EVENT,
                    Identifier.fromNamespaceAndPath(MOD_ID, name),
                    SoundEvent.createVariableRangeEvent(
                            Identifier.fromNamespaceAndPath(MOD_ID, name)));
        }
    }
}
