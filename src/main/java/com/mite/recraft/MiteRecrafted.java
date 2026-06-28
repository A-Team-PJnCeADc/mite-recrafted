package com.mite.recraft;

import com.mite.recraft.block.ModBlocks;
import com.mite.recraft.block.workbench.ModWorkbenchBlock;
import com.mite.recraft.component.ModDataComponents;
import com.mite.recraft.entity.ModEntitys;
import com.mite.recraft.item.ModCreativeTabs;
import com.mite.recraft.item.ModItems;
import com.mite.recraft.item.tools.toolItem.WoodenItems;
import com.mite.recraft.network.CraftingProgressSyncPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
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

        // MenuType
        Registry.register(
                BuiltInRegistries.MENU,
                Identifier.fromNamespaceAndPath(MOD_ID, "workbench"),
                ModWorkbenchBlock.MENU_TYPE
        );
        PayloadTypeRegistry.clientboundPlay().register(CraftingProgressSyncPayload.TYPE, CraftingProgressSyncPayload.CODEC);

        // 燃料
        FuelValueEvents.BUILD.register((builder, ctx) -> {
            builder.add(WoodenItems.CLUB, 200);
            builder.add(WoodenItems.CUDGEL, 200);
        });
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
