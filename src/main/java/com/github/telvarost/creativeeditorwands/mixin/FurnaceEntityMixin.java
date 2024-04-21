package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.Config;
import com.github.telvarost.creativeeditorwands.ModHelper;
import com.github.telvarost.creativeeditorwands.items.Fish;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemBase;
import net.minecraft.tileentity.TileEntityBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.item.ItemInstance;

@Mixin(TileEntityFurnace.class)
public abstract class FurnaceEntityMixin extends TileEntityBase implements InventoryBase {

    @Shadow
    private ItemInstance[] inventory;

    @Shadow protected abstract boolean canAcceptRecipeOutput();

    @Inject(method = "craftRecipe", at = @At(value = "HEAD"), cancellable = true)
    public void creativeEditorWands_craftRecipe(CallbackInfo ci) {
        if (this.canAcceptRecipeOutput()) {
            if (Config.config.enableRandomFishSizes) {
                if (this.inventory[0] != null) {
                    if (this.inventory[0].itemId == ItemBase.rawFish.id) {
                        ModHelper.ModHelperFields.COOKED_RAW_FISH_SIZE = this.inventory[0].getDamage();
                        ModHelper.ModHelperFields.COOKED_RAW_FISH_TYPE = -1;
                        ModHelper.ModHelperFields.IS_RAW_FISH_CONSUMED = true;
                    } else if (this.inventory[0].itemId == Fish.raw_common_fish.id) {
                        ModHelper.ModHelperFields.COOKED_RAW_FISH_SIZE = this.inventory[0].getDamage();
                        ModHelper.ModHelperFields.COOKED_RAW_FISH_TYPE = 0;
                        ModHelper.ModHelperFields.IS_RAW_FISH_CONSUMED = true;
                    } else if (this.inventory[0].itemId == Fish.raw_river_fish.id) {
                        ModHelper.ModHelperFields.COOKED_RAW_FISH_SIZE = this.inventory[0].getDamage();
                        ModHelper.ModHelperFields.COOKED_RAW_FISH_TYPE = 1;
                        ModHelper.ModHelperFields.IS_RAW_FISH_CONSUMED = true;
                    } else if (this.inventory[0].itemId == Fish.raw_sea_fish.id) {
                        ModHelper.ModHelperFields.COOKED_RAW_FISH_SIZE = this.inventory[0].getDamage();
                        ModHelper.ModHelperFields.COOKED_RAW_FISH_TYPE = 2;
                        ModHelper.ModHelperFields.IS_RAW_FISH_CONSUMED = true;
                    } else if (this.inventory[0].itemId == Fish.raw_ocean_fish.id) {
                        ModHelper.ModHelperFields.COOKED_RAW_FISH_SIZE = this.inventory[0].getDamage();
                        ModHelper.ModHelperFields.COOKED_RAW_FISH_TYPE = 3;
                        ModHelper.ModHelperFields.IS_RAW_FISH_CONSUMED = true;
                    }
                }
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "RETURN"))
    public void creativeEditorWands_tickConsumeFishReturnCookedFish(CallbackInfo ci) {
        if(ModHelper.ModHelperFields.IS_RAW_FISH_CONSUMED) {
            switch (ModHelper.ModHelperFields.COOKED_RAW_FISH_TYPE)
            {
                default:
                case -1:
                    this.inventory[2] = new ItemInstance(ItemBase.cookedFish);
                    break;

                case 0:
                    this.inventory[2] = new ItemInstance(Fish.cooked_common_fish);
                    break;

                case 1:
                    this.inventory[2] = new ItemInstance(Fish.cooked_river_fish);
                    break;

                case 2:
                    this.inventory[2] = new ItemInstance(Fish.cooked_sea_fish);
                    break;

                case 3:
                    this.inventory[2] = new ItemInstance(Fish.cooked_ocean_fish);
                    break;
            }
            this.inventory[2].setDamage(ModHelper.ModHelperFields.COOKED_RAW_FISH_SIZE);
            ModHelper.ModHelperFields.IS_RAW_FISH_CONSUMED = false;
        }
    }
}
