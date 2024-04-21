package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.Config;
import com.github.telvarost.creativeeditorwands.items.Fish;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.food.FoodBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FoodBase.class)
public abstract class FoodBaseMixin extends ItemBase implements CustomTooltipProvider {

    @Shadow private int healAmount;

    public FoodBaseMixin(int i, int j, boolean bl) {
        super(i);
    }

    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    public void creativeEditorWands_useFood(ItemInstance itemInstance, Level arg2, PlayerBase arg3, CallbackInfoReturnable<ItemInstance> cir) {
        if (  (Config.config.enableRandomFishSizes)
           && (  (creativeEditorWands_isRawFish(itemInstance.itemId))
              || (creativeEditorWands_isCookedFish(itemInstance.itemId))
              )
        ) {
            --itemInstance.count;
            int fishSize = (0 != itemInstance.getDamage()) ? itemInstance.getDamage() : 250;
            double healingDivisor = (creativeEditorWands_isRawFish(itemInstance.itemId)) ? 100.0 : 50.0;
            int healingAmount = (int)Math.floor(fishSize / healingDivisor);
            arg3.addHealth(healingAmount);
            cir.setReturnValue(itemInstance);
        }
    }

    @Override
    public String[] getTooltip(ItemInstance itemInstance, String originalTooltip) {
        if (  (Config.config.enableRandomFishSizes)
           && (  (creativeEditorWands_isRawFish(itemInstance.itemId))
              || (creativeEditorWands_isCookedFish(itemInstance.itemId))
              )
        ) {
            int fishSize = (0 != itemInstance.getDamage()) ? itemInstance.getDamage() : 250;

            if (creativeEditorWands_isRawFish(itemInstance.itemId)) {
                if (990 <= fishSize) {
                    if (1090 <= fishSize) {
                        originalTooltip = "§bLegendary Fish";
                    } else {
                        originalTooltip = "§6Rare Fish";
                    }
                } else if (690 <= fishSize && fishSize <= 700) {
                    originalTooltip = "§eLucky Fish";
                }
            } else {
                if (990 <= fishSize) {
                    if (1090 <= fishSize) {
                        originalTooltip = "§bCooked Legendary Fish";
                    } else {
                        originalTooltip = "§6Cooked Rare Fish";
                    }
                } else if (690 <= fishSize && fishSize <= 700) {
                    originalTooltip = "§eCooked Lucky Fish";
                }
            }

            if (Config.config.enableFishHealingTooltip) {
                double healingDivisor = (creativeEditorWands_isRawFish(itemInstance.itemId)) ? 100.0 : 50.0;
                double healingAmount = (Math.floor(fishSize / healingDivisor) / 2.0);
                return new String[]{originalTooltip, "§4" + "Heals " + healingAmount, "§7" + (fishSize / 10.0) + " cm"};
            } else {
                return new String[]{originalTooltip, "§7" + (fishSize / 10.0) + " cm"};
            }
        } else {
            if (  (creativeEditorWands_isRawFish(itemInstance.itemId))
               || (creativeEditorWands_isCookedFish(itemInstance.itemId))
               )
            {
                if (Config.config.enableFishHealingTooltip) {
                    return new String[]{originalTooltip, "§4" + "Heals " + (this.healAmount / 2.0)};
                } else {
                    return new String[]{originalTooltip};
                }
            } else {
                if (Config.config.enableFoodHealingTooltips) {
                    return new String[]{originalTooltip, "§4" + "Heals " + (this.healAmount / 2.0)};
                } else {
                    return new String[]{originalTooltip};
                }
            }
        }
    }

    @Unique
    private boolean creativeEditorWands_isRawFish(int itemId) {
        return (  (ItemBase.rawFish.id     == itemId)
               || (Fish.raw_common_fish.id  == itemId)
               || (Fish.raw_river_fish.id == itemId)
               || (Fish.raw_sea_fish.id == itemId)
               || (Fish.raw_ocean_fish.id  == itemId)
               );
    }

    @Unique
    private boolean creativeEditorWands_isCookedFish(int itemId) {
        return (  (ItemBase.cookedFish.id     == itemId)
                || (Fish.cooked_common_fish.id  == itemId)
                || (Fish.cooked_river_fish.id == itemId)
                || (Fish.cooked_sea_fish.id == itemId)
                || (Fish.cooked_ocean_fish.id  == itemId)
        );
    }
}
