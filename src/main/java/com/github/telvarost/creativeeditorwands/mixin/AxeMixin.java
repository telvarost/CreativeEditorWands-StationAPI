package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.BHCreative;
import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.Hatchet;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Hatchet.class)
public class AxeMixin extends ToolBase implements CustomTooltipProvider {
    /** - Copy/paste with wooden axe */
    @Shadow
    private static BlockBase[] effectiveBlocks;
    public AxeMixin(int i, ToolMaterial arg) {
        super(i, 1, arg, effectiveBlocks);
    }

    @Override
    public ItemInstance use(ItemInstance item, Level arg2, PlayerBase player) {
        if (  (this.id == ItemBase.woodAxe.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
           && (ModHelper.IsPlayerCreative(player))
        ) {
            item.count++;
            if (3 < item.count) {
                item.count = 1;
            }
        }
        return item;
    }

    @Override
    public String[] getTooltip(ItemInstance itemInstance, String originalTooltip) {
        if (  (this.id == ItemBase.woodAxe.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            PlayerBase player = PlayerHelper.getPlayerFromGame();
            if (  (null != player)
               && (ModHelper.IsPlayerCreative(player))
            ) {
                String selection1 = "null";
                String selection2 = "null";

                if (  (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint1_X)
                   && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint1_Y)
                   && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint1_Z)
                ) {
                    selection1 = "[" + ModHelper.ModHelperFields.copyPoint1_X
                               + "," + ModHelper.ModHelperFields.copyPoint1_Y
                               + "," + ModHelper.ModHelperFields.copyPoint1_Z
                               + "]";

                    if ((Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint2_X)
                            && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint2_Y)
                            && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint2_Z)
                    ) {
                        selection2 = "[" + ModHelper.ModHelperFields.copyPoint2_X
                                   + "," + ModHelper.ModHelperFields.copyPoint2_Y
                                   + "," + ModHelper.ModHelperFields.copyPoint2_Z
                                   + "]";
                    }
                }

                String selectionMode = "Select";

                switch (itemInstance.count) {
                    case 2:
                        selectionMode = "Copy";
                        break;

                    case 3:
                        selectionMode = "Fill";
                        break;
                }

                return new String[]{"§b" + "Selection", "Point 1: " + selection1, "Point 2: " + selection2, "Mode: " + selectionMode};
            } else {
                return new String[]{originalTooltip};
            }
        } else {
            return new String[]{originalTooltip};
        }
    }

    @Override
    public boolean useOnTile(ItemInstance item, PlayerBase player, Level level, int i, int j, int k, int meta) {
        if (  (this.id == ItemBase.woodAxe.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
           && (ModHelper.IsPlayerCreative(player))
        ) {
            int blockId = level.getTileId(i, j, k);
            int blockMeta = level.getTileMeta(i, j, k);

            if (3 == item.count) {
                /** - Fill selection */
                if (  (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint1_X)
                   && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint1_Y)
                   && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint1_Z)
                   && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint2_X)
                   && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint2_Y)
                   && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint2_Z)
                ) {
                    int add_x = 0;
                    int add_y = 0;
                    int add_z = 0;
                    boolean allowSingleIteration_X = false;
                    boolean allowSingleIteration_Y = false;
                    boolean allowSingleIteration_Z = false;

                    if (ModHelper.ModHelperFields.copyPoint1_X < ModHelper.ModHelperFields.copyPoint2_X) {
                        add_x = 1;
                    } else if (ModHelper.ModHelperFields.copyPoint1_X > ModHelper.ModHelperFields.copyPoint2_X) {
                        add_x = -1;
                    } else {
                        allowSingleIteration_X = true;
                    }

                    if (ModHelper.ModHelperFields.copyPoint1_Y < ModHelper.ModHelperFields.copyPoint2_Y) {
                        add_y = 1;
                    } else if (ModHelper.ModHelperFields.copyPoint1_Y > ModHelper.ModHelperFields.copyPoint2_Y) {
                        add_y = -1;
                    } else {
                        allowSingleIteration_Y = true;
                    }

                    if (ModHelper.ModHelperFields.copyPoint1_Z < ModHelper.ModHelperFields.copyPoint2_Z) {
                        add_z = 1;
                    } else if (ModHelper.ModHelperFields.copyPoint1_Z > ModHelper.ModHelperFields.copyPoint2_Z) {
                        add_z = -1;
                    } else {
                        allowSingleIteration_Z = true;
                    }

                    boolean stateIteration_X = allowSingleIteration_X;
                    boolean stateIteration_Y = allowSingleIteration_Y;
                    boolean stateIteration_Z = allowSingleIteration_Z;

                    for(int var6 = ModHelper.ModHelperFields.copyPoint1_X; (stateIteration_X || var6 != (ModHelper.ModHelperFields.copyPoint2_X + add_x)); var6 += add_x) {
                        for(int var7 = ModHelper.ModHelperFields.copyPoint1_Y; (stateIteration_Y || var7 != (ModHelper.ModHelperFields.copyPoint2_Y + add_y)); var7 += add_y) {
                            for(int var8 = ModHelper.ModHelperFields.copyPoint1_Z; (stateIteration_Z || var8 != (ModHelper.ModHelperFields.copyPoint2_Z + add_z)); var8 += add_z) {
                                level.setTile(var6, var7, var8, blockId);
                                level.setTileMeta(var6, var7, var8, blockMeta);

                                stateIteration_Z = false;
                            }
                            stateIteration_Z = allowSingleIteration_Z;
                            stateIteration_Y = false;
                        }
                        stateIteration_Y = allowSingleIteration_Y;
                        stateIteration_X = false;
                    }
                    stateIteration_X = allowSingleIteration_X;
                }
            } else if (2 == item.count) {
                /** - Paste selection */
                if (  (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint1_X)
                   && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint1_Y)
                   && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint1_Z)
                   && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint2_X)
                   && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint2_Y)
                   && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint2_Z)
                ) {
                    int add_x = 0;
                    int add_y = 0;
                    int add_z = 0;
                    boolean allowSingleIteration_X = false;
                    boolean allowSingleIteration_Y = false;
                    boolean allowSingleIteration_Z = false;

                    if (ModHelper.ModHelperFields.copyPoint1_X < ModHelper.ModHelperFields.copyPoint2_X) {
                        add_x = 1;
                    } else if (ModHelper.ModHelperFields.copyPoint1_X > ModHelper.ModHelperFields.copyPoint2_X) {
                        add_x = -1;
                    } else {
                        allowSingleIteration_X = true;
                    }

                    if (ModHelper.ModHelperFields.copyPoint1_Y < ModHelper.ModHelperFields.copyPoint2_Y) {
                        add_y = 1;
                    } else if (ModHelper.ModHelperFields.copyPoint1_Y > ModHelper.ModHelperFields.copyPoint2_Y) {
                        add_y = -1;
                    } else {
                        allowSingleIteration_Y = true;
                    }

                    if (ModHelper.ModHelperFields.copyPoint1_Z < ModHelper.ModHelperFields.copyPoint2_Z) {
                        add_z = 1;
                    } else if (ModHelper.ModHelperFields.copyPoint1_Z > ModHelper.ModHelperFields.copyPoint2_Z) {
                        add_z = -1;
                    } else {
                        allowSingleIteration_Z = true;
                    }

                    int offset_x = 0;
                    int offset_y = 0;
                    int offset_z = 0;
                    boolean stateIteration_X = allowSingleIteration_X;
                    boolean stateIteration_Y = allowSingleIteration_Y;
                    boolean stateIteration_Z = allowSingleIteration_Z;

                    for(int var6 = ModHelper.ModHelperFields.copyPoint1_X; (stateIteration_X || var6 != (ModHelper.ModHelperFields.copyPoint2_X + add_x)); var6 += add_x) {
                        for(int var7 = ModHelper.ModHelperFields.copyPoint1_Y; (stateIteration_Y || var7 != (ModHelper.ModHelperFields.copyPoint2_Y + add_y)); var7 += add_y) {
                            for(int var8 = ModHelper.ModHelperFields.copyPoint1_Z; (stateIteration_Z || var8 != (ModHelper.ModHelperFields.copyPoint2_Z + add_z)); var8 += add_z) {

                                int copyBlockId = level.getTileId(var6, var7, var8);
                                int copyBlockMeta = level.getTileMeta(var6, var7, var8);
                                level.setTile(i + offset_x, j + offset_y, k + offset_z, copyBlockId);
                                level.setTileMeta(i + offset_x, j + offset_y, k + offset_z, copyBlockMeta);

                                offset_z += add_z;
                                stateIteration_Z = false;
                            }
                            stateIteration_Z = allowSingleIteration_Z;
                            offset_z = 0;
                            offset_y += add_y;
                            stateIteration_Y = false;
                        }
                        stateIteration_Y = allowSingleIteration_Y;
                        offset_y = 0;
                        offset_x += add_x;
                        stateIteration_X = false;
                    }
                    offset_x = 0;
                    stateIteration_X = allowSingleIteration_X;
                }
            } else {
                /** - Select/clear points */
                if (  (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint2_X)
                   && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint2_Y)
                   && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint2_Z)
                ) {
                    ModHelper.ModHelperFields.copyPoint1_X = Integer.MAX_VALUE;
                    ModHelper.ModHelperFields.copyPoint1_Y = Integer.MAX_VALUE;
                    ModHelper.ModHelperFields.copyPoint1_Z = Integer.MAX_VALUE;
                    ModHelper.ModHelperFields.copyPoint2_X = Integer.MAX_VALUE;
                    ModHelper.ModHelperFields.copyPoint2_Y = Integer.MAX_VALUE;
                    ModHelper.ModHelperFields.copyPoint2_Z = Integer.MAX_VALUE;
                    item.setDamage(0);
                } else if (  (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint1_X)
                          && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint1_Y)
                          && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint1_Z)
                ) {
                    ModHelper.ModHelperFields.copyPoint2_X = i;
                    ModHelper.ModHelperFields.copyPoint2_Y = j;
                    ModHelper.ModHelperFields.copyPoint2_Z = k;
                    item.setDamage(1);
                } else {
                    ModHelper.ModHelperFields.copyPoint1_X = i;
                    ModHelper.ModHelperFields.copyPoint1_Y = j;
                    ModHelper.ModHelperFields.copyPoint1_Z = k;
                    item.setDamage(item.getDurability() / 2);
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
