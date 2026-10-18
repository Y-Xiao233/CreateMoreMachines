package net.yxiao233.createmoremachines.api.content.crushing_wheel;

import com.simibubi.create.content.kinetics.crusher.CrushingWheelBlock;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlock;
import net.createmod.catnip.data.Iterate;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.yxiao233.createmoremachines.api.CMMTierTooltip;
import net.yxiao233.createmoremachines.api.content.IHaveTierInformation;
import net.yxiao233.createmoremachines.api.registry.BuiltInAdvancedMachineTypes;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CMMCrushingWheelBlock extends CrushingWheelBlock implements IHaveTierInformation {
    private final CMMTier tier;

    public CMMCrushingWheelBlock(CMMTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    public CMMTier getTier() {
        return tier;
    }
    private Block getControllerBlock() {
        return CMMRegistryEntry.getCrushingWheelControllers().get(tier.getId())
                .get();
    }

    private boolean isController(BlockState state) {
        return CMMRegistryEntry.getCrushingWheelControllers().get(tier.getId()) != null && state.is(CMMRegistryEntry.getCrushingWheelControllers().get(tier.getId()).get());
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        for (Direction d : Iterate.directions) {
            if (d.getAxis() == state.getValue(AXIS)){
                continue;
            }
            if (isController(worldIn.getBlockState(pos.relative(d)))){
                worldIn.removeBlock(pos.relative(d), isMoving);
            }
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public void updateControllers(BlockState state, Level world, BlockPos pos, Direction side) {
        if (side.getAxis() == state.getValue(AXIS))
            return;
        if (world == null)
            return;

        BlockPos controllerPos = pos.relative(side);
        BlockPos otherWheelPos = pos.relative(side, 2);

        boolean controllerExists = isController(world.getBlockState(controllerPos));
        boolean controllerIsValid = controllerExists && world.getBlockState(controllerPos)
                .getValue(CrushingWheelControllerBlock.VALID);
        Direction controllerOldDirection = controllerExists ? world.getBlockState(controllerPos)
                .getValue(CrushingWheelControllerBlock.FACING) : null;

        boolean controllerShouldExist = false;
        boolean controllerShouldBeValid = false;
        Direction controllerNewDirection = Direction.DOWN;

        BlockState otherState = world.getBlockState(otherWheelPos);
        if (otherState.getBlock() == this) {
            controllerShouldExist = true;

            CMMCrushingWheelBlockEntity be = (CMMCrushingWheelBlockEntity) getBlockEntity(world, pos);
            CMMCrushingWheelBlockEntity otherBE = (CMMCrushingWheelBlockEntity) getBlockEntity(world, otherWheelPos);

            if (be != null && otherBE != null && (be.getSpeed() > 0) != (otherBE.getSpeed() > 0)
                    && be.getSpeed() != 0 && otherBE.getSpeed() != 0) {
                Direction.Axis wheelAxis = state.getValue(AXIS);
                Direction.Axis sideAxis = side.getAxis();
                int controllerADO = Math.round(Math.signum(be.getSpeed())) * side.getAxisDirection()
                        .getStep();
                Vec3 controllerDirVec = new Vec3(wheelAxis == Direction.Axis.X ? 1 : 0, wheelAxis == Direction.Axis.Y ? 1 : 0,
                        wheelAxis == Direction.Axis.Z ? 1 : 0).cross(
                        new Vec3(sideAxis == Direction.Axis.X ? 1 : 0, sideAxis == Direction.Axis.Y ? 1 : 0, sideAxis == Direction.Axis.Z ? 1 : 0));

                controllerNewDirection = Direction.getNearest(controllerDirVec.x * controllerADO,
                        controllerDirVec.y * controllerADO, controllerDirVec.z * controllerADO);

                controllerShouldBeValid = true;
            }
            if (otherState.getValue(AXIS) != state.getValue(AXIS))
                controllerShouldExist = false;
        }

        if (!controllerShouldExist) {
            if (controllerExists){
                world.setBlockAndUpdate(controllerPos, Blocks.AIR.defaultBlockState());
            }
            return;
        }

        if (!controllerExists) {
            if (!world.getBlockState(controllerPos)
                    .canBeReplaced())
                return;
            world.setBlockAndUpdate(controllerPos, getControllerBlock().defaultBlockState()
                    .setValue(CMMCrushingWheelControllerBlock.VALID, controllerShouldBeValid)
                    .setValue(CMMCrushingWheelControllerBlock.FACING, controllerNewDirection));
        } else if (controllerIsValid != controllerShouldBeValid || controllerOldDirection != controllerNewDirection) {
            world.setBlockAndUpdate(controllerPos, world.getBlockState(controllerPos)
                    .setValue(CMMCrushingWheelControllerBlock.VALID, controllerShouldBeValid)
                    .setValue(CMMCrushingWheelControllerBlock.FACING, controllerNewDirection));
        }

        ((CMMCrushingWheelControllerBlock) getControllerBlock()).updateSpeed(world.getBlockState(controllerPos), world,
                controllerPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        for (Direction direction : Iterate.directions) {
            BlockPos neighbourPos = pos.relative(direction);
            BlockState neighbourState = worldIn.getBlockState(neighbourPos);
            Direction.Axis stateAxis = state.getValue(AXIS);
            if (isController(neighbourState) && direction.getAxis() != stateAxis){
                return false;
            }
            if (neighbourState.getBlock() != this){
                continue;
            }
            if (neighbourState.getValue(AXIS) != stateAxis || stateAxis != direction.getAxis()){
                return false;
            }
        }

        return true;
    }

    @Override
    public BlockEntityType<? extends CMMCrushingWheelBlockEntity> getBlockEntityType() {
        return CMMRegistryEntry.getCrushingWheelEntities().get(tier.getId())
                .get();
    }

    @Override
    public @NotNull String getDescriptionId() {
        return ChatFormatting.YELLOW + Component.translatable(Util.makeDescriptionId("tier", tier.getId())).getString() + ChatFormatting.WHITE + Component.translatable("block.create.crushing_wheel").getString();
    }


    @Override
    public void addTierInformation(List<Component> tooltips) {
        CMMTierTooltip.byTypes(tooltips,tier, BuiltInAdvancedMachineTypes.CRUSHING_WHEEL, CMMTierTooltip.Type.PROCESSING_MULTIPLE);
        tooltips.add(Component.translatable("tooltip.createmoremachines.crushing_wheel_speed",tier.getSpecial(BuiltInAdvancedMachineTypes.CRUSHING_WHEEL,"speed")));
    }
}
