package net.yxiao233.createmoremachines.compact.jei;

import com.simibubi.create.Create;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;
import net.yxiao233.createmoremachines.utils.MapUtil;
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
        MapUtil.valueList(CMMRegistryEntry.getMechanicalMixers()).forEach(mixer ->{
            registration.addRecipeCatalyst(mixer, mixing);
            registration.addRecipeCatalyst(mixer, automaticShapeless);
            registration.addRecipeCatalyst(mixer, automaticBrewing);
        });
        MapUtil.valueList(CMMRegistryEntry.getDepots()).forEach(depot ->{
            registration.addRecipeCatalyst(depot, deploying);
        });
        MapUtil.valueList(CMMRegistryEntry.getDeployers()).forEach(deployer ->{
            registration.addRecipeCatalyst(deployer, deploying);
        });
        MapUtil.valueList(CMMRegistryEntry.getMechanicalPresses()).forEach(press ->{
            registration.addRecipeCatalyst(press, pressing);
            registration.addRecipeCatalyst(press, packing);
            registration.addRecipeCatalyst(press, automaticPacking);
        });
        MapUtil.valueList(CMMRegistryEntry.getSpouts()).forEach(spout ->{
            registration.addRecipeCatalyst(spout, spoutFilling);
        });
        MapUtil.valueList(CMMRegistryEntry.getBasins()).forEach(basin ->{
            registration.addRecipeCatalyst(basin, automaticShapeless);
            registration.addRecipeCatalyst(basin, automaticBrewing);
            registration.addRecipeCatalyst(basin, mixing);
            registration.addRecipeCatalyst(basin, packing);
            registration.addRecipeCatalyst(basin, automaticPacking);
        });
    }
}
