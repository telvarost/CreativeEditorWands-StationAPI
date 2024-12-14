package com.github.telvarost.creativeeditorwands.mixin.client;

import com.github.telvarost.creativeeditorwands.ModHelper;
import com.github.telvarost.creativeeditorwands.util.HotbarTooltipHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
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
@Mixin(HandledScreen.class)
public abstract class ContainerBaseMixin extends Screen {
    @Shadow
    protected abstract Slot getSlotAt(int x, int y);

    @Unique
    private Slot slot = null;

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    protected void creativeEditorWands_mouseClicked(int mouseX, int mouseY, int button, CallbackInfo ci) {

        /** - Left-click */
        if (button == 0) {
            if (ModHelper.ModHelperFields.enableWorldEditTools) {
                PlayerEntity player = PlayerHelper.getPlayerFromGame();
                if (  (ModHelper.IsPlayerCreative(player))
                ) {
                    if (!minecraft.world.isRemote) {
                        slot = this.getSlotAt(mouseX, mouseY);

                        /** - Do nothing if mouse is not over a slot */
                        if (slot == null)
                            return;

                        ItemStack cursorStack = minecraft.player.inventory.getCursorStack();
                        ItemStack slotItemToExamine = slot.getStack();

                        if (   (null != cursorStack)
                            && (null != slotItemToExamine)
                            && (   (cursorStack.itemId == Item.WOODEN_PICKAXE.id)
                                || (cursorStack.itemId == Item.WOODEN_SHOVEL.id)
                                || (cursorStack.itemId == Item.WOODEN_SWORD.id)
                               )
                            && (   (slotItemToExamine.itemId == Item.WOODEN_SHOVEL.id)
                                || (slotItemToExamine.itemId == Item.WOODEN_SWORD.id)
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
            if (!minecraft.world.isRemote) {
                slot = this.getSlotAt(mouseX, mouseY);

                /** - Do nothing if mouse is not over a slot */
                if (slot == null)
                    return;

                int currentWheelDegrees = Mouse.getDWheel();
                if (0 != currentWheelDegrees) {
                    PlayerEntity player = PlayerHelper.getPlayerFromGame();
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
        ItemStack cursorStack = minecraft.player.inventory.getCursorStack();
        ItemStack slotItemToExamine = slot.getStack();

        if (  (null == cursorStack)
           && (null != slotItemToExamine)
           &&   (  (slotItemToExamine.itemId == Item.WOODEN_PICKAXE.id)
                || (slotItemToExamine.itemId == Item.WOODEN_SHOVEL.id)
                || (slotItemToExamine.itemId == Item.WOODEN_SWORD.id)
                || (slotItemToExamine.itemId == Item.WOODEN_HOE.id)
                || (slotItemToExamine.itemId == Item.WOODEN_AXE.id)
                )
           )
        {
            float numberOfTurns = (float)wheelDegrees / 120.0f;
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                if (slotItemToExamine.itemId == Item.WOODEN_AXE.id) {
                    int damageValue = slotItemToExamine.getDamage();
                    if (0 < damageValue && (ModHelper.SELECTION_TOOL_DURABILITY - 2) >= damageValue) {
                        int curCount = slotItemToExamine.count;
                        slotItemToExamine.damage((int) numberOfTurns, null);
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
                    slotItemToExamine.damage((int) numberOfTurns, null);
                    slotItemToExamine.count = curCount;
                    if (0 > slotItemToExamine.getDamage()) {
                        if (slotItemToExamine.itemId != Item.WOODEN_HOE.id) {
                            slotItemToExamine.setDamage(ModHelper.BLOCK_ID_DURABILITY);
                        } else {
                            slotItemToExamine.setDamage(ModHelper.BRUSH_SIZE_DURABILITY);
                        }
                    }
                }
            } else {
                slotItemToExamine.count += numberOfTurns;
                if (slotItemToExamine.itemId == Item.WOODEN_AXE.id) {
                    if (3 < slotItemToExamine.count) {
                        slotItemToExamine.count = 1;
                    }
                    if (1 > slotItemToExamine.count) {
                        slotItemToExamine.count = 3;
                    }
                } else if (slotItemToExamine.itemId == Item.WOODEN_HOE.id) {
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
