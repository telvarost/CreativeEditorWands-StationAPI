package com.github.telvarost.creativeeditorwands.mixin;

import net.minecraft.item.ItemBase;
import net.minecraft.item.tool.Hoe;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.StationHoeItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Hoe.class)
public class HoeMixin extends ItemBase implements StationHoeItem {
    /** - Change paint/erase brush type with wooden hoe */
    public HoeMixin(int i, ToolMaterial arg) {
        super(i);
    }
}
