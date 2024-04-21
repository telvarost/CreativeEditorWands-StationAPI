package com.github.telvarost.creativeeditorwands;

import blue.endless.jankson.Comment;
import net.glasslauncher.mods.api.gcapi.api.*;

public class Config {

    @GConfig(value = "config", visibleName = "CreativeEditorWands")
    public static ConfigFields config = new ConfigFields();

    public static class ConfigFields {

        @ConfigName("Allow Catching Random Fish Sizes")
        @MultiplayerSynced
        public Boolean enableRandomFishSizes = true;

        @ConfigName("Calculate Water Surface Size")
        @Comment("Adjusts fish size/type based on area")
        @MultiplayerSynced
        public Boolean calculateWaterSurfaceSize = true;

        @ConfigName("Display Fish Healing Tooltip")
        public Boolean enableFishHealingTooltip = false;

        @ConfigName("Display Food Healing Tooltips")
        public Boolean enableFoodHealingTooltips = false;

        @ConfigName("Enable Non-vanilla Fish")
        @Comment("Mod should not be removed if set to true")
        @MultiplayerSynced
        @ValueOnVanillaServer(booleanValue = TriBoolean.FALSE)
        public Boolean enableNonVanillaFish = false;

        @ConfigName("Enable Oceanic Fish")
        @Comment("Increases the max fish size from 70 to 110")
        @MultiplayerSynced
        public Boolean enableBiggerFish = false;
    }
}
