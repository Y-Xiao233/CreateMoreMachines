package net.yxiao233.createmoremachines.datagen.content;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.Nullable;

public class CMMAssetLookup {
    public static ModelFile partialBaseModel(DataGenContext<?, ?> ctx, RegistrateBlockstateProvider prov, String pathPrefix, @Nullable String namePrefix) {
        String location = "block/" + pathPrefix + "/" + ctx.getName();
        if(namePrefix == null || namePrefix.isEmpty()){
            return prov.models().getExistingFile(prov.modLoc(location));
        }
        location = location + namePrefix;
        return prov.models().getExistingFile(prov.modLoc(location));
    }

    public static ModelFile partialBaseModel(DataGenContext<?, ?> ctx, RegistrateBlockstateProvider prov, String pathPrefix) {
        return partialBaseModel(ctx,prov,pathPrefix,null);
    }
}
