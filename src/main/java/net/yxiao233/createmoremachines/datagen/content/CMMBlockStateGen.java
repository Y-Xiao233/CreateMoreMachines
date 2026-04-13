package net.yxiao233.createmoremachines.datagen.content;

import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.function.Function;

public class CMMBlockStateGen {
    public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> horizontalBlockProvider(String prefix) {
        return (c, p) -> {
            boolean customItem = prefix != null && !prefix.isEmpty();
            p.horizontalBlock(c.get(), getBlockModel(customItem, c, p, prefix));
        };
    }
    private static <T extends Block> Function<BlockState, ModelFile> getBlockModel(boolean customItem, DataGenContext<Block, T> c, RegistrateBlockstateProvider p, String prefix) {
        return ($) -> {
            return customItem ? CMMAssetLookup.partialBaseModel(c, p, prefix) : AssetLookup.standardModel(c, p);
        };
    }

    public static <T extends DirectionalAxisKineticBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> directionalAxisBlockProvider() {
        return (c, p) -> {
            BlockStateGen.directionalAxisBlock(c, p, ($, vertical) -> {
                BlockModelProvider provider = p.models();
                String name = c.getName();
                return provider.getExistingFile(p.modLoc("block/" + "deployer/" +  name + "_" + (vertical ? "vertical" : "horizontal")));
            });
        };
    }
}
