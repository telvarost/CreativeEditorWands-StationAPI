package com.github.telvarost.creativeeditorwands.mixin;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(World.class)
public interface WorldAccessor {
    @Invoker("blockUpdate")
    void invokeBlockUpdate(int x, int y, int z, int blockId);
}
