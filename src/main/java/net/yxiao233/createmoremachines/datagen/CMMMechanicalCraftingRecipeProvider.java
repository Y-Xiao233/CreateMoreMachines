package net.yxiao233.createmoremachines.datagen;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeGen;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.api.annotation.RecipeGen;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;

import java.util.concurrent.CompletableFuture;

@RecipeGen
public class CMMMechanicalCraftingRecipeProvider extends MechanicalCraftingRecipeGen {
    public CMMMechanicalCraftingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreateMoreMachines.MODID);
        this.create(CMMRegistryEntry.BEYOND_ALLOY::get).returns(1).recipe(builder -> {
            return builder
                    .key('A', AllItems.ANDESITE_ALLOY)
                    .key('B', CommonMetal.BRASS.ingots)
                    .key('N', CMMRegistryEntry.NETHERITE_ALLOY)
                    .key('E', CMMRegistryEntry.END_ALLOY)
                    .patternLine("AAAA")
                    .patternLine("ANBA")
                    .patternLine("ABEA")
                    .patternLine("AAAA")
                    .disallowMirrored();
        });
    }
}
