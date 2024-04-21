package com.github.telvarost.creativeeditorwands.events.init;

import com.github.telvarost.dispensertweaks.Config;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;
import net.modificationstation.stationapi.api.recipe.FuelRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

public class RecipeListener {

    @EventListener
    public void registerRecipes(RecipeRegisterEvent event) {
        Identifier type = event.recipeId;

        if (type == RecipeRegisterEvent.Vanilla.SMELTING.type()) {
            if (Config.ConfigFields.enableLavaBlockSmeltingRecipe) {
                /** - 1000 second fuel duration */
                FuelRegistry.addFuelItem(BlockBase.FLOWING_LAVA.asItem(), 20000);
            }
        }

        if (type == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPELESS.type())
        {
            if (Config.ConfigFields.moddedDispenserFluidPlacement)
            {
                CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.FLOWING_WATER, 1), ItemBase.waterBucket);
                CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.FLOWING_LAVA, 1), ItemBase.lavaBucket);
            }
        }
    }
}