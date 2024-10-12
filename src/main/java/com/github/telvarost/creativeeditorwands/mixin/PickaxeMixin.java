package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.block.Block;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PickaxeItem.class)
public class PickaxeMixin extends ToolItem implements CustomTooltipProvider {
    /** - Change paint/erase color with wooden pickaxe */
    @Shadow
    private static Block[] pickaxeEffectiveBlocks;
    public PickaxeMixin(int i, ToolMaterial arg) {
        super(i, 1, arg, pickaxeEffectiveBlocks);
    }

    @Override
    public String[] getTooltip(ItemStack item, String originalTooltip) {
        if (  (this.id == Item.WOODEN_PICKAXE.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            PlayerEntity player = PlayerHelper.getPlayerFromGame();
            if (  (ModHelper.IsPlayerCreative(player))
            ) {
                int paintId = item.getDamage();
                int paintMeta = (item.count - 1);

                String blockName;

                if (1 <= paintId && 255 >= paintId) {
                    blockName = Block.BLOCKS[paintId].getTranslatedName();
                    if (35 == paintId) {
                        int itemMeta = (1 > item.count || 16 < item.count) ? 0 : (item.count - 1);
                        String translationKey = Block.WOOL.getTranslationKey() + "." + DyeItem.names[net.minecraft.block.WoolBlock.getBlockMeta(itemMeta)];
                        blockName = I18n.getTranslation(translationKey + ".name");
                    } else if (44 == paintId) {
                        int itemMeta = (1 > item.count || 4 < item.count) ? 0 : (item.count - 1);
                        String translationKey = Block.SLAB.getTranslationKey() + "." + net.minecraft.block.SlabBlock.names[itemMeta];
                        blockName = I18n.getTranslation(translationKey + ".name");
                    }
                } else if (0 == paintId) {
                    blockName = "Any";
                } else {
                    blockName = "Unknown";
                }

                return new String[]{ "§b" + "Block Picker"
                                   , "Block: " + paintId + " (" + blockName + ")"
                                   , "Metadata: " + paintMeta };
            } else {
                return new String[]{originalTooltip};
            }
        } else {
            return new String[]{originalTooltip};
        }
    }

    @Override
    public ItemStack use(ItemStack item, World arg2, PlayerEntity player) {
        if (  (this.id == Item.WOODEN_PICKAXE.id)
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
    public boolean useOnBlock(ItemStack item, PlayerEntity player, World level, int i, int j, int k, int meta) {
        if (  (this.id == Item.WOODEN_PICKAXE.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
           && (ModHelper.IsPlayerCreative(player))
        ) {
            int blockId = level.getBlockId(i, j, k);
            int blockMeta = level.getBlockMeta(i, j, k);

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
