package net.yxiao233.createmoremachines.utils;

import net.minecraft.world.item.ItemStack;

public class BigItemHelper {
    public static boolean canItemStackAmountsStack(ItemStack a, ItemStack b, int maxSize) {
        return ItemStack.isSameItemSameComponents(a, b) && a.getCount() + b.getCount() <= maxSize;
    }
}
