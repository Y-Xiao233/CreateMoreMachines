package net.yxiao233.createmoremachines.mixin;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.yxiao233.createmoremachines.api.content.depot.CMMDepotBlockEntity;
import net.yxiao233.createmoremachines.utils.BigItemHelper;
import net.yxiao233.createmoremachines.utils.BigTransportedItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Mixin(DepotBehaviour.class)
public abstract class MixinDepotBehaviour extends BlockEntityBehaviour implements Clearable {
    @Shadow
    Supplier<Boolean> canAcceptItems;

    @Shadow
    Predicate<ItemStack> acceptedItems;

    @Shadow
    public abstract boolean canMergeItems();

    @Shadow
    public abstract int getRemainingSpace();

    @Shadow
    TransportedItemStack heldItem;

    @Shadow
    List<TransportedItemStack> incoming;

    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    Consumer<ItemStack> onHeldInserted;

    @Shadow
    private ItemStackHandler processingOutputBuffer;

    public MixinDepotBehaviour(SmartBlockEntity be) {
        super(be);
    }

    /**
     * @author Y_Xiao233
     * @reason allow insert stack size over 99
     */
    @Inject(method = "insert", at = @At("HEAD"), cancellable = true)
    public void insert(TransportedItemStack heldItem, boolean simulate, CallbackInfoReturnable<ItemStack> cir) {
        if(blockEntity instanceof CMMDepotBlockEntity depotBlockEntity){
            System.out.println(this.getRemainingSpace());
            if (!(Boolean)this.canAcceptItems.get()) {
                cir.setReturnValue(heldItem.stack);
            } else if (!this.acceptedItems.test(heldItem.stack)) {
                cir.setReturnValue(heldItem.stack);
            } else if (this.canMergeItems()) {
                int remainingSpace = this.getRemainingSpace();
                ItemStack inserted = heldItem.stack;
                if (remainingSpace <= 0) {
                    cir.setReturnValue(inserted);
                } else if (this.heldItem != null && !BigItemHelper.canItemStackAmountsStack(this.heldItem.stack, inserted,depotBlockEntity.getTier().getItemCapability())) {
                    cir.setReturnValue(inserted);
                } else {
                    ItemStack returned = ItemStack.EMPTY;
                    if (remainingSpace < inserted.getCount()) {
                        returned = heldItem.stack.copyWithCount(inserted.getCount() - remainingSpace);
                        if (!simulate) {
                            TransportedItemStack copy = heldItem.copy();
                            copy.stack.setCount(remainingSpace);
                            if (this.heldItem != null) {
                                this.incoming.add(copy);
                            } else {
                                this.heldItem = copy;
                            }
                        }
                    } else if (!simulate) {
                        if (this.heldItem != null) {
                            this.incoming.add(heldItem);
                        } else {
                            this.heldItem = heldItem;
                        }
                    }

                    cir.setReturnValue(returned);
                }
            } else {
                ItemStack returned = ItemStack.EMPTY;
                int maxCount = depotBlockEntity.getTier().getItemCapability();
                boolean stackTooLarge = maxCount < heldItem.stack.getCount();
                if (stackTooLarge) {
                    returned = heldItem.stack.copyWithCount(heldItem.stack.getCount() - maxCount);
                }

                if (simulate) {
                    cir.setReturnValue(returned);
                } else {
                    if (this.isEmpty()) {
                        if (heldItem.insertedFrom.getAxis().isHorizontal()) {
                            AllSoundEvents.DEPOT_SLIDE.playOnServer(this.getWorld(), this.getPos());
                        } else {
                            AllSoundEvents.DEPOT_PLOP.playOnServer(this.getWorld(), this.getPos());
                        }
                    }

                    if (stackTooLarge) {
                        heldItem = heldItem.copy();
                        heldItem.stack.setCount(maxCount);
                    }

                    this.heldItem = heldItem;
                    this.onHeldInserted.accept(heldItem.stack);
                    cir.setReturnValue(returned);
                }
            }
        }
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        if (this.heldItem != null) {
            compound.put("HeldItem", BigTransportedItemStack.serializeNBT(this.heldItem,registries));
        }

        compound.put("OutputBuffer", this.processingOutputBuffer.serializeNBT(registries));
        if (this.canMergeItems() && !this.incoming.isEmpty()) {
            compound.put("Incoming", NBTHelper.writeCompoundList(this.incoming, (stack) -> BigTransportedItemStack.serializeNBT(stack,registries)));
        }

    }

    @Override
    public void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        this.heldItem = null;
        if (compound.contains("HeldItem")) {
            this.heldItem = BigTransportedItemStack.read(compound.getCompound("HeldItem"), registries);
        }

        this.processingOutputBuffer.deserializeNBT(registries, compound.getCompound("OutputBuffer"));
        if (this.canMergeItems()) {
            ListTag list = compound.getList("Incoming", 10);
            this.incoming = NBTHelper.readCompoundList(list, (c) -> BigTransportedItemStack.read(c, registries));
        }

    }
}
