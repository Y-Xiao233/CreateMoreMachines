package net.yxiao233.createmoremachines.api.content.mechanical.press;

import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CMMBeltPressingCallbacks {
    public CMMBeltPressingCallbacks() {
    }

    static BeltProcessingBehaviour.ProcessingResult onItemReceived(TransportedItemStack transported, TransportedItemStackHandlerBehaviour handler, CMMPressingBehaviour behaviour) {
        if (behaviour.specifics.getKineticSpeed() == 0.0F) {
            return BeltProcessingBehaviour.ProcessingResult.PASS;
        } else if (behaviour.running) {
            return BeltProcessingBehaviour.ProcessingResult.HOLD;
        } else if (!behaviour.specifics.tryProcessOnBelt(transported, null, true)) {
            return BeltProcessingBehaviour.ProcessingResult.PASS;
        } else {
            behaviour.start(PressingBehaviour.Mode.BELT);
            return BeltProcessingBehaviour.ProcessingResult.HOLD;
        }
    }

    @SuppressWarnings("deprecation")
    static BeltProcessingBehaviour.ProcessingResult whenItemHeld(TransportedItemStack transported, TransportedItemStackHandlerBehaviour handler, CMMPressingBehaviour behaviour) {
        if (behaviour.specifics.getKineticSpeed() == 0.0F) {
            return BeltProcessingBehaviour.ProcessingResult.PASS;
        } else if (!behaviour.running) {
            return BeltProcessingBehaviour.ProcessingResult.PASS;
        } else if (behaviour.runningTicks != 120) {
            return BeltProcessingBehaviour.ProcessingResult.HOLD;
        } else {
            behaviour.particleItems.clear();
            ArrayList<ItemStack> results = new ArrayList<>();
            if (!behaviour.specifics.tryProcessOnBelt(transported, results, false)) {
                return BeltProcessingBehaviour.ProcessingResult.PASS;
            } else {
                transported.clearFanProcessingData();
                List<TransportedItemStack> collect = results.stream().map((stack) -> {
                    TransportedItemStack copy = transported.copy();
                    boolean centered = BeltHelper.isItemUpright(stack);
                    copy.stack = stack;
                    copy.locked = true;
                    copy.angle = centered ? 180 : Create.RANDOM.nextInt(360);
                    return copy;
                }).collect(Collectors.toList());
                TransportedItemStack left = transported.copy();
                int max = Math.min(behaviour.getTier().getProcessingMultiple(), left.stack.getCount());
                left.stack.shrink(max);
                if (collect.isEmpty()) {
                    handler.handleProcessingOnItem(transported, TransportedItemStackHandlerBehaviour.TransportedResult.convertTo(left));
                } else {
                    handler.handleProcessingOnItem(transported, TransportedItemStackHandlerBehaviour.TransportedResult.convertToAndLeaveHeld(collect, left));
                }

                behaviour.blockEntity.sendData();
                return BeltProcessingBehaviour.ProcessingResult.HOLD;
            }
        }
    }
}
