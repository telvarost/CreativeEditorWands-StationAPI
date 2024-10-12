package com.github.telvarost.creativeeditorwands;

import net.minecraft.entity.player.PlayerEntity;
import paulevs.bhcreative.interfaces.CreativePlayer;

public class BHCreative {
    public static boolean get(PlayerEntity player) {
        return ((CreativePlayer) (player)).creative_isCreative();
    }
}
