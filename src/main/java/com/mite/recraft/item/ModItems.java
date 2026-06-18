package com.mite.recraft.item;

import com.mite.recraft.item.material.ModMaterials;
import com.mite.recraft.item.tools.ModTools;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModItems {
    private static final List<Item> MATERIALS = new ArrayList<>();
    private static final List<Item> TOOLS = new ArrayList<>();

    public static void addMaterial(Item item) {
        MATERIALS.add(item);
    }

    public static void addTool(Item item) {
        TOOLS.add(item);
    }

    public static List<Item> getMaterials() {
        return Collections.unmodifiableList(MATERIALS);
    }

    public static List<Item> getTools() {
        return Collections.unmodifiableList(TOOLS);
    }

    public static void init() {
        ModMaterials.init();
        ModTools.init();
    }
}
