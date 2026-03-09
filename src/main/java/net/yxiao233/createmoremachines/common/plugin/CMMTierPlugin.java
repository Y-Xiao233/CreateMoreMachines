package net.yxiao233.createmoremachines.common.plugin;

import net.yxiao233.createmoremachines.CMMConfig;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.api.registry.*;

@SuppressWarnings("unused")
@CMMPlugin
public class CMMTierPlugin implements ICMMPlugin {

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
        CMMTier.create(CreateMoreMachines.makeId("brass"))
                .fromConfig(CMMConfig.BRASS)
                .defaultRenderer();
        CMMTier.create(CreateMoreMachines.makeId("netherite"))
                .fromConfig(CMMConfig.NETHERITE)
                .defaultRenderer();
        CMMTier.create(CreateMoreMachines.makeId("end"))
                .fromConfig(CMMConfig.END)
                .defaultRenderer();
        CMMTier.create(CreateMoreMachines.makeId("beyond"))
                .fromConfig(CMMConfig.BEYOND)
                .defaultRenderer();
        CMMTier.create(CreateMoreMachines.makeId("creative"))
                .fromConfig(CMMConfig.CREATIVE)
                .defaultRenderer();
    }
}
