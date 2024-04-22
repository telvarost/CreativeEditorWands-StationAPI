package com.github.telvarost.creativeeditorwands.mixin;

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
    public ItemInstance use(ItemInstance item, Level arg2, PlayerBase arg3) {
        if (  (this.id == ItemBase.woodAxe.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
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

                if (  (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint2_X)
                   && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint2_Y)
                   && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint2_Z)
                ) {
                    selection2 = "[" + ModHelper.ModHelperFields.copyPoint2_X
                               + "," + ModHelper.ModHelperFields.copyPoint2_Y
                               + "," + ModHelper.ModHelperFields.copyPoint2_Z
                               + "]";
                }
            }

            return new String[]{"Copy/Paste", "Point 1: " + selection1, "Point 2: " + selection2, "Mode: " + itemInstance.count};
        } else {
            return new String[]{originalTooltip};
        }
    }

    @Override
    public boolean useOnTile(ItemInstance item, PlayerBase player, Level level, int i, int j, int k, int meta) {
        if (  (this.id == ItemBase.woodAxe.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            int blockId = level.getTileId(i, j, k);
            int blockMeta = level.getTileMeta(i, j, k);

            if (3 == item.count) {
                /** - Fill selection */
            } else if (2 == item.count) {
                /** - Paste selection */
            } else {
                /** - Select/clear points */
                if ((Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint2_X)
                   && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint2_Y)
                   && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint2_Z)
                ) {
                    ModHelper.ModHelperFields.copyPoint1_X = Integer.MAX_VALUE;
                    ModHelper.ModHelperFields.copyPoint1_Y = Integer.MAX_VALUE;
                    ModHelper.ModHelperFields.copyPoint1_Z = Integer.MAX_VALUE;
                    ModHelper.ModHelperFields.copyPoint2_X = Integer.MAX_VALUE;
                    ModHelper.ModHelperFields.copyPoint2_Y = Integer.MAX_VALUE;
                    ModHelper.ModHelperFields.copyPoint2_Z = Integer.MAX_VALUE;
                } else if (  (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint1_X)
                          && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint1_Y)
                          && (Integer.MAX_VALUE != ModHelper.ModHelperFields.copyPoint1_Z)
                ) {
                    ModHelper.ModHelperFields.copyPoint2_X = i;
                    ModHelper.ModHelperFields.copyPoint2_Y = j;
                    ModHelper.ModHelperFields.copyPoint2_Z = k;
                } else {
                    ModHelper.ModHelperFields.copyPoint1_X = i;
                    ModHelper.ModHelperFields.copyPoint1_Y = j;
                    ModHelper.ModHelperFields.copyPoint1_Z = k;
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
