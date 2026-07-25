package com.mite.recraft.enchantment;

/**
 * MITE 劈裂 (Cleaving) — 战斧版穿透，每级无视 1 点护甲值。
 * <p>
 * 复用 {@link Piercing} 的护甲穿透逻辑。
 * 最终伤害 = 造成伤害 - (护甲值 - 穿透)，穿透大于护甲时完全穿透但不额外增伤。
 * <p>
 * 最高 V 级，每级 1 点护甲穿透。
 */
public final class Cleaving {

    private Cleaving() {
    }
}
