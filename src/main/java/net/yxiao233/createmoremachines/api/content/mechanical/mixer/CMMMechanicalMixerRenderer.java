package net.yxiao233.createmoremachines.api.content.mechanical.mixer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class CMMMechanicalMixerRenderer extends KineticBlockEntityRenderer<CMMMechanicalMixerBlockEntity> {
    PartialModel[] BITS;

    public void initBits(CMMMechanicalMixerBlockEntity be) {
        if(BITS == null || BITS.length == 0){
            BITS = be.getPartialModels();
        }
    }

    public CMMMechanicalMixerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    protected void renderSafe(CMMMechanicalMixerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!VisualizationManager.supportsVisualization(be.getLevel())) {
            initBits(be);
            BlockState blockState = be.getBlockState();
            VertexConsumer vb = buffer.getBuffer(RenderType.solid());
            SuperByteBuffer superBuffer = CachedBuffers.partial(AllPartialModels.SHAFTLESS_COGWHEEL, blockState);
            standardKineticRotationTransform(superBuffer, be, light).renderInto(ms, vb);
            float renderedHeadOffset = be.getRenderedHeadOffset(partialTicks);
            float speed = be.getRenderedHeadRotationSpeed(partialTicks);
            float time = AnimationTickHolder.getRenderTime(be.getLevel());
            float angle = time * speed * 6.0F / 10.0F % 360.0F / 180.0F * 3.1415927F;
            SuperByteBuffer poleRender = CachedBuffers.partial(AllPartialModels.MECHANICAL_MIXER_POLE, blockState);
            poleRender.translate(0.0F, -renderedHeadOffset, 0.0F).light(light).renderInto(ms, vb);
            VertexConsumer vbCutout = buffer.getBuffer(RenderType.cutoutMipped());
            SuperByteBuffer headRender = CachedBuffers.partial(BITS[0], blockState);
            headRender.rotateCentered(angle, Direction.UP).translate(0.0F, -renderedHeadOffset, 0.0F).light(light).renderInto(ms, vbCutout);
        }
    }
}
