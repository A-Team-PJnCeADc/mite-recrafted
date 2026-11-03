package com.mite.recraft.content.block.workbench;

import com.mite.recraft.registry.ModMenus;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * MITE 工作台方块。材料决定可制作等级与合成速度系数，菜单类型见 {@link ModMenus#WORKBENCH}。
 */
public class ModWorkbenchBlock extends CraftingTableBlock {

    private final WorkbenchMaterial material;

    public ModWorkbenchBlock(WorkbenchMaterial material, Properties settings) {
        super(settings);
        this.material = material;
    }

    public WorkbenchMaterial getMaterial() {
        return material;
    }

    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level world, BlockPos pos) {
        WorkbenchMaterial benchMaterial = this.material;
        ContainerLevelAccess access = ContainerLevelAccess.create(world, pos);
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return getName();
            }

            @Override
            public AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player) {
                return new ModWorkbenchMenu(syncId, inventory, access, benchMaterial);
            }
        };
    }
}
