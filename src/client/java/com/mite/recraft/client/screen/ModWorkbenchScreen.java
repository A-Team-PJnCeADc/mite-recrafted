package com.mite.recraft.client.screen;

import com.mite.recraft.block.workbench.ModWorkbenchMenu;
import com.mite.recraft.client.MiteRecraftedClient;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CraftingMenu;

public class ModWorkbenchScreen extends CraftingScreen {

    // 原版工作台 GUI 纹理
    private static final Identifier CRAFTING_TABLE_GUI = Identifier.withDefaultNamespace("textures/gui/container/crafting_table.png");

    public ModWorkbenchScreen(CraftingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {

        // 先渲染原版全部内容（背景、槽位、物品、手持物品、tooltip 等）
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        // 仅当菜单是 ModWorkbenchMenu 时才绘制进度
        if (!(getMenu() instanceof ModWorkbenchMenu)) return;

        // 从客户端同步数据中读取进度信息
        int period = MiteRecraftedClient.syncedCraftingPeriod;
        int ticks = MiteRecraftedClient.syncedCraftingTicks;
        if (period <= 0 || ticks <= 0) return;

        int pct = Math.min(100, ticks * 100 / period);
        int arrowLeft = this.leftPos + 79;
        int arrowTop = this.topPos + 35;
        int arrowWidth = 24;
        int arrowHeight = 17;

        // 绘制箭头纹理的一部分（从左到右裁剪）
        // 箭头在 crafting_table.png 中与 GUI 坐标一致：UV (79, 35)，宽24高17
        // 依据: CraftingMenu 结果槽(124,35)，网格(30,17)+18×2→右边界84，
        //       箭头位于两者之间 ≈ (79,35)~(103,52)
//        int filledWidth = arrowWidth * pct / 100;
//        if (filledWidth > 0) {
//            graphics.blit(
//                    RenderPipelines.GUI_TEXTURED,       // pipeline
//                    CRAFTING_TABLE_GUI,                 // 纹理 ID
//                    arrowLeft, arrowTop,                // 屏幕 x, y
//                    79.0F, 35.0F,                    // 纹理 UV 起始 = GUI 坐标
//                    filledWidth, arrowHeight,           // 屏幕绘制宽高
//                    256, 256                            // 纹理总尺寸
//            );
//        }

        // 进度文字（覆盖在箭头下方）
        String text = /*"合成进度 " +*/pct + "%";
        int color = (pct >= 100) ? 0xFF55FF55 : 0xFFFFFFFF;
        int textWidth = Minecraft.getInstance().font.width(text);
        int x = this.leftPos + 79 + (24 - textWidth) / 2;
        int y = this.topPos + 35 + (17 - 8) / 2 ;
        graphics.text(Minecraft.getInstance().font, text, x, y, color);
    }
}