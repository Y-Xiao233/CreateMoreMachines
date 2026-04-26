package net.yxiao233.createmoremachines.mixin;

import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.yxiao233.createmoremachines.api.content.basin.CMMBasinBlock;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "com.simibubi.create.foundation.item.SmartInventory$SyncedStackHandler")
public abstract class MixinSyncedStackHandler extends ItemStackHandler {
    @Shadow private SyncedBlockEntity blockEntity;

    @Override
    public int getSlotLimit(int slot) {
        if(blockEntity.getBlockState().getBlock() instanceof CMMBasinBlock basin){
            return basin.getTier().getItemCapability();
        }
        return super.getSlotLimit(slot);
    }

    @Override
    protected int getStackLimit(int slot, @NotNull ItemStack stack) {
        if(blockEntity.getBlockState().getBlock() instanceof CMMBasinBlock basin){
            return basin.getTier().getItemCapability();
        }
        return super.getStackLimit(slot, stack);
    }
}
