package net.yxiao233.createmoremachines.mixin;

import com.simibubi.create.foundation.blockEntity.ItemHandlerContainer;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.yxiao233.createmoremachines.api.capabilities.BigItemStackHandler;
import net.yxiao233.createmoremachines.utils.ReflectionUtil;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SmartInventory.class)
public abstract class MixinSmartInventory extends ItemHandlerContainer implements IItemHandlerModifiable, INBTSerializable<CompoundTag> {

    public MixinSmartInventory(IItemHandlerModifiable inv) {
        super(inv);
    }

    @Unique
    private ItemStackHandler createMoreMachines$getInv(){
        return (ItemStackHandler) ReflectionUtil.getPrivateField("inv", IItemHandlerModifiable.class, this, ItemHandlerContainer.class);
    }

    public CompoundTag serializeNBT(HolderLookup.@NotNull Provider registries) {
        return BigItemStackHandler.serializeNBT(this.createMoreMachines$getInv(),registries);
    }

    public void deserializeNBT(HolderLookup.@NotNull Provider registries, @NotNull CompoundTag nbt) {
        BigItemStackHandler.deserializeNBT(this.createMoreMachines$getInv(), registries, nbt);
    }
}
