package net.yxiao233.createmoremachines.api.content.spout;

import com.simibubi.create.api.behaviour.spouting.CauldronSpoutingBehavior;
import com.simibubi.create.api.registry.SimpleRegistry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

public enum CMMCauldronSpoutingBehavior implements CMMBlockSpoutingBehaviour {
    INSTANCE;

    public static final SimpleRegistry<Fluid, CauldronSpoutingBehavior.CauldronInfo> CAULDRON_INFO = Util.make(() -> {
        SimpleRegistry<Fluid, CauldronSpoutingBehavior.CauldronInfo> registry = SimpleRegistry.create();
        registry.register(Fluids.WATER, new CauldronSpoutingBehavior.CauldronInfo(250, Blocks.WATER_CAULDRON));
        registry.register(Fluids.LAVA, new CauldronSpoutingBehavior.CauldronInfo(1000, Blocks.LAVA_CAULDRON));
        return registry;
    });

    CMMCauldronSpoutingBehavior() {
    }

    @Override
    public int fillBlock(Level level, BlockPos pos, CMMSpoutBlockEntity spout, FluidStack availableFluid, boolean simulate) {
        CauldronSpoutingBehavior.CauldronInfo info = CAULDRON_INFO.get(availableFluid.getFluid());
        if (info == null) {
            return 0;
        } else if (availableFluid.getAmount() < info.amount()) {
            return 0;
        } else {
            if (!simulate) {
                level.setBlockAndUpdate(pos, info.cauldron());
            }

            return info.amount();
        }
    }
}