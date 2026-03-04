package net.yxiao233.createmoremachines.api.content.spout;

import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public enum CMMSpoutCasting implements CMMBlockSpoutingBehaviour {
    INSTANCE;

    CMMSpoutCasting() {
    }

    @Override
    public int fillBlock(Level level, BlockPos pos, CMMSpoutBlockEntity spout, FluidStack availableFluid, boolean simulate) {
        if (!this.enabled()) {
            return 0;
        } else {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity == null) {
                return 0;
            } else {
                IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, blockEntity.getBlockPos(), Direction.UP);
                if (handler == null) {
                    return 0;
                } else if (handler.getTanks() != 1) {
                    return 0;
                } else if (!handler.isFluidValid(0, availableFluid)) {
                    return 0;
                } else {
                    FluidStack containedFluid = handler.getFluidInTank(0);
                    if (!containedFluid.isEmpty() && !FluidStack.isSameFluidSameComponents(containedFluid, availableFluid)) {
                        return 0;
                    } else {
                        int amount = availableFluid.getAmount();
                        return amount < 1000 && handler.fill(FluidHelper.copyStackWithAmount(availableFluid, amount + 1), IFluidHandler.FluidAction.SIMULATE) > amount ? 0 : handler.fill(availableFluid, simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            }
        }
    }

    private boolean enabled() {
        return AllConfigs.server().recipes.allowCastingBySpout.get();
    }
}
