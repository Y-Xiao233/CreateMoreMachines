package net.yxiao233.createmoremachines.common.plugin;

import net.yxiao233.createmoremachines.CMMConfig;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.api.registry.*;

@SuppressWarnings("unused")
@CMMPlugin
public class CMMTierPlugin implements ICMMPlugin {
    public static CMMTier BRASS;
    public static CMMTier NETHERITE;
    public static CMMTier END;
    public static CMMTier BEYOND;
    public static CMMTier CREATIVE;

    @Override
    public void registryPartialModels() {
        CMMPartialModelsRegistry.registrySpouts();
        CMMPartialModelsRegistry.registryMixers();
    }

    @Override
    public void registryRegistrate() {
        CMMTier.of(CreateMoreMachines.registrate());
    }

    @Override
    public void registryTiers() {
        BRASS = CMMTier.create(CreateMoreMachines.makeId("brass"))
                .fromConfig(CMMConfig.BRASS)
                .defaultRenderer();
        NETHERITE = CMMTier.create(CreateMoreMachines.makeId("netherite"))
                .fromConfig(CMMConfig.NETHERITE)
                .defaultRenderer();
        END = CMMTier.create(CreateMoreMachines.makeId("end"))
                .fromConfig(CMMConfig.END)
                .defaultRenderer();
        BEYOND = CMMTier.create(CreateMoreMachines.makeId("beyond"))
                .fromConfig(CMMConfig.BEYOND)
                .defaultRenderer();
        CREATIVE = CMMTier.create(CreateMoreMachines.makeId("creative"))
                .fromConfig(CMMConfig.CREATIVE)
                .defaultRenderer();
    }

    @Override
    public void onPostRegisterTier() {
        //Temp disabled
        BRASS.without(BuiltInAdvancedMachineTypes.FLUID_TANK, BuiltInAdvancedMachineTypes.STEAM_ENGINE);
        NETHERITE.without(BuiltInAdvancedMachineTypes.FLUID_TANK, BuiltInAdvancedMachineTypes.STEAM_ENGINE);
        END.without(BuiltInAdvancedMachineTypes.FLUID_TANK, BuiltInAdvancedMachineTypes.STEAM_ENGINE);
        BEYOND.without(BuiltInAdvancedMachineTypes.FLUID_TANK, BuiltInAdvancedMachineTypes.STEAM_ENGINE);
        CREATIVE.without(BuiltInAdvancedMachineTypes.STEAM_ENGINE);


        CREATIVE.without(BuiltInAdvancedMachineTypes.FLUID_TANK);
    }
}
