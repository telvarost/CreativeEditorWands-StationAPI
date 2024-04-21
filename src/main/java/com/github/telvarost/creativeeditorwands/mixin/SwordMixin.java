package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.Sword;
import net.minecraft.item.tool.ToolMaterial;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.item.tool.StationSwordItem;
import org.spongepowered.asm.mixin.Mixin;
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
    public String[] getTooltip(ItemInstance itemInstance, String originalTooltip) {
        if (  (this.id == ItemBase.woodSword.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            return new String[]{"Paint Brush", "Block: " + itemInstance.getDamage(), "Metadata: " + (itemInstance.count - 1)};
        } else {
            return new String[]{originalTooltip};
        }
    }

    @Override
    public boolean useOnTile(ItemInstance item, PlayerBase player, Level level, int i, int j, int k, int meta) {
        if (  (this.id == ItemBase.woodSword.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            int blockId = level.getTileId(i, j, k);
            int blockMeta = level.getTileMeta(i, j, k);

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

            if (0 == item.getDamage()) {
                level.setTile(i, j, k, blockId);
                level.setTileMeta(i, j, k, blockMeta);
            } else {
                level.setTile(i, j, k, item.getDamage());
                level.setTileMeta(i, j, k, (item.count - 1));
            }

            return true;
        } else {
            return false;
        }
    }
}
