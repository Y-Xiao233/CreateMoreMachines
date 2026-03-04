package net.yxiao233.createmoremachines.api.content.mechanical.press;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

public class CMMMechanicalPressRenderer extends KineticBlockEntityRenderer<CMMMechanicalPressBlockEntity> {
    public CMMMechanicalPressRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRenderOffScreen(@NotNull CMMMechanicalPressBlockEntity be) {
        return true;
    }

    @Override
    protected void renderSafe(CMMMechanicalPressBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (VisualizationManager.supportsVisualization(be.getLevel())) {
            BlockState state = this.getRenderedBlockState(be);
            BlockState blockState = be.getBlockState();
            RenderType type = this.getRenderType(be, state);
            CMMPressingBehaviour pressingBehaviour = be.getPressingBehaviour();
            float renderedHeadOffset = pressingBehaviour.getRenderedHeadOffset(partialTicks) * pressingBehaviour.mode.headOffset;
            SuperByteBuffer headRender = CachedBuffers.partialFacing(AllPartialModels.MECHANICAL_PRESS_HEAD, blockState, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING));
            renderRotatingBuffer(be, this.getRotatedModel(be, state), ms, buffer.getBuffer(type), light);
            headRender.translate(0.0F, -renderedHeadOffset, 0.0F).light(light).renderInto(ms, buffer.getBuffer(RenderType.solid()));
        }
    }

    @Override
    protected BlockState getRenderedBlockState(CMMMechanicalPressBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }
}
