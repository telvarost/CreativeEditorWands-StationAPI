package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.item.tool.StationHoeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoeItem.class)
public class HoeMixin extends Item implements StationHoeItem, CustomTooltipProvider {
    /** - Change paint/erase brush type with wooden hoe */
    public HoeMixin(int i, ToolMaterial arg) {
        super(i);
    }

    @Override
    public boolean postHit(ItemStack item, LivingEntity arg2, LivingEntity arg3) {
        if (  (this.id == Item.WOODEN_HOE.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
           && (arg3 instanceof  PlayerEntity)
           && (ModHelper.IsPlayerCreative((PlayerEntity) arg3))
        ) {
            int curCount = item.count;
            item.damage(1, null);
            item.count = curCount;
            ModHelper.ModHelperFields.brushSize = item.getDamage();
            ModHelper.ModHelperFields.brushType = item.count;
        }
        return false;
    }

    @Override
    public String[] getTooltip(ItemStack itemInstance, String originalTooltip) {
        if (  (this.id == Item.WOODEN_HOE.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
        ) {
            PlayerEntity player = PlayerHelper.getPlayerFromGame();
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
    public ItemStack use(ItemStack item, World arg2, PlayerEntity player) {
        if (  (this.id == Item.WOODEN_HOE.id)
           && (ModHelper.ModHelperFields.enableWorldEditTools)
           && (ModHelper.IsPlayerCreative(player))
        ) {
            ModHelper.ModHelperFields.brushSize = item.getDamage();
            ModHelper.ModHelperFields.brushType = item.count;

            String brushType = "Point";

            switch (ModHelper.ModHelperFields.brushType) {
                case 1:
                    brushType = "Square";
                    break;

                case 2:
                    brushType = "Cube";
                    break;
            }

            ModHelper.setTooltip( brushType
                                + " Brush of Size "
                                + ModHelper.ModHelperFields.brushSize
                                + " Set!"
                                , 40);
        }
        return item;
    }

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void useOnTile(ItemStack item, PlayerEntity player, World arg3, int i, int j, int k, int l, CallbackInfoReturnable<Boolean> cir) {
        if (  (this.id == Item.WOODEN_HOE.id)
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
