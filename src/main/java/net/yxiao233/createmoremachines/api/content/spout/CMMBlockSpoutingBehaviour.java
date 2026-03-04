package net.yxiao233.createmoremachines.api.content.spout;

import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import com.simibubi.create.api.registry.SimpleRegistry;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface CMMBlockSpoutingBehaviour {
    SimpleRegistry<Block, CMMBlockSpoutingBehaviour> BY_BLOCK = SimpleRegistry.create();
    SimpleRegistry<BlockEntityType<?>, CMMBlockSpoutingBehaviour> BY_BLOCK_ENTITY = SimpleRegistry.create();

    static @Nullable CMMBlockSpoutingBehaviour get(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        CMMBlockSpoutingBehaviour byBlock = BY_BLOCK.get(state.getBlock());
        if (byBlock != null) {
            return byBlock;
        } else {
            BlockEntity be = level.getBlockEntity(pos);
            return be == null ? null : BY_BLOCK_ENTITY.get(be.getType());
        }
    }

    int fillBlock(Level var1, BlockPos var2, CMMSpoutBlockEntity var3, FluidStack var4, boolean var5);
}
