package net.yxiao233.createmoremachines.api.content.basin;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.utils.TankCapabilityHelper;

public class CMMBasinBlockEntity extends BasinBlockEntity {
    private final CMMTier tier;
    private final TankCapabilityHelper capabilityHelper;
    public CMMBasinBlockEntity(CMMTier tier, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tier = tier;
        this.capabilityHelper = new TankCapabilityHelper(this.inputTank,this.outputTank);
        this.capabilityHelper.setCapability(tier.getFluidCapability());

        this.inputInventory.withMaxStackSize(tier.getItemCapability());
        this.outputInventory.withMaxStackSize(tier.getItemCapability());
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
            compound.putInt("item_capability", this.tier.getItemCapability());
        }
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        if(compound.contains("fluid_capability")){
            int fluidCapability = compound.getInt("fluid_capability");
            int itemCapability = compound.getInt("item_capability");
            if(this.capabilityHelper != null && this.outputTank != null && this.inputTank != null){
                this.capabilityHelper.setCapability(fluidCapability);
            }
            if(this.inputInventory != null && this.outputInventory != null){
                this.inputInventory.withMaxStackSize(itemCapability);
                this.outputInventory.withMaxStackSize(itemCapability);
            }
        }
    }
}
