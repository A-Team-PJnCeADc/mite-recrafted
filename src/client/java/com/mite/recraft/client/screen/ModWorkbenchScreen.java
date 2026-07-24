package com.mite.recraft.client.screen;

import com.mite.recraft.block.workbench.ModWorkbenchMenu;
import com.mite.recraft.client.MiteRecraftedClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CraftingMenu;

public class ModWorkbenchScreen extends CraftingScreen {

    private static final Identifier ARROW_SPRITE = Identifier.withDefaultNamespace("container/furnace/burn_progress");
    private static final String XP_COST_KEY = "gui.mite-recraft.workbench.xp_cost";

    public ModWorkbenchScreen(CraftingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        if (!(getMenu() instanceof ModWorkbenchMenu)) return;

        drawCraftingProgress(graphics);
        drawXpCost(graphics);
    }

    /** 合成进度箭头 + 百分比 */
    private void drawCraftingProgress(GuiGraphicsExtractor graphics) {
        int period = MiteRecraftedClient.syncedCraftingPeriod;
        int ticks = MiteRecraftedClient.syncedCraftingTicks;
        if (period <= 0 || ticks <= 0) return;

        int pct = Math.min(100, ticks * 100 / period);
        int arrowLeft = this.leftPos + 88;
        int arrowTop = this.topPos + 35;
        int arrowWidth = 24;
        int arrowHeight = 17;
        int filledWidth = arrowWidth * pct / 100;

        if (filledWidth > 0) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARROW_SPRITE,
                    arrowWidth, arrowHeight, 0, 0,
                    arrowLeft, arrowTop, filledWidth, arrowHeight);
        }

        String text = pct + "%";
        int color = (pct >= 100) ? 0xFF55FF55 : 0xFFFFFFFF;
        int textWidth = Minecraft.getInstance().font.width(text);
        int x = this.leftPos + 88 + (24 - textWidth) / 2;
        int y = this.topPos + 35 + (17 - 8) / 2 - 13;
        graphics.text(Minecraft.getInstance().font, text, x, y, color);
    }

    /** 产物槽下方显示「合成花费：?级」，绿色可合成 / 红色不足 */
    private void drawXpCost(GuiGraphicsExtractor graphics) {
        int xpCost = MiteRecraftedClient.syncedXpCost;
        if (xpCost <= 0) return;

        var player = Minecraft.getInstance().player;
        if (player == null) return;

        boolean affordable = player.totalExperience >= xpCost;
        int color = affordable ? 0xFF55FF55 : 0xFFFF5555;

        // 服务端发来需要花费的 XP 总量，直接用总量公式逆算等级
        int levelCost = getLevelFromTotalXp(xpCost);

        Component xpLine = Component.translatable(XP_COST_KEY, levelCost);
        graphics.text(Minecraft.getInstance().font, xpLine.getString(),
                this.leftPos + 106,
                this.topPos + 57,
                color);
    }

    /** 从经验总量反推等级 */
    private static int getLevelFromTotalXp(int totalXp) {
        if (totalXp <= 352) { // 0-16级
            return (int) ((-6 + Math.sqrt(36 + 4 * totalXp)) / 2);
        }
        if (totalXp <= 1507) { // 17-31级
            return (int) ((40.5 + Math.sqrt(10 * totalXp - 1959.75)) / 5);
        }
        // 32级以上
        return (int) ((162.5 + Math.sqrt(18 * totalXp - 13553.75)) / 9);
    }
}
