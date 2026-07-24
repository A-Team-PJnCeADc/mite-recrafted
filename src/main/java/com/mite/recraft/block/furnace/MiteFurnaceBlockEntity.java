package com.mite.recraft.block.furnace;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;

public class MiteFurnaceBlockEntity extends AbstractFurnaceBlockEntity {

    private final FurnaceTier tier;

    public MiteFurnaceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, FurnaceTier tier) {
        super(type, pos, state, RecipeType.SMELTING);
        this.tier = tier;
    }

    public FurnaceTier getTier() {
        return tier;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container." + this.tier.registryId);
    }

    @Override
    protected int getBurnDuration(FuelValues fuelValues, ItemStack fuel) {
        int heat = FuelHeatHelper.getHeatLevel(fuel);
        if (heat > tier.maxHeat || heat < 1) return 0;
        return super.getBurnDuration(fuelValues, fuel);
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new FurnaceMenu(containerId, inventory, this, this.dataAccess);
    }
}
