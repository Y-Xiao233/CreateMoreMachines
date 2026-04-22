package net.yxiao233.createmoremachines.compact.kubejs.plugin;

import net.neoforged.fml.loading.LoadingModList;
import net.yxiao233.createmoremachines.api.registry.CMMPlugin;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.api.registry.ICMMPlugin;
import net.yxiao233.createmoremachines.compact.kubejs.event.CMMEventJS;

@CMMPlugin
public class KubeCMMPlugin implements ICMMPlugin {
    public static boolean isLoaded = !LoadingModList.get().getMods().stream().filter(info -> info.getModId().equals("kubejs")).toList().isEmpty();
    @Override
    public boolean shouldLoad() {
        return isLoaded;
    }

    @Override
    public void registryRegistrate() {
        CMMTier.createRegistrate("kubejs");
    }

    @Override
    public void registryTiers() {
        CMMEventJS.RegistryTierEventJS.ENTRIES.forEach(tier ->{
            CMMTier.create(tier.id())
                    .setItemCapability(tier.itemCapability())
                    .setFluidCapability(tier.fluidCapability())
                    .setProcessingMultiple(tier.processingMultiple())
                    .setMechanicalPressImpact(tier.mechanicalPressImpact())
                    .setMechanicalMixerImpact(tier.mechanicalMixerImpact())
                    .setDeployerImpact(tier.deployerImpact())
                    .setDeployerProcessingMultiple(tier.deployerProcessingMultiple())
                    .defaultRenderer();
        });
    }
}
