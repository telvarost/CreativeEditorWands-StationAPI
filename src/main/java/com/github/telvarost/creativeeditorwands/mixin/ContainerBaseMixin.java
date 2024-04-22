package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.container.slot.Slot;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerBase.class)
public abstract class ContainerBaseMixin extends ScreenBase {
    @Shadow
    protected abstract Slot getSlot(int x, int y);

    @Unique
    private Slot slot = null;

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    protected void creativeEditorWands_mouseClicked(int mouseX, int mouseY, int button, CallbackInfo ci) {

//        /** - Check if client is on a server */
//        boolean isClientOnServer = minecraft.level.isServerSide;
//
//        /** - Right-click */
//        if (button == 1) {
//        }

        /** - Left-click */
        if (button == 0) {
            if (ModHelper.ModHelperFields.enableWorldEditTools) {
                if (!minecraft.level.isServerSide) {
                    slot = this.getSlot(mouseX, mouseY);

                    /** - Do nothing if mouse is not over a slot */
                    if (slot == null)
                        return;

                    ItemInstance cursorStack = minecraft.player.inventory.getCursorItem();
                    ItemInstance slotItemToExamine = slot.getItem();

                    if (   (null != cursorStack)
                        && (null != slotItemToExamine)
                        &&  (  (cursorStack.itemId == ItemBase.woodPickaxe.id)
                            || (cursorStack.itemId == ItemBase.woodShovel.id)
                            || (cursorStack.itemId == ItemBase.woodSword.id)
                            )
                        &&  (  (slotItemToExamine.itemId == ItemBase.woodPickaxe.id)
                            || (slotItemToExamine.itemId == ItemBase.woodShovel.id)
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

    @Inject(method = "mouseReleased", at = @At("RETURN"), cancellable = true)
    private void creativeEditorWands_mouseReleasedOrSlotChanged(int mouseX, int mouseY, int button, CallbackInfo ci) {

        if (ModHelper.ModHelperFields.enableWorldEditTools) {
            if (!minecraft.level.isServerSide) {
                slot = this.getSlot(mouseX, mouseY);

                /** - Do nothing if mouse is not over a slot */
                if (slot == null)
                    return;

                int currentWheelDegrees = Mouse.getDWheel();
                if (  (0 != currentWheelDegrees)
                ) {
                    /** - Handle scroll wheel */
                    inventoryTweaks_handleScrollWheel(currentWheelDegrees);
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
                )
           )
        {
            float numberOfTurns = (float)wheelDegrees / 120.0f;
            if (false == Mouse.isButtonDown(2)) {
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
            } else {
                slotItemToExamine.count += numberOfTurns;
                if (slotItemToExamine.itemId != ItemBase.woodHoe.id) {
                    if (17 < slotItemToExamine.count) {
                        slotItemToExamine.count = 1;
                    }
                    if (1 > slotItemToExamine.count) {
                        slotItemToExamine.count = 17;
                    }
                } else {
                    if (3 < slotItemToExamine.count) {
                        slotItemToExamine.count = 1;
                    }
                    if (1 > slotItemToExamine.count) {
                        slotItemToExamine.count = 3;
                    }
                }
            }
        }
    }
}
