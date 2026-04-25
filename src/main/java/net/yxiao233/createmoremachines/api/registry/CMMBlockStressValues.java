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

    public static NonNullConsumer<Block> setGeneratorSpeed(int value, boolean mayGenerateLess) {
        return (block) -> {
            BlockStressValues.RPM.register(block, new BlockStressValues.GeneratedRpm(value, mayGenerateLess));
        };
    }

    public static <T extends Block> NonNullConsumer<T> setCapacities(double value) {
        return (block) -> {
            BlockStressValues.CAPACITIES.register(block, () -> value);
        };
    }
}
