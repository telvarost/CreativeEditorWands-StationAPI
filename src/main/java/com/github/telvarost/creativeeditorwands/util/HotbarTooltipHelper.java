package com.github.telvarost.creativeeditorwands.util;

import com.github.telvarost.creativeeditorwands.mixin.client.InGameHudAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

public class HotbarTooltipHelper {
    private static Minecraft client;

    public static void setTooltip(String message, int remaining) {
        if(client == null){
            client = (Minecraft) FabricLoader.getInstance().getGameInstance();
        }

        if (client != null && client.inGameHud != null) {
            ((InGameHudAccessor)client.inGameHud).setOverlayMessage(message);
            ((InGameHudAccessor)client.inGameHud).setOverlayRemaining(remaining);
        }
    }
}
