package net.yxiao233.createmoremachines.api.content.saw;

import com.simibubi.create.content.kinetics.saw.SawBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.createmoremachines.api.registry.CMMTier;

public class CMMSawBlockEntity extends SawBlockEntity {
    private final CMMTier tier;

    public CMMSawBlockEntity(CMMTier tier, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tier = tier;
    }

    public CMMTier getTier() {
        return tier;
    }
}
