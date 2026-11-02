package com.mite.recraft.chest;

import com.mite.recraft.api.MiteIds;

import net.minecraft.resources.Identifier;

import java.util.Set;

/**
 * 箱子界面几何：由列数与行数推导面板尺寸、槽位坐标与底图切片。
 *
 * <p>全部尺寸取自原版容器底图 {@code generic_54.png}（176x222）实测：</p>
 * <ul>
 *   <li>标题带 17 像素，正文每行 18 像素，玩家背包区 97 像素（1 像素分隔行 + 96 像素背包面板）。</li>
 *   <li>格区列起点 x = 8 + 列 × 18，行起点 y = 18 + 行 × 18；行条带贴图比槽位高 1 像素绘制在 y = 17 + 行 × 18。</li>
 *   <li>左边框 7 像素（黑 / 2 白 / 4 底色），每格 18 像素（1 阴影边 / 16 槽底 / 1 白边）；</li>
 *   <li>右边框 6 像素（3 底色 / 2 阴影 / 1 黑），9 列取原版底图时是 7 像素（4 底色），宽面板沿用 6 像素。</li>
 *   <li>玩家背包首行 y = 格区末行 + 14，快捷栏再下移 58；背包 9 列在面板内居中。</li>
 *   <li>列数超过 9 时格区比背包面板宽，格区下方补一条整宽收边条（6 像素面板底 + 1 条黑边），
 *       背包面板接在收边条下面，因此面板高与背包槽位比 9 列多 7 像素。</li>
 * </ul>
 *
 * <p>9 列直接取原版底图，行数超过原版 6 行时重复第 6 行条带；列数超过 9 时改用
 * {@code textures/gui/container/wide/} 下的条带贴图，这些条带是由原版底图按上述边框与槽格
 * 几何横向延长生成的，像素与原版同色。</p>
 */
public final class ChestPanelLayout {

    public static final int SLOT_STEP = 18;
    public static final int SLOT_ORIGIN_X = 8;
    public static final int SLOT_ORIGIN_Y = 18;

    /** 标题带高度，也是行条带相对槽位行的上移量。 */
    public static final int TITLE_HEIGHT = 17;
    public static final int ROW_HEIGHT = 18;

    /** 容器格区末行到玩家背包首行的间距。 */
    public static final int INVENTORY_GAP = 14;
    public static final int HOTBAR_OFFSET = 58;
    public static final int PLAYER_COLUMNS = 9;
    public static final int PLAYER_ROWS = 3;

    /** 宽面板在格区下方补的收边条高度：面板底 + 整宽黑边。 */
    public static final int FOOTER_FRAME_HEIGHT = 7;
    /** 背包切片顶边到首行槽位的距离：1 像素分隔行 + 14 像素留白。 */
    private static final int FOOTER_SLOT_OFFSET = INVENTORY_GAP + 1;

    /** 原版底图 {@code generic_54.png} 的面板宽度与非槽区部分的高度。 */
    public static final int VANILLA_WIDTH = 176;
    public static final int VANILLA_COLUMNS = 9;
    public static final int VANILLA_ROWS = 6;
    public static final int VANILLA_TEXTURE_SIZE = 256;
    /** 玩家背包切片起点：1 像素分隔行 + 96 像素面板。 */
    public static final float FOOTER_V = 125.0F;
    public static final int FOOTER_HEIGHT = 97;

    /** 宽面板边框：左边 7 像素、右边 6 像素。 */
    private static final int LEFT_EDGE = 7;
    private static final int RIGHT_EDGE = 6;

    /** 已生成条带贴图的列数。 */
    private static final Set<Integer> WIDE_COLUMNS = Set.of(12, 14);

    public static final Identifier VANILLA_BACKGROUND =
            Identifier.withDefaultNamespace("textures/gui/container/generic_54.png");

    /** 面板底图取用方式。 */
    public enum Style {
        /** 9 列、行数不超过原版 6 行：整块原版底图。 */
        VANILLA,
        /** 9 列、行数超过 6 行：原版底图重复行条带。 */
        TALL,
        /** 列数超过 9：宽条带贴图。 */
        WIDE
    }

    private final int columns;
    private final int rows;
    private final Style style;

    public ChestPanelLayout(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        this.style = resolveStyle(columns, rows);
        if (this.style == Style.WIDE && !WIDE_COLUMNS.contains(columns)) {
            throw new IllegalArgumentException("缺少 " + columns + " 列的界面条带贴图");
        }
    }

    private static Style resolveStyle(int columns, int rows) {
        if (columns > VANILLA_COLUMNS) {
            return Style.WIDE;
        }
        return rows > VANILLA_ROWS ? Style.TALL : Style.VANILLA;
    }

    public Style style() {
        return this.style;
    }

    public int columns() {
        return this.columns;
    }

    public int rows() {
        return this.rows;
    }

    /** 面板宽度：9 列取原版 176，更宽的列数按边框与槽格宽度推导。 */
    public int width() {
        if (this.columns() <= VANILLA_COLUMNS) {
            return VANILLA_WIDTH;
        }
        return LEFT_EDGE + this.columns() * SLOT_STEP + RIGHT_EDGE;
    }

    /** 面板高度：标题带 + 格区 + 宽面板收边条 + 玩家背包区。 */
    public int height() {
        return this.playerInventoryTop() + FOOTER_HEIGHT;
    }

    public int chestSlotX(int column) {
        return SLOT_ORIGIN_X + column * SLOT_STEP;
    }

    public int chestSlotY(int row) {
        return SLOT_ORIGIN_Y + row * SLOT_STEP;
    }

    /** 宽面板在格区下方补的收边条高度；9 列与原版一样直接接背包切片。 */
    public int footerFrameHeight() {
        return this.style == Style.WIDE ? FOOTER_FRAME_HEIGHT : 0;
    }

    /** 玩家背包切片的起始 y。 */
    public int playerInventoryTop() {
        return this.footerY() + this.footerFrameHeight();
    }

    /** 玩家背包首行 y。 */
    public int playerInventoryY() {
        return this.playerInventoryTop() + FOOTER_SLOT_OFFSET;
    }

    public int hotbarY() {
        return this.playerInventoryY() + HOTBAR_OFFSET;
    }

    /** 玩家背包与快捷栏的横向起点：9 列在面板内居中。 */
    public int playerInventoryX() {
        return SLOT_ORIGIN_X
                + (this.columns() * SLOT_STEP - PLAYER_COLUMNS * SLOT_STEP) / 2;
    }

    /** 玩家背包切片在面板内的起始 y。 */
    public int footerY() {
        return TITLE_HEIGHT + this.rows() * ROW_HEIGHT;
    }

    /** 宽面板标题条带贴图。 */
    public Identifier titleTexture() {
        return MiteIds.id("textures/gui/container/wide/title_" + this.columns() + ".png");
    }

    /** 宽面板行条带贴图。 */
    public Identifier rowTexture() {
        return MiteIds.id("textures/gui/container/wide/row_" + this.columns() + ".png");
    }

    /** 宽面板收边条贴图：面板底 + 整宽黑边。 */
    public Identifier playerStripTexture() {
        return MiteIds.id("textures/gui/container/wide/player_" + this.columns() + ".png");
    }
}
