package net.yxiao233.createmoremachines.api.content.depot;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import com.simibubi.create.content.logistics.depot.DepotBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.utils.ReflectionUtil;
import net.yxiao233.createmoremachines.utils.ReflectionValue;

import java.util.List;

public class CMMDepotBlockEntity extends DepotBlockEntity implements IHaveGoggleInformation {
    private ReflectionValue<DepotBehaviour, DepotBlockEntity> behaviour;
    private CMMDepotBehaviour cmmDepotBehaviour;
    private final CMMTier tier;
    public CMMDepotBlockEntity(CMMTier tier, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tier = tier;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return false;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.behaviour = new ReflectionValue<>("depotBehaviour", DepotBehaviour.class, this, DepotBlockEntity.class);
        CMMDepotBehaviour depotBehaviour = new CMMDepotBehaviour(this);
        depotBehaviour.tier = ((CMMDepotBlock) this.getBlockState().getBlock()).getTier();
        this.cmmDepotBehaviour = depotBehaviour;
        this.behaviour.setValue(depotBehaviour);
        behaviours.clear();
        behaviours.add(depotBehaviour);
        depotBehaviour.addSubBehaviours(behaviours);
    }
    @Override
    public ItemStack getHeldItem() {
        return this.behaviour.getValue().getHeldItemStack();
    }

    @Override
    public void setHeldItem(ItemStack item) {
        TransportedItemStack newStack = new TransportedItemStack(item);
        TransportedItemStack heldItem = ReflectionUtil.getPrivateField("heldItem", TransportedItemStack.class, this.behaviour.getValue(), DepotBehaviour.class);
        if (heldItem != null) {
            newStack.angle = heldItem.angle;
        }

        this.behaviour.getValue().setHeldItem(newStack);
    }

    public CMMDepotBehaviour getBehaviour() {
        return cmmDepotBehaviour;
    }

    public CMMTier getTier() {
        return tier;
    }
}
