package net.yxiao233.createmoremachines.api.content.spout;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public record CMMStateChangingBehavior(int amount, Predicate<Fluid> fluidTest, Predicate<BlockState> canFill, UnaryOperator<BlockState> fillFunction) implements CMMBlockSpoutingBehaviour {
    public CMMStateChangingBehavior(int amount, Predicate<Fluid> fluidTest, Predicate<BlockState> canFill, UnaryOperator<BlockState> fillFunction) {
        this.amount = amount;
        this.fluidTest = fluidTest;
        this.canFill = canFill;
        this.fillFunction = fillFunction;
    }

    @Override
    public int fillBlock(Level level, BlockPos pos, CMMSpoutBlockEntity spout, FluidStack availableFluid, boolean simulate) {
        if (availableFluid.getAmount() >= this.amount && this.fluidTest.test(availableFluid.getFluid())) {
            BlockState state = level.getBlockState(pos);
            if (!this.canFill.test(state)) {
                return 0;
            } else {
                if (!simulate) {
                    BlockState newState = (BlockState)this.fillFunction.apply(state);
                    level.setBlockAndUpdate(pos, newState);
                }

                return this.amount;
            }
        } else {
            return 0;
        }
    }

    public static CMMBlockSpoutingBehaviour setTo(int amount, Predicate<Fluid> fluidTest, Block block) {
        return setTo(amount, fluidTest, block.defaultBlockState());
    }

    public static CMMBlockSpoutingBehaviour setTo(int amount, Predicate<Fluid> fluidTest, BlockState newState) {
        return new CMMStateChangingBehavior(amount, fluidTest, (state) -> {
            return true;
        }, (state) -> {
            return newState;
        });
    }

    public static CMMBlockSpoutingBehaviour incrementingState(int amount, Predicate<Fluid> fluidTest, IntegerProperty property) {
        int max = (Integer)property.getPossibleValues().stream().max(Integer::compareTo).orElseThrow();
        Predicate<BlockState> canFill = (state) -> {
            return (Integer)state.getValue(property) < max;
        };
        UnaryOperator<BlockState> fillFunction = (state) -> {
            return (BlockState)state.setValue(property, (Integer)state.getValue(property) + 1);
        };
        return new CMMStateChangingBehavior(amount, fluidTest, canFill, fillFunction);
    }

    public int amount() {
        return this.amount;
    }

    public Predicate<Fluid> fluidTest() {
        return this.fluidTest;
    }

    public Predicate<BlockState> canFill() {
        return this.canFill;
    }

    public UnaryOperator<BlockState> fillFunction() {
        return this.fillFunction;
    }
}
