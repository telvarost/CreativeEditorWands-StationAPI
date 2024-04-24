package com.github.telvarost.creativeeditorwands.events.init;

import com.github.telvarost.creativeeditorwands.ModHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.registry.AfterBlockAndItemRegisterEvent;

public class RegistryListener {

    @EventListener
    public void handleAfterBlockAndItemRegisterEvent(AfterBlockAndItemRegisterEvent event)
    {
        ModHelper.ModHelperFields.blocksAndItemsRegistered = true;
        ModHelper.ModHelperFields.bhcreativeLoaded = FabricLoader.getInstance().isModLoaded("bhcreative");
        ModHelper.AttemptToSetEditingToolProperties();
    }
}
