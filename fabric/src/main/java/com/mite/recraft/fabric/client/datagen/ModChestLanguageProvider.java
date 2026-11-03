package com.mite.recraft.fabric.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 箱子文案的语言文件生成器：按语种输出 {@link ModChestLanguageData} 里的条目。
 */
public class ModChestLanguageProvider extends FabricLanguageProvider {

    private final String locale;
    private final Map<String, String> entries;

    public ModChestLanguageProvider(FabricPackOutput output,
                                    CompletableFuture<HolderLookup.Provider> registryLookup,
                                    String locale, Map<String, String> entries) {
        super(output, locale, registryLookup);
        this.locale = locale;
        this.entries = entries;
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder builder) {
        this.entries.forEach(builder::add);
    }

    @Override
    public String getName() {
        return "Chest Translations (" + this.locale + ")";
    }
}
