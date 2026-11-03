package com.mite.recraft.content.item.quality;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

/**
 * 物品品质，从 WRETCHED 到 LEGENDARY 共 8 级。
 * 等级决定耐久修正系数，并由材料的上限决定可制作范围。
 */
public enum Quality implements StringRepresentable {
    WRETCHED(0, 0.5f, "wretched"),
    POOR(1, 0.75f, "poor"),
    AVERAGE(2, 1.0f, "average"),
    FINE(3, 1.5f, "fine"),
    EXCELLENT(4, 2.0f, "excellent"),
    SUPERB(5, 2.5f, "superb"),
    MASTERWORK(6, 3.0f, "masterwork"),
    LEGENDARY(7, 3.5f, "legendary");

    public static final Codec<Quality> CODEC = StringRepresentable.fromEnum(Quality::values);

    private final int level;
    private final float durabilityModifier;
    private final String name;

    Quality(int level, float durabilityModifier, String name) {
        this.level = level;
        this.durabilityModifier = durabilityModifier;
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public float getDurabilityModifier() {
        return durabilityModifier;
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return name;
    }

    /**
     * 根据等级ID获取对应的品质枚举
     * 如果找不到匹配的等级，则返回默认品质 AVERAGE
     *
     * @param level 品质等级ID
     * @return 对应的品质枚举，未找到时返回 AVERAGE
     */
    public static Quality byLevel(int level) {
        for (Quality q : values()) {
            if (q.level == level) return q;
        }
        return AVERAGE;
    }

    public Quality next() {
        if (this == LEGENDARY) return WRETCHED;
        return values()[ordinal() + 1];
    }

    public Quality previous() {
        if (this == WRETCHED) return LEGENDARY;
        return values()[ordinal() - 1];
    }

    public String getTranslationKey() {
        return "quality.mite_recraft." + name;
    }
}
