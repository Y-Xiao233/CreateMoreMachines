package net.yxiao233.createmoremachines.datagen;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.api.annotation.RecipeGen;
import net.yxiao233.createmoremachines.api.content.basin.CMMBasinBlock;
import net.yxiao233.createmoremachines.api.content.depot.CMMDepotBlock;
import net.yxiao233.createmoremachines.api.content.mechanical.deployer.CMMDeployerBlock;
import net.yxiao233.createmoremachines.api.content.mechanical.mixer.CMMMechanicalMixerBlock;
import net.yxiao233.createmoremachines.api.content.mechanical.press.CMMMechanicalPressBlock;
import net.yxiao233.createmoremachines.api.content.spout.CMMSpoutBlock;
import net.yxiao233.createmoremachines.api.registry.BuiltInAdvancedMachineTypes;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RecipeGen
public class CMMBaseRecipeProvider extends RecipeProvider {
    public CMMBaseRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMMRegistryEntry.NETHERITE_ALLOY_BLOCK)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', CMMRegistryEntry.NETHERITE_ALLOY)
                .unlockedBy("has_item",has(CMMRegistryEntry.NETHERITE_ALLOY))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMMRegistryEntry.END_ALLOY_BLOCK)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', CMMRegistryEntry.END_ALLOY)
                .unlockedBy("has_item",has(CMMRegistryEntry.END_ALLOY))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CMMRegistryEntry.BEYOND_ALLOY_BLOCK)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', CMMRegistryEntry.BEYOND_ALLOY)
                .unlockedBy("has_item",has(CMMRegistryEntry.BEYOND_ALLOY))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CMMRegistryEntry.NETHERITE_ALLOY, 9)
                .requires(CMMRegistryEntry.NETHERITE_ALLOY_BLOCK)
                .unlockedBy("has_item",has(CMMRegistryEntry.NETHERITE_ALLOY_BLOCK))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CMMRegistryEntry.END_ALLOY, 9)
                .requires(CMMRegistryEntry.END_ALLOY_BLOCK)
                .unlockedBy("has_item",has(CMMRegistryEntry.END_ALLOY_BLOCK))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CMMRegistryEntry.BEYOND_ALLOY, 9)
                .requires(CMMRegistryEntry.BEYOND_ALLOY_BLOCK)
                .unlockedBy("has_item",has(CMMRegistryEntry.BEYOND_ALLOY_BLOCK))
                .save(recipeOutput);



        mechanicalCraftingRecipe(CMMRegistryEntry.getBasins(), AllBlocks.BASIN, AllItems.BRASS_SHEET, null, "brass").save(recipeOutput);
        mechanicalCraftingRecipe(CMMRegistryEntry.getBasins(), null, CMMRegistryEntry.NETHERITE_ALLOY_SHEET, "brass", "netherite").save(recipeOutput);
        mechanicalCraftingRecipe(CMMRegistryEntry.getBasins(), null, CMMRegistryEntry.END_ALLOY_SHEET, "netherite", "end").save(recipeOutput);
        mechanicalCraftingRecipe(CMMRegistryEntry.getBasins(), null, CMMRegistryEntry.BEYOND_ALLOY_SHEET, "end", "beyond").save(recipeOutput);

        mechanicalCraftingRecipe(CMMRegistryEntry.getDepots(), AllBlocks.DEPOT, AllItems.BRASS_SHEET, null, "brass").save(recipeOutput);
        mechanicalCraftingRecipe(CMMRegistryEntry.getDepots(), null, CMMRegistryEntry.NETHERITE_ALLOY_SHEET, "brass", "netherite").save(recipeOutput);
        mechanicalCraftingRecipe(CMMRegistryEntry.getDepots(), null, CMMRegistryEntry.END_ALLOY_SHEET, "netherite", "end").save(recipeOutput);
        mechanicalCraftingRecipe(CMMRegistryEntry.getDepots(), null, CMMRegistryEntry.BEYOND_ALLOY_SHEET, "end", "beyond").save(recipeOutput);

        mechanicalCraftingRecipe(CMMRegistryEntry.getDeployers(), AllBlocks.DEPLOYER, AllItems.BRASS_SHEET, null, "brass").save(recipeOutput);
        mechanicalCraftingRecipe(CMMRegistryEntry.getDeployers(), null, CMMRegistryEntry.NETHERITE_ALLOY_SHEET, "brass", "netherite").save(recipeOutput);
        mechanicalCraftingRecipe(CMMRegistryEntry.getDeployers(), null, CMMRegistryEntry.END_ALLOY_SHEET, "netherite", "end").save(recipeOutput);
        mechanicalCraftingRecipe(CMMRegistryEntry.getDeployers(), null, CMMRegistryEntry.BEYOND_ALLOY_SHEET, "end", "beyond").save(recipeOutput);

        mechanicalCraftingRecipe(CMMRegistryEntry.getMechanicalMixers(), AllBlocks.MECHANICAL_MIXER, AllItems.BRASS_SHEET, null, "brass").save(recipeOutput);
        mechanicalCraftingRecipe(CMMRegistryEntry.getMechanicalMixers(), null, CMMRegistryEntry.NETHERITE_ALLOY_SHEET, "brass", "netherite").save(recipeOutput);
        mechanicalCraftingRecipe(CMMRegistryEntry.getMechanicalMixers(), null, CMMRegistryEntry.END_ALLOY_SHEET, "netherite", "end").save(recipeOutput);
        mechanicalCraftingRecipe(CMMRegistryEntry.getMechanicalMixers(), null, CMMRegistryEntry.BEYOND_ALLOY_SHEET, "end", "beyond").save(recipeOutput);

        mechanicalCraftingRecipe(CMMRegistryEntry.getMechanicalPresses(), AllBlocks.MECHANICAL_PRESS, AllItems.BRASS_SHEET, null, "brass").save(recipeOutput);
        mechanicalCraftingRecipe(CMMRegistryEntry.getMechanicalPresses(), null, CMMRegistryEntry.NETHERITE_ALLOY_SHEET, "brass", "netherite").save(recipeOutput);
        mechanicalCraftingRecipe(CMMRegistryEntry.getMechanicalPresses(), null, CMMRegistryEntry.END_ALLOY_SHEET, "netherite", "end").save(recipeOutput);
        mechanicalCraftingRecipe(CMMRegistryEntry.getMechanicalPresses(), null, CMMRegistryEntry.BEYOND_ALLOY_SHEET, "end", "beyond").save(recipeOutput);

        mechanicalCraftingRecipe(CMMRegistryEntry.getSpouts(), AllBlocks.SPOUT, AllItems.BRASS_SHEET, null, "brass").save(recipeOutput);
        mechanicalCraftingRecipe(CMMRegistryEntry.getSpouts(), null, CMMRegistryEntry.NETHERITE_ALLOY_SHEET, "brass", "netherite").save(recipeOutput);
        mechanicalCraftingRecipe(CMMRegistryEntry.getSpouts(), null, CMMRegistryEntry.END_ALLOY_SHEET, "netherite", "end").save(recipeOutput);
        mechanicalCraftingRecipe(CMMRegistryEntry.getSpouts(), null, CMMRegistryEntry.BEYOND_ALLOY_SHEET, "end", "beyond").save(recipeOutput);
    }

    private <T extends Block> ShapelessRecipeBuilder mechanicalCraftingRecipe(Map<ResourceLocation, BlockEntry<T>> map, @Nullable BlockEntry<?> createMechanical, ItemEntry<?> material, String requireId, String resultId){
        BlockEntry<?> require;
        if(createMechanical != null){
            require = createMechanical;
        }else{
            require = map.get(CreateMoreMachines.makeId(requireId));
        }
        BlockEntry<?> result = map.get(CreateMoreMachines.makeId(resultId));
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,result)
                .requires(require)
                .requires(material)
                .unlockedBy("has_item",has(require))
                .unlockedBy("has_item",has(material));
    }
}
