package net.yxiao233.createmoremachines.common.event;

import com.simibubi.create.AllBlockEntityTypes;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;

@SuppressWarnings({"removal","unused"})
@EventBusSubscriber(modid = CreateMoreMachines.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RegisterCapabilitiesHandler {
    @SubscribeEvent
    public static void onRegister(RegisterCapabilitiesEvent event){
        CMMRegistryEntry.getDepotEntities().forEach((id, type) ->{
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type.get(), (be, context) ->{
                return be.getBehaviour().itemHandler;
            });
        });
        CMMRegistryEntry.getSpoutEntities().forEach((id, type) ->{
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, type.get(), (be, context) ->{
                return be.tank.getCapability();
            });
        });
        CMMRegistryEntry.getBasinEntities().forEach((id, type) ->{
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, type.get(), (be, context) ->{
                return be.getFluidCapability();
            });
        });
        CMMRegistryEntry.getBasinEntities().forEach((id, type) ->{
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type.get(), (be, context) ->{
                return be.getItemCapability();
            });
        });

        CMMRegistryEntry.getDeployerEntities().forEach((id,type) ->{
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type.get(), (be, context) -> {
                if (be.getInvHandler().getValue() == null) {
                    be.initHandler();
                }

                return be.getInvHandler().getValue();
            });
        });
    }
}
