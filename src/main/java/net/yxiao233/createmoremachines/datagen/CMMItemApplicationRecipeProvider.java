package net.yxiao233.createmoremachines.datagen;

import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.ItemApplicationRecipeGen;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.api.annotation.RecipeGen;
import net.yxiao233.createmoremachines.api.registry.BuiltInAdvancedMachineTypes;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;

import java.util.Map;
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

        mechanicalApplicationRecipes(BuiltInAdvancedMachineTypes.BASIN);
        mechanicalApplicationRecipes(BuiltInAdvancedMachineTypes.DEPOT);
        mechanicalApplicationRecipes(BuiltInAdvancedMachineTypes.DEPLOYER);
        mechanicalApplicationRecipes(BuiltInAdvancedMachineTypes.MIXER);
        mechanicalApplicationRecipes(BuiltInAdvancedMachineTypes.PRESS);
        mechanicalApplicationRecipes(BuiltInAdvancedMachineTypes.SPOUT);
    }

    private <T extends Block> void  mechanicalApplicationRecipes(BuiltInAdvancedMachineTypes.AdvancedMachineType<T> type){
        mechanicalApplicationRecipe(type, AllItems.BRASS_SHEET,null,"brass");
        mechanicalApplicationRecipe(type, CMMRegistryEntry.NETHERITE_ALLOY_SHEET,"brass","netherite");
        mechanicalApplicationRecipe(type, CMMRegistryEntry.END_ALLOY_SHEET,"netherite","end");
        mechanicalApplicationRecipe(type, CMMRegistryEntry.BEYOND_ALLOY_SHEET,"end","beyond");
    }


    private <T extends Block> void mechanicalApplicationRecipe(BuiltInAdvancedMachineTypes.AdvancedMachineType<T> type, ItemEntry<?> material, String requireId, String resultId){
        BlockEntry<?> require;
        if(requireId == null){
            require = type.getMechanical();
        }else{
            require = type.getAdvancedMechanicals().get(CreateMoreMachines.makeId(requireId));
        }
        BlockEntry<?> result = type.getAdvancedMechanicals().get(CreateMoreMachines.makeId(resultId));
        this.create(type.getName() + "/" + resultId, builder ->{
            return builder
                    .require(require)
                    .require(material)
                    .output(result);
        });
    }
}
