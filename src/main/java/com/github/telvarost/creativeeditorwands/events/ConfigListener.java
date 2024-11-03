package com.github.telvarost.creativeeditorwands.events;

import com.github.telvarost.creativeeditorwands.ModHelper;
import net.glasslauncher.mods.gcapi3.api.PreConfigSavedListener;
import net.glasslauncher.mods.gcapi3.impl.GlassYamlFile;
import net.mine_diver.unsafeevents.listener.EventListener;

@EventListener
public class ConfigListener implements PreConfigSavedListener {

    @Override
    public void onPreConfigSaved(int source, GlassYamlFile oldValues, GlassYamlFile newValues)
    {
        /** - Update max durability of editing tools */
        ModHelper.AttemptToSetEditingToolProperties();
    }
}
