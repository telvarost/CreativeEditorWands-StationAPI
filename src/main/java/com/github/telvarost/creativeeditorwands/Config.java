package com.github.telvarost.creativeeditorwands;

import blue.endless.jankson.Comment;
import net.glasslauncher.mods.api.gcapi.api.*;

public class Config {

    @GConfig(value = "config", visibleName = "CreativeEditorWands")
    public static ConfigFields config = new ConfigFields();

    public static class ConfigFields {

        @ConfigName("Activate Editing Tool Tooltips On Server")
        @Comment("Tools cannot be used if player is not OP")
        public Boolean activateEditingToolTooltipsOnServer = false;

        @ConfigName("Disable All Editing Tools")
        @Comment("Use this to disable tools in MP or SP")
        @MultiplayerSynced
        @ValueOnVanillaServer(booleanValue = TriBoolean.FALSE)
        public Boolean disableAllEditingTools = false;

        @ConfigName("Enable Toggling Editing Tools With Bedrock")
        @Comment("Single-player only")
        public Boolean enableTogglingEditingToolsWithBedrock = true;

//        @ConfigName("Use Vanilla Item Editing Tools")
//        @MultiplayerSynced
//        @ValueOnVanillaServer(booleanValue = TriBoolean.FALSE)
//        public Boolean useVanillaItemEditingTools = true;
    }
}
