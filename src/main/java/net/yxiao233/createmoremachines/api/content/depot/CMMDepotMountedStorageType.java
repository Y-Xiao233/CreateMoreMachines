package net.yxiao233.createmoremachines.api.content.depot;

import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import com.simibubi.create.content.logistics.depot.DepotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import org.jetbrains.annotations.Nullable;

public class CMMDepotMountedStorageType extends MountedItemStorageType<CMMDepotMountedStorage> {
    private final CMMTier tier;
    public CMMDepotMountedStorageType(CMMTier tier) {
        super(CMMDepotMountedStorage.CODEC);
        this.tier = tier;
    }

    @Override
    public @Nullable CMMDepotMountedStorage mount(Level level, BlockState state, BlockPos pos, @Nullable BlockEntity be) {
        if (be instanceof DepotBlockEntity depot) {
            return CMMDepotMountedStorage.fromDepot(tier.getId(),depot);
        } else {
            return null;
        }
    }
}
