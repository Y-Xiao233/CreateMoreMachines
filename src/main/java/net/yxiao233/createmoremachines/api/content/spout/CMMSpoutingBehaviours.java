package net.yxiao233.createmoremachines.api.content.spout;

import com.simibubi.create.Create;
import com.simibubi.create.compat.Mods;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.function.Predicate;

public class CMMSpoutingBehaviours {
    public static void registerDefaults() {
        Predicate<Fluid> isWater = (fluid) -> {
            return fluid.isSame(Fluids.WATER);
        };
        CMMBlockSpoutingBehaviour toMud = CMMStateChangingBehavior.setTo(250, isWater, Blocks.MUD);

        for (Block dirt : List.of(Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.ROOTED_DIRT)) {
            CMMBlockSpoutingBehaviour.BY_BLOCK.register(dirt, toMud);
        }

        CMMBlockSpoutingBehaviour.BY_BLOCK.register(Blocks.FARMLAND, CMMStateChangingBehavior.incrementingState(100, isWater, FarmBlock.MOISTURE));
        CMMBlockSpoutingBehaviour.BY_BLOCK.register(Blocks.WATER_CAULDRON, CMMStateChangingBehavior.incrementingState(250, isWater, LayeredCauldronBlock.LEVEL));
        CMMBlockSpoutingBehaviour.BY_BLOCK.register(Blocks.CAULDRON, CMMCauldronSpoutingBehavior.INSTANCE);
        if (Mods.TCONSTRUCT.isLoaded()) {

            for (String name : List.of("table", "basin")) {
                ResourceLocation id = Mods.TCONSTRUCT.rl(name);
                if (BuiltInRegistries.BLOCK_ENTITY_TYPE.containsKey(id)) {
                    BlockEntityType<?> table = BuiltInRegistries.BLOCK_ENTITY_TYPE.get(id);
                    CMMBlockSpoutingBehaviour.BY_BLOCK_ENTITY.register(table, CMMSpoutCasting.INSTANCE);
                } else {
                    Create.LOGGER.warn("Block entity {} wasn't found. Outdated compat?", id);
                }
            }
        }
    }
}
