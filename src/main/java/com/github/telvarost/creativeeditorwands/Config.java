package com.github.telvarost.creativeeditorwands;

import blue.endless.jankson.Comment;
import net.glasslauncher.mods.api.gcapi.api.*;

public class Config {

    @GConfig(value = "config", visibleName = "CreativeEditorWands")
    public static ConfigFields config = new ConfigFields();

    public static class ConfigFields {

        @ConfigName("Enable Toggling Editing Tools With Bone")
        @MultiplayerSynced
        @ValueOnVanillaServer(booleanValue = TriBoolean.FALSE)
        public Boolean enableTogglingEditingToolsWithBone = true;

        @ConfigName("Disable All Editing Tools")
        @MultiplayerSynced
        @ValueOnVanillaServer(booleanValue = TriBoolean.FALSE)
        public Boolean disableAllEditingTools = false;

        @ConfigName("Use Vanilla Item Editing Tools")
        @MultiplayerSynced
        @ValueOnVanillaServer(booleanValue = TriBoolean.FALSE)
        public Boolean useVanillaItemEditingTools = true;
    }
}
