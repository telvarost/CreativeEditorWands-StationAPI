package com.github.telvarost.creativeeditorwands;

import net.minecraft.item.ItemBase;

public class ModHelper {
    public static final int WOODEN_TOOL_DURABILITY = 59;
    public static final int SELECTION_TOOL_DURABILITY = 64;
    public static final int BRUSH_SIZE_DURABILITY = 100;
    public static final int BLOCK_ID_DURABILITY = 96;

    public static void AttemptToSetEditingToolProperties()
    {
        if (ModHelper.ModHelperFields.blocksAndItemsRegistered) {
            if (ModHelper.ModHelperFields.enableWorldEditTools) {

                if (SELECTION_TOOL_DURABILITY != ItemBase.woodAxe.getDurability()) {
                    ItemBase.woodAxe.setDurability(SELECTION_TOOL_DURABILITY);
                }

                if (BRUSH_SIZE_DURABILITY != ItemBase.woodHoe.getDurability()) {
                    ItemBase.woodHoe.setDurability(BRUSH_SIZE_DURABILITY);
                }

                if (BLOCK_ID_DURABILITY != ItemBase.woodPickaxe.getDurability()) {
                    ItemBase.woodPickaxe.setDurability(BLOCK_ID_DURABILITY);
                }

                if (BLOCK_ID_DURABILITY != ItemBase.woodShovel.getDurability()) {
                    ItemBase.woodShovel.setDurability(BLOCK_ID_DURABILITY);
                }

                if (BLOCK_ID_DURABILITY != ItemBase.woodSword.getDurability()) {
                    ItemBase.woodSword.setDurability(BLOCK_ID_DURABILITY);
                }
            } else {

                if (WOODEN_TOOL_DURABILITY != ItemBase.woodAxe.getDurability()) {
                    ItemBase.woodAxe.setDurability(WOODEN_TOOL_DURABILITY);
                }

                if (WOODEN_TOOL_DURABILITY != ItemBase.woodHoe.getDurability()) {
                    ItemBase.woodHoe.setDurability(WOODEN_TOOL_DURABILITY);
                }

                if (WOODEN_TOOL_DURABILITY != ItemBase.woodPickaxe.getDurability()) {
                    ItemBase.woodPickaxe.setDurability(WOODEN_TOOL_DURABILITY);
                }

                if (WOODEN_TOOL_DURABILITY != ItemBase.woodShovel.getDurability()) {
                    ItemBase.woodShovel.setDurability(WOODEN_TOOL_DURABILITY);
                }

                if (WOODEN_TOOL_DURABILITY != ItemBase.woodSword.getDurability()) {
                    ItemBase.woodSword.setDurability(WOODEN_TOOL_DURABILITY);
                }
            }
        }
    }

    public static class ModHelperFields {

        public static Boolean blocksAndItemsRegistered = false;
        public static Boolean enableWorldEditTools = true;
        public static Integer brushSize = 1;
        public static Integer brushType = 1;
    }
}
