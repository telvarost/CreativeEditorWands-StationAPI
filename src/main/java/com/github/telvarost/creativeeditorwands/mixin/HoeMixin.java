package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.Hoe;
import net.minecraft.item.tool.ToolMaterial;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.item.tool.StationHoeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Hoe.class)
public class HoeMixin extends ItemBase implements StationHoeItem, CustomTooltipProvider {
    /** - Change paint/erase brush type with wooden hoe */
    public HoeMixin(int i, ToolMaterial arg) {
        super(i);
    }

    @Override
    public float getStrengthOnBlock(ItemInstance item, BlockBase arg2) {
        if (  (this.id == ItemBase.woodHoe.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            int curCount = item.count;
            item.applyDamage(1, null);
            item.count = curCount;
            ModHelper.ModHelperFields.brushSize = item.getDamage();
            ModHelper.ModHelperFields.brushType = item.count;
        }
        return 1.0F;
    }

    @Override
    public String[] getTooltip(ItemInstance itemInstance, String originalTooltip) {
        if (  (this.id == ItemBase.woodHoe.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            return new String[]{"Brush Mode", "Size: " + itemInstance.getDamage(), "Type: " + itemInstance.count};
        } else {
            return new String[]{originalTooltip};
        }
    }

    @Override
    public ItemInstance use(ItemInstance item, Level arg2, PlayerBase arg3) {
        if (  (this.id == ItemBase.woodHoe.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            ModHelper.ModHelperFields.brushSize = item.getDamage();
            ModHelper.ModHelperFields.brushType = item.count;
        }
        return item;
    }

    @Inject(method = "useOnTile", at = @At("HEAD"), cancellable = true)
    public void useOnTile(ItemInstance item, PlayerBase arg2, Level arg3, int i, int j, int k, int l, CallbackInfoReturnable<Boolean> cir) {
        if (  (this.id == ItemBase.woodHoe.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            if (3 <= item.count) {
                item.count = 1;
            } else {
                item.count++;
            }
            ModHelper.ModHelperFields.brushSize = item.getDamage();
            ModHelper.ModHelperFields.brushType = item.count;
            cir.setReturnValue(true);
        }
    }
}
