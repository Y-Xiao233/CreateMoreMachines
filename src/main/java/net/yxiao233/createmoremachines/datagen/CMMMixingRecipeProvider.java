package net.yxiao233.createmoremachines.datagen;

import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.Tags;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;

import java.util.concurrent.CompletableFuture;

public class CMMMixingRecipeProvider extends MixingRecipeGen {
    public CMMMixingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreateMoreMachines.MODID);
        this.create("netherite_alloy", (builder) -> {
            return builder
                    .require(Tags.Items.INGOTS_NETHERITE)
                    .require(Tags.Items.GEMS_QUARTZ)
                    .output(CMMRegistryEntry.NETHERITE_ALLOY,1)
                    .requiresHeat(HeatCondition.SUPERHEATED);
        });
    }
}
