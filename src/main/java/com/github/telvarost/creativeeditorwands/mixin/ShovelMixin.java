package com.github.telvarost.creativeeditorwands.mixin;

import net.minecraft.item.ItemBase;
import net.minecraft.item.tool.Shovel;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.StationSwordItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Shovel.class)
public class ShovelMixin extends ItemBase implements StationSwordItem {
    /** - Erase with wooden shovel */
    public ShovelMixin(int i, ToolMaterial arg) {
        super(i);
    }
}
