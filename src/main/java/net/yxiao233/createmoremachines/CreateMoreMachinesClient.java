package net.yxiao233.createmoremachines;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.yxiao233.createmoremachines.api.registry.CMMTierManager;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;

@Mod(value = "createmoremachines", dist = Dist.CLIENT)
@SuppressWarnings("unused")
public class CreateMoreMachinesClient {
    public CreateMoreMachinesClient(IEventBus modEventBus) {
        modEventBus.addListener(CreateMoreMachinesClient::clientInit);
    }

    @SuppressWarnings("deprecation")
    public static void clientInit(FMLClientSetupEvent event) {
        CMMTierManager.registryModels();
        CMMRegistryEntry.getMechanicalMixers().forEach((id, mixer) ->{
            ItemBlockRenderTypes.setRenderLayer(mixer.get(), RenderType.cutoutMipped());
        });
        CMMRegistryEntry.getSpouts().forEach((id, spout) ->{
            ItemBlockRenderTypes.setRenderLayer(spout.get(), RenderType.cutoutMipped());
        });
        CMMRegistryEntry.getBasins().forEach((id, basin) ->{
            ItemBlockRenderTypes.setRenderLayer(basin.get(), RenderType.cutoutMipped());
        });
        CMMRegistryEntry.getFluidTanks().forEach((id, tank) ->{
            ItemBlockRenderTypes.setRenderLayer(tank.get(), RenderType.cutoutMipped());
        });
    }
}
