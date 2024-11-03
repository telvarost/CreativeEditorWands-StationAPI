package com.github.telvarost.creativeeditorwands;

import net.glasslauncher.mods.gcapi3.api.*;

public class Config {

    @ConfigRoot(value = "config", visibleName = "CreativeEditorWands")
    public static ConfigFields config = new ConfigFields();

    public static class ConfigFields {

        @ConfigEntry(
                name = "Activate Editing Tool Tooltips On Server",
                description = "Tools cannot be used if player is not OP"
        )
        public Boolean activateEditingToolTooltipsOnServer = false;

        @ConfigEntry(
                name = "Disable All Editing Tools",
                description = "Use this to disable tools in MP or SP",
                multiplayerSynced = true
        )
        @ValueOnVanillaServer(booleanValue = TriBoolean.FALSE)
        public Boolean disableAllEditingTools = false;

        @ConfigEntry(
                name = "Enable Toggling Editing Tools With Bedrock",
                description = "Single-player only"
        )
        public Boolean enableTogglingEditingToolsWithBedrock = true;
    }
}
