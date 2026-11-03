package com.mite.recraft.registry;

import com.mite.recraft.api.MiteIds;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import static com.mite.recraft.MiteRecrafted.MOD_ID;

/**
 * 自定义音效事件。音频文件位于 assets/{@code mite_recrafted}/sounds 下。
 */
public final class ModSounds {

    private static final String[] DISCS = {
            "music_disc.descent",
            "music_disc.legends",
            "music_disc.underworld",
            "music_disc.wanderer"
    };

    private ModSounds() {
    }

    public static void register() {
        for (String name : DISCS) {
            Registry.register(BuiltInRegistries.SOUND_EVENT,
                    MiteIds.id(name),
                    SoundEvent.createVariableRangeEvent(MiteIds.id(name)));
        }
    }
}
