package com.mite.recraft.item.tools.toolItem;

import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.world.item.Item;

/**
 * 镰刀（Scythe）—— 农用收割工具，numComponents=2，仅金属
 *
 * 攻击伤害：默认 +4；远古金属 +5、秘银 +6、艾德曼 +7。攻击速度统一 -3.0。
 *
 * TODO 收割高草/小麦加速（收割其他作物无速度加成）
 * TODO 银镰刀作为银质工具对亡灵怪物有伤害加成，且可攻击白色食尸鬼
 * TODO 锈铁镰刀无法合成（配方已禁用），手持锈铁镰刀的僵尸死亡后有几率掉落
 * TODO 秘银镰刀没有“吸血”附魔
 */
public class ScytheItems {
    public static final Item COPPER_SCYTHE        = ToolType.SCYTHE.create(ModToolMaterial.COPPER, 4.0F, -3.0F);
    public static final Item SILVER_SCYTHE        = ToolType.SCYTHE.create(ModToolMaterial.SILVER, 4.0F, -3.0F);
    public static final Item GOLD_SCYTHE          = ToolType.SCYTHE.create(ModToolMaterial.GOLD, 4.0F, -3.0F);
    public static final Item RUSTED_IRON_SCYTHE   = ToolType.SCYTHE.create(ModToolMaterial.RUSTED_IRON, 4.0F, -3.0F);
    public static final Item IRON_SCYTHE          = ToolType.SCYTHE.create(ModToolMaterial.IRON, 4.0F, -3.0F);
    public static final Item ANCIENT_METAL_SCYTHE = ToolType.SCYTHE.create(ModToolMaterial.ANCIENT_METAL, 5.0F, -3.0F);
    public static final Item MITHRIL_SCYTHE       = ToolType.SCYTHE.create(ModToolMaterial.MITHRIL, 6.0F, -3.0F);
    public static final Item ADAMANTIUM_SCYTHE    = ToolType.SCYTHE.create(ModToolMaterial.ADAMANTIUM, 7.0F, -3.0F);
}
