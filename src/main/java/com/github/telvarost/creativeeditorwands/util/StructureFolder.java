package com.github.telvarost.creativeeditorwands.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

public class StructureFolder {
    public static String getPath() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            return String.valueOf(FabricLoader.getInstance().getGameDir()) + "/structures";
        } else {
            return getServerGameDir();
        }
    }


    private static String getServerGameDir() {
        MinecraftServer ms = (MinecraftServer)(FabricLoader.getInstance().getGameInstance());
        return String.valueOf(ms.getFile("structures"));
    }
}
