// package com.github.telvarost.creativeeditorwands.events.init;

// import com.github.telvarost.creativeeditorwands.items.Fish;
// import net.mine_diver.unsafeevents.listener.EventListener;
// import net.minecraft.item.ItemInstance;
// import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
// import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;
// import net.modificationstation.stationapi.api.util.Identifier;

// public class RecipeListener {

//     @EventListener
//     public void registerRecipes(RecipeRegisterEvent event) {
//         Identifier type = event.recipeId;

//         if (type == RecipeRegisterEvent.Vanilla.SMELTING.type()) {
//             SmeltingRegistry.addSmeltingRecipe(Fish.raw_common_fish.id, new ItemInstance(Fish.cooked_common_fish));
//             SmeltingRegistry.addSmeltingRecipe(Fish.raw_river_fish.id, new ItemInstance(Fish.cooked_river_fish));
//             SmeltingRegistry.addSmeltingRecipe(Fish.raw_sea_fish.id, new ItemInstance(Fish.cooked_sea_fish));
//             SmeltingRegistry.addSmeltingRecipe(Fish.raw_ocean_fish.id, new ItemInstance(Fish.cooked_ocean_fish));
//         }
//     }
// }