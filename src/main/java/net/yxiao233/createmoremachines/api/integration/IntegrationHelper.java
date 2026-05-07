package net.yxiao233.createmoremachines.api.integration;

import net.neoforged.fml.loading.LoadingModList;

public class IntegrationHelper {
    public static boolean isModLoaded(String modId){
        return !LoadingModList.get().getMods().stream().filter(info -> info.getModId().equals(modId)).toList().isEmpty();
    }
}
