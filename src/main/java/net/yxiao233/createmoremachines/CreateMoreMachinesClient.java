package net.yxiao233.createmoremachines;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.yxiao233.createmoremachines.api.registry.CMMTierClientManager;
import net.yxiao233.createmoremachines.api.registry.CMMTierManager;

@Mod(value = "createmoremachines", dist = Dist.CLIENT)
@SuppressWarnings("unused")
public class CreateMoreMachinesClient {
    public CreateMoreMachinesClient(IEventBus modEventBus) {
        CMMTierManager.registryModels();
        modEventBus.addListener(this::clientSetup);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(CMMTierClientManager::registerFluidTankModels);
    }
}
