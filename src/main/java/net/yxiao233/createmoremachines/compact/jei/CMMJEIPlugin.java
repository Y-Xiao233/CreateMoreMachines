package net.yxiao233.createmoremachines.compact.jei;

import com.simibubi.create.Create;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class CMMJEIPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return CreateMoreMachines.makeId("jei");
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        RecipeType<?> mixing = RecipeType.createRecipeHolderType(Create.asResource("mixing"));
        RecipeType<?> automaticShapeless = RecipeType.createRecipeHolderType(Create.asResource("automatic_shapeless"));
        RecipeType<?> automaticBrewing = RecipeType.createRecipeHolderType(Create.asResource("automatic_brewing"));
        RecipeType<?> pressing = RecipeType.createRecipeHolderType(Create.asResource("pressing"));
        RecipeType<?> deploying = RecipeType.createRecipeHolderType(Create.asResource("deploying"));
        RecipeType<?> packing = RecipeType.createRecipeHolderType(Create.asResource("packing"));
        RecipeType<?> automaticPacking = RecipeType.createRecipeHolderType(Create.asResource("automatic_packing"));
        RecipeType<?> spoutFilling = RecipeType.createRecipeHolderType(Create.asResource("spout_filling"));
        CMMRegistryEntry.getMechanicalMixers().values().stream().toList().forEach(mixer ->{
            registration.addRecipeCatalyst(mixer, mixing);
            registration.addRecipeCatalyst(mixer, automaticShapeless);
            registration.addRecipeCatalyst(mixer, automaticBrewing);
        });
        CMMRegistryEntry.getDepots().values().stream().toList().forEach(depot ->{
            registration.addRecipeCatalyst(depot, deploying);
        });
        CMMRegistryEntry.getMechanicalPresses().values().stream().toList().forEach(press ->{
            registration.addRecipeCatalyst(press, pressing);
            registration.addRecipeCatalyst(press, packing);
            registration.addRecipeCatalyst(press, automaticPacking);
        });
        CMMRegistryEntry.getSpouts().values().stream().toList().forEach(spout ->{
            registration.addRecipeCatalyst(spout, spoutFilling);
        });
    }
}
