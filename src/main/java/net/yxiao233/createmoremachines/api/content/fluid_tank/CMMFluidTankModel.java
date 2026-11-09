package net.yxiao233.createmoremachines.api.content.fluid_tank;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.fluids.tank.FluidTankCTBehaviour;
import com.simibubi.create.foundation.block.connected.CTModel;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import net.createmod.catnip.data.Iterate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.api.registry.FluidTankCTSpriteShiftEntry;
import net.yxiao233.createmoremachines.common.registry.CMMSpriteShifts;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CMMFluidTankModel extends CTModel {
    protected static final ModelProperty<CullData> CULL_PROPERTY = new ModelProperty<>();
    public static CMMFluidTankModel brass(BakedModel originalModel) {
        return new CMMFluidTankModel(originalModel, CMMSpriteShifts.BRASS_FLUID_TANK);
    }
    public static CMMFluidTankModel netherite(BakedModel originalModel) {
        return new CMMFluidTankModel(originalModel, CMMSpriteShifts.NETHERITE_FLUID_TANK);
    }
    public static CMMFluidTankModel end(BakedModel originalModel) {
        return new CMMFluidTankModel(originalModel, CMMSpriteShifts.END_FLUID_TANK);
    }
    public static CMMFluidTankModel beyond(BakedModel originalModel) {
        return new CMMFluidTankModel(originalModel, CMMSpriteShifts.BEYOND_FLUID_TANK);
    }

    public static CMMFluidTankModel byTier(BakedModel originalModel, CMMTier tier){
        String path = tier.getId().getPath();
        return switch (path) {
            case "brass" -> brass(originalModel);
            case "netherite" -> netherite(originalModel);
            case "end" -> end(originalModel);
            case "beyond" -> beyond(originalModel);
            default -> brass(originalModel);
        };
    }
    public CMMFluidTankModel(BakedModel originalModel, CTSpriteShiftEntry side, CTSpriteShiftEntry top, CTSpriteShiftEntry inner) {
        super(originalModel, new FluidTankCTBehaviour(side, top, inner));
    }

    public CMMFluidTankModel(BakedModel originalModel, FluidTankCTSpriteShiftEntry spriteShiftEntry) {
        this(originalModel, spriteShiftEntry.tank(), spriteShiftEntry.top(), spriteShiftEntry.inner());
    }

    @Override
    protected ModelData.Builder gatherModelData(ModelData.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state, ModelData blockEntityData) {
        super.gatherModelData(builder, world, pos, state, blockEntityData);
        CullData cullData = new CullData();

        for(Direction d : Iterate.horizontalDirections) {
            cullData.setCulled(d, ConnectivityHandler.isConnected(world, pos, pos.relative(d)));
        }

        return builder.with(CULL_PROPERTY, cullData);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType renderType) {
        if (side != null) {
            return Collections.emptyList();
        } else {
            List<BakedQuad> quads = new ArrayList<>();

            for(Direction d : Iterate.directions) {
                if (!extraData.has(CULL_PROPERTY) || !Objects.requireNonNull(extraData.get(CULL_PROPERTY)).isCulled(d)) {
                    quads.addAll(super.getQuads(state, d, rand, extraData, renderType));
                }
            }

            quads.addAll(super.getQuads(state, null, rand, extraData, renderType));
            return quads;
        }
    }

    private static class CullData {
        boolean[] culledFaces = new boolean[4];

        public CullData() {
            Arrays.fill(this.culledFaces, false);
        }

        void setCulled(Direction face, boolean cull) {
            if (!face.getAxis().isVertical()) {
                this.culledFaces[face.get2DDataValue()] = cull;
            }
        }

        boolean isCulled(Direction face) {
            return !face.getAxis().isVertical() && this.culledFaces[face.get2DDataValue()];
        }
    }
}
