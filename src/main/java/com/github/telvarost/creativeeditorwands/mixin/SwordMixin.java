package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.Sword;
import net.minecraft.item.tool.ToolMaterial;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.item.tool.StationSwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sword.class)
public class SwordMixin extends ItemBase implements StationSwordItem, CustomTooltipProvider {
    /**
     * - Paint with wooden sword
     */
    public SwordMixin(int i, ToolMaterial arg) {
        super(i);
    }

    @Inject(at = @At("HEAD"), method = "postHit", cancellable = true)
    public void creativeEditorWands_postHit(ItemInstance arg, Living arg2, Living arg3, CallbackInfoReturnable<Boolean> cir) {
        if (  (this.id == ItemBase.woodSword.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            cir.setReturnValue(true);
        }
    }

    @Inject(at = @At("HEAD"), method = "postMine", cancellable = true)
    public void creativeEditorWands_postMine(ItemInstance arg, int i, int j, int k, int l, Living arg2, CallbackInfoReturnable<Boolean> cir) {
        if (  (this.id == ItemBase.woodSword.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public String[] getTooltip(ItemInstance item, String originalTooltip) {
        if (  (this.id == ItemBase.woodSword.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            int paintId;
            int paintMeta;

            if (  (null  != PlayerHelper.getPlayerFromGame())
               && (null  != PlayerHelper.getPlayerFromGame().level)
               && (false == PlayerHelper.getPlayerFromGame().level.isServerSide)
            ) {
                paintId = item.getDamage();
                paintMeta = (item.count - 1);
            } else {
                paintId = ModHelper.ModHelperFields.serverBlockId;
                paintMeta = ModHelper.ModHelperFields.serverBlockMeta;
            }

            return new String[]{"Paint Brush", "Block: " + paintId, "Metadata: " + paintMeta};
        } else {
            return new String[]{originalTooltip};
        }
    }

    @Override
    public boolean useOnTile(ItemInstance item, PlayerBase player, Level level, int i, int j, int k, int meta) {
        if (  (this.id == ItemBase.woodSword.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            int x = i;
            int y = j;
            int z = k;
            int blockId = level.getTileId(i, j, k);
            int blockMeta = level.getTileMeta(i, j, k);
            int paintId = item.getDamage();
            int paintMeta = (item.count - 1);

            if (  (null == PlayerHelper.getPlayerFromGame())
               || (false != level.isServerSide)
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
                creativeEditorWands_cubePaintBrush(level, x, y, z, paintId, paintMeta);
            } else if (2 == ModHelper.ModHelperFields.brushType) {
                creativeEditorWands_squarePaintBrush(level, x, y, z, paintId, paintMeta, meta);
            } else {
                level.setTile(x, y, z, paintId);
                level.setTileMeta(x, y, z, paintId);
            }

            return true;
        } else {
            return false;
        }
    }

    @Unique
    private void creativeEditorWands_cubePaintBrush(Level level, int x, int y, int z, int blockId, int blockMeta) {
        byte var5 = ModHelper.ModHelperFields.brushSize.byteValue();

        for(int var6 = x - var5; var6 <= x + var5; ++var6) {
            for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                    level.setTile(var6, var7, var8, blockId);
                    level.setTileMeta(var6, var7, var8, blockMeta);
                }
            }
        }
    }

    @Unique
    private void creativeEditorWands_squarePaintBrush(Level level, int x, int y, int z, int blockId, int blockMeta, int direction) {
        byte var5 = ModHelper.ModHelperFields.brushSize.byteValue();

        if (direction == 0) {

            for(int var6 = x - var5; var6 <= x + var5; ++var6) {
                int var7 = y;
                //for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                    for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                        level.setTile(var6, var7, var8, blockId);
                        level.setTileMeta(var6, var7, var8, blockMeta);
                    }
                //}
            }
        } else if (direction == 1) {

            for(int var6 = x - var5; var6 <= x + var5; ++var6) {
                int var7 = y;
                //for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                    level.setTile(var6, var7, var8, blockId);
                    level.setTileMeta(var6, var7, var8, blockMeta);
                }
                //}
            }
        } else if (direction == 2) {

            for(int var6 = x - var5; var6 <= x + var5; ++var6) {
                for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                    //for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                    int var8 = z;
                    level.setTile(var6, var7, var8, blockId);
                    level.setTileMeta(var6, var7, var8, blockMeta);
                    //}
                }
            }
        } else if (direction == 3) {

            for(int var6 = x - var5; var6 <= x + var5; ++var6) {
                for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                    //for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                    int var8 = z;
                    level.setTile(var6, var7, var8, blockId);
                    level.setTileMeta(var6, var7, var8, blockMeta);
                    //}
                }
            }
        } else if (direction == 4) {

            //for(int var6 = x - var5; var6 <= x + var5; ++var6) {
            int var6 = x;
            for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                    level.setTile(var6, var7, var8, blockId);
                    level.setTileMeta(var6, var7, var8, blockMeta);
                }
            }
            //}
        } else if (direction == 5) {

            //for(int var6 = x - var5; var6 <= x + var5; ++var6) {
            int var6 = x;
            for(int var7 = y - var5; var7 <= y + var5; ++var7) {
                for(int var8 = z - var5; var8 <= z + var5; ++var8) {
                    level.setTile(var6, var7, var8, blockId);
                    level.setTileMeta(var6, var7, var8, blockMeta);
                }
            }
            //}
        }
    }
}
