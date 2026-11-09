package net.yxiao233.createmoremachines.datagen.content;

import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class CMMFluidTankGenerator extends SpecialBlockStateGen {
    private final String prefix;

    public CMMFluidTankGenerator() {
        this("");
    }

    public CMMFluidTankGenerator(String prefix) {
        this.prefix = prefix;
    }

    protected int getXRotation(BlockState state) {
        return 0;
    }

    protected int getYRotation(BlockState state) {
        return 0;
    }

    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        Boolean top = state.getValue(FluidTankBlock.TOP);
        Boolean bottom = state.getValue(FluidTankBlock.BOTTOM);
        FluidTankBlock.Shape shape = state.getValue(FluidTankBlock.SHAPE);
        String shapeName = "middle";
        if (top && bottom) {
            shapeName = "single";
        } else if (top) {
            shapeName = "top";
        } else if (bottom) {
            shapeName = "bottom";
        }

        String modelName = shapeName + (shape == FluidTankBlock.Shape.PLAIN ? "" : "_" + shape.getSerializedName());
        return !this.prefix.isEmpty() ? prov.models().withExistingParent(this.prefix + modelName, prov.modLoc("block/fluid_tank/block_" + modelName)).texture("0", prov.modLoc("block/" + this.prefix + "casing")).texture("1", prov.modLoc("block/" + this.prefix + "fluid_tank")).texture("3", prov.modLoc("block/" + this.prefix + "fluid_tank_window")).texture("4", prov.modLoc("block/" + this.prefix + "casing")).texture("5", prov.modLoc("block/" + this.prefix + "fluid_tank_window_single")).texture("particle", prov.modLoc("block/" + this.prefix + "fluid_tank")) : CMMAssetLookup.partialBaseModel(ctx, prov, "fluid_tank","/block_" + modelName);
    }
}
