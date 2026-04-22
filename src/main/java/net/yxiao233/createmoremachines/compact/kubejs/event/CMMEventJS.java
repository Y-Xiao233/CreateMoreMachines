package net.yxiao233.createmoremachines.compact.kubejs.event;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class CMMEventJS {
    public static final EventGroup GROUP = EventGroup.of("CMMEvent");
    public static final EventHandler REGISTRY_TIER = GROUP.startup("registryTier",() -> RegistryTierEventJS.class);

    public static class RegistryTierEventJS implements KubeEvent {
        @HideFromJS
        public static final List<KubeCMMTier> ENTRIES = new ArrayList<>();
        public void add(String path, int itemCapability, int fluidCapability, int processingMultiple, int deployerProcessingMultiple, double mechanicalPressImpact, double mechanicalMixerImpact, double deployerImpact){
            ENTRIES.add(new KubeCMMTier(ResourceLocation.fromNamespaceAndPath("kubejs",path), itemCapability, fluidCapability, processingMultiple, deployerProcessingMultiple, mechanicalPressImpact, mechanicalMixerImpact, deployerImpact));
        }
    }

    public record KubeCMMTier(ResourceLocation id, int itemCapability, int fluidCapability, int processingMultiple, int deployerProcessingMultiple, double mechanicalPressImpact, double mechanicalMixerImpact, double deployerImpact){

    }
}
