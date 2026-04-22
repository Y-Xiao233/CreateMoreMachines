package net.yxiao233.createmoremachines.api.content.fluid_tank;

import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;

public class CMMFluidTankBlock extends FluidTankBlock {
    private final CMMTier tier;
    public CMMFluidTankBlock(CMMTier tier, Properties properties) {
        super(properties, false);
        this.tier = tier;
    }

    public CMMTier getTier() {
        return tier;
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moved) {
        //TODO 一键为大型的储罐添加一层操作要在这里实现,目前有问题
        super.onPlace(state, world, pos, oldState, moved);
    }

    @Override
    public BlockEntityType<? extends FluidTankBlockEntity> getBlockEntityType() {
        return CMMRegistryEntry.getFluidTankEntities().get(tier.getId()).get();
    }
}
