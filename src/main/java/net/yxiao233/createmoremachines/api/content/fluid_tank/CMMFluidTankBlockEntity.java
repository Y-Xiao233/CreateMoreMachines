package net.yxiao233.createmoremachines.api.content.fluid_tank;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.yxiao233.createmoremachines.api.IPartialModelRenderer;
import net.yxiao233.createmoremachines.api.registry.BuiltInAdvancedMachineTypes;
import net.yxiao233.createmoremachines.api.registry.CMMPartialModelsRegistry;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.utils.ReflectionUtil;

public class CMMFluidTankBlockEntity extends FluidTankBlockEntity implements IPartialModelRenderer {
    private CMMTier tier;
    public CMMFluidTankBlockEntity(CMMTier tier, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tier = tier;
    }

    public boolean hasWindow(){
        return window;
    }

    @Override
    public void applyFluidTankSize(int blocks) {
        this.tankInventory.setCapacity(Math.min(Integer.MAX_VALUE, blocks * this.tier.getFluidCapability(BuiltInAdvancedMachineTypes.FLUID_TANK)));
        int overflow = this.tankInventory.getFluidAmount() - this.tankInventory.getCapacity();
        if (overflow > 0) {
            this.tankInventory.drain(overflow, IFluidHandler.FluidAction.EXECUTE);
        }

        this.forceFluidLevelUpdate = true;
    }

    public IFluidHandler getFluidCapability(){
        return this.fluidCapability;
    }

    public void superRefreshCapability() {
        ReflectionUtil.runPrivateMethod("refreshCapability", null, this, FluidTankBlockEntity.class, null, null);
    }

    @Override
    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(getFluidTankCapacity() * Math.max(1,getTotalTankSize()), this::onFluidStackChanged);
    }
    public int getFluidTankCapacity() {
        if(tier == null){
            this.tier = this.getBlockState().getBlock() instanceof CMMFluidTankBlock tankBlock ? tankBlock.getTier() : this.tier;
        }
        return tier.getFluidCapability(BuiltInAdvancedMachineTypes.FLUID_TANK);
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        if(this.tier != null){
            compound.putInt("fluid_capability", this.tier.getFluidCapability(BuiltInAdvancedMachineTypes.FLUID_TANK));
        }
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        if(compound.contains("fluid_capability")){
            int fluidCapability = compound.getInt("fluid_capability");
            if(this.tankInventory != null){
                this.getTankInventory().setCapacity(fluidCapability * this.getTotalTankSize());
            }
        }
    }

    @Override
    public PartialModel[] getPartialModels() {
        return CMMPartialModelsRegistry.getPartialModels(tier,"fluid_tank");
    }
}
