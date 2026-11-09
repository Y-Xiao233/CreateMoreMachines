package net.yxiao233.createmoremachines.api.content.fluid_tank;

import com.simibubi.create.content.equipment.symmetryWand.SymmetryWandItem;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class CMMFluidTankItem extends BlockItem {

    public CMMFluidTankItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @NotNull InteractionResult place(@NotNull BlockPlaceContext context) {
        InteractionResult result = super.place(context);
        if (result.consumesAction()) {
            tryMultiPlace(context);
        }
        return result;
    }

    private boolean isSameTier(Block block) {
        return block instanceof CMMFluidTankBlock other && this.getBlock() instanceof CMMFluidTankBlock self && other.getTier().getId().equals(self.getTier().getId());
    }

    private void tryMultiPlace(BlockPlaceContext context) {
        Player player = context.getPlayer();
        if (player == null || player.isShiftKeyDown()) {
            return;
        }

        Direction face = context.getClickedFace();
        if (!face.getAxis().isVertical()) {
            return;
        }

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockPos placedOnPos = pos.relative(face.getOpposite());
        BlockState placedOnState = level.getBlockState(placedOnPos);

        if (!(placedOnState.getBlock() instanceof CMMFluidTankBlock tankBlock) || !isSameTier(tankBlock)) {
            return;
        }
        if (SymmetryWandItem.presentInHotbar(player)) {
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(placedOnPos);
        if (!(blockEntity instanceof FluidTankBlockEntity tankAt)) {
            return;
        }
        FluidTankBlockEntity controller = tankAt.getControllerBE();
        if (controller == null) {
            return;
        }

        int width = controller.getWidth();
        if (width <= 1) {
            return;
        }

        ItemStack stack = context.getItemInHand();
        BlockPos startPos = face == Direction.DOWN
                ? controller.getBlockPos().below()
                : controller.getBlockPos().above(controller.getHeight());

        if (startPos.getY() != pos.getY()) {
            return;
        }

        int tanksToPlace = 0;
        for (int xOffset = 0; xOffset < width; xOffset++) {
            for (int zOffset = 0; zOffset < width; zOffset++) {
                BlockPos targetPos = startPos.offset(xOffset, 0, zOffset);
                BlockState targetState = level.getBlockState(targetPos);
                if (FluidTankBlock.isTank(targetState)) {
                    continue;
                }
                if (!targetState.canBeReplaced()) {
                    return;
                }
                tanksToPlace++;
            }
        }

        if (!player.isCreative() && stack.getCount() < tanksToPlace) {
            return;
        }

        for (int xOffset = 0; xOffset < width; xOffset++) {
            for (int zOffset = 0; zOffset < width; zOffset++) {
                BlockPos targetPos = startPos.offset(xOffset, 0, zOffset);
                if (FluidTankBlock.isTank(level.getBlockState(targetPos))) {
                    continue;
                }
                BlockPlaceContext placeContext = BlockPlaceContext.at(context, targetPos, face);
                player.getPersistentData().putBoolean("SilenceTankSound", true);
                super.place(placeContext);
                player.getPersistentData().remove("SilenceTankSound");
            }
        }
    }

    @Override
    protected boolean updateCustomBlockEntityTag(@NotNull BlockPos pos, Level level, Player player, @NotNull ItemStack stack, @NotNull BlockState state) {
        MinecraftServer server = level.getServer();
        if (server == null) {
            return false;
        }
        CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (data != null) {
            CompoundTag nbt = data.copyTag();
            nbt.remove("Luminosity");
            nbt.remove("Size");
            nbt.remove("Height");
            nbt.remove("Controller");
            nbt.remove("LastKnownPos");
            if (nbt.contains("TankContent")) {
                FluidStack fluid = FluidStack.parseOptional(server.registryAccess(), nbt.getCompound("TankContent"));
                if (!fluid.isEmpty()) {
                    fluid.setAmount(Math.min(FluidTankBlockEntity.getCapacityMultiplier(), fluid.getAmount()));
                    nbt.put("TankContent", fluid.saveOptional(server.registryAccess()));
                }
            }
            BlockEntity.addEntityType(nbt, ((IBE<?>) this.getBlock()).getBlockEntityType());
            stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(nbt));
        }
        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }
}
