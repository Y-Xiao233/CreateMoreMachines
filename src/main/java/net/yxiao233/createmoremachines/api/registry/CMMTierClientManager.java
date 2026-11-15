package net.yxiao233.createmoremachines.api.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.yxiao233.createmoremachines.api.content.fluid_tank.CMMFluidTankModel;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;

@OnlyIn(Dist.CLIENT)
public final class CMMTierClientManager {
    public static void registerFluidTankModels() {
        CMMRegistryEntry.getFluidTanks().forEach((id, tank) -> {
            CMMTier tier = CMMTier.getTiers().get(id);
            if (tier == null) {
                return;
            }
            CreateRegistrate.blockModel(() -> model -> CMMFluidTankModel.byTier(model, tier)).accept(tank.get());
        });
    }
}
