package net.yxiao233.createmoremachines.api.content.crushing_wheel;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingInventory;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import net.yxiao233.createmoremachines.api.capabilities.BigItemStackHandler;
import net.yxiao233.createmoremachines.api.registry.BuiltInAdvancedMachineTypes;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CMMCrushingWheelControllerBlockEntity extends CrushingWheelControllerBlockEntity {
    private static final String CMM_OVERFLOW_TAG = "CMMOverflow";
    private final CMMTier tier;
    private final RecipeWrapper createmoremachines$recipeWrapper;

    public CMMCrushingWheelControllerBlockEntity(CMMTier tier, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tier = tier;
        this.inventory = new ProcessingInventory(this::cmmItemInserted) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (slot != 0 || processingEntity != null) {
                    return false;
                }
                ItemStack current = getStackInSlot(0);
                if (!current.isEmpty()) {
                    return ItemStack.isSameItemSameComponents(stack, current);
                }
                return isEmpty();
            }

            @Override
            protected int getStackLimit(int slot, @NotNull ItemStack stack) {
                return slot == 0 ? createmoremachines$slot0Capacity(stack) : super.getStackLimit(slot, stack);
            }

            @Override
            public int getSlotLimit(int slot) {
                int multiple = getMultiple();
                if (slot == 0 && multiple > 1) {
                    return Math.min(multiple, 64) * Item.ABSOLUTE_MAX_STACK_SIZE;
                }
                return super.getSlotLimit(slot);
            }

            @Override
            public void setSize(int size) {
                super.setSize(Math.max(size, minOutputSlots()));
            }

            @Override
            public @NotNull CompoundTag serializeNBT(HolderLookup.@NotNull Provider registries) {
                CompoundTag nbt = BigItemStackHandler.serializeNBT(this,registries);
                nbt.putFloat("ProcessingTime", this.remainingTime);
                nbt.putFloat("RecipeTime", this.recipeDuration);
                nbt.putBoolean("AppliedRecipe", this.appliedRecipe);
                return nbt;
            }

            @Override
            public void deserializeNBT(HolderLookup.@NotNull Provider registries, CompoundTag nbt) {
                this.remainingTime = nbt.getFloat("ProcessingTime");
                this.recipeDuration = nbt.getFloat("RecipeTime");
                this.appliedRecipe = nbt.getBoolean("AppliedRecipe");
                BigItemStackHandler.deserializeNBT(this,registries,nbt);
                if (this.isEmpty()) {
                    this.appliedRecipe = false;
                }
            }
        };
        this.inventory.setSize(this.minOutputSlots());
        this.createmoremachines$recipeWrapper = new RecipeWrapper(this.inventory);
    }

    private int minOutputSlots() {
        return 2 + 49 * Math.clamp(getMultiple(), 1, 64);
    }

    private int createmoremachines$slot0Capacity(ItemStack stack) {
        if (getMultiple() > 1) {
            return Math.min(getMultiple(), 64) * stack.getMaxStackSize();
        }
        return Math.min(Item.ABSOLUTE_MAX_STACK_SIZE, stack.getMaxStackSize());
    }

    public boolean createmoremachines$mergeFromEntity(ItemEntity entity) {
        if (getMultiple() <= 1) {
            return false;
        }
        if (inventory.appliedRecipe || processingEntity != null) {
            return false;
        }
        ItemStack incoming = entity.getItem();
        if (incoming.isEmpty()) {
            entity.discard();
            return true;
        }
        int capacity = createmoremachines$slot0Capacity(incoming);
        ItemStack current = inventory.getStackInSlot(0);
        int merged;
        if (current.isEmpty()) {
            if (!inventory.isEmpty()) {
                return false;
            }
            merged = Math.min(incoming.getCount(), capacity);
            inventory.setStackInSlot(0, incoming.copyWithCount(merged));
        } else {
            if (!ItemStack.isSameItemSameComponents(incoming, current)) {
                return false;
            }
            merged = Math.min(incoming.getCount(), capacity - current.getCount());
            if (merged <= 0) {
                return false;
            }
            current.grow(merged);
        }
        incoming.shrink(merged);
        cmmItemInserted(inventory.getStackInSlot(0));
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 18);
        if (incoming.isEmpty()) {
            entity.discard();
            return true;
        }
        return false;
    }

    public CMMTier getTier() {
        return tier;
    }

    private void cmmItemInserted(ItemStack stack) {
        Optional<RecipeHolder<StandardProcessingRecipe<RecipeWrapper>>> recipe = findRecipe();
        inventory.remainingTime = recipe.isPresent() ? recipe.get().value().getProcessingDuration() : 100.0F;
        inventory.appliedRecipe = false;
    }

    private int getMultiple(){
        return tier.getProcessingMultiple(BuiltInAdvancedMachineTypes.CRUSHING_WHEEL);
    }

    @Override
    public Optional<RecipeHolder<StandardProcessingRecipe<RecipeWrapper>>> findRecipe() {
        Optional<RecipeHolder<StandardProcessingRecipe<RecipeWrapper>>> recipe = AllRecipeTypes.CRUSHING.find(createmoremachines$recipeWrapper, level);
        if (recipe.isEmpty()) {
            recipe = AllRecipeTypes.MILLING.find(createmoremachines$recipeWrapper, level);
        }

        int multiple = (int) tier.getSpecial(BuiltInAdvancedMachineTypes.CRUSHING_WHEEL,"speed");
        if (recipe.isEmpty() || multiple <= 1) {
            return recipe;
        }

        RecipeHolder<StandardProcessingRecipe<RecipeWrapper>> holder = recipe.get();
        StandardProcessingRecipe<RecipeWrapper> wrapper = getRecipeWrapperStandardProcessingRecipe(holder, multiple);
        return Optional.of(new RecipeHolder<>(holder.id(), wrapper));
    }

    private static @NotNull StandardProcessingRecipe<RecipeWrapper> getRecipeWrapperStandardProcessingRecipe(RecipeHolder<StandardProcessingRecipe<RecipeWrapper>> holder, int multiple) {
        StandardProcessingRecipe<RecipeWrapper> value = holder.value();
        return new StandardProcessingRecipe<>(value.getTypeInfo(), value.getParams()) {

            @Override
            public int getProcessingDuration() {
                return Math.max(60, value.getProcessingDuration() / multiple);
            }

            @Override
            public boolean matches(@NotNull RecipeWrapper input, @NotNull Level level1) {
                return value.matches(input, level1);
            }

            @Override
            protected int getMaxInputCount() {
                return 1;
            }

            @Override
            protected int getMaxOutputCount() {
                return 7;
            }
        };
    }
}
