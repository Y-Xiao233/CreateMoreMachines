package net.yxiao233.createmoremachines.api.content.depot;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.funnel.AbstractFunnelBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.yxiao233.createmoremachines.api.capabilities.BigItemStackHandler;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.utils.BigTransportedItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CMMDepotBehaviour extends BlockEntityBehaviour {
    public static final BehaviourType<CMMDepotBehaviour> TYPE = new BehaviourType<>();
    CMMTier tier;
    TransportedItemStack heldItem;
    List<TransportedItemStack> incoming = new ArrayList<>();
    BigItemStackHandler processingOutputBuffer;
    public CMMDepotItemHandler itemHandler = new CMMDepotItemHandler(this);
    TransportedItemStackHandlerBehaviour transportedHandler;
    Supplier<Integer> maxStackSize = () -> {
        return tier.getItemCapability();
    };
    Supplier<Boolean> canAcceptItems = () -> {
        return true;
    };
    Predicate<Direction> canFunnelsPullFrom = ($) -> {
        return true;
    };
    Consumer<ItemStack> onHeldInserted = ($) -> {
    };
    Predicate<ItemStack> acceptedItems = ($) -> {
        return true;
    };
    boolean allowMerge;
    public CMMDepotBehaviour(CMMDepotBlockEntity entity) {
        super(entity);
        this.processingOutputBuffer = new BigItemStackHandler(8) {
            protected void onContentsChanged(int slot) {
                entity.notifyUpdate();
            }
        };
        if(entity.getBlockState().getBlock() instanceof CMMDepotBlock depotBlock){
            this.tier = depotBlock.getTier();
        }
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    public void enableMerging() {
        this.allowMerge = true;
    }

    public CMMDepotBehaviour withCallback(Consumer<ItemStack> changeListener) {
        this.onHeldInserted = changeListener;
        return this;
    }

    public CMMDepotBehaviour onlyAccepts(Predicate<ItemStack> filter) {
        this.acceptedItems = filter;
        return this;
    }

    public void tick() {
        super.tick();
        Level world = this.blockEntity.getLevel();
        Iterator<TransportedItemStack> iterator = this.incoming.iterator();

        while(true) {
            TransportedItemStack ts;
            do {
                do {
                    if (!iterator.hasNext()) {
                        if (this.heldItem == null) {
                            return;
                        }

                        if (!this.tick(this.heldItem)) {
                            return;
                        }

                        BlockPos pos = this.blockEntity.getBlockPos();
                        if (world.isClientSide) {
                            return;
                        }

                        if (this.handleBeltFunnelOutput()) {
                            return;
                        }

                        BeltProcessingBehaviour processingBehaviour = BlockEntityBehaviour.get(world, pos.above(2), BeltProcessingBehaviour.TYPE);
                        if (processingBehaviour == null) {
                            return;
                        }

                        if (!this.heldItem.locked && BeltProcessingBehaviour.isBlocked(world, pos)) {
                            return;
                        }

                        ItemStack previousItem = this.heldItem.stack;
                        boolean wasLocked = this.heldItem.locked;
                        BeltProcessingBehaviour.ProcessingResult result = wasLocked ? processingBehaviour.handleHeldItem(this.heldItem, this.transportedHandler) : processingBehaviour.handleReceivedItem(this.heldItem, this.transportedHandler);
                        if (result == BeltProcessingBehaviour.ProcessingResult.REMOVE) {
                            this.heldItem = null;
                            this.blockEntity.sendData();
                            return;
                        }

                        this.heldItem.locked = result == BeltProcessingBehaviour.ProcessingResult.HOLD;
                        if (this.heldItem.locked != wasLocked || !ItemStack.matches(previousItem, this.heldItem.stack)) {
                            this.blockEntity.sendData();
                        }

                        return;
                    }

                    ts = iterator.next();
                } while(!this.tick(ts));
            } while(world.isClientSide && !this.blockEntity.isVirtual());

            if (this.heldItem == null) {
                this.heldItem = ts;
            } else if (!ItemStack.isSameItemSameComponents(this.heldItem.stack, ts.stack)) {
                Vec3 vec = VecHelper.getCenterOf(this.blockEntity.getBlockPos());
                Containers.dropItemStack(this.blockEntity.getLevel(), vec.x, vec.y + 0.5, vec.z, ts.stack);
            } else {
                this.heldItem.stack.grow(ts.stack.getCount());
            }

            iterator.remove();
            this.blockEntity.notifyUpdate();
        }
    }

    protected boolean tick(TransportedItemStack heldItem) {
        heldItem.prevBeltPosition = heldItem.beltPosition;
        heldItem.prevSideOffset = heldItem.sideOffset;
        float diff = 0.5F - heldItem.beltPosition;
        if (diff > 0.001953125F) {
            if (diff > 0.03125F && !BeltHelper.isItemUpright(heldItem.stack)) {
                ++heldItem.angle;
            }

            heldItem.beltPosition += diff / 4.0F;
        }

        return diff < 0.0625F;
    }

    private boolean handleBeltFunnelOutput() {
        BlockState funnel = this.getWorld().getBlockState(this.getPos().above());
        Direction funnelFacing = AbstractFunnelBlock.getFunnelFacing(funnel);
        if (funnelFacing != null && this.canFunnelsPullFrom.test(funnelFacing.getOpposite())) {
            ItemStack afterInsert;
            for(int slot = 0; slot < this.processingOutputBuffer.getSlots(); ++slot) {
                afterInsert = this.processingOutputBuffer.getStackInSlot(slot);
                if (!afterInsert.isEmpty()) {
                    afterInsert = this.blockEntity.getBehaviour(DirectBeltInputBehaviour.TYPE).tryExportingToBeltFunnel(afterInsert, null, false);
                    if (afterInsert == null) {
                        return false;
                    }

                    if (afterInsert.getCount() != afterInsert.getCount()) {
                        this.processingOutputBuffer.setStackInSlot(slot, afterInsert);
                        this.blockEntity.notifyUpdate();
                        return true;
                    }
                }
            }

            ItemStack previousItem = this.heldItem.stack;
            afterInsert = ((DirectBeltInputBehaviour)this.blockEntity.getBehaviour(DirectBeltInputBehaviour.TYPE)).tryExportingToBeltFunnel(previousItem, null, false);
            if (afterInsert == null) {
                return false;
            } else if (previousItem.getCount() != afterInsert.getCount()) {
                if (afterInsert.isEmpty()) {
                    this.heldItem = null;
                } else {
                    this.heldItem.stack = afterInsert;
                }

                this.blockEntity.notifyUpdate();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void destroy() {
        super.destroy();
        Level level = this.getWorld();
        BlockPos pos = this.getPos();
        ItemHelper.dropContents(level, pos, this.processingOutputBuffer);

        for (TransportedItemStack transportedItemStack : this.incoming) {
            Block.popResource(level, pos, transportedItemStack.stack);
        }

        if (!this.getHeldItemStack().isEmpty()) {
            Block.popResource(level, pos, this.getHeldItemStack());
        }

    }

    public void unload() {
        if (this.itemHandler != null) {
            this.blockEntity.invalidateCapabilities();
        }

    }

    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        if (this.heldItem != null) {
            compound.put("HeldItem", BigTransportedItemStack.serializeNBT(heldItem,registries));
        }

        compound.put("OutputBuffer", this.processingOutputBuffer.serializeNBT(registries));
        if (this.canMergeItems() && !this.incoming.isEmpty()) {
            compound.put("Incoming", NBTHelper.writeCompoundList(this.incoming, (stack) -> {
                return BigTransportedItemStack.serializeNBT(stack,registries);
            }));
        }

    }

    public void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        this.heldItem = null;
        if (compound.contains("HeldItem")) {
            this.heldItem = BigTransportedItemStack.read(compound.getCompound("HeldItem"), registries);
        }

        this.processingOutputBuffer.deserializeNBT(registries, compound.getCompound("OutputBuffer"));
        if (this.canMergeItems()) {
            ListTag list = compound.getList("Incoming", 10);
            this.incoming = NBTHelper.readCompoundList(list, (c) -> {
                return BigTransportedItemStack.read(c, registries);
            });
        }

    }

    public void addSubBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add((new DirectBeltInputBehaviour(this.blockEntity)).allowingBeltFunnels().setInsertionHandler(this::tryInsertingFromSide).considerOccupiedWhen(this::isOccupied));
        this.transportedHandler = (new TransportedItemStackHandlerBehaviour(this.blockEntity, this::applyToAllItems)).withStackPlacement(this::getWorldPositionOf);
        behaviours.add(this.transportedHandler);
    }

    public ItemStack getHeldItemStack() {
        return this.heldItem == null ? ItemStack.EMPTY : this.heldItem.stack;
    }

    public boolean canMergeItems() {
        return this.allowMerge;
    }

    public int getPresentStackSize() {
        int cumulativeStackSize = 0;
        cumulativeStackSize += this.getHeldItemStack().getCount();

        for(int slot = 0; slot < this.processingOutputBuffer.getSlots(); ++slot) {
            cumulativeStackSize += this.processingOutputBuffer.getStackInSlot(slot).getCount();
        }

        return cumulativeStackSize;
    }

    public int getRemainingSpace() {
        int cumulativeStackSize = this.getPresentStackSize();

        TransportedItemStack transportedItemStack;
        for(Iterator<TransportedItemStack> iterator = this.incoming.iterator(); iterator.hasNext(); cumulativeStackSize += transportedItemStack.stack.getCount()) {
            transportedItemStack = iterator.next();
        }

        int fromGetter = this.maxStackSize.get() == 0 ? 64 : this.maxStackSize.get();
        return fromGetter - cumulativeStackSize;
    }

    public ItemStack insert(TransportedItemStack heldItem, boolean simulate) {
        if (!(Boolean)this.canAcceptItems.get()) {
            System.out.println("1");
            return heldItem.stack;
        } else if (!this.acceptedItems.test(heldItem.stack)) {
            System.out.println("2");
            return heldItem.stack;
        } else if (this.canMergeItems()) {
            System.out.println("3");
            int remainingSpace = this.getRemainingSpace();
            ItemStack inserted = heldItem.stack;
            if (remainingSpace <= 0) {
                System.out.println("4");
                return inserted;
            } else if (this.heldItem != null && !ItemStack.isSameItemSameComponents(this.heldItem.stack, inserted)) {
                System.out.println("5");
                return inserted;
            } else {
                System.out.println("6");
                ItemStack returned = ItemStack.EMPTY;
                if (remainingSpace < inserted.getCount()) {
                    System.out.println("7");
                    returned = heldItem.stack.copyWithCount(inserted.getCount() - remainingSpace);
                    if (!simulate) {
                        System.out.println("8");
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

                return returned;
            }
        } else {
            ItemStack returned = ItemStack.EMPTY;
            int maxCount = heldItem.stack.getMaxStackSize();
            boolean stackTooLarge = maxCount < heldItem.stack.getCount();
            if (stackTooLarge) {
                returned = heldItem.stack.copyWithCount(heldItem.stack.getCount() - maxCount);
            }

            if (simulate) {
                return returned;
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
                return returned;
            }
        }
    }

    public void setHeldItem(TransportedItemStack heldItem) {
        this.heldItem = heldItem;
    }

    public void removeHeldItem() {
        this.heldItem = null;
    }

    public void setCenteredHeldItem(TransportedItemStack heldItem) {
        this.heldItem = heldItem;
        this.heldItem.beltPosition = 0.5F;
        this.heldItem.prevBeltPosition = 0.5F;
    }

    private boolean isOccupied(Direction side) {
        if (!this.getHeldItemStack().isEmpty() && !this.canMergeItems()) {
            return true;
        } else if (!this.isOutputEmpty() && !this.canMergeItems()) {
            return true;
        } else {
            return !(Boolean)this.canAcceptItems.get();
        }
    }

    private ItemStack tryInsertingFromSide(TransportedItemStack transportedStack, Direction side, boolean simulate) {
        ItemStack inserted = transportedStack.stack;
        if (this.isOccupied(side)) {
            return inserted;
        } else {
            int size = transportedStack.stack.getCount();
            transportedStack = transportedStack.copy();
            transportedStack.beltPosition = side.getAxis().isVertical() ? 0.5F : 0.0F;
            transportedStack.insertedFrom = side;
            transportedStack.prevSideOffset = transportedStack.sideOffset;
            transportedStack.prevBeltPosition = transportedStack.beltPosition;
            ItemStack remainder = this.insert(transportedStack, simulate);
            if (remainder.getCount() != size) {
                this.blockEntity.notifyUpdate();
            }

            return remainder;
        }
    }

    private void applyToAllItems(float maxDistanceFromCentre, Function<TransportedItemStack, TransportedItemStackHandlerBehaviour.TransportedResult> processFunction) {
        if (this.heldItem != null) {
            if (!(0.5F - this.heldItem.beltPosition > maxDistanceFromCentre)) {
                TransportedItemStack transportedItemStack = this.heldItem;
                ItemStack stackBefore = transportedItemStack.stack.copy();
                TransportedItemStackHandlerBehaviour.TransportedResult result = processFunction.apply(transportedItemStack);
                if (result != null && !result.didntChangeFrom(stackBefore)) {
                    this.heldItem = null;
                    if (result.hasHeldOutput()) {
                        this.setCenteredHeldItem(result.getHeldOutput());
                    }

                    for (TransportedItemStack added : result.getOutputs()) {
                        if(this.getHeldItemStack().isEmpty()) {
                            this.setCenteredHeldItem(added);
                        }else{
                            ItemStack remainder = ItemHandlerHelper.insertItemStacked(this.processingOutputBuffer, added.stack, false);
                            Vec3 vec = VecHelper.getCenterOf(this.blockEntity.getBlockPos());
                            Containers.dropItemStack(this.blockEntity.getLevel(), vec.x, vec.y + 0.5, vec.z, remainder);
                        }
                    }
                    this.blockEntity.notifyUpdate();
                }
            }
        }
    }

    public boolean isEmpty() {
        return this.heldItem == null && this.isOutputEmpty();
    }

    public boolean isOutputEmpty() {
        for(int i = 0; i < this.processingOutputBuffer.getSlots(); ++i) {
            if (!this.processingOutputBuffer.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private Vec3 getWorldPositionOf(TransportedItemStack transported) {
        return VecHelper.getCenterOf(this.blockEntity.getBlockPos());
    }

    public boolean isItemValid(ItemStack stack) {
        return this.acceptedItems.test(stack);
    }
}
