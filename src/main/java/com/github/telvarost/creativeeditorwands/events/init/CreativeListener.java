// package com.github.telvarost.creativeeditorwands.events.init;

// import com.github.telvarost.creativeeditorwands.items.Fish;
// import net.mine_diver.unsafeevents.listener.EventListener;
// import net.minecraft.item.ItemBase;
// import net.minecraft.item.ItemInstance;
// import paulevs.bhcreative.api.CreativeTab;
// import paulevs.bhcreative.api.SimpleTab;
// import paulevs.bhcreative.registry.TabRegistryEvent;

// public class CreativeListener {
//     public static CreativeTab tabCreativeEditorWandsFish;

//     @EventListener
//     public void onTabInit(TabRegistryEvent event){
//         tabCreativeEditorWandsFish = new SimpleTab(Fish.NAMESPACE.id("raw_common_fish"), Fish.raw_common_fish);
//         event.register(tabCreativeEditorWandsFish);
//         for (ItemBase item : Fish.items){
//             tabCreativeEditorWandsFish.addItem(new ItemInstance(item, 1));
//         }
//     }
// }