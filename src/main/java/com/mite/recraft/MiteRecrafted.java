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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiteRecrafted implements ModInitializer {
	public static final String MOD_ID = "mite-recrafted";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
	    LOGGER.info("Hello Fabric world!");

	    ModDataComponents.initialize();
	    ModItems.init();
	    ModCreativeTabs.init();
		ModBlocks.init();
	    ModEntitys.init();

		// MenuType
	    net.minecraft.core.Registry.register(
	            net.minecraft.core.registries.BuiltInRegistries.MENU,
	            net.minecraft.resources.Identifier.fromNamespaceAndPath(MOD_ID, "workbench"),
	            ModWorkbenchBlock.MENU_TYPE
	    );
	    PayloadTypeRegistry.clientboundPlay().register(CraftingProgressSyncPayload.TYPE, CraftingProgressSyncPayload.CODEC);

	    // 燃料
	    FuelValueEvents.BUILD.register((builder, ctx) -> {
	        builder.add(WoodenItems.CLUB, 200);
	        builder.add(WoodenItems.CUDGEL, 200);
	    });
	}
}
