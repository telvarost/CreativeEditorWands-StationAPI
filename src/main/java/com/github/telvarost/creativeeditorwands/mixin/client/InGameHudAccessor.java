package com.github.telvarost.creativeeditorwands.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public interface InGameHudAccessor {
    @Accessor("overlayRemaining")
    void setOverlayRemaining(int value);

    @Accessor("overlayMessage")
    void setOverlayMessage(String value);
}
