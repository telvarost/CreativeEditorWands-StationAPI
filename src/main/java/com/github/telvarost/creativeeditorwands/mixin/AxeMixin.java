package com.github.telvarost.creativeeditorwands.mixin;

import net.minecraft.item.ItemBase;
import net.minecraft.item.tool.Hatchet;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.StationSwordItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Hatchet.class)
public class AxeMixin extends ItemBase implements StationSwordItem {
    /** - Copy/paste with wooden axe */
    public AxeMixin(int i, ToolMaterial arg) {
        super(i);
    }
}
