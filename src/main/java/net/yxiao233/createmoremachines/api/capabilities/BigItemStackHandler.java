package net.yxiao233.createmoremachines.api.capabilities;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.yxiao233.createmoremachines.utils.BigItemStack;
import org.jetbrains.annotations.NotNull;

public class BigItemStackHandler extends ItemStackHandler {
    public BigItemStackHandler(int slot){
        super(slot);
    }

    @Override
    public @NotNull CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        return serializeNBT(this,provider);
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag nbt) {
        deserializeNBT(this,provider,nbt);
        this.onLoad();
    }

    public static @NotNull CompoundTag serializeNBT(ItemStackHandler handler, HolderLookup.@NotNull Provider provider) {
        ListTag nbtTagList = new ListTag();

        for(int i = 0; i < handler.getSlots(); ++i) {
            if (!handler.getStackInSlot(i).isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                nbtTagList.add(BigItemStack.of(handler.getStackInSlot(i)).save(provider,itemTag));
            }
        }

        CompoundTag nbt = new CompoundTag();
        nbt.put("Items", nbtTagList);
        nbt.putInt("Size", handler.getSlots());
        return nbt;
    }

    public static void deserializeNBT(ItemStackHandler handler, HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
        handler.setSize(nbt.contains("Size", 3) ? nbt.getInt("Size") : handler.getSlots());
        ListTag tagList = nbt.getList("Items", 10);

        for(int i = 0; i < tagList.size(); ++i) {
            CompoundTag itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");
            if (slot >= 0 && slot < handler.getSlots()) {
                BigItemStack.parse(provider,itemTags).ifPresent(stack -> {
                    handler.setStackInSlot(slot,stack);
                });
            }
        }
    }
}
