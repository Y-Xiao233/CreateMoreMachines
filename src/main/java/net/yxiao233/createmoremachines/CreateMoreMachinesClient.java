package net.yxiao233.createmoremachines;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.yxiao233.createmoremachines.api.registry.CMMTierManager;

@Mod(value = "createmoremachines", dist = Dist.CLIENT)
@SuppressWarnings("unused")
public class CreateMoreMachinesClient {
    public CreateMoreMachinesClient(IEventBus modEventBus) {
        modEventBus.addListener(CreateMoreMachinesClient::clientInit);
    }
    public static void clientInit(FMLClientSetupEvent event) {
        CMMTierManager.registryModels();
    }
}
