package com.mite.recraft.content.block.furnace;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
        return Component.translatable("container." + tier.registryId);
    }

    /**
     * 燃料热量高于熔炉上限或物品不可燃时返回 0。时长优先取 {@link MiteFuels} 登记值，
     * 未登记时回退到原版数据组件。
     */
    @Override
    protected int getBurnDuration(ServerLevel level, ItemStack fuel) {
        int burnTicks = MiteFuels.burnTicks(fuel);
        if (burnTicks == MiteFuels.VANILLA_BURN_TICKS) {
            burnTicks = super.getBurnDuration(level, fuel);
        }
        if (burnTicks <= 0) return 0;
        return MiteFuels.heat(fuel) > tier.maxHeat ? 0 : burnTicks;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new FurnaceMenu(containerId, inventory, this, this.dataAccess);
    }
}
