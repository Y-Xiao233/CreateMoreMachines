package net.yxiao233.createmoremachines.api.content.fluid;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.*;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class CMMBaseFluidInstance {
    private final DeferredHolder<FluidType, FluidType> fluidType;
    private final DeferredHolder<Fluid, Fluid> flowingFluid;
    private final DeferredHolder<Fluid, Fluid> sourceFluid;
    private final DeferredHolder<Item, Item> bucketFluid;
    private final DeferredHolder<Block, Block> blockFluid;
    private final String fluid;

    @SuppressWarnings("removal")
    public CMMBaseFluidInstance(DeferredRegister<Item> itemDeferredRegister, DeferredRegister<Block> blockDeferredRegister, DeferredRegister<Fluid> fluidDeferredRegister, DeferredRegister<FluidType> fluidTypeDeferredRegister, String fluid, FluidType.Properties fluidTypeProperties, IClientFluidTypeExtensions renderProperties) {
        this.fluid = fluid;
        this.sourceFluid = fluidDeferredRegister.register(fluid,() -> {
            return new Source<>(this);
        });
        this.flowingFluid = fluidDeferredRegister.register(fluid + "_flowing",() -> {
            return new Flowing<>(this);
        });
        this.fluidType = fluidTypeDeferredRegister.register(fluid, () -> {
            return new FluidType(fluidTypeProperties) {
                public void initializeClient(@NotNull Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(renderProperties);
                }
            };
        });
        this.bucketFluid = itemDeferredRegister.register(fluid + "_bucket", () -> {
            return new BucketItem(this.sourceFluid.get(), (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1));
        });
        this.blockFluid = blockDeferredRegister.register(fluid, () -> {
            return new LiquidBlock((FlowingFluid)this.sourceFluid.get(), BlockBehaviour.Properties.of().mapColor(MapColor.WATER).replaceable().noCollission().strength(100.0F).pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY));
        });
    }


    public DeferredHolder<FluidType, FluidType> getFluidType() {
        return this.fluidType;
    }

    public DeferredHolder<Fluid, Fluid> getFlowingFluid() {
        return this.flowingFluid;
    }

    public DeferredHolder<Fluid, Fluid> getSourceFluid() {
        return this.sourceFluid;
    }

    public DeferredHolder<Item, Item> getBucketFluid() {
        return this.bucketFluid;
    }

    public DeferredHolder<Block, Block> getBlockFluid() {
        return this.blockFluid;
    }

    public String getFluid() {
        return this.fluid;
    }

    public static class Source<T extends CMMBaseFluidInstance> extends CMMBaseFluid {
        public Source(T instance) {
            super(instance);
        }

        public int getAmount(@Nonnull FluidState state) {
            return 8;
        }

        public boolean isSource(@Nonnull FluidState state) {
            return true;
        }
    }

    public static class Flowing<T extends CMMBaseFluidInstance> extends CMMBaseFluid {
        public Flowing(T instance) {
            super(instance);
            this.registerDefaultState(this.getStateDefinition().any().setValue(LEVEL, 7));
        }

        protected void createFluidStateDefinition(StateDefinition.@NotNull Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        public int getAmount(@Nonnull FluidState fluidState) {
            return fluidState.getValue(LEVEL);
        }
    }
}
