package com.github.telvarost.creativeeditorwands.mixin;

import com.github.telvarost.creativeeditorwands.ModHelper;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.container.ContainerBase;
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

    @Shadow
    public net.minecraft.container.ContainerBase container;

    @Shadow
    protected abstract boolean isMouseOverSlot(Slot slot, int x, int Y);

    @Unique
    private Slot slot = null;

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    protected void inventoryTweaks_mouseClicked(int mouseX, int mouseY, int button, CallbackInfo ci) {

        /** - Check if client is on a server */
        boolean isClientOnServer = minecraft.level.isServerSide;

        /** - Right-click */
        if (button == 1) {
        }

        /** - Left-click */
        if (button == 0) {

            if (ModHelper.ModHelperFields.enableWorldEditTools) {
                /** - Handle if a button was clicked */
                /** - If items are matching */
//                super.mouseClicked(mouseX, mouseY, button);
//                ci.cancel();
//                return;
            }
        }
    }

    @Inject(method = "mouseReleased", at = @At("RETURN"), cancellable = true)
    private void inventoryTweaks_mouseReleasedOrSlotChanged(int mouseX, int mouseY, int button, CallbackInfo ci) {
        slot = this.getSlot(mouseX, mouseY);

        /** - Do nothing if mouse is not over a slot */
        if (slot == null)
            return;

        if (ModHelper.ModHelperFields.enableWorldEditTools) {
            if (!minecraft.level.isServerSide) {
                int currentWheelDegrees = Mouse.getDWheel();
                if (  (0 != currentWheelDegrees)
                ) {
                    /** - Handle scroll wheel */
                }
            }
        }
    }

    @Unique private void inventoryTweaks_handleScrollWheel(int wheelDegrees) {
        ItemInstance cursorStack = minecraft.player.inventory.getCursorItem();
        ItemInstance slotItemToExamine = slot.getItem();

        if (  (null != cursorStack)
           || (null != slotItemToExamine)
           )
        {
            //boolean isShiftKeyDown = (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT));
            boolean transferAllowed = true;
            float numberOfTurns = (float)wheelDegrees / 120.0f;
            int cursorStackAmount = 0;
            int slotStackAmount = 0;
            ItemInstance itemBeingTransfered = null;

            if (null != cursorStack) {
                itemBeingTransfered = cursorStack;
                cursorStackAmount = cursorStack.count;
            }

            if (null != slotItemToExamine) {
                itemBeingTransfered = slotItemToExamine;
                slotStackAmount = slotItemToExamine.count;
            }

            if (  (null != cursorStack)
               && (null != slotItemToExamine)
            ) {
                transferAllowed = cursorStack.isDamageAndIDIdentical(slotItemToExamine);
            }
        }
    }
}
