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
    public String[] getTooltip(ItemInstance itemInstance, String originalTooltip) {
        if (  (this.id == ItemBase.woodAxe.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            String selection = "null";

            if (false) {
                selection = "[" + 1 + "," + 2 + "," + 3 + "] to " + "[" + 1 + "," + 2 + "," + 3 + "]";
            }

            return new String[]{"Copy/Paste", "Selection: " + selection, "Mode: " + itemInstance.count};
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

            return true;
        } else {
            return false;
        }
    }
}
