package net.yxiao233.createmoremachines.api.content.fluid_tank;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.utils.ReflectionUtil;

public class CMMFluidTankBlockEntity extends FluidTankBlockEntity {
    private final CMMTier tier;
    public CMMFluidTankBlockEntity(CMMTier tier, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tier = tier;
        this.getTankInventory().setCapacity(tier.getFluidCapability());
    }

    @Override
    public void applyFluidTankSize(int blocks) {
        this.tankInventory.setCapacity(Math.min(Integer.MAX_VALUE, blocks * this.tier.getFluidCapability()));
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
        ReflectionUtil.runPrivateMethod("refreshCapability", null, this, FluidTankBlockEntity.class, null);
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        if(this.tier != null){
            compound.putInt("fluid_capability", this.tier.getFluidCapability());
        }
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        if(compound.contains("fluid_capability")){
            int fluidCapability = compound.getInt("fluid_capability");
            if(this.getTankInventory() != null){
                this.getTankInventory().setCapacity(fluidCapability);
            }
        }
    }
}
