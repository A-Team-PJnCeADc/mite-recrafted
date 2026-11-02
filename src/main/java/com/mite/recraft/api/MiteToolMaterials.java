package com.mite.recraft.api;

import com.mite.recraft.content.item.quality.Quality;
import com.mite.recraft.content.item.tool.ModToolMaterial;

import java.util.Arrays;
import java.util.List;

/**
 * 工具材料查询。材料本身是枚举，此处只做只读访问与反查。
 */
public final class MiteToolMaterials {

    private MiteToolMaterials() {
    }

    public static List<ModToolMaterial> all() {
        return Arrays.asList(ModToolMaterial.ALL);
    }

    public static List<ModToolMaterial> metals() {
        return Arrays.asList(ModToolMaterial.METALS);
    }

    public static List<ModToolMaterial> nonMetals() {
        return Arrays.asList(ModToolMaterial.NON_METALS);
    }

    /** 名称反查，未知名称回退 IRON。 */
    public static ModToolMaterial byName(String name) {
        return ModToolMaterial.byName(name);
    }

    public static ModToolMaterial byDurabilityCoefficient(float coefficient) {
        return ModToolMaterial.fromCoefficient(coefficient);
    }

    /** 该材料可制作出的最高品质。 */
    public static Quality maxQuality(ModToolMaterial material) {
        return material.getMaxQuality();
    }
}
