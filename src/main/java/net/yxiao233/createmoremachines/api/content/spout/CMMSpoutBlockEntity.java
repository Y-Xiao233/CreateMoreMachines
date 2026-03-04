package net.yxiao233.createmoremachines.api.content.spout;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.content.fluids.spout.FillingBySpout;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.yxiao233.createmoremachines.api.processing.CMMFilingBySpout;
import net.yxiao233.createmoremachines.api.registry.CMMTier;

import java.util.ArrayList;
import java.util.List;

public class CMMSpoutBlockEntity  extends SmartBlockEntity implements IHaveGoggleInformation {
    protected BeltProcessingBehaviour beltProcessing;
    public int processingTicks = -1;
    public boolean sendSplash;
    public CMMBlockSpoutingBehaviour customProcess;
    public SmartFluidTankBehaviour tank;
    private boolean createdSweetRoll;
    private boolean createdHoneyApple;
    private boolean createdChocolateBerries;
    protected static int SPLASH_PARTICLE_COUNT = 20;
    private final CMMTier tier;
    private int fluidCapability;
    public CMMSpoutBlockEntity(CMMTier tier, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tier = tier;
        this.fluidCapability = tier.getFluidCapability();
        this.tank.getPrimaryHandler().setCapacity(fluidCapability);
    }

    public CMMTier getTier() {
        return tier;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().expandTowards(0.0, -2.0, 0.0);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        this.tank = SmartFluidTankBehaviour.single(this, 1000);
        behaviours.add(this.tank);
        this.beltProcessing = (new BeltProcessingBehaviour(this)).whenItemEnters(this::onItemReceived).whileItemHeld(this::whenItemHeld);
        behaviours.add(this.beltProcessing);
        this.registerAwardables(behaviours, AllAdvancements.SPOUT, AllAdvancements.FOODS);
    }

    @SuppressWarnings("ConstantConditions")
    protected BeltProcessingBehaviour.ProcessingResult onItemReceived(TransportedItemStack transported, TransportedItemStackHandlerBehaviour handler) {
        if (handler.blockEntity.isVirtual()) {
            return BeltProcessingBehaviour.ProcessingResult.PASS;
        } else if (!FillingBySpout.canItemBeFilled(this.level, transported.stack)) {
            return BeltProcessingBehaviour.ProcessingResult.PASS;
        } else if (this.tank.isEmpty()) {
            return BeltProcessingBehaviour.ProcessingResult.HOLD;
        } else {
            return FillingBySpout.getRequiredAmountForItem(this.level, transported.stack, this.getCurrentFluidInTank()) == -1 ? BeltProcessingBehaviour.ProcessingResult.PASS : BeltProcessingBehaviour.ProcessingResult.HOLD;
        }
    }

    @SuppressWarnings("ConstantConditions")
    protected BeltProcessingBehaviour.ProcessingResult whenItemHeld(TransportedItemStack transported, TransportedItemStackHandlerBehaviour handler) {
        if (this.processingTicks != -1 && this.processingTicks != 5) {
            return BeltProcessingBehaviour.ProcessingResult.HOLD;
        } else if (!FillingBySpout.canItemBeFilled(this.level, transported.stack)) {
            return BeltProcessingBehaviour.ProcessingResult.PASS;
        } else if (this.tank.isEmpty()) {
            return BeltProcessingBehaviour.ProcessingResult.HOLD;
        } else {
            FluidStack fluid = this.getCurrentFluidInTank();
            int requiredAmountForItem = FillingBySpout.getRequiredAmountForItem(this.level, transported.stack, fluid.copy());
            if (requiredAmountForItem == -1) {
                return BeltProcessingBehaviour.ProcessingResult.PASS;
            } else if (requiredAmountForItem > fluid.getAmount()) {
                return BeltProcessingBehaviour.ProcessingResult.HOLD;
            } else if (this.processingTicks == -1) {
                this.processingTicks = 20;
                this.notifyUpdate();
                AllSoundEvents.SPOUTING.playOnServer(this.level, this.worldPosition, 0.75F, 0.9F + 0.2F * (float)Math.random());
                return BeltProcessingBehaviour.ProcessingResult.HOLD;
            } else {
                int multiple = tier == null ? 1 : tier.getProcessingMultiple();
                int max = Math.min(multiple, Math.min(transported.stack.copy().getCount(), fluid.getAmount() / requiredAmountForItem));
                ItemStack out = CMMFilingBySpout.fillItem(this.level, requiredAmountForItem, transported.stack, fluid, max);
                if (!out.isEmpty()) {
                    transported.clearFanProcessingData();
                    List<TransportedItemStack> outList = new ArrayList<>();
                    TransportedItemStack held = null;
                    TransportedItemStack result = transported.copy();
                    result.stack = out;
                    if (!transported.stack.isEmpty()) {
                        held = transported.copy();
                    }

                    outList.add(result);
                    handler.handleProcessingOnItem(transported, TransportedItemStackHandlerBehaviour.TransportedResult.convertToAndLeaveHeld(outList, held));
                }

                this.award(AllAdvancements.SPOUT);
                if (this.trackFoods()) {
                    this.createdChocolateBerries |= AllItems.CHOCOLATE_BERRIES.isIn(out);
                    this.createdHoneyApple |= AllItems.HONEYED_APPLE.isIn(out);
                    this.createdSweetRoll |= AllItems.SWEET_ROLL.isIn(out);
                    if (this.createdChocolateBerries && this.createdHoneyApple && this.createdSweetRoll) {
                        this.award(AllAdvancements.FOODS);
                    }
                }

                this.tank.getPrimaryHandler().setFluid(fluid);
                this.sendSplash = true;
                this.notifyUpdate();
                return BeltProcessingBehaviour.ProcessingResult.HOLD;
            }
        }
    }

    private FluidStack getCurrentFluidInTank() {
        return this.tank.getPrimaryHandler().getFluid();
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.putInt("ProcessingTicks", this.processingTicks);
        if (this.sendSplash && clientPacket) {
            compound.putBoolean("Splash", true);
            this.sendSplash = false;
        }

        if (this.trackFoods()) {
            if (this.createdChocolateBerries) {
                NBTHelper.putMarker(compound, "ChocolateBerries");
            }

            if (this.createdHoneyApple) {
                NBTHelper.putMarker(compound, "HoneyApple");
            }

            if (this.createdSweetRoll) {
                NBTHelper.putMarker(compound, "SweetRoll");
            }

        }

        if(this.tier != null){
            compound.putInt("fluid_capability", this.tier.getFluidCapability());
        }
    }

    private boolean trackFoods() {
        return this.getBehaviour(AdvancementBehaviour.TYPE).isOwnerPresent();
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        this.processingTicks = compound.getInt("ProcessingTicks");
        this.createdChocolateBerries = compound.contains("ChocolateBerries");
        this.createdHoneyApple = compound.contains("HoneyApple");
        this.createdSweetRoll = compound.contains("SweetRoll");
        if(compound.contains("fluid_capability")){
            this.fluidCapability = compound.getInt("fluid_capability");
            if(this.tank != null){
                this.tank.getPrimaryHandler().setCapacity(this.fluidCapability);
            }
        }
        if (clientPacket) {
            if (compound.contains("Splash")) {
                this.spawnSplash(this.tank.getPrimaryTank().getRenderedFluid());
            }

        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void tick() {
        super.tick();
        FluidStack currentFluidInTank = this.getCurrentFluidInTank();
        if (this.processingTicks == -1 && (this.isVirtual() || !this.level.isClientSide()) && !currentFluidInTank.isEmpty()) {
            BlockPos filling = this.worldPosition.below(2);
            CMMBlockSpoutingBehaviour behavior = CMMBlockSpoutingBehaviour.get(this.level, filling);
            if (behavior != null && behavior.fillBlock(this.level, filling, this, currentFluidInTank.copy(), true) > 0) {
                this.processingTicks = 20;
                this.customProcess = behavior;
                this.notifyUpdate();
            }
        }

        if (this.processingTicks >= 0) {
            --this.processingTicks;
            if (this.processingTicks == 5 && this.customProcess != null) {
                int fillBlock = this.customProcess.fillBlock(this.level, this.worldPosition.below(2), this, currentFluidInTank.copy(), false);
                this.customProcess = null;
                if (fillBlock > 0) {
                    this.tank.getPrimaryHandler().setFluid(FluidHelper.copyStackWithAmount(currentFluidInTank, currentFluidInTank.getAmount() - fillBlock));
                    this.sendSplash = true;
                    this.notifyUpdate();
                }
            }
        }

        if (this.processingTicks >= 8 && this.level.isClientSide) {
            this.spawnProcessingParticles(this.tank.getPrimaryTank().getRenderedFluid());
        }

    }

    @SuppressWarnings("ConstantConditions")
    protected void spawnProcessingParticles(FluidStack fluid) {
        if (!this.isVirtual() && !fluid.isEmpty()) {
            Vec3 vec = VecHelper.getCenterOf(this.worldPosition);
            vec = vec.subtract(0.0, 0.5, 0.0);
            ParticleOptions particle = FluidFX.getFluidParticle(fluid);
            this.level.addAlwaysVisibleParticle(particle, vec.x, vec.y, vec.z, 0.0, -0.10000000149011612, 0.0);
        }
    }

    @SuppressWarnings("ConstantConditions")
    protected void spawnSplash(FluidStack fluid) {
        if (!this.isVirtual() && !fluid.isEmpty()) {
            Vec3 vec = VecHelper.getCenterOf(this.worldPosition);
            vec = vec.subtract(0.0, 1.6875, 0.0);
            ParticleOptions particle = FluidFX.getFluidParticle(fluid);

            for(int i = 0; i < SPLASH_PARTICLE_COUNT; ++i) {
                Vec3 m = VecHelper.offsetRandomly(Vec3.ZERO, this.level.random, 0.125F);
                m = new Vec3(m.x, Math.abs(m.y), m.z);
                this.level.addAlwaysVisibleParticle(particle, vec.x, vec.y, vec.z, m.x, m.y, m.z);
            }

        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return this.containedFluidTooltip(tooltip, isPlayerSneaking, this.level.getCapability(Capabilities.FluidHandler.BLOCK, this.worldPosition, null));
    }
}
