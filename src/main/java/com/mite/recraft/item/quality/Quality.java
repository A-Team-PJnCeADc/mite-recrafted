package com.mite.recraft.item.quality;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

/**
 * 物品品质枚举类
 * 定义了从低到高的8个品质等级，每个品质等级包含对应的ID、耐久度修正系数和名称标识。
 * 品质等级影响物品的属性和表现。
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

    /**
     * 构造品质枚举实例
     *
     * @param level 品质等级ID，数值越大品质越高
     * @param durabilityModifier 耐久度修正系数，用于计算物品的实际耐久度
     * @param name 品质的内部名称标识，用于本地化和序列化
     */
    Quality(int level, float durabilityModifier, String name) {
        this.level = level;
        this.durabilityModifier = durabilityModifier;
        this.name = name;
    }

    /**
     * 获取品质等级ID
     *
     * @return 品质等级ID
     */
    public int getLevel() {
        return level;
    }

    /**
     * 获取耐久度修正系数
     *
     * @return 耐久度修正系数，基础值为1.0f，小于1.0f降低耐久，大于1.0f提升耐久
     */
    public float getDurabilityModifier() {
        return durabilityModifier;
    }

    /**
     * 获取品质的字符串表示形式
     *
     * @return 品质的内部名称标识
     */
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

    /**
     * 获取下一个品质等级（循环）
     * 当前品质为 LEGENDARY 时，下一个品质将循环回到 WRETCHED
     *
     * @return 下一个品质等级
     */
    public Quality next() {
        if (this == LEGENDARY) return WRETCHED;
        return values()[ordinal() + 1];
    }

    /**
     * 获取上一个品质等级（循环）
     * 当前品质为 WRETCHED 时，上一个品质将循环回到 LEGENDARY
     *
     * @return 上一个品质等级
     */
    public Quality previous() {
        if (this == WRETCHED) return LEGENDARY;
        return values()[ordinal() - 1];
    }

    /**
     * 获取本地化翻译键
     * 用于在语言文件中查找对应的翻译文本
     *
     * @return 本地化翻译键，格式为 "quality.mite-recraft.{name}"
     */
    public String getTranslationKey() {
        return "quality.mite_recraft." + name;
    }
}
