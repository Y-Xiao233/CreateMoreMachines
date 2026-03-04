package net.yxiao233.createmoremachines.api.content.mechanical.mixer;

import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.createmoremachines.api.processing.CMMBasinRecipe;
import net.yxiao233.createmoremachines.api.registry.CMMTier;

import java.util.List;
import java.util.Optional;

public class CMMMechanicalMixerBlockEntity extends MechanicalMixerBlockEntity {
    private final CMMTier tier;
    public CMMMechanicalMixerBlockEntity(CMMTier tier, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tier = tier;
    }

    public CMMTier getTier() {
        return tier;
    }


    @Override
    protected void applyBasinRecipe() {
        if (this.currentRecipe != null) {
            Optional<BasinBlockEntity> optionalBasin = this.getBasin();
            if (optionalBasin.isPresent()) {
                BasinBlockEntity basin = optionalBasin.get();
                boolean wasEmpty = basin.canContinueProcessing();
                if (CMMBasinRecipe.apply(basin, this.currentRecipe, tier.getProcessingMultiple())) {
                    this.getProcessedRecipeTrigger().ifPresent(this::award);
                    basin.inputTank.sendDataImmediately();
                    if (wasEmpty && this.matchBasinRecipe(this.currentRecipe)) {
                        this.continueWithPreviousRecipe();
                        this.sendData();
                    }

                    basin.notifyChangeOfContents();
                }
            }
        }
    }
}
