package net.yxiao233.createmoremachines.utils;

import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.kinetics.fan.processing.AllFanProcessingTypes;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class BigTransportedItemStack {
    public static CompoundTag serializeNBT(TransportedItemStack stack, HolderLookup.Provider registries) {
        CompoundTag nbt = new CompoundTag();
        nbt.put("Item", BigItemStack.of(stack.stack).saveOptional(registries));
        nbt.putFloat("Pos", stack.beltPosition);
        nbt.putFloat("PrevPos", stack.prevBeltPosition);
        nbt.putFloat("Offset", stack.sideOffset);
        nbt.putFloat("PrevOffset", stack.prevSideOffset);
        nbt.putInt("InSegment", stack.insertedAt);
        nbt.putInt("Angle", stack.angle);
        nbt.putInt("InDirection", stack.insertedFrom.get3DDataValue());
        if (stack.processedBy != null) {
            ResourceLocation key = CreateBuiltInRegistries.FAN_PROCESSING_TYPE.getKey(stack.processedBy);
            if (key == null) {
                throw new IllegalArgumentException("Could not get id for FanProcessingType " + String.valueOf(stack.processedBy) + "!");
            }

            nbt.putString("FanProcessingType", key.toString());
            nbt.putInt("FanProcessingTime", stack.processingTime);
        }

        if (stack.locked) {
            nbt.putBoolean("Locked", stack.locked);
        }

        if (stack.lockedExternally) {
            nbt.putBoolean("LockedExternally", stack.lockedExternally);
        }

        return nbt;
    }

    public static TransportedItemStack read(CompoundTag nbt, HolderLookup.Provider registries) {
        TransportedItemStack stack = new TransportedItemStack(BigItemStack.parseOptional(registries, nbt.getCompound("Item")));
        stack.beltPosition = nbt.getFloat("Pos");
        stack.prevBeltPosition = nbt.getFloat("PrevPos");
        stack.sideOffset = nbt.getFloat("Offset");
        stack.prevSideOffset = nbt.getFloat("PrevOffset");
        stack.insertedAt = nbt.getInt("InSegment");
        stack.angle = nbt.getInt("Angle");
        stack.insertedFrom = Direction.from3DDataValue(nbt.getInt("InDirection"));
        stack.locked = nbt.getBoolean("Locked");
        stack.lockedExternally = nbt.getBoolean("LockedExternally");
        if (nbt.contains("FanProcessingType")) {
            stack.processedBy = AllFanProcessingTypes.parseLegacy(nbt.getString("FanProcessingType"));
            stack.processingTime = nbt.getInt("FanProcessingTime");
        }

        return stack;
    }
}
