package net.yxiao233.createmoremachines.common.registry;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import net.yxiao233.createmoremachines.CreateMoreMachines;

@SuppressWarnings("SameParameterValue")
public class CMMSpriteShifts {
    public static final CTSpriteShiftEntry CREATIVE_CASING = omni("creative_casing");
    public static final CTSpriteShiftEntry NETHERITE_CASING = omni("netherite_casing");
    private static CTSpriteShiftEntry omni(String name) {
        return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
    }
    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }
    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, CreateMoreMachines.makeId("block/" + blockTextureName), CreateMoreMachines.makeId("block/" + connectedTextureName + "_connected"));
    }

}
