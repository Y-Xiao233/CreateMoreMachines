package net.yxiao233.createmoremachines.api.content.depot;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.box.PackageEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.yxiao233.createmoremachines.api.capabilities.BigItemStackHandler;
import net.yxiao233.createmoremachines.api.registry.CMMTier;

public class SharedCMMDepotBlockMethods {
    public SharedCMMDepotBlockMethods() {
    }

    protected static CMMDepotBehaviour get(BlockGetter worldIn, BlockPos pos) {
        return BlockEntityBehaviour.get(worldIn, pos, CMMDepotBehaviour.TYPE);
    }

    public static ItemInteractionResult onUse(CMMTier tier, ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (ray.getDirection() != Direction.UP) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        } else if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        } else {
            CMMDepotBehaviour behaviour = get(level, pos);
            if (behaviour == null) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            } else if (!(Boolean)behaviour.canAcceptItems.get()) {
                return ItemInteractionResult.SUCCESS;
            } else {
                boolean wasEmptyHanded = stack.isEmpty();
                boolean shouldntPlaceItem = AllBlocks.MECHANICAL_ARM.isIn(stack);
                ItemStack mainItemStack = behaviour.getHeldItemStack();
                int capability = tier != null ? tier.getItemCapability() : (!mainItemStack.isEmpty() ? mainItemStack.getMaxStackSize() : 64);
                if(!mainItemStack.isEmpty() && (capability <= mainItemStack.getCount() || wasEmptyHanded)){
                    player.getInventory().placeItemBackInInventory(mainItemStack);
                    behaviour.removeHeldItem();
                    level.playSound((Player)null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, 1.0F + level.getRandom().nextFloat());
                }

                BigItemStackHandler outputs = behaviour.processingOutputBuffer;

                for(int i = 0; i < outputs.getSlots(); ++i) {
                    player.getInventory().placeItemBackInInventory(outputs.extractItem(i, 64, false));
                }

                if (!wasEmptyHanded && !shouldntPlaceItem) {
                    ItemStack remain = stack.copy();
                    TransportedItemStack old = behaviour.heldItem;
                    int oldCount = old != null ? old.stack.copy().getCount() : 0;
                    int handCount = stack.getCount();
                    int maxPutCount = Math.min(capability, handCount + oldCount);
                    TransportedItemStack transported = new TransportedItemStack(stack.copyWithCount(maxPutCount));
                    transported.insertedFrom = player.getDirection();
                    transported.prevBeltPosition = 0.25F;
                    transported.beltPosition = 0.25F;
                    if(old == null){
                        behaviour.setHeldItem(transported);
                        player.setItemInHand(hand, remain.copyWithCount(handCount - maxPutCount));
                        AllSoundEvents.DEPOT_SLIDE.playOnServer(level, pos);
                    }else if(ItemStack.isSameItemSameComponents(old.stack.copy(),stack.copy())){
                        behaviour.setHeldItem(transported);
                        player.setItemInHand(hand, remain.copyWithCount(handCount - (maxPutCount - oldCount)));
                        AllSoundEvents.DEPOT_SLIDE.playOnServer(level, pos);
                    }else{
                        TransportedItemStack t = new TransportedItemStack(stack.copyWithCount(Math.min(capability,handCount)));
                        player.getInventory().placeItemBackInInventory(old.stack.copy());
                        behaviour.removeHeldItem();
                        behaviour.setHeldItem(t);
                        player.setItemInHand(hand, remain.copyWithCount(handCount - maxPutCount));
                        AllSoundEvents.DEPOT_SLIDE.playOnServer(level, pos);
                    }
                }

                behaviour.blockEntity.notifyUpdate();
                return ItemInteractionResult.SUCCESS;
            }
        }
    }

    public static void onLanded(BlockGetter worldIn, Entity entityIn) {
        ItemStack asItem = ItemHelper.fromItemEntity(entityIn);
        if (!asItem.isEmpty()) {
            if (!entityIn.level().isClientSide) {
                BlockPos pos = entityIn.blockPosition();
                DirectBeltInputBehaviour inputBehaviour = BlockEntityBehaviour.get(worldIn, pos, DirectBeltInputBehaviour.TYPE);
                if (inputBehaviour != null) {
                    Vec3 targetLocation = VecHelper.getCenterOf(pos).add(0.0, 0.3125, 0.0);
                    if (PackageEntity.centerPackage(entityIn, targetLocation)) {
                        ItemStack remainder = inputBehaviour.handleInsertion(asItem, Direction.DOWN, false);
                        if (entityIn instanceof ItemEntity) {
                            ((ItemEntity)entityIn).setItem(remainder);
                        }

                        if (remainder.isEmpty()) {
                            entityIn.discard();
                        }

                    }
                }
            }
        }
    }

    public static int getComparatorInputOverride(BlockState blockState, Level worldIn, BlockPos pos) {
        CMMDepotBehaviour depotBehaviour = get(worldIn, pos);
        if (depotBehaviour == null) {
            return 0;
        } else {
            float f = (float)depotBehaviour.getPresentStackSize();
            Integer max = (Integer)depotBehaviour.maxStackSize.get();
            f /= (float)(max == 0 ? 64 : max);
            return Mth.clamp(Mth.floor(f * 14.0F) + (f > 0.0F ? 1 : 0), 0, 15);
        }
    }
}
