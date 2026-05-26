package net.yxiao233.createmoremachines.api.content.depot;

import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.depot.DepotItemHandler;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class CMMDepotItemHandler extends DepotItemHandler {
    private static final int MAIN_SLOT = 0;
    private final CMMDepotBehaviour behaviour;

    public CMMDepotItemHandler(CMMDepotBehaviour behaviour) {
        super(behaviour);
        this.behaviour = behaviour;
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return slot == 0 ? this.behaviour.getHeldItemStack() : this.behaviour.getProcessingOutputBuffer().getStackInSlot(slot - 1);
    }

    @Override
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

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot != 0) {
            return this.behaviour.getProcessingOutputBuffer().extractItem(slot - 1, amount, simulate);
        } else {
            TransportedItemStack held = this.behaviour.getHeldTransportedItemStack().getValue();
            if (held == null) {
                return ItemStack.EMPTY;
            } else {
                ItemStack stack = held.stack.copy();
                ItemStack extracted = stack.split(amount);
                if (!simulate) {
                    this.behaviour.getHeldTransportedItemStack().getValue().stack = stack;
                    if (stack.isEmpty()) {
                        this.behaviour.getHeldTransportedItemStack().setValue(null);
                    }

                    this.behaviour.blockEntity.notifyUpdate();
                }

                return extracted;
            }
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return slot == 0 ? this.behaviour.getMaxStackSize().get() : 64;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return slot == 0 && this.behaviour.isItemValid(stack);
    }
}
