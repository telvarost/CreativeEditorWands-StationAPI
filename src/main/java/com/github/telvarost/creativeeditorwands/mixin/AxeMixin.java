package com.github.telvarost.creativeeditorwands.mixin;

import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.Hatchet;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Hatchet.class)
public class AxeMixin extends ToolBase {
    /** - Copy/paste with wooden axe */
    @Shadow
    private static BlockBase[] effectiveBlocks;
    public AxeMixin(int i, ToolMaterial arg) {
        super(i, 1, arg, effectiveBlocks);
    }
}
