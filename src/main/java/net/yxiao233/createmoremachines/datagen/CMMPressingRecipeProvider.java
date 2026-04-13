package net.yxiao233.createmoremachines.datagen;

import com.simibubi.create.api.data.recipe.PressingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.api.annotation.RecipeGen;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;

import java.util.concurrent.CompletableFuture;

@RecipeGen
public class CMMPressingRecipeProvider extends PressingRecipeGen {
    public CMMPressingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreateMoreMachines.MODID);

        this.create("netherite_sheet", builder ->{
            return builder.require(CMMRegistryEntry.NETHERITE_ALLOY).output(CMMRegistryEntry.NETHERITE_ALLOY_SHEET);
        });
        this.create("end_sheet", builder ->{
            return builder.require(CMMRegistryEntry.END_ALLOY).output(CMMRegistryEntry.END_ALLOY_SHEET);
        });
        this.create("beyond_sheet", builder ->{
            return builder.require(CMMRegistryEntry.BEYOND_ALLOY).output(CMMRegistryEntry.BEYOND_ALLOY_SHEET);
        });
    }
}
