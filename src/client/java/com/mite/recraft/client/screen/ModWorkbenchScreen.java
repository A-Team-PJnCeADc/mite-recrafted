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

    public ModWorkbenchScreen(CraftingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        if (!(getMenu() instanceof ModWorkbenchMenu)) return;

        int period = MiteRecraftedClient.syncedCraftingPeriod;
        int ticks = MiteRecraftedClient.syncedCraftingTicks;
        if (period <= 0 || ticks <= 0) return;

        int pct = Math.min(100, ticks * 100 / period);
        int arrowLeft = this.leftPos + 88;
        int arrowTop = this.topPos + 35;
        int arrowWidth = 24;
        int arrowHeight = 17;
        int filledWidth = arrowWidth * pct / 100;

        // 原版熔炉进度箭头
        if (filledWidth > 0) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARROW_SPRITE,
                    arrowWidth, arrowHeight, 0, 0,
                    arrowLeft, arrowTop, filledWidth, arrowHeight);
        }

        // 进度文字
        String text = pct + "%";
        int color = (pct >= 100) ? 0xFF55FF55 : 0xFFFFFFFF;
        int textWidth = Minecraft.getInstance().font.width(text);
        int x = this.leftPos + 88 + (24 - textWidth) / 2;
        int y = this.topPos + 35 + (17 - 8) / 2 - 13;
        graphics.text(Minecraft.getInstance().font, text, x, y, color);
    }
}
