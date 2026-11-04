package com.mite.recraft.content.entity.projectile;

import com.mite.recraft.content.combat.MiteDissolve;

import com.mite.recraft.content.item.throwable.ModThrowables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * 凝胶球投掷物。MITE 1.6.4 EntityGelatinousSphere：命中生物造成 1 点伤害加上球体自身伤害，
 * 命中方块时按 pepsin/acid 侵蚀接触面。
 */
public class GelatinousSphereEntity extends ThrowableProjectile implements net.minecraft.world.entity.projectile.ItemSupplier {

    /** 0 粘液（pepsin）、1 软泥（acid）。对应 MITE 的 subtype 分组。 */
    private int subtype;

    public GelatinousSphereEntity(EntityType<? extends GelatinousSphereEntity> type, Level level) {
        super(type, level);
    }

    public GelatinousSphereEntity(EntityType<? extends GelatinousSphereEntity> type, double x, double y, double z, Level level) {
        super(type, x, y, z, level);
    }

    public void setSubtype(int subtype) {
        this.subtype = subtype;
    }

    public int getSubtype() {
        return subtype;
    }

    private MiteDissolve.Type dissolveType() {
        return subtype == 1 ? MiteDissolve.Type.ACID : MiteDissolve.Type.PEPSIN;
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (level().isClientSide()) {
            return;
        }

        if (result instanceof EntityHitResult entityHit) {
            Entity target = entityHit.getEntity();
            if (target instanceof LivingEntity) {
                target.hurt(damageSources().thrown(this, getOwner()), 1.0F + subtype * 1.0F);
            }
        } else if (result instanceof BlockHitResult blockHit) {
            BlockPos pos = blockHit.getBlockPos();
            BlockState state = level().getBlockState(pos);
            if (MiteDissolve.period(state, dissolveType()) >= 0) {
                level().destroyBlock(pos, false);
            }
        }

        level().addParticle(ParticleTypes.ITEM_SLIME, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
        discard();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected double getDefaultGravity() {
        return 0.07D;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(ModThrowables.GELATINOUS_SPHERE);
    }
}
