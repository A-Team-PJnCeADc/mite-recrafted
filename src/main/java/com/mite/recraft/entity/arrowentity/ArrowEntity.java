package com.mite.recraft.entity.arrowentity;

import com.mite.recraft.component.ModDataComponents;
import com.mite.recraft.entity.ModEntitys;
import com.mite.recraft.item.tools.toolItem.ArrowItems;
import com.mite.recraft.util.StuckArrowTracker;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.EntityHitResult;

public class ArrowEntity extends Arrow {

    private static final EntityDataAccessor<String> DATA_MATERIAL =
            SynchedEntityData.defineId(ArrowEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> DATA_RECOVERY_CHANCE =
            SynchedEntityData.defineId(ArrowEntity.class, EntityDataSerializers.FLOAT);

    public ArrowEntity(EntityType<? extends ArrowEntity> type, Level level) {
        super(type, level);
    }

    public ArrowEntity(Level level, LivingEntity owner, ItemStack pickupItem, ItemStack weapon) {
        super(ModEntitys.MITE_ARROW, level);
        this.setOwner(owner);
        this.setPos(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
        // 创造性模式：ProjectileWeaponItem.useAmmo 在箭矢物品上设置 INTANGIBLE_PROJECTILE
        // 创造性箭矢允许捡起实体但不能获得物品（CREATIVE_ONLY 模式下 tryPickup 不调 add）
        if (pickupItem.has(DataComponents.INTANGIBLE_PROJECTILE)) {
            this.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        } else {
            this.pickup = AbstractArrow.Pickup.ALLOWED;
        }
        this.setPickupItemStack(pickupItem);
        initFromItem(pickupItem);
    }

    private void initFromItem(ItemStack stack) {
        this.setBaseDamage(stack.getOrDefault(ModDataComponents.BASE_DAMAGE, 1.0f));
        this.entityData.set(DATA_RECOVERY_CHANCE,
                stack.getOrDefault(ModDataComponents.RECOVERY_CHANCE, 0.0f));
        this.entityData.set(DATA_MATERIAL, extractMaterial(stack));
    }

    private static String extractMaterial(ItemStack stack) {
        var key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (key == null) return "flint";
        String path = key.getPath();
        int idx = path.lastIndexOf("_arrow");
        return idx > 0 ? path.substring(0, idx) : "flint";
    }

    /**
     * 覆盖 onHitEntity：原版箭无穿透时直接 discard()，箭不掉落。
     * 改为将箭物品寄存到目标生物，生物死亡时 dropAllDeathLoot 统一掉落。
     * 创造性模式箭矢（pickup == CREATIVE_ONLY）不寄存 —— 不应产生物品。
     */
    @Override
    protected void onHitEntity(EntityHitResult result) {
        ItemStack pickup = this.getPickupItem();
        super.onHitEntity(result);
        if (this.pickup != AbstractArrow.Pickup.CREATIVE_ONLY
                && result.getEntity() instanceof LivingEntity target) {
            StuckArrowTracker.add(target, pickup);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_MATERIAL, "flint");
        builder.define(DATA_RECOVERY_CHANCE, 0.5f);
    }

    public String getMaterial() { return this.entityData.get(DATA_MATERIAL); }
    public float getRecoveryChance() { return this.entityData.get(DATA_RECOVERY_CHANCE); }

    @Override
    public void addAdditionalSaveData(ValueOutput out) {
        super.addAdditionalSaveData(out);
        out.putFloat("MiteRecoveryChance", this.entityData.get(DATA_RECOVERY_CHANCE));
        out.putString("MiteMaterial", this.entityData.get(DATA_MATERIAL));
    }

    @Override
    public void readAdditionalSaveData(ValueInput in) {
        super.readAdditionalSaveData(in);
        this.entityData.set(DATA_RECOVERY_CHANCE, in.getFloatOr("MiteRecoveryChance", 0.5f));
        this.entityData.set(DATA_MATERIAL, in.getStringOr("MiteMaterial", "flint"));
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ArrowItems.FLINT_ARROW);
    }
}
