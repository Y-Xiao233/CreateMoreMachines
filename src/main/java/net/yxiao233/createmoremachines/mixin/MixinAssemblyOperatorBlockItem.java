package net.yxiao233.createmoremachines.mixin;

import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.createmoremachines.api.processing.IAdjustPlacedBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AssemblyOperatorBlockItem.class)
public class MixinAssemblyOperatorBlockItem {
    @Inject(
            method = "operatesOn",
            at = @At("RETURN"),
            cancellable = true
    )
    private void addCreateMoreMachinesSupport(LevelReader world, BlockPos pos, BlockState placedOnState, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            return;
        }

        if (placedOnState.getBlock() instanceof IAdjustPlacedBlock) {
            cir.setReturnValue(true);
        }
    }
}
