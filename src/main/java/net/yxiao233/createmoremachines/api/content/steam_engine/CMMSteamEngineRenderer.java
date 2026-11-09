package net.yxiao233.createmoremachines.api.content.steam_engine;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class CMMSteamEngineRenderer extends SafeBlockEntityRenderer<CMMSteamEngineBlockEntity> {
    PartialModel[] BITS;
    public CMMSteamEngineRenderer(BlockEntityRendererProvider.Context context) {
    }
    public void initBits(CMMSteamEngineBlockEntity be) {
        if(BITS == null || BITS.length == 0){
            BITS = be.getPartialModels();
        }
    }

    protected void renderSafe(CMMSteamEngineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!VisualizationManager.supportsVisualization(be.getLevel())) {
            initBits(be);
            Float angle = be.getTargetAngle();
            if (angle != null) {
                BlockState blockState = be.getBlockState();
                Direction facing = SteamEngineBlock.getFacing(blockState);
                Direction.Axis facingAxis = facing.getAxis();
                Direction.Axis axis = Direction.Axis.Y;
                PoweredShaftBlockEntity shaft = be.getShaft();
                if (shaft != null) {
                    axis = KineticBlockEntityRenderer.getRotationAxisOf(shaft);
                }

                boolean roll90 = facingAxis.isHorizontal() && axis == Direction.Axis.Y || facingAxis.isVertical() && axis == Direction.Axis.Z;
                float piston = 0.375F * Mth.sin(angle) - Mth.sqrt(Mth.square(0.875F) - Mth.square(0.375F) * Mth.square(Mth.cos(angle)));
                float distance = Mth.sqrt(Mth.square(piston - 0.375F * Mth.sin(angle)));
                float angle2 = (float)Math.acos((distance / 0.875F)) * (Mth.cos(angle) >= 0.0F ? 1.0F : -1.0F);
                VertexConsumer vb = buffer.getBuffer(RenderType.solid());
                this.transformed(BITS[0], blockState, facing, roll90).translate(0.0F, piston + 1.25F, 0.0F).light(light).renderInto(ms, vb);
                this.transformed(BITS[1], blockState, facing, roll90).center().translate(0.0F, 1.0F, 0.0F).uncenter().translate(0.0F, piston + 1.25F, 0.0F).translate(0.0F, 0.25F, 0.5F).rotateX(angle2).translate(0.0F, -0.25F, -0.5F).light(light).renderInto(ms, vb);
                this.transformed(BITS[2], blockState, facing, roll90).translate(0.0F, 2.0F, 0.0F).center().rotateX(-(angle + ((float)Math.PI / 2F))).uncenter().light(light).renderInto(ms, vb);
            }
        }
    }

    private SuperByteBuffer transformed(PartialModel model, BlockState blockState, Direction facing, boolean roll90) {
        return CachedBuffers.partial(model, blockState).center().rotateYDegrees(AngleHelper.horizontalAngle(facing)).rotateXDegrees(AngleHelper.verticalAngle(facing) + 90.0F).rotateYDegrees(roll90 ? -90.0F : 0.0F).uncenter();
    }

    public int getViewDistance() {
        return 128;
    }
}
