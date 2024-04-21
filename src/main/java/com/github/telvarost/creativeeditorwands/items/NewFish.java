// package com.github.telvarost.creativeeditorwands.items;

// import com.github.telvarost.creativeeditorwands.Config;
// import net.minecraft.item.ItemBase;
// import net.minecraft.item.ItemInstance;
// import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
// import net.modificationstation.stationapi.api.template.item.TemplateFoodItem;
// import net.modificationstation.stationapi.api.util.Identifier;

// public class NewFish extends TemplateFoodItem implements CustomTooltipProvider {

//     public NewFish(Identifier identifier, int healAmount, boolean isWolfFood) {
//         super(identifier, healAmount, isWolfFood);
//     }

//     @Override
//     public String[] getTooltip(ItemInstance itemInstance, String originalTooltip) {
//         if (Config.config.enableRandomFishSizes) {
//             int fishSize = (0 != itemInstance.getDamage()) ? itemInstance.getDamage() : 250;
//             if (Config.config.enableFishHealingTooltip) {
//                 double healingDivisor = (creativeEditorWands_isRawFish(itemInstance.itemId)) ? 100.0 : 50.0;
//                 double healingAmount = (Math.floor(fishSize / healingDivisor) / 2.0);
//                 return new String[]{originalTooltip, "§4" + "Heals " + healingAmount, "§7" + (fishSize / 10.0) + " cm"};
//             } else {
//                 return new String[]{originalTooltip, "§7" + (fishSize / 10.0) + " cm"};
//             }
//         } else {
//             if (Config.config.enableFishHealingTooltip) {
//                 return new String[]{originalTooltip, "§4" + "Heals " + (this.getHealAmount() / 2.0)};
//             } else {
//                 return new String[]{originalTooltip};
//             }
//         }
//     }

//     private boolean creativeEditorWands_isRawFish(int itemId) {
//         return (  (ItemBase.rawFish.id     == itemId)
//                || (Fish.raw_common_fish.id  == itemId)
//                || (Fish.raw_river_fish.id == itemId)
//                || (Fish.raw_sea_fish.id == itemId)
//                || (Fish.raw_ocean_fish.id  == itemId)
//         );
//     }
// }
