package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.StationToolItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ToolItem.class)
public class ToolBaseMixin extends Item implements StationToolItem {
    public ToolBaseMixin(int i, int j, ToolMaterial arg, Block[] args) {
        super(i);
    }

    @Inject(at = @At("HEAD"), method = "postHit", cancellable = true)
    public void creativeEditorWands_postHit(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        if (  (ModHelper.ModHelperFields.enableWorldEditTools)
           && (  (this.id == Item.WOODEN_SHOVEL.id)
              || (this.id == Item.WOODEN_AXE.id)
              || (this.id == Item.WOODEN_PICKAXE.id)
              )
           && (attacker instanceof PlayerEntity)
        ) {
            PlayerEntity player = (PlayerEntity) attacker;
            if (ModHelper.IsPlayerCreative(player)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "postMine", cancellable = true)
    public void creativeEditorWands_postMine(ItemStack stack, int blockId, int x, int y, int z, LivingEntity miner, CallbackInfoReturnable<Boolean> cir) {
        if (  (ModHelper.ModHelperFields.enableWorldEditTools)
           && (  (this.id == Item.WOODEN_SHOVEL.id)
              || (this.id == Item.WOODEN_AXE.id)
              || (this.id == Item.WOODEN_PICKAXE.id)
              )
           && (miner instanceof PlayerEntity)
        ) {
            PlayerEntity player = (PlayerEntity) miner;
            if (ModHelper.IsPlayerCreative(player)) {
                cir.setReturnValue(true);
            }
        }
    }
}
