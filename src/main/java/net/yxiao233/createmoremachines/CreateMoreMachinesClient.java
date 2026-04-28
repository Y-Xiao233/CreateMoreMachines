package net.yxiao233.createmoremachines;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.yxiao233.createmoremachines.api.registry.CMMTierManager;

@Mod(value = "createmoremachines", dist = Dist.CLIENT)
@SuppressWarnings("unused")
public class CreateMoreMachinesClient {
    public CreateMoreMachinesClient(IEventBus modEventBus) {
        CMMTierManager.registryModels();
    }
}
