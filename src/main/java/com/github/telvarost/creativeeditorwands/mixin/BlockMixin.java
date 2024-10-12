package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.item.StationFlatteningBlockItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockItem.class)
public class BlockMixin extends Item implements StationFlatteningBlockItem, CustomTooltipProvider {
    @Shadow private int itemId;

    /** - Enable/disable world edit style tools with bedrock */
    public BlockMixin(int i) {
        super(i);
    }

    @Override
    public String[] getTooltip(ItemStack itemInstance, String originalTooltip) {
        if (this.itemId == Block.BEDROCK.id) {
            PlayerEntity player = PlayerHelper.getPlayerFromGame();
            if (  (ModHelper.IsPlayerCreative(player))
               && (null != player.world)
               && (!player.world.isRemote)
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
    public ItemStack use(ItemStack item, World arg2, PlayerEntity player) {
        if (  (this.itemId == Block.BEDROCK.id)
           && (ModHelper.IsPlayerCreative(player))
           && (null != player.world)
           && (!player.world.isRemote)
        ) {
            ModHelper.SetEnableWorldEditTools(!ModHelper.ModHelperFields.enableWorldEditTools);
        }
        return item;
    }
}
