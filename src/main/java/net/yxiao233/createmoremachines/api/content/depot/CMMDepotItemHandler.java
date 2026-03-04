package net.yxiao233.createmoremachines.api.content.depot;

import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class CMMDepotItemHandler implements IItemHandler {
    private static final int MAIN_SLOT = 0;
    private final CMMDepotBehaviour behaviour;

    public CMMDepotItemHandler(CMMDepotBehaviour behaviour) {
        this.behaviour = behaviour;
    }

    public int getSlots() {
        return 9;
    }

    public @NotNull ItemStack getStackInSlot(int slot) {
        return slot == 0 ? this.behaviour.getHeldItemStack() : this.behaviour.processingOutputBuffer.getStackInSlot(slot - 1);
    }

    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (slot != 0) {
            return stack;
        } else if (!this.behaviour.getHeldItemStack().isEmpty() && !this.behaviour.canMergeItems()) {
            return stack;
        } else if (!this.behaviour.isOutputEmpty() && !this.behaviour.canMergeItems()) {
            return stack;
        } else {
            ItemStack remainder = this.behaviour.insert(new TransportedItemStack(stack), simulate);
            if (!simulate && remainder != stack) {
                this.behaviour.blockEntity.notifyUpdate();
            }

            return remainder;
        }
    }

    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot != 0) {
            return this.behaviour.processingOutputBuffer.extractItem(slot - 1, amount, simulate);
        } else {
            TransportedItemStack held = this.behaviour.heldItem;
            if (held == null) {
                return ItemStack.EMPTY;
            } else {
                ItemStack stack = held.stack.copy();
                ItemStack extracted = stack.split(amount);
                if (!simulate) {
                    this.behaviour.heldItem.stack = stack;
                    if (stack.isEmpty()) {
                        this.behaviour.heldItem = null;
                    }

                    this.behaviour.blockEntity.notifyUpdate();
                }

                return extracted;
            }
        }
    }

    public int getSlotLimit(int slot) {
        return slot == 0 ? (Integer)this.behaviour.maxStackSize.get() : 64;
    }

    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return slot == 0 && this.behaviour.isItemValid(stack);
    }
}
