package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.Shovel;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Shovel.class)
public class ShovelMixin extends ToolBase implements CustomTooltipProvider {
    /** - Erase with wooden shovel */
    @Shadow
    private static BlockBase[] effectiveBlocks;
    public ShovelMixin(int i, ToolMaterial arg) {
        super(i, 1, arg, effectiveBlocks);
    }

    @Override
    public String[] getTooltip(ItemInstance itemInstance, String originalTooltip) {
        if (  (this.id == ItemBase.woodShovel.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            return new String[]{"Eraser", "Size: " + 1};
        } else {
            return new String[]{originalTooltip};
        }
    }

    @Override
    public boolean useOnTile(ItemInstance item, PlayerBase player, Level level, int i, int j, int k, int meta) {
        if (  (this.id == ItemBase.woodShovel.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            int blockId = level.getTileId(i, j, k);
            int blockMeta = level.getTileMeta(i, j, k);

            level.setTile(i, j, k, 0);
            level.setTileMeta(i, j, k, 0);

            if (meta == 0) {
                --j;
            } else if (meta == 1) {
                ++j;
            } else if (meta == 2) {
                --k;
            } else if (meta == 3) {
                ++k;
            } else if (meta == 4) {
                --i;
            } else if (meta == 5) {
                ++i;
            }

            return true;
        } else {
            return false;
        }
    }
}
