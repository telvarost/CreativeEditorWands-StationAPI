package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.block.Block;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.item.tool.StationSwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SwordItem.class)
public class SwordMixin extends Item implements StationSwordItem, CustomTooltipProvider {
    /** - Paint with wooden sword */
    public SwordMixin(int i, ToolMaterial arg) {
        super(i);
    }

    @Inject(at = @At("HEAD"), method = "postHit", cancellable = true)
    public void creativeEditorWands_postHit(ItemStack arg, LivingEntity arg2, LivingEntity arg3, CallbackInfoReturnable<Boolean> cir) {
        if (  (this.id == Item.WOODEN_SWORD.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
           && (arg3 instanceof  PlayerEntity)
        ) {
            PlayerEntity player = (PlayerEntity) arg3;
            if (ModHelper.IsPlayerCreative(player)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "postMine", cancellable = true)
    public void creativeEditorWands_postMine(ItemStack arg, int i, int j, int k, int l, LivingEntity arg2, CallbackInfoReturnable<Boolean> cir) {
        if (  (this.id == Item.WOODEN_SWORD.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
           && (arg2 instanceof  PlayerEntity)
        ) {
            PlayerEntity player = (PlayerEntity) arg2;
            if (ModHelper.IsPlayerCreative(player)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Override
    public String[] getTooltip(ItemStack item, String originalTooltip) {
        if (  (this.id == Item.WOODEN_SWORD.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            PlayerEntity player = PlayerHelper.getPlayerFromGame();
            int paintId;
            int paintMeta;

            if (  (ModHelper.IsPlayerCreative(player))
            ) {
                if (  (null != player.world)
                   && (!player.world.isRemote)
                ) {
                    paintId = item.getDamage();
                    paintMeta = (item.count - 1);
                } else {
                    paintId = ModHelper.ModHelperFields.serverBlockId;
                    paintMeta = ModHelper.ModHelperFields.serverBlockMeta;
                }

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

                return new String[]{ "§b" + "Paint Brush"
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
    public boolean useOnBlock(ItemStack item, PlayerEntity player, World level, int i, int j, int k, int meta) {
        if (  (this.id == Item.WOODEN_SWORD.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
           && (ModHelper.IsPlayerCreative(player))
        ) {
            int x = i;
            int y = j;
            int z = k;
            int blockId = level.getBlockId(i, j, k);
            int blockMeta = level.getBlockMeta(i, j, k);
            int paintId = item.getDamage();
            int paintMeta = (item.count - 1);

            if (  (null == PlayerHelper.getPlayerFromGame())
               || (false != level.isRemote)
            ) {
                paintId = ModHelper.ModHelperFields.serverBlockId;
                paintMeta = ModHelper.ModHelperFields.serverBlockMeta;
            }

            if (meta == 0) {
                --y;
            } else if (meta == 1) {
                ++y;
            } else if (meta == 2) {
                --z;
            } else if (meta == 3) {
                ++z;
            } else if (meta == 4) {
                --x;
            } else if (meta == 5) {
                ++x;
            }

            if (0 == paintId) {
                paintId = blockId;
                paintMeta = blockMeta;
            }

            if (1 == ModHelper.ModHelperFields.brushType) {
                creativeEditorWands_squarePaintBrush(level, x, y, z, paintId, paintMeta, meta);
            } else if (2 == ModHelper.ModHelperFields.brushType) {
                creativeEditorWands_cubePaintBrush(level, x, y, z, paintId, paintMeta);
            } else {
                level.setBlock(x, y, z, paintId);
                level.setBlockMeta(x, y, z, paintId);
            }

            return true;
        } else {
            return false;
        }
    }

    @Unique
    private void creativeEditorWands_cubePaintBrush(World level, int x, int y, int z, int blockId, int blockMeta) {
        byte var5 = ModHelper.ModHelperFields.brushSize.byteValue();

        for(int var6 = x - var5; var6 <= x + var5; ++var6) {
            for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                    level.setBlock(var6, var7, var8, blockId);
                    level.setBlockMeta(var6, var7, var8, blockMeta);
                }
            }
        }
    }

    @Unique
    private void creativeEditorWands_squarePaintBrush(World level, int x, int y, int z, int blockId, int blockMeta, int direction) {
        byte var5 = ModHelper.ModHelperFields.brushSize.byteValue();

        if (direction == 0) {

            for(int var6 = x - var5; var6 <= x + var5; ++var6) {
                int var7 = y;
                //for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                    for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                        level.setBlock(var6, var7, var8, blockId);
                        level.setBlockMeta(var6, var7, var8, blockMeta);
                    }
                //}
            }
        } else if (direction == 1) {

            for(int var6 = x - var5; var6 <= x + var5; ++var6) {
                int var7 = y;
                //for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                    level.setBlock(var6, var7, var8, blockId);
                    level.setBlockMeta(var6, var7, var8, blockMeta);
                }
                //}
            }
        } else if (direction == 2) {

            for(int var6 = x - var5; var6 <= x + var5; ++var6) {
                for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                    //for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                    int var8 = z;
                    level.setBlock(var6, var7, var8, blockId);
                    level.setBlockMeta(var6, var7, var8, blockMeta);
                    //}
                }
            }
        } else if (direction == 3) {

            for(int var6 = x - var5; var6 <= x + var5; ++var6) {
                for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                    //for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                    int var8 = z;
                    level.setBlock(var6, var7, var8, blockId);
                    level.setBlockMeta(var6, var7, var8, blockMeta);
                    //}
                }
            }
        } else if (direction == 4) {

            //for(int var6 = x - var5; var6 <= x + var5; ++var6) {
            int var6 = x;
            for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                    level.setBlock(var6, var7, var8, blockId);
                    level.setBlockMeta(var6, var7, var8, blockMeta);
                }
            }
            //}
        } else if (direction == 5) {

            //for(int var6 = x - var5; var6 <= x + var5; ++var6) {
            int var6 = x;
            for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                    level.setBlock(var6, var7, var8, blockId);
                    level.setBlockMeta(var6, var7, var8, blockMeta);
                }
            }
            //}
        }
    }
}
