package net.yxiao233.createmoremachines.datagen;

import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.api.annotation.RecipeGen;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;

import java.util.concurrent.CompletableFuture;

@RecipeGen
public class CMMMixingRecipeProvider extends MixingRecipeGen {
    public CMMMixingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreateMoreMachines.MODID);
        this.create("netherite_alloy", (builder) -> {
            return builder
                    .require(Tags.Items.INGOTS_NETHERITE)
                    .require(Tags.Items.GEMS_QUARTZ)
                    .require(Tags.Items.NETHER_STARS)
                    .require(SizedFluidIngredient.of(CMMRegistryEntry.BINDER.getSourceFluid().get(),2000))
                    .output(CMMRegistryEntry.NETHERITE_ALLOY,1)
                    .requiresHeat(HeatCondition.HEATED);
        });

        this.create("end_alloy", (builder) -> {
            return builder
                    .require(Items.DRAGON_BREATH)
                    .require(Items.DRAGON_HEAD)
                    .require(Items.CHORUS_FRUIT)
                    .require(SizedFluidIngredient.of(CMMRegistryEntry.BINDER.getSourceFluid().get(), 4000))
                    .output(CMMRegistryEntry.END_ALLOY,1)
                    .requiresHeat(HeatCondition.SUPERHEATED);
        });

        this.create("pure_water", (builder) -> {
            return builder
                    .require(Tags.Fluids.WATER, 20)
                    .require(Tags.Fluids.LAVA, 1)
                    .output(CMMRegistryEntry.PURE_WATER.getSourceFluid().get(), 20)
                    .requiresHeat(HeatCondition.HEATED);
        });

        this.create("binder", (builder) -> {
            return builder
                    .require(SizedFluidIngredient.of(CMMRegistryEntry.PURE_WATER.getSourceFluid().get(), 1000))
                    .require(Tags.Items.SLIME_BALLS)
                    .require(Items.MAGMA_CREAM)
                    .output(CMMRegistryEntry.BINDER.getSourceFluid().get(), 1000)
                    .requiresHeat(HeatCondition.HEATED);
        });
    }
}
