package com.mite.recraft.block.workbench;

import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;

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
        return new SimpleMenuProvider(
                (syncId, inventory, player) -> new ModWorkbenchMenu(
                        syncId,
                        inventory,
                        ContainerLevelAccess.create(world, pos),
                        material
                ),
                getName()
        );
    }
}
