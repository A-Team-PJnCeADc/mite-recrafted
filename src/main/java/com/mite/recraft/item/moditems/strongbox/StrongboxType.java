package com.mite.recraft.item.moditems.strongbox;

import com.mite.recraft.MiteRecrafted;
import net.minecraft.resources.Identifier;

/**
 * Strongbox types for MITE Recrafted, modeled after IronChests' ChestTypes.
 */
//todo 配平数值
public enum StrongboxType {
    SILVER(54, 9, "silver_strongbox",
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "entity/chest/silver_strongbox")),
    ANCIENT_METAL(81, 9, "ancient_metal_strongbox",
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "entity/chest/ancient_metal_strongbox")),
    MITHRIL(108, 12, "mithril_strongbox",
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "entity/chest/mithril_strongbox")),
    ADAMANTIUM(126, 14, "adamantium_strongbox",
            Identifier.fromNamespaceAndPath(MiteRecrafted.MOD_ID, "entity/chest/adamantium_strongbox"));

    public static final StrongboxType[] VALUES = values();

    public final int size;
    public final int rowLength;
    public final String registryId;
    public final Identifier texture;

    StrongboxType(int size, int rowLength, String registryId, Identifier texture) {
        this.size = size;
        this.rowLength = rowLength;
        this.registryId = registryId;
        this.texture = texture;
    }

    public int getRowCount() {
        return this.size / this.rowLength;
    }
}
