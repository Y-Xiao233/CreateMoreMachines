package net.yxiao233.createmoremachines.api.content.encased_fan;

import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.yxiao233.createmoremachines.api.registry.BuiltInAdvancedMachineTypes;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class CMMAirCurrent extends AirCurrent {
    private final CMMEncasedFanBlockEntity entity;
    public CMMAirCurrent(CMMEncasedFanBlockEntity source) {
        super(source);
        this.entity = source;
    }

    private int speedMultiple() {
        return (int) entity.getTier().getSpecial(BuiltInAdvancedMachineTypes.ENCASED_FAN,"speed");
    }

    @Override
    public void tickAffectedHandlers() {
        int multiple = speedMultiple();
        if (multiple > 1) {
            Level level = source.getAirCurrentWorld();
            if (level != null && !level.isClientSide) {
                List<Pair<TransportedItemStackHandlerBehaviour, FanProcessingType>> handlers = affectedItemHandlers;
                for (Pair<TransportedItemStackHandlerBehaviour, FanProcessingType> pair : handlers) {
                    if (pair.getRight() == null){
                        continue;
                    }
                    pair.getKey().handleProcessingOnAllItems(transported -> preDeductProcessingTime(transported, multiple));
                }
            }
        }
        super.tickAffectedHandlers();
    }

    private TransportedItemStackHandlerBehaviour.TransportedResult preDeductProcessingTime(TransportedItemStack transported, int multiple) {
        if (transported.processingTime > 0){
            transported.processingTime = Math.max(1, transported.processingTime - (multiple - 1));
        }
        return TransportedItemStackHandlerBehaviour.TransportedResult.doNothing();
    }

    @Override
    protected void tickAffectedEntities(Level level) {
        int multiple = speedMultiple();
        if (multiple > 1 && level != null && !level.isClientSide) {
            for (Entity entity : caughtEntities) {
                if (entity instanceof ItemEntity itemEntity) {
                    CompoundTag processing = itemEntity.getPersistentData().getCompound("CreateData").getCompound("Processing");
                    int time = processing.getInt("Time");
                    if (time > 0){
                        processing.putInt("Time", Math.max(1, time - (multiple - 1)));
                    }
                }
            }
        }
        super.tickAffectedEntities(level);
    }
}
