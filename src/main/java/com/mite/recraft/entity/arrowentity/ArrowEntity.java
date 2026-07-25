package com.mite.recraft.entity.arrowentity;

import com.mite.recraft.component.ModDataComponents;
import com.mite.recraft.enchantment.ModEnchantments;
import com.mite.recraft.entity.ModEntitys;
import com.mite.recraft.item.tools.toolItem.ArrowItems;
import com.mite.recraft.util.StuckArrowTracker;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.EntityHitResult;

public class ArrowEntity extends Arrow {

    private static final EntityDataAccessor<String> DATA_MATERIAL =
            SynchedEntityData.defineId(ArrowEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> DATA_RECOVERY_CHANCE =
            SynchedEntityData.defineId(ArrowEntity.class, EntityDataSerializers.FLOAT);

    private ItemStack weaponStack = ItemStack.EMPTY;
    private boolean preventRecovery = false;

    public ArrowEntity(EntityType<? extends ArrowEntity> type, Level level) {
        super(type, level);
    }

    public ArrowEntity(Level level, LivingEntity owner, ItemStack pickupItem, ItemStack weapon) {
        super(ModEntitys.MITE_ARROW, level);
        this.weaponStack = weapon == null ? ItemStack.EMPTY : weapon;
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

        // 检查武器上的箭矢回收附魔，提高回收率
        if (weapon != null && !weapon.isEmpty() && this.level() instanceof ServerLevel serverLevel) {
            int enchLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    serverLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                            .getOrThrow(ModEnchantments.ARROW_RECOVERY.key()),
                    weapon
            );
            if (enchLevel > 0) {
                float base = pickupItem.getOrDefault(ModDataComponents.RECOVERY_CHANCE, 0.0f);
                float boosted = com.mite.recraft.enchantment.ArrowRecovery.calculateChance(base, enchLevel);
                this.entityData.set(DATA_RECOVERY_CHANCE, boosted);
            }
        }
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
        Entity hit = result.getEntity();
        // 史莱姆或幼年动物击中后箭矢不回收
        this.preventRecovery = hit instanceof Slime
                || (hit instanceof LivingEntity lt && lt.isBaby());
        if (this.pickup != AbstractArrow.Pickup.CREATIVE_ONLY
                && hit instanceof LivingEntity target) {
            StuckArrowTracker.add(target, pickup);
        }
    }

    @Override
    public ItemStack getWeaponItem() {
        return weaponStack;
    }

    @Override
    public ItemEntity spawnAtLocation(ServerLevel level, ItemStack stack, float yOffset) {
        // 无限箭矢不产生掉落物
        if (stack.has(DataComponents.INTANGIBLE_PROJECTILE)) return null;

        Float chance = stack.get(ModDataComponents.RECOVERY_CHANCE);
        if (chance != null) {
            if (preventRecovery) return null;
            if (this.random.nextFloat() >= chance) return null;
        }
        return super.spawnAtLocation(level, stack, yOffset);
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
