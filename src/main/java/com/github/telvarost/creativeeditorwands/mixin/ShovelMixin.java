package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.block.Block;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ShovelItem.class)
public class ShovelMixin extends ToolItem implements CustomTooltipProvider {
    /** - Erase with wooden shovel */
    @Shadow
    private static Block[] shovelEffectiveBlocks;
    public ShovelMixin(int i, ToolMaterial arg) {
        super(i, 1, arg, shovelEffectiveBlocks);
    }

    @Override
    public String[] getTooltip(ItemStack item, String originalTooltip) {
        if (  (this.id == Item.WOODEN_SHOVEL.id)
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

                return new String[]{ "§b" + "Erase Brush"
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
        if (  (this.id == Item.WOODEN_SHOVEL.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
           && (ModHelper.IsPlayerCreative(player))
        ) {
            int x = i;
            int y = j;
            int z = k;
            int blockId = level.getBlockId(i, j, k);
            int blockMeta = level.getBlockMeta(i, j, k);
            int eraseId = item.getDamage();
            int eraseMeta = (item.count - 1);
            boolean eraseMatching = true;

            if (  (null == PlayerHelper.getPlayerFromGame())
               || (false != level.isRemote)
            ) {
                eraseId = ModHelper.ModHelperFields.serverBlockId;
                eraseMeta = ModHelper.ModHelperFields.serverBlockMeta;
            }

            if (0 == eraseId) {
                eraseMatching = false;
            }


            if (1 == ModHelper.ModHelperFields.brushType) {
                creativeEditorWands_squareEraseBrush(level, i, j, k, eraseId, eraseMeta, meta, eraseMatching);
            } else if (2 == ModHelper.ModHelperFields.brushType) {
                creativeEditorWands_cubeEraseBrush(level, i, j, k, eraseId, eraseMeta, eraseMatching);
            } else {
                if (false == eraseMatching) {
                    level.setBlock(i, j, k, 0);
                    level.setBlockMeta(i, j, k, 0);
                } else if (  (blockId == eraseId)
                          && (blockMeta == eraseMeta)
                ) {
                    level.setBlock(i, j, k, 0);
                    level.setBlockMeta(i, j, k, 0);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Unique
    private void creativeEditorWands_cubeEraseBrush(World level, int x, int y, int z, int eraseId, int eraseMeta, boolean eraseMatching) {
        byte var5 = ModHelper.ModHelperFields.brushSize.byteValue();

        for(int var6 = x - var5; var6 <= x + var5; ++var6) {
            for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                    if (eraseMatching) {
                        int blockId = level.getBlockId(var6, var7, var8);
                        int blockMeta = level.getBlockMeta(var6, var7, var8);
                        if (  (blockId == eraseId)
                           && (blockMeta == eraseMeta)
                        ) {
                            level.setBlock(var6, var7, var8, 0);
                            level.setBlockMeta(var6, var7, var8, 0);
                        }
                    } else {
                        level.setBlock(var6, var7, var8, 0);
                        level.setBlockMeta(var6, var7, var8, 0);
                    }
                }
            }
        }
    }

    @Unique
    private void creativeEditorWands_squareEraseBrush(World level, int x, int y, int z, int eraseId, int eraseMeta, int direction, boolean eraseMatching) {
        byte var5 = ModHelper.ModHelperFields.brushSize.byteValue();

        if (direction == 0) {

            for(int var6 = x - var5; var6 <= x + var5; ++var6) {
                int var7 = y;
                //for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                    int blockId = level.getBlockId(var6, var7, var8);
                    int blockMeta = level.getBlockMeta(var6, var7, var8);
                    if (false == eraseMatching) {
                        level.setBlock(var6, var7, var8, 0);
                        level.setBlockMeta(var6, var7, var8, 0);
                    } else if (  (blockId == eraseId)
                              && (blockMeta == eraseMeta)
                    ) {
                        level.setBlock(var6, var7, var8, 0);
                        level.setBlockMeta(var6, var7, var8, 0);
                    }
                }
                //}
            }
        } else if (direction == 1) {

            for(int var6 = x - var5; var6 <= x + var5; ++var6) {
                int var7 = y;
                //for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                    int blockId = level.getBlockId(var6, var7, var8);
                    int blockMeta = level.getBlockMeta(var6, var7, var8);
                    if (false == eraseMatching) {
                        level.setBlock(var6, var7, var8, 0);
                        level.setBlockMeta(var6, var7, var8, 0);
                    } else if (  (blockId == eraseId)
                            && (blockMeta == eraseMeta)
                    ) {
                        level.setBlock(var6, var7, var8, 0);
                        level.setBlockMeta(var6, var7, var8, 0);
                    }
                }
                //}
            }
        } else if (direction == 2) {

            for(int var6 = x - var5; var6 <= x + var5; ++var6) {
                for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                    //for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                    int var8 = z;
                    int blockId = level.getBlockId(var6, var7, var8);
                    int blockMeta = level.getBlockMeta(var6, var7, var8);
                    if (false == eraseMatching) {
                        level.setBlock(var6, var7, var8, 0);
                        level.setBlockMeta(var6, var7, var8, 0);
                    } else if (  (blockId == eraseId)
                            && (blockMeta == eraseMeta)
                    ) {
                        level.setBlock(var6, var7, var8, 0);
                        level.setBlockMeta(var6, var7, var8, 0);
                    }
                    //}
                }
            }
        } else if (direction == 3) {

            for(int var6 = x - var5; var6 <= x + var5; ++var6) {
                for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                    //for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                    int var8 = z;
                    int blockId = level.getBlockId(var6, var7, var8);
                    int blockMeta = level.getBlockMeta(var6, var7, var8);
                    if (false == eraseMatching) {
                        level.setBlock(var6, var7, var8, 0);
                        level.setBlockMeta(var6, var7, var8, 0);
                    } else if (  (blockId == eraseId)
                            && (blockMeta == eraseMeta)
                    ) {
                        level.setBlock(var6, var7, var8, 0);
                        level.setBlockMeta(var6, var7, var8, 0);
                    }
                    //}
                }
            }
        } else if (direction == 4) {

            //for(int var6 = x - var5; var6 <= x + var5; ++var6) {
            int var6 = x;
            for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                    int blockId = level.getBlockId(var6, var7, var8);
                    int blockMeta = level.getBlockMeta(var6, var7, var8);
                    if (false == eraseMatching) {
                        level.setBlock(var6, var7, var8, 0);
                        level.setBlockMeta(var6, var7, var8, 0);
                    } else if (  (blockId == eraseId)
                            && (blockMeta == eraseMeta)
                    ) {
                        level.setBlock(var6, var7, var8, 0);
                        level.setBlockMeta(var6, var7, var8, 0);
                    }
                }
            }
            //}
        } else if (direction == 5) {

            //for(int var6 = x - var5; var6 <= x + var5; ++var6) {
            int var6 = x;
            for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                    int blockId = level.getBlockId(var6, var7, var8);
                    int blockMeta = level.getBlockMeta(var6, var7, var8);
                    if (false == eraseMatching) {
                        level.setBlock(var6, var7, var8, 0);
                        level.setBlockMeta(var6, var7, var8, 0);
                    } else if (  (blockId == eraseId)
                            && (blockMeta == eraseMeta)
                    ) {
                        level.setBlock(var6, var7, var8, 0);
                        level.setBlockMeta(var6, var7, var8, 0);
                    }
                }
            }
            //}
        }
    }
}
