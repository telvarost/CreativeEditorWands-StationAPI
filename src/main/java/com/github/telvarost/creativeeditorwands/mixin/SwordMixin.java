package com.github.telvarost.creativeeditorwands.mixin;

import net.minecraft.item.ItemBase;
import net.minecraft.item.tool.Sword;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.StationSwordItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Sword.class)
public class SwordMixin extends ItemBase implements StationSwordItem {
    /** - Paint with wooden sword */
    public SwordMixin(int i, ToolMaterial arg) {
        super(i);
    }
}
