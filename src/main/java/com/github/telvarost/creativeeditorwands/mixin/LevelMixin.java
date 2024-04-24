package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.Config;
import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class LevelMixin {

    @Shadow public boolean isServerSide;

    @Inject(
            method = "spawnEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/level/Level;method_219(Lnet/minecraft/entity/EntityBase;)V"
            )
    )
    public void spawnEntity(EntityBase arg, CallbackInfoReturnable<Boolean> cir) {
        if (  (!Config.config.disableAllEditingTools)
           && (arg instanceof PlayerBase)
        ) {
            try {
                ModHelper.getConnectionManager();
                ModHelper.ModHelperFields.enableWorldEditTools = true;
                ModHelper.AttemptToSetEditingToolProperties();
            } catch (Exception ex) {
                if (this.isServerSide) {
                    if (Config.config.activateEditingToolTooltipsOnServer) {
                        ModHelper.ModHelperFields.enableWorldEditTools = true;
                    } else {
                        ModHelper.ModHelperFields.enableWorldEditTools = false;
                    }
                }
            }
        }
    }
}
