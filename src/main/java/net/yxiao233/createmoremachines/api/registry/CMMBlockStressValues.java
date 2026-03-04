package net.yxiao233.createmoremachines.api.registry;

import com.simibubi.create.api.stress.BlockStressValues;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.world.level.block.Block;

public class CMMBlockStressValues {
    public static <T extends Block> NonNullConsumer<T> setImpact(double value) {
        return (block) -> {
            BlockStressValues.IMPACTS.register(block, () -> value);
        };
    }
}
