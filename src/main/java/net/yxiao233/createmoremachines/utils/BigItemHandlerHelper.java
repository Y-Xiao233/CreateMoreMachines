package net.yxiao233.createmoremachines.utils;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class BigItemHandlerHelper {

    public static ItemStack insertItem(ItemStackHandler dest, ItemStack stack, int limit,boolean simulate) {
        if (dest != null && !stack.isEmpty()) {
            for(int i = 0; i < dest.getSlots(); ++i) {
                stack = insertItem(dest, i, stack, limit, simulate);
                if (stack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }

            return stack;
        } else {
            return stack;
        }
    }
    public static ItemStack insertItem(ItemStackHandler handler, int slot, ItemStack stack, int limit, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else if (!handler.isItemValid(slot, stack)) {
            return stack;
        } else {
            if (slot < 0 || slot >= handler.getSlots()) {
                throw new RuntimeException("Slot " + slot + " not in valid range - [0," + handler.getSlots() + ")");
            }
            ItemStack existing = handler.getStackInSlot(slot);
            if (!existing.isEmpty()) {
                if (!ItemStack.isSameItemSameComponents(stack, existing)) {
                    return stack;
                }

                limit -= existing.getCount();
            }

            if (limit <= 0) {
                return stack;
            } else {
                boolean reachedLimit = stack.getCount() > limit;
                if (!simulate) {
                    if (existing.isEmpty()) {
                        handler.setStackInSlot(slot, reachedLimit ? stack.copyWithCount(limit) : stack);
                    } else {
                        existing.grow(reachedLimit ? limit : stack.getCount());
                    }
                }

                return reachedLimit ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
            }
        }
    }
}
