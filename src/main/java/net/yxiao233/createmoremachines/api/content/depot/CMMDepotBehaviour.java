package net.yxiao233.createmoremachines.api.content.depot;

import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.yxiao233.createmoremachines.api.capabilities.BigItemStackHandler;
import net.yxiao233.createmoremachines.api.content.mechanical.press.CMMMechanicalPressBlockEntity;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.utils.ReflectionUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class CMMDepotBehaviour extends DepotBehaviour {
    public static final BehaviourType<CMMDepotBehaviour> TYPE = new BehaviourType<>();
    public CMMTier tier;
    boolean allowMerge;
    public CMMDepotBehaviour(CMMDepotBlockEntity entity) {
        super(entity);
        ReflectionUtil.setPrivateField("processingOutputBuffer", ItemStackHandler.class, this, DepotBehaviour.class, new BigItemStackHandler(8) {
            protected void onContentsChanged(int slot) {
                entity.notifyUpdate();
            }

            @Override
            protected int getStackLimit(int slot, @NotNull ItemStack stack) {
                return getTier().getItemCapability();
            }
        });
        this.itemHandler = new CMMDepotItemHandler(this);
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    public CMMTier getTier() {
        if(this.tier == null){
            if(this.blockEntity instanceof CMMMechanicalPressBlockEntity pressBlockEntity){
                this.tier = pressBlockEntity.getTier();
            }
        }
        return tier;
    }

    public ItemStackHandler getProcessingOutputBuffer(){
        return ReflectionUtil.getPrivateField("processingOutputBuffer",ItemStackHandler.class, this, DepotBehaviour.class);
    }

    public Supplier<Integer> getMaxStackSize(){
        return this.getTier()::getItemCapability;
    }


    @SuppressWarnings("unchecked")
    public List<TransportedItemStack> getIncoming(){
        return ReflectionUtil.getPrivateField("incoming",List.class,this, DepotBehaviour.class);
    }

    @Override
    public int getRemainingSpace() {
        int cumulativeStackSize = this.getPresentStackSize();

        TransportedItemStack transportedItemStack;
        if(getIncoming() == null){
            return 64;
        }
        for(Iterator<TransportedItemStack> iterator = getIncoming().iterator(); iterator.hasNext(); cumulativeStackSize += transportedItemStack.stack.getCount()) {
            transportedItemStack = iterator.next();
        }

        int fromGetter = this.getMaxStackSize().get() == 0 ? 64 : this.getMaxStackSize().get();
        return fromGetter - cumulativeStackSize;
    }
}
