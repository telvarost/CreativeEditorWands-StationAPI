package com.github.telvarost.creativeeditorwands;

import com.github.telvarost.creativeeditorwands.util.HotbarTooltipHelper;
import com.github.telvarost.creativeeditorwands.util.command.StructureCommands;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;

public class ModHelper implements ModInitializer {
    public static final int WOODEN_TOOL_DURABILITY = 59;
    public static final int SELECTION_TOOL_DURABILITY = 10;
    public static int BRUSH_SIZE_DURABILITY = 25;
    public static final int BLOCK_ID_DURABILITY = 96;

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().isModLoaded("retrocommands")) {
            StructureCommands.init();
        }
    }

    public static MinecraftServer getServer() {
        return (MinecraftServer) FabricLoader.getInstance().getGameInstance();
    }

    public static PlayerManager getConnectionManager() {
        return getServer().playerManager;
    }

    public static void setTooltip(String message, int remaining) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            HotbarTooltipHelper.setTooltip(message, remaining);
        }
    }

    public static boolean IsPlayerCreative(PlayerEntity player) {
        if (null != player) {
            if (null != PlayerHelper.getPlayerFromGame()) {
                return true;
            } else if (  (null != player.world)
                      && (!player.world.isRemote)
            ) {
                return ModHelper.getConnectionManager().isOperator(player.name);
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
        if (ModHelperFields.blocksAndItemsRegistered) {
            ModHelperFields.enableWorldEditTools = (  (ModHelperFields.enableWorldEditTools)
                                                   && (false == Config.config.disableAllEditingTools)
                                                   );

            if (ModHelperFields.enableWorldEditTools) {

                if (SELECTION_TOOL_DURABILITY != Item.WOODEN_AXE.getMaxDamage()) {
                    Item.WOODEN_AXE.setMaxDamage(SELECTION_TOOL_DURABILITY);
                }

                if (BRUSH_SIZE_DURABILITY != Item.WOODEN_HOE.getMaxDamage()) {
                    Item.WOODEN_HOE.setMaxDamage(BRUSH_SIZE_DURABILITY);
                }

                if (BLOCK_ID_DURABILITY != Item.WOODEN_PICKAXE.getMaxDamage()) {
                    Item.WOODEN_PICKAXE.setMaxDamage(BLOCK_ID_DURABILITY);
                }

                if (BLOCK_ID_DURABILITY != Item.WOODEN_SHOVEL.getMaxDamage()) {
                    Item.WOODEN_SHOVEL.setMaxDamage(BLOCK_ID_DURABILITY);
                }

                if (BLOCK_ID_DURABILITY != Item.WOODEN_SWORD.getMaxDamage()) {
                    Item.WOODEN_SWORD.setMaxDamage(BLOCK_ID_DURABILITY);
                }
            } else {

                if (WOODEN_TOOL_DURABILITY != Item.WOODEN_AXE.getMaxDamage()) {
                    Item.WOODEN_AXE.setMaxDamage(WOODEN_TOOL_DURABILITY);
                }

                if (WOODEN_TOOL_DURABILITY != Item.WOODEN_HOE.getMaxDamage()) {
                    Item.WOODEN_HOE.setMaxDamage(WOODEN_TOOL_DURABILITY);
                }

                if (WOODEN_TOOL_DURABILITY != Item.WOODEN_PICKAXE.getMaxDamage()) {
                    Item.WOODEN_PICKAXE.setMaxDamage(WOODEN_TOOL_DURABILITY);
                }

                if (WOODEN_TOOL_DURABILITY != Item.WOODEN_SHOVEL.getMaxDamage()) {
                    Item.WOODEN_SHOVEL.setMaxDamage(WOODEN_TOOL_DURABILITY);
                }

                if (WOODEN_TOOL_DURABILITY != Item.WOODEN_SWORD.getMaxDamage()) {
                    Item.WOODEN_SWORD.setMaxDamage(WOODEN_TOOL_DURABILITY);
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
