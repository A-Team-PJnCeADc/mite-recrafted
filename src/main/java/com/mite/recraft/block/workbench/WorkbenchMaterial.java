package com.mite.recraft.block.workbench;

import com.mite.recraft.MiteRecrafted;
import com.mite.recraft.item.tools.modtoolmaterials.ModToolMaterial;
import net.minecraft.resources.Identifier;

public enum WorkbenchMaterial {
    FLINT("flint", ModToolMaterial.FLINT),
    COPPER("copper", ModToolMaterial.COPPER),
    SILVER("silver", ModToolMaterial.SILVER),
    GOLD("gold", ModToolMaterial.GOLD),
    RUSTED_IRON("rusted_iron", ModToolMaterial.RUSTED_IRON),
    IRON("iron", ModToolMaterial.IRON),
    ANCIENT_METAL("ancient_metal", ModToolMaterial.ANCIENT_METAL),
    MITHRIL("mithril", ModToolMaterial.MITHRIL),
    ADAMANTIUM("adamantium", ModToolMaterial.ADAMANTIUM),
    OBSIDIAN("obsidian", ModToolMaterial.OBSIDIAN);

    private final String name;
    private final ModToolMaterial toolMaterial; // 用于合成升级时的工具材料

    WorkbenchMaterial(String name, ModToolMaterial toolMaterial) {
        this.name = name;
        this.toolMaterial = toolMaterial;
    }

    public String getName() { return name; }
    public ModToolMaterial getToolMaterial() { return toolMaterial; }

    public Identifier getSideTexture() {
        return Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "block/workbench/" + name + "_side");
    }

    public Identifier getFrontTexture() {
        return Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "block/workbench/" + name + "_front");
    }

    public Identifier getTopTexture() {
        return Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "block/workbench/" + name + "_top");
    }

    public Identifier getBottomTexture() {
        return Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "block/workbench/" + name + "_bottom");
    }
}
