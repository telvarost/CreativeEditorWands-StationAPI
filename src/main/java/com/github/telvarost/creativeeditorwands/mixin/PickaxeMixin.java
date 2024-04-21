package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.Pickaxe;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Pickaxe.class)
public class PickaxeMixin extends ToolBase implements CustomTooltipProvider {
    /** - Change paint/erase color with wooden pickaxe */
    @Shadow
    private static BlockBase[] effectiveBlocks;
    public PickaxeMixin(int i, ToolMaterial arg) {
        super(i, 1, arg, effectiveBlocks);
    }

    @Override
    public String[] getTooltip(ItemInstance itemInstance, String originalTooltip) {
        if (  (this.id == ItemBase.woodPickaxe.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            return new String[]{"Color Picker", "Color: " + itemInstance.getDamage(), "Metadata: " + (itemInstance.count - 1)};
        } else {
            return new String[]{originalTooltip};
        }
    }

    @Override
    public boolean useOnTile(ItemInstance item, PlayerBase player, Level level, int i, int j, int k, int meta) {
        if (  (this.id == ItemBase.woodPickaxe.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            int blockId = level.getTileId(i, j, k);
            int blockMeta = level.getTileMeta(i, j, k);

            item.setDamage(blockId);
            item.count = (blockMeta + 1);

            return true;
        } else {
            return false;
        }
    }
}
