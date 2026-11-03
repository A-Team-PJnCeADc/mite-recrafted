package com.mite.recraft.client.state;

import com.mite.recraft.network.CraftingProgressSyncPayload;

/**
 * 工作台合成进度的客户端副本，由 {@link CraftingProgressSyncPayload} 覆盖写入，界面与配方列表读取。
 */
public final class ClientWorkbenchState {

    private static volatile int craftingPeriod;
    private static volatile int craftingTicks;
    private static volatile float benchCoefficient = 1.0F;
    private static volatile int qualityIndex = -1;
    private static volatile int xpCost;

    private ClientWorkbenchState() {
    }

    public static void update(CraftingProgressSyncPayload payload) {
        craftingPeriod = payload.period();
        craftingTicks = payload.ticks();
        benchCoefficient = payload.benchCoefficient();
        qualityIndex = payload.qualityIndex();
        xpCost = payload.xpCost();
    }

    public static int craftingPeriod() {
        return craftingPeriod;
    }

    public static int craftingTicks() {
        return craftingTicks;
    }

    public static float benchCoefficient() {
        return benchCoefficient;
    }

    public static int qualityIndex() {
        return qualityIndex;
    }

    public static int xpCost() {
        return xpCost;
    }
}
