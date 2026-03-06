package net.yxiao233.createmoremachines.api.content.mechanical.press;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.kinetics.crafter.MechanicalCraftingRecipe;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.yxiao233.createmoremachines.api.processing.CMMBasinRecipe;
import net.yxiao233.createmoremachines.api.registry.CMMTier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class CMMMechanicalPressBlockEntity extends BasinOperatingBlockEntity implements PressingBehaviour.PressingBehaviourSpecifics {
    private static final Object compressingRecipesKey = new Object();
    public CMMPressingBehaviour pressingBehaviour;
    private int tracksCreated;
    private final CMMTier tier;
    public CMMMechanicalPressBlockEntity(CMMTier tier, BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.tier = tier;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return (new AABB(this.worldPosition)).expandTowards(0.0, -1.5, 0.0).expandTowards(0.0, 1.0, 0.0);
    }

    public CMMTier getTier() {
        return tier;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.pressingBehaviour = new CMMPressingBehaviour(this);
        behaviours.add(this.pressingBehaviour);
        this.registerAwardables(behaviours, AllAdvancements.PRESS, AllAdvancements.COMPACTING, AllAdvancements.TRACK_CRAFTING);
    }

    public void onItemPressed(ItemStack result) {
        this.award(AllAdvancements.PRESS);
        if (AllTags.AllBlockTags.TRACKS.matches(result)) {
            this.tracksCreated += result.getCount();
        }

        if (this.tracksCreated >= 1000) {
            this.award(AllAdvancements.TRACK_CRAFTING);
            this.tracksCreated = 0;
        }

    }

    public CMMPressingBehaviour getPressingBehaviour() {
        return pressingBehaviour;
    }

    @Override
    public boolean tryProcessInBasin(boolean b) {
        this.applyBasinRecipe();
        Optional<BasinBlockEntity> basin = this.getBasin();
        if (basin.isPresent()) {
            SmartInventory inputs = basin.get().getInputInventory();

            for(int slot = 0; slot < inputs.getSlots(); ++slot) {
                ItemStack stackInSlot = inputs.getItem(slot);
                if (!stackInSlot.isEmpty()) {
                    this.pressingBehaviour.particleItems.add(stackInSlot);
                }
            }
        }

        return true;
    }

    @Override
    protected void applyBasinRecipe() {
        if (this.currentRecipe != null) {
            Optional<BasinBlockEntity> optionalBasin = this.getBasin();
            if (optionalBasin.isPresent()) {
                BasinBlockEntity basin = optionalBasin.get();
                boolean wasEmpty = basin.canContinueProcessing();
                if (CMMBasinRecipe.apply(basin, this.currentRecipe, tier.getProcessingMultiple())) {
                    this.getProcessedRecipeTrigger().ifPresent(this::award);
                    basin.inputTank.sendDataImmediately();
                    if (wasEmpty && this.matchBasinRecipe(this.currentRecipe)) {
                        this.continueWithPreviousRecipe();
                        this.sendData();
                    }

                    basin.notifyChangeOfContents();
                }
            }
        }
    }

    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        if (this.getBehaviour(AdvancementBehaviour.TYPE).isOwnerPresent()) {
            compound.putInt("TracksCreated", this.tracksCreated);
        }

    }

    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        this.tracksCreated = compound.getInt("TracksCreated");
    }

    @Override
    public boolean tryProcessOnBelt(TransportedItemStack input, List<ItemStack> outputList, boolean simulate) {
        Optional<RecipeHolder<PressingRecipe>> recipe = this.getRecipe(input.stack);
        if (recipe.isEmpty()) {
            return false;
        } else if (simulate) {
            return true;
        } else {
            this.pressingBehaviour.particleItems.add(input.stack);
            int max = Math.min(tier.getProcessingMultiple(), input.stack.getCount());
            List<ItemStack> outputs = RecipeApplier.applyRecipeOn(this.level, input.stack.copyWithCount(max), recipe.get().value(), true);

            for (ItemStack created : outputs) {
                if (!created.isEmpty()) {
                    this.onItemPressed(created);
                    break;
                }
            }

            outputList.addAll(outputs);
            return true;
        }
    }

    @Override
    public boolean tryProcessInWorld(ItemEntity itemEntity, boolean simulate) {
        ItemStack item = itemEntity.getItem();
        Optional<RecipeHolder<PressingRecipe>> recipe = this.getRecipe(item);
        if (recipe.isEmpty()) {
            return false;
        } else if (simulate) {
            return true;
        } else {
            ItemStack itemCreated = ItemStack.EMPTY;
            this.pressingBehaviour.particleItems.add(item.copy());
            int max = Math.min(item.getCount(), this.getTier().getProcessingMultiple());
            for (ItemStack result : RecipeApplier.applyRecipeOn(this.level, item.copyWithCount(max), recipe.get().value(), true)) {
                if (itemCreated.isEmpty()) {
                    itemCreated = result.copy();
                }

                ItemEntity created = new ItemEntity(this.level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), result);
                created.setDefaultPickUpDelay();
                created.setDeltaMovement(VecHelper.offsetRandomly(Vec3.ZERO, this.level.random, 0.05F));
                this.level.addFreshEntity(created);
            }
            item.shrink(max);

            if (!itemCreated.isEmpty()) {
                this.onItemPressed(itemCreated);
            }

            return true;
        }
    }

    @Override
    public void onPressingCompleted() {
        if (this.pressingBehaviour.onBasin() && this.matchBasinRecipe(this.currentRecipe) && this.getBasin().filter(BasinBlockEntity::canContinueProcessing).isPresent()) {
            this.startProcessingBasin();
        } else {
            this.basinChecker.scheduleUpdate();
        }
    }

    public Optional<RecipeHolder<PressingRecipe>> getRecipe(ItemStack item) {
        Optional<RecipeHolder<PressingRecipe>> assemblyRecipe = SequencedAssemblyRecipe.getRecipe(this.level, item, AllRecipeTypes.PRESSING.getType(), PressingRecipe.class);
        return assemblyRecipe.isPresent() ? assemblyRecipe : AllRecipeTypes.PRESSING.find(new SingleRecipeInput(item), this.level);
    }

    public static boolean canCompress(Recipe<?> recipe) {
        if (recipe instanceof CraftingRecipe && AllConfigs.server().recipes.allowShapedSquareInPress.get()) {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            return (ingredients.size() == 4 || ingredients.size() == 9) && ItemHelper.matchAllIngredients(ingredients);
        } else {
            return false;
        }
    }

    @Override
    protected boolean matchStaticFilters(RecipeHolder<? extends Recipe<?>> recipe) {
        return recipe.value() instanceof CraftingRecipe && !(recipe.value() instanceof MechanicalCraftingRecipe) && canCompress(recipe.value()) && !AllRecipeTypes.shouldIgnoreInAutomation(recipe) || recipe.value().getType() == AllRecipeTypes.COMPACTING.getType();
    }

    @Override
    public float getKineticSpeed() {
        return this.getSpeed();
    }

    @Override
    public boolean canProcessInBulk() {
        return AllConfigs.server().recipes.bulkPressing.get();
    }

    @Override
    protected Object getRecipeCacheKey() {
        return compressingRecipesKey;
    }

    @Override
    public int getParticleAmount() {
        return 15;
    }
    public void startProcessingBasin() {
        if (!this.pressingBehaviour.running || this.pressingBehaviour.runningTicks > 120) {
            super.startProcessingBasin();
            this.pressingBehaviour.start(PressingBehaviour.Mode.BASIN);
        }
    }

    protected void onBasinRemoved() {
        this.pressingBehaviour.particleItems.clear();
        this.pressingBehaviour.running = false;
        this.pressingBehaviour.runningTicks = 0;
        this.sendData();
    }

    @Override
    protected boolean isRunning() {
        return this.pressingBehaviour.running;
    }

    @Override
    protected Optional<CreateAdvancement> getProcessedRecipeTrigger() {
        return Optional.of(AllAdvancements.COMPACTING);
    }
}
