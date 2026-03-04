package net.yxiao233.createmoremachines.api.content.mechanical.press;

import com.simibubi.create.AllShapes;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.processing.basin.BasinBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;
import org.jetbrains.annotations.NotNull;

public class CMMMechanicalPressBlock extends HorizontalKineticBlock implements IBE<CMMMechanicalPressBlockEntity> {
    private final CMMTier tier;
    public CMMMechanicalPressBlock(CMMTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    @Override
    public Class<CMMMechanicalPressBlockEntity> getBlockEntityClass() {
        return CMMMechanicalPressBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CMMMechanicalPressBlockEntity> getBlockEntityType() {
        return CMMRegistryEntry.getMechanicalPressEntities().get(tier.getId()).get();
    }

    public CMMTier getTier() {
        return tier;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return context instanceof EntityCollisionContext && ((EntityCollisionContext)context).getEntity() instanceof Player ? AllShapes.CASING_14PX.get(Direction.DOWN) : AllShapes.MECHANICAL_PROCESSOR_SHAPE;
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader worldIn, BlockPos pos) {
        return !BasinBlock.isBasin(worldIn, pos.below());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferredSide = this.getPreferredHorizontalFacing(context);
        return preferredSide != null ? this.defaultBlockState().setValue(HORIZONTAL_FACING, preferredSide) : super.getStateForPlacement(context);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING).getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(HORIZONTAL_FACING).getAxis();
    }

    @Override
    protected boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public @NotNull String getDescriptionId() {
        return ChatFormatting.YELLOW + Component.translatable(Util.makeDescriptionId("tier",tier.getId())).getString() + ChatFormatting.WHITE + Component.translatable("block.create.mechanical_press").getString();
    }
}
