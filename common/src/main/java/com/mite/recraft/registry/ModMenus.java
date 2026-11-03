package com.mite.recraft.registry;

import com.mite.recraft.api.MiteIds;
import com.mite.recraft.content.block.workbench.ModWorkbenchMenu;
import com.mite.recraft.content.block.workbench.WorkbenchMaterial;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;

/**
 * 菜单类型注册。Strongbox 的菜单随方块注册一并完成，此处只处理工作台。
 */
public final class ModMenus {

    public static final MenuType<ModWorkbenchMenu> WORKBENCH =
            new MenuType<>((syncId, inventory) -> new ModWorkbenchMenu(
                    syncId,
                    inventory,
                    ContainerLevelAccess.create(inventory.player.level(), BlockPos.ZERO),
                    WorkbenchMaterial.FLINT
            ), FeatureFlags.DEFAULT_FLAGS);

    private ModMenus() {
    }

    public static void register() {
        Registry.register(BuiltInRegistries.MENU, MiteIds.id("workbench"), WORKBENCH);
    }
}
