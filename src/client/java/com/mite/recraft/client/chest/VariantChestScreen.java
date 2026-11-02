package com.mite.recraft.client.chest;

import com.mite.recraft.chest.ChestMenu;
import com.mite.recraft.chest.ChestPanelLayout;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * 箱子界面：面板底图由 {@link ChestPanelPainter} 绘制，尺寸随列数与行数变化，一屏显示全部格子。
 */
@Environment(EnvType.CLIENT)
public class VariantChestScreen extends AbstractContainerScreen<ChestMenu> {

    /** 原版容器标题与背包标签的颜色。 */
    private static final int LABEL_COLOR = 4210752;

    private final ChestPanelLayout layout;

    public VariantChestScreen(ChestMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, menu.layout().width(), menu.layout().height());
        this.layout = menu.layout();
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
