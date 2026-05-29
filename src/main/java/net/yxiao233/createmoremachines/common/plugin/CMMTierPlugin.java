package net.yxiao233.createmoremachines.common.plugin;

import net.yxiao233.createmoremachines.CMMConfig;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.api.registry.*;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;

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
        BRASS = CMMTier.create(CreateMoreMachines.makeId("brass"),2, CMMRegistryEntry.NETHERITE_ALLOY_SHEET)
                .fromConfig(CMMConfig.BRASS)
                .defaultRenderer();
        NETHERITE = CMMTier.create(CreateMoreMachines.makeId("netherite"),3, CMMRegistryEntry.END_ALLOY_SHEET)
                .fromConfig(CMMConfig.NETHERITE)
                .defaultRenderer();
        END = CMMTier.create(CreateMoreMachines.makeId("end"),4, CMMRegistryEntry.BEYOND_ALLOY_SHEET)
                .fromConfig(CMMConfig.END)
                .defaultRenderer();
        BEYOND = CMMTier.create(CreateMoreMachines.makeId("beyond"),5, null)
                .fromConfig(CMMConfig.BEYOND)
                .defaultRenderer();
        CREATIVE = CMMTier.create(CreateMoreMachines.makeId("creative"),-1, null)
                .fromConfig(CMMConfig.CREATIVE)
                .defaultRenderer();
    }

    @Override
    public void onPostRegisterTier() {
        //Temp disabled
        BuiltInAdvancedMachineTypes.STEAM_ENGINE.withoutAll();
        BuiltInAdvancedMachineTypes.FLUID_TANK.without(BRASS, NETHERITE, END, BEYOND);
        BuiltInAdvancedMachineTypes.SAW.withoutAll();

        CREATIVE.without(BuiltInAdvancedMachineTypes.FLUID_TANK);
    }
}
