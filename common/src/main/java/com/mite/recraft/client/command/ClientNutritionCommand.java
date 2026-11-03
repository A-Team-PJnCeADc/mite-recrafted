package com.mite.recraft.client.command;

import com.mite.recraft.content.item.food.NutritionSystem;

import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

/**
 * 客户端营养调试命令 /mite_recrafted:cnutrition。
 */
@Environment(EnvType.CLIENT)
public final class ClientNutritionCommand {

    private ClientNutritionCommand() {
    }

    public static void register() {
        ClientCommandRegistrationEvent.EVENT.register((dispatcher, buildContext) -> dispatcher.register(
                ClientCommandRegistrationEvent.literal("mite_recrafted:cnutrition")
                        .executes(context -> {
                            ClientCommandRegistrationEvent.ClientCommandSourceStack source = context.getSource();
                            var player = source.arch$getPlayer();
                            if (player == null) {
                                source.arch$sendFailure(
                                        Component.translatable("command.mite_recrafted.cnutrition.no_player"));
                                return 0;
                            }

                            int protein = NutritionSystem.getProtein(player);
                            int phytonutrients = NutritionSystem.getPhytonutrients(player);
                            int essentialFats = NutritionSystem.getEssentialFats(player);
                            int sugarContent = NutritionSystem.getSugarContent(player);
                            int insulinResistance = NutritionSystem.getInsulinResistance(player);

                            source.arch$sendSuccess(
                                    () -> Component.translatable("command.mite_recrafted.cnutrition.title"), false);
                            source.arch$sendSuccess(
                                    () -> Component.translatable("command.mite_recrafted.cnutrition.protein", protein),
                                    false);
                            source.arch$sendSuccess(() -> Component.translatable(
                                    "command.mite_recrafted.cnutrition.phytonutrients", phytonutrients), false);
                            source.arch$sendSuccess(
                                    () -> Component.translatable("command.mite_recrafted.cnutrition.fats", essentialFats),
                                    false);
                            source.arch$sendSuccess(
                                    () -> Component.translatable("command.mite_recrafted.cnutrition.sugar", sugarContent),
                                    false);
                            source.arch$sendSuccess(() -> Component.translatable(
                                    "command.mite_recrafted.cnutrition.insulin", insulinResistance), false);

                            return 1;
                        })
        ));
    }
}
