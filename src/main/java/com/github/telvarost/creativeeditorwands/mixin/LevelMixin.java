package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.Config;
import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class LevelMixin {

    @Shadow public boolean isRemote;

    @Inject(
            method = "spawnEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;notifyEntityAdded(Lnet/minecraft/entity/Entity;)V"
            )
    )
    public void spawnEntity(Entity arg, CallbackInfoReturnable<Boolean> cir) {
        if (  (!Config.config.disableAllEditingTools)
           && (arg instanceof PlayerEntity)
        ) {
            if (  (null == PlayerHelper.getPlayerFromGame())
               && (!this.isRemote)
            ) {
                try {
                    ModHelper.getConnectionManager();
                    ModHelper.ModHelperFields.enableWorldEditTools = true;
                    ModHelper.AttemptToSetEditingToolProperties();
                } catch (Exception ex) {
                }
            } else if (this.isRemote) {
                if (Config.config.activateEditingToolTooltipsOnServer) {
                    ModHelper.ModHelperFields.enableWorldEditTools = true;
                } else {
                    ModHelper.ModHelperFields.enableWorldEditTools = false;
                }
            } else {
                ModHelper.ModHelperFields.enableWorldEditTools = false;
            }
        }
    }
}
