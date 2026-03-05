package net.yxiao233.createmoremachines.datagen;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.api.annotation.RecipeGen;
import net.yxiao233.createmoremachines.api.content.basin.CMMBasinBlock;
import net.yxiao233.createmoremachines.api.content.depot.CMMDepotBlock;
import net.yxiao233.createmoremachines.api.content.mechanical.mixer.CMMMechanicalMixerBlock;
import net.yxiao233.createmoremachines.api.content.mechanical.press.CMMMechanicalPressBlock;
import net.yxiao233.createmoremachines.api.content.spout.CMMSpoutBlock;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;
import org.jetbrains.annotations.NotNull;

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



        pressRecipe(AllBlocks.BRASS_CASING, "brass").save(recipeOutput);
        pressRecipe(CMMRegistryEntry.NETHERITE_CASING, "netherite").save(recipeOutput);
        pressRecipe(CMMRegistryEntry.END_CASING, "end").save(recipeOutput);
        pressRecipe(CMMRegistryEntry.BEYOND_CASING, "beyond").save(recipeOutput);

        mixerRecipe(AllBlocks.BRASS_CASING, "brass").save(recipeOutput);
        mixerRecipe(CMMRegistryEntry.NETHERITE_CASING, "netherite").save(recipeOutput);
        mixerRecipe(CMMRegistryEntry.END_CASING, "end").save(recipeOutput);
        mixerRecipe(CMMRegistryEntry.BEYOND_CASING, "beyond").save(recipeOutput);

        spoutRecipe(AllBlocks.BRASS_CASING, "brass").save(recipeOutput);
        spoutRecipe(CMMRegistryEntry.NETHERITE_CASING, "netherite").save(recipeOutput);
        spoutRecipe(CMMRegistryEntry.END_CASING, "end").save(recipeOutput);
        spoutRecipe(CMMRegistryEntry.BEYOND_CASING, "beyond").save(recipeOutput);

        depotRecipe(AllItems.BRASS_INGOT,AllBlocks.BRASS_CASING, "brass").save(recipeOutput);
        depotRecipe(CMMRegistryEntry.NETHERITE_ALLOY,CMMRegistryEntry.NETHERITE_CASING, "netherite").save(recipeOutput);
        depotRecipe(CMMRegistryEntry.END_ALLOY,CMMRegistryEntry.END_CASING, "end").save(recipeOutput);
        depotRecipe(CMMRegistryEntry.BEYOND_ALLOY,CMMRegistryEntry.BEYOND_CASING, "beyond").save(recipeOutput);

        basinRecipe(AllItems.BRASS_INGOT,"brass").save(recipeOutput);
        basinRecipe(CMMRegistryEntry.NETHERITE_ALLOY, "netherite").save(recipeOutput);
        basinRecipe(CMMRegistryEntry.END_ALLOY, "end").save(recipeOutput);
        basinRecipe(CMMRegistryEntry.BEYOND_ALLOY, "beyond").save(recipeOutput);

    }

    private ShapedRecipeBuilder pressRecipe(ItemLike casing, String id){
        BlockEntry<CMMMechanicalPressBlock> press = CMMRegistryEntry.getMechanicalPresses().get(CreateMoreMachines.makeId(id));
        return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, press)
                .pattern(" S ")
                .pattern(" C ")
                .pattern(" I ")
                .define('S', AllBlocks.SHAFT)
                .define('C', casing)
                .define('I', Tags.Items.STORAGE_BLOCKS_IRON)
                .unlockedBy("has_item",has(casing));
    }

    private ShapedRecipeBuilder mixerRecipe(ItemLike casing, String id){
        BlockEntry<CMMMechanicalMixerBlock> mixer = CMMRegistryEntry.getMechanicalMixers().get(CreateMoreMachines.makeId(id));
        return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, mixer)
                .pattern(" L ")
                .pattern(" C ")
                .pattern(" W ")
                .define('L', AllBlocks.COGWHEEL)
                .define('C', casing)
                .define('W', AllItems.WHISK)
                .unlockedBy("has_item",has(casing));
    }

    private ShapedRecipeBuilder spoutRecipe(ItemLike casing, String id){
        BlockEntry<CMMSpoutBlock> spout = CMMRegistryEntry.getSpouts().get(CreateMoreMachines.makeId(id));
        return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, spout)
                .pattern(" C ")
                .pattern(" D ")
                .define('C', casing)
                .define('D', Items.DRIED_KELP)
                .unlockedBy("has_item",has(casing));
    }

    private ShapelessRecipeBuilder depotRecipe(ItemLike alloy, ItemLike casing, String id){
        BlockEntry<CMMDepotBlock> depot = CMMRegistryEntry.getDepots().get(CreateMoreMachines.makeId(id));
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, depot)
                .requires(alloy)
                .requires(casing)
                .unlockedBy("has_item",has(casing))
                .unlockedBy("has_item",has(alloy));
    }


    private ShapedRecipeBuilder basinRecipe(ItemLike alloy, String id){
        BlockEntry<CMMBasinBlock> basin = CMMRegistryEntry.getBasins().get(CreateMoreMachines.makeId(id));
        return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, basin)
                .pattern("   ")
                .pattern("A A")
                .pattern("AAA")
                .define('A', alloy)
                .unlockedBy("has_item",has(alloy));
    }
}
