package com.mite.recraft.content.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

/**
 * 蛛网投掷物。MITE 1.6.4 EntityWeb：命中后把目标所在格与预测位置变成蜘蛛网；
 * 燃烧状态下先点燃命中目标。
 *
 * <p>放置顺序与 MITE 一致：先按目标朝向预测 4 格后的位置，再取目标脚下所在格，
 * 最后退化为遍历目标包围盒。</p>
 */
public class WebEntity extends ThrowableProjectile implements net.minecraft.world.entity.projectile.ItemSupplier {

    public WebEntity(EntityType<? extends WebEntity> type, Level level) {
        super(type, level);
    }

    public WebEntity(EntityType<? extends WebEntity> type, double x, double y, double z, Level level) {
        super(type, x, y, z, level);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!level().isClientSide() && result.getEntity() instanceof LivingEntity living) {
            placeWebAt(living);
            discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!level().isClientSide() && level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            placeWeb(serverLevel, result.getBlockPos().relative(result.getDirection()));
            discard();
        }
    }

    /** MITE EntityWeb.onImpact：预测格 -> 脚下格 -> 包围盒内任意格。 */
    public void placeWebAt(LivingEntity target) {
        if (!(level() instanceof net.minecraft.server.level.ServerLevel serverLevel)) {
            return;
        }
        if (isOnFire()) {
            target.setRemainingFireTicks(100);
        }

        BlockPos feet = target.blockPosition();
        BlockPos predicted = BlockPos.containing(target.getX() + target.getDeltaMovement().x * 4.0D,
                target.getY(),
                target.getZ() + target.getDeltaMovement().z * 4.0D);

        if (!predicted.equals(feet) && placeWeb(serverLevel, predicted)) {
            return;
        }
        if (placeWeb(serverLevel, feet)) {
            return;
        }
        var box = target.getBoundingBox();
        for (BlockPos pos : BlockPos.betweenClosed(
                (int) Math.floor(box.minX), (int) Math.floor(box.minY), (int) Math.floor(box.minZ),
                (int) Math.floor(box.maxX), (int) Math.floor(box.maxY), (int) Math.floor(box.maxZ))) {
            if (placeWeb(serverLevel, pos)) {
                return;
            }
        }
    }

    private boolean placeWeb(net.minecraft.server.level.ServerLevel level, BlockPos pos) {
        if (level.getBlockState(pos).canBeReplaced()) {
            level.setBlockAndUpdate(pos, Blocks.COBWEB.defaultBlockState());
            return true;
        }
        return false;
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
        return new ItemStack(net.minecraft.world.item.Items.COBWEB);
    }
}
