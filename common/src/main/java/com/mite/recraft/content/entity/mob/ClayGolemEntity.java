package com.mite.recraft.content.entity.mob;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/**
 * 黏土傀儡。MITE 1.6.4 EntityClayGolem：土元素的黏土形态，攻击 6、生命 30。
 *
 * <p>自然防御：硬化黏土 2，普通黏土 0；在火里会硬化。</p>
 */
public class ClayGolemEntity extends EarthElementalEntity {

    public ClayGolemEntity(EntityType<? extends ClayGolemEntity> type, Level level) {
        super(type, level);
        setElementalType(TYPE_CLAY);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return EarthElementalEntity.createAttributes()
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.MAX_HEALTH, 30.0);
    }

    private boolean hardened;

    @Override
    protected int defaultElementalType() {
        return TYPE_CLAY;
    }

    @Override
    public void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("hardened", hardened);
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput input) {
        super.readAdditionalSaveData(input);
        hardened = input.getBooleanOr("hardened", false);
    }

    /** 硬化形态不随环境降温。 */
    @Override
    protected boolean coolsDown() {
        return false;
    }

    /** 硬化形态复用熔岩状态位，渲染器与火焰免疫都读它。 */
    @Override
    public boolean isMagma() {
        return hardened;
    }

    @Override
    protected float naturalDefense() {
        return isHardened() ? 2.0F : 0.0F;
    }

    public boolean isHardened() {
        return isMagma();
    }
}
