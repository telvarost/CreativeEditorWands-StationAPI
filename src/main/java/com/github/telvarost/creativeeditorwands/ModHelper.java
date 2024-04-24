package com.github.telvarost.creativeeditorwands;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerPlayerConnectionManager;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;

public class ModHelper {
    public static final int WOODEN_TOOL_DURABILITY = 59;
    public static final int SELECTION_TOOL_DURABILITY = 64;
    public static int BRUSH_SIZE_DURABILITY = 25;
    public static final int BLOCK_ID_DURABILITY = 96;

    public static MinecraftServer getServer() {
        return (MinecraftServer) FabricLoader.getInstance().getGameInstance();
    }

    public static ServerPlayerConnectionManager getConnectionManager() {
        return getServer().serverPlayerConnectionManager;
    }

    public static boolean IsPlayerCreative(PlayerBase player) {
        if (null != player) {
            if (null != PlayerHelper.getPlayerFromGame()) {
                return true;
            } else if (  (null != player.level)
                      && (!player.level.isServerSide)
            ) {
                return ModHelper.getConnectionManager().isOp(player.name);
            } else {
                return  false;
            }
//        return (  (ModHelperFields.bhcreativeLoaded)
//               && (BHCreative.get(player))
//               );
        } else {
            return false;
        }
    }

    public static void AttemptToSetEditingToolProperties()
    {
        if (ModHelper.ModHelperFields.blocksAndItemsRegistered) {
            ModHelper.ModHelperFields.enableWorldEditTools = (  (ModHelper.ModHelperFields.enableWorldEditTools)
                                                             && (false == Config.config.disableAllEditingTools)
                                                             );

            if (ModHelper.ModHelperFields.enableWorldEditTools) {

                if (SELECTION_TOOL_DURABILITY != ItemBase.woodAxe.getDurability()) {
                    ItemBase.woodAxe.setDurability(SELECTION_TOOL_DURABILITY);
                }

                if (BRUSH_SIZE_DURABILITY != ItemBase.woodHoe.getDurability()) {
                    ItemBase.woodHoe.setDurability(BRUSH_SIZE_DURABILITY);
                }

                if (BLOCK_ID_DURABILITY != ItemBase.woodPickaxe.getDurability()) {
                    ItemBase.woodPickaxe.setDurability(BLOCK_ID_DURABILITY);
                }

                if (BLOCK_ID_DURABILITY != ItemBase.woodShovel.getDurability()) {
                    ItemBase.woodShovel.setDurability(BLOCK_ID_DURABILITY);
                }

                if (BLOCK_ID_DURABILITY != ItemBase.woodSword.getDurability()) {
                    ItemBase.woodSword.setDurability(BLOCK_ID_DURABILITY);
                }
            } else {

                if (WOODEN_TOOL_DURABILITY != ItemBase.woodAxe.getDurability()) {
                    ItemBase.woodAxe.setDurability(WOODEN_TOOL_DURABILITY);
                }

                if (WOODEN_TOOL_DURABILITY != ItemBase.woodHoe.getDurability()) {
                    ItemBase.woodHoe.setDurability(WOODEN_TOOL_DURABILITY);
                }

                if (WOODEN_TOOL_DURABILITY != ItemBase.woodPickaxe.getDurability()) {
                    ItemBase.woodPickaxe.setDurability(WOODEN_TOOL_DURABILITY);
                }

                if (WOODEN_TOOL_DURABILITY != ItemBase.woodShovel.getDurability()) {
                    ItemBase.woodShovel.setDurability(WOODEN_TOOL_DURABILITY);
                }

                if (WOODEN_TOOL_DURABILITY != ItemBase.woodSword.getDurability()) {
                    ItemBase.woodSword.setDurability(WOODEN_TOOL_DURABILITY);
                }
            }
        }
    }

    public static void SetEnableWorldEditTools(boolean setEnable) {
        if (Config.config.enableTogglingEditingToolsWithBedrock) {
            ModHelperFields.enableWorldEditTools = setEnable;
            AttemptToSetEditingToolProperties();
        }
    }

    public static class ModHelperFields {
        public static Boolean bhcreativeLoaded = false;
        public static Boolean blocksAndItemsRegistered = false;
        public static Boolean enableWorldEditTools = false;
        public static Integer brushSize = 0;
        public static Integer brushType = 1;
        public static Integer serverBlockId = 0;
        public static Integer serverBlockMeta = 0;

        /** - Copy/Paste variables */
        public static Integer copyPoint1_X = Integer.MAX_VALUE;
        public static Integer copyPoint1_Y = Integer.MAX_VALUE;
        public static Integer copyPoint1_Z = Integer.MAX_VALUE;
        public static Integer copyPoint2_X = Integer.MAX_VALUE;
        public static Integer copyPoint2_Y = Integer.MAX_VALUE;
        public static Integer copyPoint2_Z = Integer.MAX_VALUE;
    }
}
