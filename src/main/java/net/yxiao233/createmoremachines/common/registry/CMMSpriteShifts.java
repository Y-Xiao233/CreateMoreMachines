package net.yxiao233.createmoremachines.common.registry;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.api.registry.FluidTankCTSpriteShiftEntry;

import java.util.ArrayList;

@SuppressWarnings("SameParameterValue")
public class CMMSpriteShifts {
    public static final CTSpriteShiftEntry CREATIVE_CASING = omni("creative_casing");
    public static final CTSpriteShiftEntry NETHERITE_CASING = omni("netherite_casing");
    public static final CTSpriteShiftEntry END_CASING = omni("end_casing");
    public static final CTSpriteShiftEntry BEYOND_CASING = omni("beyond_casing");
    public static final FluidTankCTSpriteShiftEntry BRASS_FLUID_TANK = tanks("brass");
    public static final FluidTankCTSpriteShiftEntry NETHERITE_FLUID_TANK = tanks("netherite");
    public static final FluidTankCTSpriteShiftEntry END_FLUID_TANK = tanks("end");
    public static final FluidTankCTSpriteShiftEntry BEYOND_FLUID_TANK = tanks("beyond");
    private static CTSpriteShiftEntry omni(String name) {
        return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
    }
    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }
    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, CreateMoreMachines.makeId("block/" + blockTextureName), CreateMoreMachines.makeId("block/" + connectedTextureName + "_connected"));
    }
    private static FluidTankCTSpriteShiftEntry tanks(String tierId){
        CTSpriteShiftEntry tank = getCT(AllCTTypes.RECTANGLE,tierId + "_fluid_tank");
        CTSpriteShiftEntry top = getCT(AllCTTypes.RECTANGLE,tierId + "_fluid_tank_top");
        CTSpriteShiftEntry inner = getCT(AllCTTypes.RECTANGLE,tierId + "_fluid_tank_inner");
        return new FluidTankCTSpriteShiftEntry(tank,top,inner);
    }
}
