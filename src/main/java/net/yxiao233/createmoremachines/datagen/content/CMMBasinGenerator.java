package net.yxiao233.createmoremachines.datagen.content;

import com.simibubi.create.content.processing.basin.BasinBlock;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class CMMBasinGenerator extends SpecialBlockStateGen {
    public CMMBasinGenerator() {
    }

    protected int getXRotation(BlockState state) {
        return 0;
    }

    protected int getYRotation(BlockState state) {
        return this.horizontalAngle(state.getValue(BasinBlock.FACING));
    }

    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return state.getValue(BasinBlock.FACING).getAxis().isVertical() ? CMMAssetLookup.partialBaseModel(ctx, prov, "basin") : CMMAssetLookup.partialBaseModel(ctx, prov, "basin","_directional");
    }
}
