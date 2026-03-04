package net.yxiao233.createmoremachines.api.content.depot;

import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.depot.DepotBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.createmoremachines.api.registry.CMMTier;

import java.util.List;

public class CMMDepotBlockEntity extends DepotBlockEntity {
    private CMMDepotBehaviour behaviour;
    private final CMMTier tier;
    public CMMDepotBlockEntity(CMMTier tier, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tier = tier;
    }


    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(this.behaviour = new CMMDepotBehaviour(this));
        this.behaviour.addSubBehaviours(behaviours);
    }
    @Override
    public ItemStack getHeldItem() {
        return this.behaviour.getHeldItemStack();
    }

    @Override
    public void setHeldItem(ItemStack item) {
        TransportedItemStack newStack = new TransportedItemStack(item);
        if (this.behaviour.heldItem != null) {
            newStack.angle = this.behaviour.heldItem.angle;
        }

        this.behaviour.setHeldItem(newStack);
    }

    public CMMDepotBehaviour getBehaviour() {
        return behaviour;
    }

    public CMMTier getTier() {
        return tier;
    }
}
