package com.mite.recraft.client.chest;

import com.mite.recraft.chest.ChestPanelLayout;
import com.mite.recraft.chest.ChestPanelLayout.Style;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

/**
 * 容器面板底图绘制：标题带 + 每行条带 + 玩家背包切片，纵向上无缝相接。
 *
 * <p>箱子与保险箱共用同一份几何 {@link ChestPanelLayout}，避免两处各画一套导致错位或贴图缺失。</p>
 */
@Environment(EnvType.CLIENT)
public final class ChestPanelPainter {

    private ChestPanelPainter() {
    }

    public static void drawPanel(GuiGraphicsExtractor graphics, ChestPanelLayout layout, int x, int y) {
        int width = layout.width();
        boolean wide = layout.style() == Style.WIDE;

        if (wide) {
            blitStrip(graphics, layout.titleTexture(), x, y, width, ChestPanelLayout.TITLE_HEIGHT);
        } else {
            blitVanilla(graphics, x, y, width, ChestPanelLayout.TITLE_HEIGHT, 0.0F, 0.0F);
        }

        for (int row = 0; row < layout.rows(); row++) {
            int rowY = y + ChestPanelLayout.TITLE_HEIGHT + row * ChestPanelLayout.ROW_HEIGHT;
            if (wide) {
                blitStrip(graphics, layout.rowTexture(), x, rowY, width, ChestPanelLayout.ROW_HEIGHT);
            } else {
                float v = ChestPanelLayout.TITLE_HEIGHT
                        + Math.min(row, ChestPanelLayout.VANILLA_ROWS - 1) * ChestPanelLayout.ROW_HEIGHT;
                blitVanilla(graphics, x, rowY, width, ChestPanelLayout.ROW_HEIGHT, 0.0F, v);
            }
        }

        if (layout.footerFrameHeight() > 0) {
            blitStrip(graphics, layout.playerStripTexture(), x, y + layout.footerY(),
                    width, layout.footerFrameHeight());
        }

        blitVanilla(graphics, x + layout.playerInventoryX() - ChestPanelLayout.SLOT_ORIGIN_X,
                y + layout.playerInventoryTop(), ChestPanelLayout.VANILLA_WIDTH,
                ChestPanelLayout.FOOTER_HEIGHT, 0.0F, ChestPanelLayout.FOOTER_V);
    }

    private static void blitVanilla(GuiGraphicsExtractor graphics, int x, int y,
                                    int width, int height, float u, float v) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, ChestPanelLayout.VANILLA_BACKGROUND, x, y, u, v,
                width, height, ChestPanelLayout.VANILLA_TEXTURE_SIZE, ChestPanelLayout.VANILLA_TEXTURE_SIZE);
    }

    /** 条带贴图按自身尺寸采样，一张贴图即一条面板切片。 */
    private static void blitStrip(GuiGraphicsExtractor graphics, Identifier texture,
                                  int x, int y, int width, int height) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 0.0F, 0.0F,
                width, height, width, height);
    }
}
