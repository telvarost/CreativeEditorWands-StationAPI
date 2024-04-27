package com.github.telvarost.creativeeditorwands.mixin.client;

import com.github.telvarost.creativeeditorwands.ModHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.container.slot.Slot;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ContainerBase.class)
public abstract class ContainerBaseMixin extends ScreenBase {
    @Shadow
    protected abstract Slot getSlot(int x, int y);

    @Unique
    private Slot slot = null;

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    protected void creativeEditorWands_mouseClicked(int mouseX, int mouseY, int button, CallbackInfo ci) {

        /** - Left-click */
        if (button == 0) {
            if (ModHelper.ModHelperFields.enableWorldEditTools) {
                PlayerBase player = PlayerHelper.getPlayerFromGame();
                if (  (ModHelper.IsPlayerCreative(player))
                ) {
                    if (!minecraft.level.isServerSide) {
                        slot = this.getSlot(mouseX, mouseY);

                        /** - Do nothing if mouse is not over a slot */
                        if (slot == null)
                            return;

                        ItemInstance cursorStack = minecraft.player.inventory.getCursorItem();
                        ItemInstance slotItemToExamine = slot.getItem();

                        if (   (null != cursorStack)
                            && (null != slotItemToExamine)
                            && (   (cursorStack.itemId == ItemBase.woodPickaxe.id)
                                || (cursorStack.itemId == ItemBase.woodShovel.id)
                                || (cursorStack.itemId == ItemBase.woodSword.id)
                               )
                            && (   (slotItemToExamine.itemId == ItemBase.woodShovel.id)
                                || (slotItemToExamine.itemId == ItemBase.woodSword.id)
                               )
                        ) {
                            slotItemToExamine.setDamage(cursorStack.getDamage());
                            slotItemToExamine.count = cursorStack.count;
                            super.mouseClicked(mouseX, mouseY, button);
                            ci.cancel();
                            return;
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "mouseReleased", at = @At("RETURN"))
    private void creativeEditorWands_mouseReleasedOrSlotChanged(int mouseX, int mouseY, int button, CallbackInfo ci) {

        if (ModHelper.ModHelperFields.enableWorldEditTools) {
            if (!minecraft.level.isServerSide) {
                slot = this.getSlot(mouseX, mouseY);

                /** - Do nothing if mouse is not over a slot */
                if (slot == null)
                    return;

                int currentWheelDegrees = Mouse.getDWheel();
                if (0 != currentWheelDegrees) {
                    PlayerBase player = PlayerHelper.getPlayerFromGame();
                    if (  (null != player)
                       && (ModHelper.IsPlayerCreative(player))
                    ) {
                        /** - Handle scroll wheel */
                        creativeEditorWands_handleScrollWheel(currentWheelDegrees);
                    }
                }
            }
        }
    }

    @Unique private void creativeEditorWands_handleScrollWheel(int wheelDegrees) {
        ItemInstance cursorStack = minecraft.player.inventory.getCursorItem();
        ItemInstance slotItemToExamine = slot.getItem();

        if (  (null == cursorStack)
           && (null != slotItemToExamine)
           &&   (  (slotItemToExamine.itemId == ItemBase.woodPickaxe.id)
                || (slotItemToExamine.itemId == ItemBase.woodShovel.id)
                || (slotItemToExamine.itemId == ItemBase.woodSword.id)
                || (slotItemToExamine.itemId == ItemBase.woodHoe.id)
                || (slotItemToExamine.itemId == ItemBase.woodAxe.id)
                )
           )
        {
            float numberOfTurns = (float)wheelDegrees / 120.0f;
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                if (slotItemToExamine.itemId == ItemBase.woodAxe.id) {
                    int damageValue = slotItemToExamine.getDamage();
                    if (0 < damageValue && (ModHelper.SELECTION_TOOL_DURABILITY - 2) >= damageValue) {
                        int curCount = slotItemToExamine.count;
                        slotItemToExamine.applyDamage((int) numberOfTurns, null);
                        slotItemToExamine.count = curCount;
                        if ((ModHelper.SELECTION_TOOL_DURABILITY - 2) < slotItemToExamine.getDamage()) {
                            slotItemToExamine.setDamage(1);
                        }
                        if (1 > slotItemToExamine.getDamage()) {
                            slotItemToExamine.setDamage((ModHelper.SELECTION_TOOL_DURABILITY - 2));
                        }
                    }
                } else {
                    int curCount = slotItemToExamine.count;
                    slotItemToExamine.applyDamage((int) numberOfTurns, null);
                    slotItemToExamine.count = curCount;
                    if (0 > slotItemToExamine.getDamage()) {
                        if (slotItemToExamine.itemId != ItemBase.woodHoe.id) {
                            slotItemToExamine.setDamage(ModHelper.BLOCK_ID_DURABILITY);
                        } else {
                            slotItemToExamine.setDamage(ModHelper.BRUSH_SIZE_DURABILITY);
                        }
                    }
                }
            } else {
                slotItemToExamine.count += numberOfTurns;
                if (slotItemToExamine.itemId == ItemBase.woodAxe.id) {
                    if (3 < slotItemToExamine.count) {
                        slotItemToExamine.count = 1;
                    }
                    if (1 > slotItemToExamine.count) {
                        slotItemToExamine.count = 3;
                    }
                } else if (slotItemToExamine.itemId == ItemBase.woodHoe.id) {
                    if (3 < slotItemToExamine.count) {
                        slotItemToExamine.count = 1;
                    }
                    if (1 > slotItemToExamine.count) {
                        slotItemToExamine.count = 3;
                    }
                } else {
                    if (16 < slotItemToExamine.count) {
                        slotItemToExamine.count = 1;
                    }
                    if (1 > slotItemToExamine.count) {
                        slotItemToExamine.count = 16;
                    }
                }
            }
        }
    }
}
