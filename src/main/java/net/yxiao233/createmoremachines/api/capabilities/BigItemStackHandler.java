package net.yxiao233.createmoremachines.api.capabilities;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.yxiao233.createmoremachines.utils.BigItemStack;
import org.jetbrains.annotations.NotNull;

public class BigItemStackHandler extends ItemStackHandler {
    public BigItemStackHandler(int slot){
        super(slot);
    }

    @Override
    public @NotNull CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        ListTag nbtTagList = new ListTag();

        for(int i = 0; i < this.stacks.size(); ++i) {
            if (!((ItemStack)this.stacks.get(i)).isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                nbtTagList.add(BigItemStack.of(this.stacks.get(i)).save(provider,itemTag));
            }
        }

        CompoundTag nbt = new CompoundTag();
        nbt.put("Items", nbtTagList);
        nbt.putInt("Size", this.stacks.size());
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
        this.setSize(nbt.contains("Size", 3) ? nbt.getInt("Size") : this.stacks.size());
        ListTag tagList = nbt.getList("Items", 10);

        for(int i = 0; i < tagList.size(); ++i) {
            CompoundTag itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");
            if (slot >= 0 && slot < this.stacks.size()) {
                BigItemStack.parse(provider,itemTags).ifPresent(stack -> {
                    this.stacks.set(slot,stack);
                });
            }
        }

        this.onLoad();
    }
}
