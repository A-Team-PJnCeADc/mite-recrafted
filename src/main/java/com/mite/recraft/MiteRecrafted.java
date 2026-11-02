package com.mite.recraft;

import com.mite.recraft.content.item.food.NutritionSystem;
import com.mite.recraft.registry.ModBlockRegister;
import com.mite.recraft.registry.ModChests;
import com.mite.recraft.registry.ModCreativeTabs;
import com.mite.recraft.registry.ModDataComponents;
import com.mite.recraft.registry.ModEnchantmentEffects;
import com.mite.recraft.registry.ModEntityTypes;
import com.mite.recraft.registry.ModFuels;
import com.mite.recraft.registry.ModItems;
import com.mite.recraft.registry.ModMenus;
import com.mite.recraft.registry.ModNetwork;
import com.mite.recraft.registry.ModSounds;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通用入口（main 源集）：服务端与客户端共用的注册顺序在此固定。
 *
 * <p>注册顺序即为注册表写入顺序，也是创造模式物品栏的展示顺序；
 * 修改顺序会改变物品栏排版。子模组可参照此顺序在自己的入口中注册内容。</p>
 */
public class MiteRecrafted implements ModInitializer {

    public static final String MOD_ID = "mite-recrafted";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModSounds.register();
        ModDataComponents.register();
        ModEnchantmentEffects.register();
        NutritionSystem.register();

        ModItems.register();
        ModBlockRegister.register();
        ModChests.register();
        ModCreativeTabs.register();
        ModMenus.register();
        ModEntityTypes.register();

        ModNetwork.register();
        ModFuels.register();
    }
}
