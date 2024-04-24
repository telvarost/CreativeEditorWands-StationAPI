package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.Block;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.item.StationFlatteningBlockItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
public class BlockMixin extends ItemBase implements StationFlatteningBlockItem, CustomTooltipProvider {
    @Shadow private int blockId;

    /** - Enable/disable world edit style tools with bedrock */
    public BlockMixin(int i) {
        super(i);
    }

    @Override
    public String[] getTooltip(ItemInstance itemInstance, String originalTooltip) {
        if (this.blockId == BlockBase.BEDROCK.id) {
            PlayerBase player = PlayerHelper.getPlayerFromGame();
            if (  (ModHelper.IsPlayerCreative(player))
               && (null != player.level)
               && (!player.level.isServerSide)
            ) {
                return new String[]{"§b" + "Creative Wands", "Enabled: " + ModHelper.ModHelperFields.enableWorldEditTools};
            } else {
                return new String[]{originalTooltip};
            }
        } else {
            return new String[]{originalTooltip};
        }
    }

    @Override
    public ItemInstance use(ItemInstance item, Level arg2, PlayerBase player) {
        if (  (this.blockId == BlockBase.BEDROCK.id)
           && (ModHelper.IsPlayerCreative(player))
           && (null != player.level)
           && (!player.level.isServerSide)
        ) {
            ModHelper.SetEnableWorldEditTools(!ModHelper.ModHelperFields.enableWorldEditTools);
        }
        return item;
    }
}
