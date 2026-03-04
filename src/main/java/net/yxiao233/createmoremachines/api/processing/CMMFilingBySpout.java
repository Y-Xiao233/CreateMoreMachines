package net.yxiao233.createmoremachines.api.processing;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class CMMFilingBySpout {
    @SuppressWarnings({"rawtypes","unchecked"})
    public static ItemStack fillItem(Level world, int requiredAmount, ItemStack stack, FluidStack availableFluid, int max) {
        FluidStack toFill = availableFluid.copy();
        toFill.setAmount(requiredAmount);
        SingleRecipeInput input = new SingleRecipeInput(stack);
        RecipeHolder<FillingRecipe> fillingRecipe = SequencedAssemblyRecipe.getRecipe(world, input, AllRecipeTypes.FILLING.getType(), FillingRecipe.class, matchItemAndFluid(world, availableFluid, input)).filter((fr) -> {
            return fr.value().getRequiredFluid().test(toFill);
        }).orElseGet(() -> {
            Iterator<RecipeHolder<Recipe<SingleRecipeInput>>> iterator = world.getRecipeManager().getRecipesFor(AllRecipeTypes.FILLING.getType(), input, world).iterator();

            RecipeHolder<Recipe<SingleRecipeInput>> recipe;
            FillingRecipe fr;
            FluidIngredient requiredFluid;
            do {
                if (!iterator.hasNext()) {
                    return null;
                }

                recipe = iterator.next();
                fr = (FillingRecipe)recipe.value();
                requiredFluid = fr.getRequiredFluid();
            } while(!requiredFluid.test(toFill));

            return new RecipeHolder(recipe.id(), fr);
        });
        if (fillingRecipe != null) {
            List<ItemStack> results = fillingRecipe.value().rollResults();
            availableFluid.shrink(requiredAmount * max);
            stack.shrink(max);
            return results.isEmpty() ? ItemStack.EMPTY : results.getFirst().copyWithCount(max);
        } else {
            return fillItem(requiredAmount, stack, availableFluid, max);
        }
    }

    public static ItemStack fillItem(int requiredAmount, ItemStack stack, FluidStack availableFluid, int max) {
        FluidStack toFill = availableFluid.copy();
        toFill.setAmount(requiredAmount);
        availableFluid.shrink(requiredAmount * max);
        ItemStack fillBottle;
        if (stack.getItem() == Items.GLASS_BOTTLE && canFillGlassBottleInternally(toFill)) {
            Fluid fluid = toFill.getFluid();
            if (FluidHelper.isWater(fluid)) {
                fillBottle = PotionContents.createItemStack(Items.POTION, Potions.WATER);
            } else if (fluid.isSame(AllFluids.TEA.get())) {
                fillBottle = AllItems.BUILDERS_TEA.asStack();
            } else {
                fillBottle = PotionFluidHandler.fillBottle(stack, toFill);
            }

            stack.shrink(max);
            return fillBottle.copyWithCount(max);
        } else {
            fillBottle = stack.copy();
            fillBottle.setCount(1);
            IFluidHandlerItem capability = fillBottle.getCapability(Capabilities.FluidHandler.ITEM);
            if (capability == null) {
                return ItemStack.EMPTY;
            } else {
                capability.fill(toFill, IFluidHandler.FluidAction.EXECUTE);
                ItemStack container = capability.getContainer().copy();
                stack.shrink(max);
                return container.copyWithCount(max);
            }
        }
    }

    private static boolean canFillGlassBottleInternally(FluidStack availableFluid) {
        Fluid fluid = availableFluid.getFluid();
        if (fluid.isSame(Fluids.WATER)) {
            return true;
        } else if (fluid.isSame(AllFluids.POTION.get())) {
            return true;
        } else {
            return fluid.isSame(AllFluids.TEA.get());
        }
    }

    private static Predicate<RecipeHolder<FillingRecipe>> matchItemAndFluid(Level world, FluidStack availableFluid, SingleRecipeInput input) {
        return (r) -> {
            return r.value().matches(input, world) && r.value().getRequiredFluid().test(availableFluid);
        };
    }
}
