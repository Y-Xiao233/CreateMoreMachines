package net.yxiao233.createmoremachines.api.content.basin;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.utils.ReflectionUtil;
import net.yxiao233.createmoremachines.utils.TankCapabilityHelper;

public class CMMBasinBlockEntity extends BasinBlockEntity {
    private final CMMTier tier;
    private final TankCapabilityHelper capabilityHelper;
    public CMMBasinBlockEntity(CMMTier tier, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tier = tier;
        this.capabilityHelper = new TankCapabilityHelper(this.inputTank,this.outputTank);
        this.capabilityHelper.setCapability(tier.getFluidCapability());
    }

    public CMMTier getTier() {
        return tier;
    }

    public IItemHandlerModifiable getItemCapability(){
        return this.itemCapability;
    }

    public IFluidHandler getFluidCapability(){
        return this.fluidCapability;
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
            if(this.capabilityHelper != null && this.outputTank != null && this.inputTank != null){
                this.capabilityHelper.setCapability(fluidCapability);
            }
        }
    }
}
