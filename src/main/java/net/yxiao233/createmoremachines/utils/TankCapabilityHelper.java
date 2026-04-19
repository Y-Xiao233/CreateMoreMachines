package net.yxiao233.createmoremachines.utils;

import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;

public class TankCapabilityHelper {
    private final SmartFluidTankBehaviour input;
    private final SmartFluidTankBehaviour output;
    public TankCapabilityHelper(SmartFluidTankBehaviour input, SmartFluidTankBehaviour output){
        this.input = input;
        this.output = output;
    }

    public void setCapability(int capability){
        capability = Math.max(0,capability);
        for (SmartFluidTankBehaviour.TankSegment tank : this.input.getTanks()) {
            SmartFluidTank fluidTank = ReflectionUtil.getPrivateField("tank", SmartFluidTank.class, tank, SmartFluidTankBehaviour.TankSegment.class);
            if(fluidTank != null){
                fluidTank.setCapacity(capability);
            }
        }
        for (SmartFluidTankBehaviour.TankSegment tank : this.output.getTanks()) {
            SmartFluidTank fluidTank = ReflectionUtil.getPrivateField("tank", SmartFluidTank.class, tank, SmartFluidTankBehaviour.TankSegment.class);
            if(fluidTank != null){
                fluidTank.setCapacity(capability);
            }
        }
    }
}
