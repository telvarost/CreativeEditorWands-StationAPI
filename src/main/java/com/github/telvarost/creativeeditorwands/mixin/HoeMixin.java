package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.Hoe;
import net.minecraft.item.tool.ToolMaterial;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
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
    public boolean postHit(ItemInstance item, Living arg2, Living arg3) {
        if (  (this.id == ItemBase.woodHoe.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
           && (arg3 instanceof  PlayerBase)
           && (ModHelper.IsPlayerCreative((PlayerBase) arg3))
        ) {
            int curCount = item.count;
            item.applyDamage(1, null);
            item.count = curCount;
            ModHelper.ModHelperFields.brushSize = item.getDamage();
            ModHelper.ModHelperFields.brushType = item.count;
        }
        return false;
    }

    @Override
    public String[] getTooltip(ItemInstance itemInstance, String originalTooltip) {
        if (  (this.id == ItemBase.woodHoe.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            PlayerBase player = PlayerHelper.getPlayerFromGame();
            if (  (ModHelper.IsPlayerCreative(player))
            ) {
                String brushType = "Point";

                switch (itemInstance.count) {
                    case 1:
                        brushType = "Square";
                        break;

                    case 2:
                        brushType = "Cube";
                        break;
                }

                return new String[]{"§b" + "Brushes Mode", "Size: " + itemInstance.getDamage(), "Type: " + brushType};
            } else {
                return new String[]{originalTooltip};
            }
        } else {
            return new String[]{originalTooltip};
        }
    }

    @Override
    public ItemInstance use(ItemInstance item, Level arg2, PlayerBase player) {
        if (  (this.id == ItemBase.woodHoe.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
           && (ModHelper.IsPlayerCreative(player))
        ) {
            ModHelper.ModHelperFields.brushSize = item.getDamage();
            ModHelper.ModHelperFields.brushType = item.count;
        }
        return item;
    }

    @Inject(method = "useOnTile", at = @At("HEAD"), cancellable = true)
    public void useOnTile(ItemInstance item, PlayerBase player, Level arg3, int i, int j, int k, int l, CallbackInfoReturnable<Boolean> cir) {
        if (  (this.id == ItemBase.woodHoe.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
           && (ModHelper.IsPlayerCreative(player))
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
