package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AxeItem.class)
public class AxeMixin extends ToolItem implements CustomTooltipProvider {
    /** - Selection with wooden axe */
    @Shadow
    private static Block[] axeEffectiveBlocks;
    public AxeMixin(int i, ToolMaterial arg) {
        super(i, 1, arg, axeEffectiveBlocks);
    }

    @Override
    public boolean postHit(ItemStack item, LivingEntity arg2, LivingEntity arg3) {
        if (  (this.id == Item.WOODEN_AXE.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
           && (arg3 instanceof  PlayerEntity)
           && (ModHelper.IsPlayerCreative((PlayerEntity) arg3))
        ) {
            int damageValue = item.getDamage();
            if (0 < damageValue && (ModHelper.SELECTION_TOOL_DURABILITY - 2) >= damageValue) {
                int curCount = item.count;
                item.damage(1, null);
                item.count = curCount;
                if ((ModHelper.SELECTION_TOOL_DURABILITY - 2) < item.getDamage()) {
                    item.setDamage(1);
                }
                if (1 > item.getDamage()) {
                    item.setDamage((ModHelper.SELECTION_TOOL_DURABILITY - 2));
                }
            }
            ModHelper.ModHelperFields.brushSize = item.getDamage();
            ModHelper.ModHelperFields.brushType = item.count;
        }
        return false;
    }

    @Override
    public ItemStack use(ItemStack item, World arg2, PlayerEntity player) {
        if (  (this.id == Item.WOODEN_AXE.id)
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
    public String[] getTooltip(ItemStack itemInstance, String originalTooltip) {
        if (  (this.id == Item.WOODEN_AXE.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            PlayerEntity player = PlayerHelper.getPlayerFromGame();
            if (  (ModHelper.IsPlayerCreative(player))
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

                String rotation = "N/A";

                switch (itemInstance.getDamage()) {
                    case 1:
                        rotation = "0 deg";
                        break;

                    case 2:
                        rotation = "90 deg";
                        break;

                    case 3:
                        rotation = "180 deg";
                        break;

                    case 4:
                        rotation = "270 deg";
                        break;

                    case 5:
                        rotation = "0 deg (flipped)";
                        break;

                    case 6:
                        rotation = "90 deg (flipped)";
                        break;

                    case 7:
                        rotation = "180 deg (flipped)";
                        break;

                    case 8:
                        rotation = "270 deg (flipped)";
                        break;
                }

                return new String[]{"§b" + "Selection", "Point 1: " + selection1, "Point 2: " + selection2, "Mode: " + selectionMode, "Rotation: " + rotation};
            } else {
                return new String[]{originalTooltip};
            }
        } else {
            return new String[]{originalTooltip};
        }
    }

    @Unique
    private int rotationMatrix(int value_x, int value_y, int value_z, int is_xyz, int rotation_value) {
        int rotation_x = 1;
        int rotation_y = 1;
        int rotation_z = 1;

        if (-1 < rotation_value) {
            if (rotation_value == 0) {
                int temp = value_x;
                value_x = value_z * -1;
                value_z = temp;
            } else if (rotation_value == 1) {
                value_x *= -1;
                value_z *= -1;
            } else if (rotation_value == 2) {
                int temp = value_x;
                value_x = value_z;
                value_z = temp;
            } else if (rotation_value == 3) {
                value_y *= -1;
            } else if (rotation_value == 4) {
                value_y *= -1;
                int temp = value_x;
                value_x = value_z * -1;
                value_z = temp;
            } else if (rotation_value == 5) {
                value_y *= -1;
                value_x *= -1;
                value_z *= -1;
            } else if (rotation_value == 6) {
                value_y *= -1;
                int temp = value_x;
                value_x = value_z;
                value_z = temp;
            }
        }

        if (0 == is_xyz) {
            return value_x;
        } else if (1 == is_xyz) {
            return  value_y;
        } else {
            return value_z;
        }
    }

    @Override
    public boolean useOnBlock(ItemStack item, PlayerEntity player, World world, int i, int j, int k, int meta) {
        if (  (this.id == Item.WOODEN_AXE.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
           && (ModHelper.IsPlayerCreative(player))
        ) {
            int blockId = world.getBlockId(i, j, k);
            int blockMeta = world.getBlockMeta(i, j, k);

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

                    int damageValue = item.getDamage();
                    if (0 < damageValue && (ModHelper.SELECTION_TOOL_DURABILITY - 2) >= damageValue) {
                        damageValue = damageValue - 2;
                    } else {
                        damageValue = -1;
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

                                world.setBlock( ModHelper.ModHelperFields.copyPoint1_X + rotationMatrix(offset_x, offset_y, offset_z, 0, damageValue)
                                              , ModHelper.ModHelperFields.copyPoint1_Y + rotationMatrix(offset_x, offset_y, offset_z, 1, damageValue)
                                              , ModHelper.ModHelperFields.copyPoint1_Z + rotationMatrix(offset_x, offset_y, offset_z, 2, damageValue)
                                              , blockId);
                                world.setBlockMeta( ModHelper.ModHelperFields.copyPoint1_X + rotationMatrix(offset_x, offset_y, offset_z, 0, damageValue)
                                                  , ModHelper.ModHelperFields.copyPoint1_Y + rotationMatrix(offset_x, offset_y, offset_z, 1, damageValue)
                                                  , ModHelper.ModHelperFields.copyPoint1_Z + rotationMatrix(offset_x, offset_y, offset_z, 2, damageValue)
                                                  , blockMeta);

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

                    ModHelper.setTooltip("Selection between Point 1 and Point 2 filled!", 40);
                } else {
                    ModHelper.setTooltip("Current Selection is Invalid!", 40);
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

                    int damageValue = item.getDamage();
                    if (0 < damageValue && (ModHelper.SELECTION_TOOL_DURABILITY - 2) >= damageValue) {
                        damageValue = damageValue - 2;
                    } else {
                        damageValue = -1;
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

                                int copyBlockId = world.getBlockId(var6, var7, var8);
                                int copyBlockMeta = world.getBlockMeta(var6, var7, var8);
                                world.setBlock( i + rotationMatrix(offset_x, offset_y, offset_z, 0, damageValue)
                                              , j + rotationMatrix(offset_x, offset_y, offset_z, 1, damageValue)
                                              , k + rotationMatrix(offset_x, offset_y, offset_z, 2, damageValue)
                                              , copyBlockId);
                                world.setBlockMeta( i + rotationMatrix(offset_x, offset_y, offset_z, 0, damageValue)
                                                  , j + rotationMatrix(offset_x, offset_y, offset_z, 1, damageValue)
                                                  , k + rotationMatrix(offset_x, offset_y, offset_z, 2, damageValue)
                                                  , copyBlockMeta);

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


                    ModHelper.setTooltip("Selection copied to ["
                                        + i + ","
                                        + j + ","
                                        + k + "]!"
                                        , 40);
                } else {
                    ModHelper.setTooltip("Current Selection is Invalid!", 40);
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

                    ModHelper.setTooltip("Selection Cleared!", 40);
                } else if (  (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint1_X)
                          && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint1_Y)
                          && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint1_Z)
                ) {
                    ModHelper.ModHelperFields.copyPoint2_X = i;
                    ModHelper.ModHelperFields.copyPoint2_Y = j;
                    ModHelper.ModHelperFields.copyPoint2_Z = k;
                    item.setDamage(1);

                    ModHelper.setTooltip("Point 2 Set! ["
                                    + ModHelper.ModHelperFields.copyPoint2_X + ","
                                    + ModHelper.ModHelperFields.copyPoint2_Y + ","
                                    + ModHelper.ModHelperFields.copyPoint2_Z + "]"
                            , 40);
                } else {
                    ModHelper.ModHelperFields.copyPoint1_X = i;
                    ModHelper.ModHelperFields.copyPoint1_Y = j;
                    ModHelper.ModHelperFields.copyPoint1_Z = k;
                    item.setDamage((ModHelper.SELECTION_TOOL_DURABILITY - 1));

                    ModHelper.setTooltip("Point 1 Set! ["
                                        + ModHelper.ModHelperFields.copyPoint1_X + ","
                                        + ModHelper.ModHelperFields.copyPoint1_Y + ","
                                        + ModHelper.ModHelperFields.copyPoint1_Z + "]"
                                        , 40);
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
