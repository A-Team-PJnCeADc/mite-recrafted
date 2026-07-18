package com.mite.recraft.client.command;

import com.mite.recraft.item.moditems.food.NutritionSystem;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

/**
 * 客户端营养调试命令 — /cnutrition
 */
public class ClientNutritionCommand {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildContext) -> {
            dispatcher.register(
                    ClientCommands.literal("mite-recrafted:cnutrition")
                            .executes(context -> {
                                FabricClientCommandSource source = context.getSource();
                                var player = source.getPlayer();
                                if (player == null) {
                                    source.sendError(Component.translatable("command.mite-recrafted.cnutrition.no_player"));
                                    return 0;
                                }

                                int satiation = NutritionSystem.getSatiation(player);
                                int nutrition = NutritionSystem.getNutrition(player);
                                int protein = NutritionSystem.getProtein(player);
                                int phytonutrients = NutritionSystem.getPhytonutrients(player);
                                int essentialFats = NutritionSystem.getEssentialFats(player);
                                int sugarContent = NutritionSystem.getSugarContent(player);
                                int insulinResistance = NutritionSystem.getInsulinResistance(player);
                                int limit = NutritionSystem.getNutrientLimit(player.experienceLevel);

                                source.sendFeedback(Component.translatable("command.mite-recrafted.cnutrition.title"));
                                source.sendFeedback(Component.translatable("command.mite-recrafted.cnutrition.satiation", satiation, limit));
                                source.sendFeedback(Component.translatable("command.mite-recrafted.cnutrition.nutrition", nutrition, limit));
                                source.sendFeedback(Component.translatable("command.mite-recrafted.cnutrition.protein", protein));
                                source.sendFeedback(Component.translatable("command.mite-recrafted.cnutrition.phytonutrients", phytonutrients));
                                source.sendFeedback(Component.translatable("command.mite-recrafted.cnutrition.fats", essentialFats));
                                source.sendFeedback(Component.translatable("command.mite-recrafted.cnutrition.sugar", sugarContent));
                                source.sendFeedback(Component.translatable("command.mite-recrafted.cnutrition.insulin", insulinResistance));

                                return 1;
                            })
            );
        });
    }
}
