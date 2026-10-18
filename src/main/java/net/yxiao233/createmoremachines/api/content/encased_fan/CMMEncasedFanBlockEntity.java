package net.yxiao233.createmoremachines.api.content.encased_fan;

import com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.createmoremachines.api.registry.CMMTier;

public class CMMEncasedFanBlockEntity extends EncasedFanBlockEntity {
    private final CMMTier tier;

    public CMMEncasedFanBlockEntity(CMMTier tier, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tier = tier;
        this.airCurrent = new CMMAirCurrent(this);
    }

    public CMMTier getTier() {
        return tier;
    }
}
