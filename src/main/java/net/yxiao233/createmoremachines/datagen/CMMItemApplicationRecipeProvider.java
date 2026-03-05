package net.yxiao233.createmoremachines.datagen;

import com.simibubi.create.api.data.recipe.ItemApplicationRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.api.annotation.RecipeGen;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;

import java.util.concurrent.CompletableFuture;

@RecipeGen
public class CMMItemApplicationRecipeProvider extends ItemApplicationRecipeGen {
    public CMMItemApplicationRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CreateMoreMachines.MODID);
        this.create("netherite_casing", builder ->{
            return builder
                    .require(Items.NETHER_BRICKS)
                    .require(CMMRegistryEntry.NETHERITE_ALLOY)
                    .output(CMMRegistryEntry.NETHERITE_CASING);
        });
        this.create("end_casing", builder ->{
            return builder
                    .require(Items.END_STONE_BRICKS)
                    .require(CMMRegistryEntry.END_ALLOY)
                    .output(CMMRegistryEntry.END_CASING);
        });
        this.create("beyond_casing", builder ->{
            return builder
                    .require(Items.DRAGON_EGG)
                    .require(CMMRegistryEntry.BEYOND_ALLOY)
                    .output(CMMRegistryEntry.BEYOND_CASING);
        });
    }
}
