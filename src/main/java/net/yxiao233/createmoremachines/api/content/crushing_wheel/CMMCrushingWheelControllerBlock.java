package net.yxiao233.createmoremachines.api.content.crushing_wheel;

import com.simibubi.create.content.kinetics.crusher.CrushingWheelBlockEntity;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlock;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;

public class CMMCrushingWheelControllerBlock extends CrushingWheelControllerBlock {
    private final CMMTier tier;

    public CMMCrushingWheelControllerBlock(CMMTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    public CMMTier getTier() {
        return tier;
    }

    private Block getWheelBlock() {
        return CMMRegistryEntry.getCrushingWheels().get(tier.getId()).get();
    }

    @Override
    public void checkEntityForProcessing(Level world, BlockPos pos, Entity entityIn) {
        if (!world.isClientSide && entityIn instanceof ItemEntity itemEntity && world.getBlockEntity(pos) instanceof CMMCrushingWheelControllerBlockEntity crushingWheelBlockEntity && crushingWheelBlockEntity.crushingspeed != 0) {
            CompoundTag data = entityIn.getPersistentData();
            if (!data.contains("BypassCrushingWheel")
                    || !pos.equals(NBTHelper.readBlockPos(data, "BypassCrushingWheel"))) {
                if (crushingWheelBlockEntity.createmoremachines$mergeFromEntity(itemEntity))
                    return;
            }
        }
        super.checkEntityForProcessing(world, pos, entityIn);
    }

    @Override
    public BlockEntityType<? extends CMMCrushingWheelControllerBlockEntity> getBlockEntityType() {
        return CMMRegistryEntry.getCrushingWheelControllerEntities().get(tier.getId())
                .get();
    }

    @Override
    public void updateSpeed(BlockState state, LevelAccessor world, BlockPos pos) {
        withBlockEntityDo(world, pos, be -> {
            if (!state.getValue(CrushingWheelControllerBlock.VALID)) {
                if (be.crushingspeed != 0) {
                    be.crushingspeed = 0;
                    be.sendData();
                }
                return;
            }

            for (Direction d : Iterate.directions) {
                BlockState neighbour = world.getBlockState(pos.relative(d));
                if (!neighbour.is(getWheelBlock())) {
                    continue;
                }
                if (neighbour.getValue(BlockStateProperties.AXIS) == d.getAxis()) {
                    continue;
                }
                BlockEntity adjBE = world.getBlockEntity(pos.relative(d));
                if (!(adjBE instanceof CrushingWheelBlockEntity crushingWheelBlockEntity)){
                    continue;
                }
                be.crushingspeed = Math.abs(crushingWheelBlockEntity.getSpeed() / 50f);
                be.sendData();

                crushingWheelBlockEntity.award(AllAdvancements.CRUSHING_WHEEL);
                if (Math.abs(crushingWheelBlockEntity.getSpeed()) > AllConfigs.server().kinetics.maxRotationSpeed.get() - 1){
                    crushingWheelBlockEntity.award(AllAdvancements.CRUSHER_MAXED);
                }

                break;
            }
        });
    }
}