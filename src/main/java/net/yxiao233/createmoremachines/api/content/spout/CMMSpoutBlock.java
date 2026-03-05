package net.yxiao233.createmoremachines.api.content.spout;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.ComparatorUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.yxiao233.createmoremachines.api.CMMTierTooltip;
import net.yxiao233.createmoremachines.api.content.IHaveTierInformation;
import net.yxiao233.createmoremachines.api.processing.IAdjustPlacedBlock;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CMMSpoutBlock extends Block implements IWrenchable, IBE<CMMSpoutBlockEntity>, IAdjustPlacedBlock, IHaveTierInformation {
    private final CMMTier tier;
    public CMMSpoutBlock(CMMTier tier, BlockBehaviour.Properties properties) {
        super(properties);
        this.tier = tier;
    }

    public CMMTier getTier() {
        return tier;
    }

    @Override
    public void addTierInformation(List<Component> tooltips) {
        CMMTierTooltip.byTypes(tooltips,tier, CMMTierTooltip.Type.FLUID_CAPABILITY, CMMTierTooltip.Type.PROCESSING_MULTIPLE);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return AllShapes.SPOUT;
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        AdvancementBehaviour.setPlacedBy(level, pos, placer);
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState blockState, @NotNull Level worldIn, @NotNull BlockPos pos) {
        return ComparatorUtil.levelOfSmartFluidTank(worldIn, pos);
    }

    @Override
    protected boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public Class<CMMSpoutBlockEntity> getBlockEntityClass() {
        return CMMSpoutBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CMMSpoutBlockEntity> getBlockEntityType() {
        return CMMRegistryEntry.getSpoutEntities().get(tier.getId()).get();
    }

    @Override
    public @NotNull String getDescriptionId() {
        return ChatFormatting.YELLOW + Component.translatable(Util.makeDescriptionId("tier",tier.getId())).getString() + ChatFormatting.WHITE + Component.translatable("block.create.spout").getString();
    }
}
