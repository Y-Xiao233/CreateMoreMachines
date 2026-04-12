package net.yxiao233.createmoremachines.api.content.mechanical.deployer;

import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;
import com.simibubi.create.content.kinetics.deployer.DeployerFilterSlot;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.utils.ReflectionUtil;
import net.yxiao233.createmoremachines.utils.ReflectionValue;

import java.util.List;

public class CMMDeployerBlockEntity extends DeployerBlockEntity {
    private final CMMTier tier;
    public CMMDeployerBlockEntity(CMMTier tier, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tier = tier;
    }

    public CMMTier getTier() {
        return tier;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        this.filtering = new FilteringBehaviour(this, new DeployerFilterSlot());
        behaviours.add(this.filtering);
        this.processingBehaviour = (new BeltProcessingBehaviour(this)).whenItemEnters((s, i) -> {
            return CMMBeltDeployerCallbacks.onItemReceived(s, i, this);
        }).whileItemHeld((s, i) -> {
            return CMMBeltDeployerCallbacks.whenItemHeld(s, i, this);
        });
        behaviours.add(this.processingBehaviour);
        this.registerAwardables(behaviours, new CreateAdvancement[]{AllAdvancements.TRAIN_CASING, AllAdvancements.ANDESITE_CASING, AllAdvancements.BRASS_CASING, AllAdvancements.COPPER_CASING, AllAdvancements.FIST_BUMP, AllAdvancements.DEPLOYER, AllAdvancements.SELF_DEPLOYING});
    }

    public ReflectionValue<IItemHandlerModifiable, DeployerBlockEntity> getInvHandler(){
        return new ReflectionValue<>("invHandler",IItemHandlerModifiable.class,this, DeployerBlockEntity.class);
    }

    public void initHandler() {
        ReflectionUtil.runPrivateMethod("initHandler",null,this,DeployerBlockEntity.class,null);
    }

    public boolean isRedstoneLocked(){
        return super.redstoneLocked;
    }

    public int getTimer(){
        return super.timer;
    }
}
