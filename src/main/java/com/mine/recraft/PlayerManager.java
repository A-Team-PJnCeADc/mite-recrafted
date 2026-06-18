package com.mine.recraft;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.GameRules;

public class PlayerManager {
    private static final int BASE_MAX_HEALTH = 6;      // 初始生命值
    private static final int ABSOLUTE_MAX_HEALTH = 20; // 最终上限
    private static final int BASE_MAX_HUNGER=6;        // 初始饱食度
    private static final int ABSOLUTE_MAX_HUNGER=20;   //最终上限

    public static void register(){
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->{
        applyMaxHealth(handler.getPlayer());
        disableNaturalRegeneration(handler.player);
        });
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) ->{
        applyMaxHealth(newPlayer);
        });

    }

    public static void applyMaxHealth(ServerPlayer player) {
        if (player.isCreative()) return;

        int level = player.experienceLevel;
        int stars = level / 5;
        double newMax = BASE_MAX_HEALTH + stars * 2.0;
        if (newMax > ABSOLUTE_MAX_HEALTH){
            newMax = ABSOLUTE_MAX_HEALTH;
        }

        // 修改生命值上限
        AttributeInstance attr = player.getAttribute(Attributes.MAX_HEALTH);
        if (attr != null && attr.getBaseValue() != newMax) {
            attr.setBaseValue(newMax);
            // 如果当前生命值超过上限，则将当前生命值设置为上限
            if (player.getHealth() > player.getMaxHealth()){
                player.setHealth(player.getMaxHealth());
            }
        }
    }

    private static void disableNaturalRegeneration(ServerPlayer  player){
        player.level().getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION).set(false, player.level().getServer());
    }
}
