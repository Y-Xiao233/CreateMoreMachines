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
        CREATIVE.without(BuiltInAdvancedMachineType.FLUID_TANK);
    }
}
