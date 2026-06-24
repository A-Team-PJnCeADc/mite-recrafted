package com.mite.recraft.block.workbench;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ModWorkbenchBlock extends CraftingTableBlock {

    public static final MenuType<ModWorkbenchMenu> MENU_TYPE =
            new MenuType<ModWorkbenchMenu>((syncId, inventory) ->
                    new ModWorkbenchMenu(syncId, inventory,
                            ContainerLevelAccess.create(inventory.player.level(), BlockPos.ZERO),
                            WorkbenchMaterial.FLINT
                    ),
                    FeatureFlags.DEFAULT_FLAGS
            );

    private final WorkbenchMaterial material;

    public ModWorkbenchBlock(WorkbenchMaterial material, Properties settings) {
        super(settings);
        this.material = material;
    }

    public WorkbenchMaterial getMaterial() { return material; }

    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level world, BlockPos pos) {
        var mat = this.material;
        var access = ContainerLevelAccess.create(world, pos);
        return new MenuProvider() {
            @Override
            public Component getDisplayName() { return getName(); }

            @Override
            public AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player) {
                return new ModWorkbenchMenu(syncId, inventory, access, mat);
            }
        };
    }
}
