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
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
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
            PlayerBase player = PlayerHelper.getPlayerFromGame();
            if (  (ModHelper.IsPlayerCreative(player))
            ) {
                int paintId = itemInstance.getDamage();
                int paintMeta = (itemInstance.count - 1);
                return new String[]{ "§b" + "Block Picker"
                                   , "Block: " + paintId + " (" + BlockBase.BY_ID[paintId].getTranslatedName() + ")"
                                   , "Metadata: " + paintMeta };
            } else {
                return new String[]{originalTooltip};
            }
        } else {
            return new String[]{originalTooltip};
        }
    }

    @Override
    public ItemInstance use(ItemInstance item, Level arg2, PlayerBase player) {
        if (  (this.id == ItemBase.woodPickaxe.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
           && (ModHelper.IsPlayerCreative(player))
        ) {
            int blockId = 0;
            int blockMeta = 0;

            item.setDamage(blockId);
            item.count = (blockMeta + 1);

            ModHelper.ModHelperFields.serverBlockId = blockId;
            ModHelper.ModHelperFields.serverBlockMeta = blockMeta;
        }
        return item;
    }

    @Override
    public boolean useOnTile(ItemInstance item, PlayerBase player, Level level, int i, int j, int k, int meta) {
        if (  (this.id == ItemBase.woodPickaxe.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
           && (ModHelper.IsPlayerCreative(player))
        ) {
            int blockId = level.getTileId(i, j, k);
            int blockMeta = level.getTileMeta(i, j, k);

            item.setDamage(blockId);
            item.count = (blockMeta + 1);

            ModHelper.ModHelperFields.serverBlockId = blockId;
            ModHelper.ModHelperFields.serverBlockMeta = blockMeta;

            return true;
        } else {
            return false;
        }
    }
}
