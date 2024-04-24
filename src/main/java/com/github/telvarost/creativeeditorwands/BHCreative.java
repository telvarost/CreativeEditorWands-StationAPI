package com.github.telvarost.creativeeditorwands;

import net.minecraft.entity.player.PlayerBase;
import paulevs.bhcreative.interfaces.CreativePlayer;

public class BHCreative {
    public static boolean get(PlayerBase player) {
        return ((CreativePlayer) (player)).creative_isCreative();
    }
}
