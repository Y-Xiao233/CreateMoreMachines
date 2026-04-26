package net.yxiao233.createmoremachines.api.content.steam_engine;

import com.simibubi.create.content.contraptions.bearing.WindmillBearingBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.utils.ReflectionUtil;
import org.jetbrains.annotations.Nullable;

public class CMMSteamEngineBlockEntity extends SteamEngineBlockEntity {
    private final CMMTier tier;
    public CMMSteamEngineBlockEntity(CMMTier tier, BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tier = tier;
    }

    public CMMTier getTier() {
        return tier;
    }
    @Override
    public void tick() {
        super.tick();
        if(level == null){
            return;
        }
        FluidTankBlockEntity tank = this.getTank();
        PoweredShaftBlockEntity shaft = this.getShaft();
        if (tank != null && shaft != null && this.isValid()) {
            boolean verticalTarget = false;
            BlockState shaftState = shaft.getBlockState();
            Direction.Axis targetAxis = Direction.Axis.X;
            Block var7 = shaftState.getBlock();
            if (var7 instanceof IRotate) {
                IRotate ir = (IRotate)var7;
                targetAxis = ir.getRotationAxis(shaftState);
            }

            verticalTarget = targetAxis == Direction.Axis.Y;
            BlockState blockState = this.getBlockState();
            if (blockState.getBlock() instanceof SteamEngineBlock) {
                Direction facing = SteamEngineBlock.getFacing(blockState);
                if (facing.getAxis() == Direction.Axis.Y) {
                    facing = blockState.getValue(SteamEngineBlock.FACING);
                }

                float efficiency = Mth.clamp(tank.boiler.getEngineEfficiency(tank.getTotalTankSize()), 0.0F, 1.0F);
                if (efficiency > 0.0F) {
                    this.award(AllAdvancements.STEAM_ENGINE);
                }

                int conveyedSpeedLevel = efficiency == 0.0F ? 1 : (verticalTarget ? 1 : (int) GeneratingKineticBlockEntity.convertToDirection(1.0F, facing));
                if (targetAxis == Direction.Axis.Z) {
                    conveyedSpeedLevel *= -1;
                }

                if (this.movementDirection.get() == WindmillBearingBlockEntity.RotationDirection.COUNTER_CLOCKWISE) {
                    conveyedSpeedLevel *= -1;
                }

                float shaftSpeed = shaft.getTheoreticalSpeed();
                if (shaft.hasSource() && shaftSpeed != 0.0F && conveyedSpeedLevel != 0 && shaftSpeed > 0.0F != conveyedSpeedLevel > 0) {
                    this.movementDirection.setValue(1 - ((WindmillBearingBlockEntity.RotationDirection)this.movementDirection.get()).ordinal());
                    conveyedSpeedLevel *= -1;
                }

                shaft.update(this.worldPosition, conveyedSpeedLevel, efficiency);
                if (this.level.isClientSide) {
                    CatnipServices.PLATFORM.executeOnClientOnly(() -> () -> ReflectionUtil.runPrivateMethod("spawnParticles",null,this,SteamEngineBlockEntity.class,null,null));
                }
            }
        } else if (!this.level.isClientSide()) {
            if (shaft != null) {
                if (shaft.getBlockPos().subtract(this.worldPosition).equals(shaft.enginePos)) {
                    if (shaft.engineEfficiency != 0.0F) {
                        Direction facing = SteamEngineBlock.getFacing(this.getBlockState());
                        if (this.level.isLoaded(this.worldPosition.relative(facing.getOpposite()))) {
                            shaft.update(this.worldPosition, 0, 0.0F);
                        }

                    }
                }
            }
        }
    }
    @Override
    public boolean isValid() {
        Direction dir = SteamEngineBlock.getConnectedDirection(this.getBlockState()).getOpposite();
        Level level = this.getLevel();
        return level != null && level.getBlockState(this.getBlockPos().relative(dir)).getBlock() instanceof FluidTankBlock;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public @Nullable Float getTargetAngle() {
        float angle = 0.0F;
        BlockState blockState = this.getBlockState();
        if (!(blockState.getBlock() instanceof SteamEngineBlock)) {
            return null;
        } else {
            Direction facing = SteamEngineBlock.getFacing(blockState);
            PoweredShaftBlockEntity shaft = this.getShaft();
            Direction.Axis facingAxis = facing.getAxis();
            Direction.Axis axis = Direction.Axis.Y;
            if (shaft == null) {
                return null;
            } else {
                axis = KineticBlockEntityRenderer.getRotationAxisOf(shaft);
                angle = KineticBlockEntityRenderer.getAngleForBe(shaft, shaft.getBlockPos(), axis);
                if (axis == facingAxis) {
                    return null;
                } else {
                    if (axis.isHorizontal() && facingAxis == Direction.Axis.X ^ facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                        angle *= -1.0F;
                    }

                    if (axis == Direction.Axis.X && facing == Direction.DOWN) {
                        angle *= -1.0F;
                    }

                    return angle;
                }
            }
        }
    }
}
