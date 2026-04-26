package net.yxiao233.createmoremachines.mixin;

import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.simibubi.create.content.fluids.tank.BoilerData;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BoilerData.class)
public class MixinBoilerData {
    @Shadow public int attachedEngines;

    @Shadow public int attachedWhistles;

    @Shadow public boolean needsHeatLevelUpdate;

    /**
     * @author Y_Xiao233
     * @reason Support the addition of steam engines to this mod
     */
    @Overwrite
    public boolean evaluate(FluidTankBlockEntity controller) {
        BlockPos controllerPos = controller.getBlockPos();
        Level level = controller.getLevel();
        if(level == null){
            return false;
        }
        int prevEngines = this.attachedEngines;
        int prevWhistles = this.attachedWhistles;
        this.attachedEngines = 0;
        this.attachedWhistles = 0;

        for(int yOffset = 0; yOffset < controller.getHeight(); ++yOffset) {
            for(int xOffset = 0; xOffset < controller.getWidth(); ++xOffset) {
                for(int zOffset = 0; zOffset < controller.getWidth(); ++zOffset) {
                    BlockPos pos = controllerPos.offset(xOffset, yOffset, zOffset);
                    BlockState blockState = level.getBlockState(pos);
                    if (FluidTankBlock.isTank(blockState)) {

                        for (Direction d : Iterate.directions) {
                            BlockPos attachedPos = pos.relative(d);
                            BlockState attachedState = level.getBlockState(attachedPos);
                            if (attachedState.getBlock() instanceof SteamEngineBlock && SteamEngineBlock.getFacing(attachedState) == d) {
                                ++this.attachedEngines;
                            }

                            if (attachedState.getBlock() instanceof WhistleBlock && WhistleBlock.getAttachedDirection(attachedState).getOpposite() == d) {
                                ++this.attachedWhistles;
                            }
                        }
                    }
                }
            }
        }

        this.needsHeatLevelUpdate = true;
        return prevEngines != this.attachedEngines || prevWhistles != this.attachedWhistles;
    }
}
