package net.yxiao233.createmoremachines.compact.kubejs;

import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.yxiao233.createmoremachines.compact.kubejs.event.CMMEventJS;

public class CMMKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(CMMEventJS.GROUP);
    }

    @Override
    public void initStartup() {
        CMMEventJS.RegistryTierEventJS event = new CMMEventJS.RegistryTierEventJS();
        CMMEventJS.REGISTRY_TIER.post(ScriptType.STARTUP, event);
    }
}
