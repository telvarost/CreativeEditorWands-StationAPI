package com.github.telvarost.creativeeditorwands.events;

import blue.endless.jankson.JsonObject;
import com.github.telvarost.creativeeditorwands.ModHelper;
import net.glasslauncher.mods.api.gcapi.api.PreConfigSavedListener;
import net.mine_diver.unsafeevents.listener.EventListener;

@EventListener
public class ConfigListener implements PreConfigSavedListener {

    @Override
    public void onPreConfigSaved(int var1, JsonObject jsonObject, JsonObject jsonObject1) {
        /** - Update max durability of editing tools */
        ModHelper.AttemptToSetEditingToolProperties();
    }
}
