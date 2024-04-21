package com.github.telvarost.creativeeditorwands.mixin;

import net.minecraft.item.ItemBase;
import net.minecraft.item.tool.Pickaxe;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.StationSwordItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Pickaxe.class)
public class PickaxeMixin extends ItemBase implements StationSwordItem {
    /** - Change paint/erase color with wooden pickaxe */
    public PickaxeMixin(int i, ToolMaterial arg) {
        super(i);
    }
}
