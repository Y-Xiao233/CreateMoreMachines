package net.yxiao233.createmoremachines.api.content.depot;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.yxiao233.createmoremachines.api.CMMTierTooltip;
import net.yxiao233.createmoremachines.api.content.IHaveTierInformation;
import net.yxiao233.createmoremachines.api.processing.IAdjustPlacedBlock;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CMMDepotBlock extends Block implements IBE<CMMDepotBlockEntity>, IWrenchable, ProperWaterloggedBlock, IAdjustPlacedBlock, IHaveTierInformation {
    private final CMMTier tier;
    public CMMDepotBlock(CMMTier tier, Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false));
        this.tier = tier;
    }

    public CMMTier getTier() {
        return tier;
    }

    @Override
    public void addTierInformation(List<Component> tooltips) {
        CMMTierTooltip.byTypes(tooltips,tier, CMMTierTooltip.Type.ITEM_CAPABILITY);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(WATERLOGGED));
    }

    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return this.fluidState(state);
    }

    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        this.updateWater(level, state, pos);
        return state;
    }

    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return this.withWater(super.getStateForPlacement(context), context);
    }

    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return AllShapes.CASING_13PX.get(Direction.UP);
    }

    public Class<CMMDepotBlockEntity> getBlockEntityClass() {
        return CMMDepotBlockEntity.class;
    }

    public BlockEntityType<? extends CMMDepotBlockEntity> getBlockEntityType() {
        return CMMRegistryEntry.getDepotEntities().get(tier.getId()).get();
    }

    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }

    public void updateEntityAfterFallOn(@NotNull BlockGetter level, @NotNull Entity entityIn) {
        super.updateEntityAfterFallOn(level, entityIn);
        SharedCMMDepotBlockMethods.onLanded(level, entityIn);
    }

    public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(@NotNull BlockState blockState, @NotNull Level worldIn, @NotNull BlockPos pos) {
        return SharedCMMDepotBlockMethods.getComparatorInputOverride(blockState, worldIn, pos);
    }

    protected boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        return SharedCMMDepotBlockMethods.onUse(tier,stack,state,level,pos,player,hand,hitResult);
    }

    @Override
    public @NotNull String getDescriptionId() {
        return ChatFormatting.YELLOW + Component.translatable(Util.makeDescriptionId("tier",tier.getId())).getString() + ChatFormatting.WHITE + Component.translatable("block.create.depot").getString();
    }
}
