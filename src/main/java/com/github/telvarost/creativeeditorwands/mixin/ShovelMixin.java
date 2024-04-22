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
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
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
            return new String[]{"Erase Brush", "Block: " + itemInstance.getDamage(), "Metadata: " + (itemInstance.count - 1)};
        } else {
            return new String[]{originalTooltip};
        }
    }

    @Override
    public boolean useOnTile(ItemInstance item, PlayerBase player, Level level, int i, int j, int k, int meta) {
        if (  (this.id == ItemBase.woodShovel.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            int x = i;
            int y = j;
            int z = k;
            int blockId = level.getTileId(i, j, k);
            int blockMeta = level.getTileMeta(i, j, k);
            int eraseId = item.getDamage();
            int eraseMeta = (item.count - 1);

            if (  (null == PlayerHelper.getPlayerFromGame())
               || (false != level.isServerSide)
            ) {
                eraseId = ModHelper.ModHelperFields.serverBlockId;
                eraseMeta = ModHelper.ModHelperFields.serverBlockMeta;
            }

            if (0 == eraseId) {
                level.setTile(i, j, k, 0);
                level.setTileMeta(i, j, k, 0);
            } else {
                if (  (blockId   == eraseId)
                   && (blockMeta == eraseMeta)
                ) {
                    level.setTile(i, j, k, 0);
                    level.setTileMeta(i, j, k, 0);
                }
            }
//            if (meta == 0) {
//                --y;
//            } else if (meta == 1) {
//                ++y;
//            } else if (meta == 2) {
//                --z;
//            } else if (meta == 3) {
//                ++z;
//            } else if (meta == 4) {
//                --x;
//            } else if (meta == 5) {
//                ++x;
//            }

            return true;
        } else {
            return false;
        }
    }
}
