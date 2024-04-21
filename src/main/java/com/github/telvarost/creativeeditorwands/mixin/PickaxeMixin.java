package com.github.telvarost.creativeeditorwands.mixin;

import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.Pickaxe;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Pickaxe.class)
public class PickaxeMixin extends ToolBase {
    /** - Change paint/erase color with wooden pickaxe */
    @Shadow
    private static BlockBase[] effectiveBlocks;
    public PickaxeMixin(int i, ToolMaterial arg) {
        super(i, 1, arg, effectiveBlocks);
    }
}
