package com.mite.recraft.content.entity.mob;

import com.mite.recraft.content.combat.MiteDissolve;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** MITE 1.6.4 的胶质生物变体，攻击倍率 3（伤害 = 体型 × 3）。 */
public class OozeEntity extends GelatinousCubeEntity {

    public OozeEntity(EntityType<? extends OozeEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected int attackMultiplier() {
        return 3;
    }

    @Override
    protected MiteDissolve.Type dissolveType() {
        return MiteDissolve.Type.ACID;
    }
}
