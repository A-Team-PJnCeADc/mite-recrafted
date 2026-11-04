package com.mite.recraft.content.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

/**
 * 砖块投掷物。MITE 1.6.4 EntityBrick：命中生物造成 2 点伤害，命中玻璃板会直接打碎。
 */
public class BrickEntity extends ThrowableProjectile implements net.minecraft.world.entity.projectile.ItemSupplier {

    public BrickEntity(EntityType<? extends BrickEntity> type, Level level) {
        super(type, level);
    }

    public BrickEntity(EntityType<? extends BrickEntity> type, double x, double y, double z, Level level) {
        super(type, x, y, z, level);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!level().isClientSide() && result.getEntity() instanceof LivingEntity target) {
            target.hurt(damageSources().thrown(this, getOwner()), 2.0F);
            discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (level().isClientSide()) {
            return;
        }
        var pos = result.getBlockPos();
        if (level().getBlockState(pos).is(Blocks.GLASS_PANE) || level().getBlockState(pos).is(Blocks.GLASS)) {
            level().destroyBlock(pos, false);
        }
        discard();
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
    }

    @Override
    protected double getDefaultGravity() {
        return 0.03D;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(com.mite.recraft.content.item.throwable.ModThrowables.BRICK);
    }
}
