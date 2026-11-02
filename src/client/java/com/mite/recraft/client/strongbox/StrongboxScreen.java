package com.mite.recraft.client.strongbox;

import com.mite.recraft.chest.ChestPanelLayout;
import com.mite.recraft.client.chest.ChestPanelPainter;
import com.mite.recraft.content.block.strongbox.StrongboxScreenHandler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * 保险箱界面：与箱子共用 {@link ChestPanelLayout} 几何与 {@link ChestPanelPainter} 底图，
 * 宽面板取同一套条带贴图。
 */
@Environment(EnvType.CLIENT)
public class StrongboxScreen extends AbstractContainerScreen<StrongboxScreenHandler> {

    /** 原版容器标题与背包标签的颜色。 */
    private static final int LABEL_COLOR = 4210752;

    private final ChestPanelLayout layout;

    public StrongboxScreen(StrongboxScreenHandler menu, Inventory inventory, Component title) {
        this(menu, inventory, title,
                new ChestPanelLayout(menu.getChestColumns(), menu.getChestRows()));
    }

    private StrongboxScreen(StrongboxScreenHandler menu, Inventory inventory, Component title,
                            ChestPanelLayout layout) {
        super(menu, inventory, title, layout.width(), layout.height());
        this.layout = layout;
        if (this.layout.style() == ChestPanelLayout.Style.WIDE) {
            this.inventoryLabelX = this.layout.playerInventoryX();
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        ChestPanelPainter.drawPanel(graphics, this.layout, this.leftPos, this.topPos);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY, LABEL_COLOR, false);
        graphics.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY,
                LABEL_COLOR, false);
    }
}
