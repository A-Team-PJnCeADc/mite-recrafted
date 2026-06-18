package com.mite.recraft.client;

import com.mite.recraft.client.datagen.ModModelProvider;
import com.mite.recraft.datagen.ModChineseLanguageProvider;
import com.mite.recraft.datagen.ModEnglishLanguageProvider;
import com.mite.recraft.datagen.ModRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MiteRecraftedDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModEnglishLanguageProvider::new);
		pack.addProvider(ModChineseLanguageProvider::new);
	}
}
