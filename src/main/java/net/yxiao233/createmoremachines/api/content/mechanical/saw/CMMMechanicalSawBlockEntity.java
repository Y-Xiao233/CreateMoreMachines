package net.yxiao233.createmoremachines.api.content.mechanical.saw;

import com.simibubi.create.content.kinetics.saw.SawBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.createmoremachines.api.registry.BuiltInAdvancedMachineTypes;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import org.jetbrains.annotations.NotNull;

public class CMMMechanicalSawBlockEntity extends SawBlockEntity {
    private final CMMTier tier;

    public CMMMechanicalSawBlockEntity(CMMTier tier, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tier = tier;
        this.inventory = new CMMSawInventory(this);
        this.inventory.remainingTime = -1.0F;
    }

    @Override
    public void start(@NotNull ItemStack inserted) {
        boolean untouchedBySuper = level.isClientSide && !isVirtual();
        super.start(inserted);
        if (untouchedBySuper || !canProcess() || inventory.isEmpty()){
            return;
        }
        int multiple = tier.getProcessingMultiple(BuiltInAdvancedMachineTypes.SAW);
        if (multiple <= 1){
            return;
        }
        inventory.recipeDuration = Math.max(25.0F, inventory.remainingTime / multiple);
        inventory.remainingTime = inventory.recipeDuration;
    }

    public CMMTier getTier() {
        return tier;
    }
}
