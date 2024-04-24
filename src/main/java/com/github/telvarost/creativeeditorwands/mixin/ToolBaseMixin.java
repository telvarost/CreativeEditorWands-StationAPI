package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.BHCreative;
import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.StationToolItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ToolBase.class)
public class ToolBaseMixin extends ItemBase implements StationToolItem {
    public ToolBaseMixin(int i, int j, ToolMaterial arg, BlockBase[] args) {
        super(i);
    }

    @Inject(at = @At("HEAD"), method = "postHit", cancellable = true)
    public void creativeEditorWands_postHit(ItemInstance arg, Living arg2, Living arg3, CallbackInfoReturnable<Boolean> cir) {
        if (  (ModHelper.ModHelperFields.enableWorldEditTools)
           && (  (this.id == ItemBase.woodShovel.id)
              || (this.id == ItemBase.woodAxe.id)
              || (this.id == ItemBase.woodPickaxe.id)
              )
           && (arg3 instanceof  PlayerBase)
        ) {
            PlayerBase player = (PlayerBase) arg2;
            if (ModHelper.IsPlayerCreative(player)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "postMine", cancellable = true)
    public void creativeEditorWands_postMine(ItemInstance arg, int i, int j, int k, int l, Living arg2, CallbackInfoReturnable<Boolean> cir) {
        if (  (ModHelper.ModHelperFields.enableWorldEditTools)
           && (  (this.id == ItemBase.woodShovel.id)
              || (this.id == ItemBase.woodAxe.id)
              || (this.id == ItemBase.woodPickaxe.id)
              )
           && (arg2 instanceof PlayerBase)
        ) {
            PlayerBase player = (PlayerBase) arg2;
            if (ModHelper.IsPlayerCreative(player)) {
                cir.setReturnValue(true);
            }
        }
    }
}
