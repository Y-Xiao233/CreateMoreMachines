package net.yxiao233.createmoremachines.datagen.content;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class CMMAssetLookup {
    public static ModelFile partialBaseModel(DataGenContext<?, ?> ctx, RegistrateBlockstateProvider prov, String prefix) {
        String location = "block/" + prefix + "/" + ctx.getName();
        return prov.models().getExistingFile(prov.modLoc(location));
    }
}
