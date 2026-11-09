package net.yxiao233.createmoremachines.api.content.fluid_tank;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.transform.PoseTransformStack;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.platform.NeoForgeCatnipServices;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class CMMFluidTankRenderer extends SafeBlockEntityRenderer<CMMFluidTankBlockEntity> {
    PartialModel[] BITS;
    public CMMFluidTankRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void initBits(CMMFluidTankBlockEntity be) {
        if(BITS == null || BITS.length == 0){
            BITS = be.getPartialModels();
        }
    }

    protected void renderSafe(CMMFluidTankBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (be.isController()) {
            initBits(be);
            if (!be.hasWindow()) {
                if (be.boiler.isActive()) {
                    this.renderAsBoiler(be, partialTicks, ms, buffer, light, overlay);
                }

            } else {
                LerpedFloat fluidLevel = be.getFluidLevel();
                if (fluidLevel != null) {
                    float capHeight = 0.25F;
                    float tankHullWidth = 0.0703125F;
                    float minPuddleHeight = 0.0625F;
                    float totalHeight = (float)be.getHeight() - 2.0F * capHeight - minPuddleHeight;
                    float level = fluidLevel.getValue(partialTicks);
                    if (!(level < 1.0F / (512.0F * totalHeight))) {
                        float clampedLevel = Mth.clamp(level * totalHeight, 0.0F, totalHeight);
                        FluidTank tank = be.getTankInventory();
                        FluidStack fluidStack = tank.getFluid();
                        if (!fluidStack.isEmpty()) {
                            boolean top = fluidStack.getFluid().getFluidType().isLighterThanAir();
                            float xMax = tankHullWidth + (float)be.getWidth() - 2.0F * tankHullWidth;
                            float yMin = totalHeight + capHeight + minPuddleHeight - clampedLevel;
                            float yMax = yMin + clampedLevel;
                            if (top) {
                                yMin += totalHeight - clampedLevel;
                                yMax += totalHeight - clampedLevel;
                            }

                            float zMax = tankHullWidth + (float)be.getWidth() - 2.0F * tankHullWidth;
                            ms.pushPose();
                            ms.translate(0.0F, clampedLevel - totalHeight, 0.0F);
                            NeoForgeCatnipServices.FLUID_RENDERER.renderFluidBox(fluidStack, tankHullWidth, yMin, tankHullWidth, xMax, yMax, zMax, buffer, ms, light, false, true);
                            ms.popPose();
                        }
                    }
                }
            }
        }
    }

    protected void renderAsBoiler(FluidTankBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState blockState = be.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.cutout());
        ms.pushPose();
        PoseTransformStack msr = TransformStack.of(ms);
        msr.translate(((float)be.getWidth() / 2.0F), 0.5F, (double)((float)be.getWidth() / 2.0F));
        float dialPivotY = 0.375F;
        float dialPivotZ = 0.5F;
        float progress = be.boiler.gauge.getValue(partialTicks);

        for(Direction d : Iterate.horizontalDirections) {
            if (!be.boiler.occludedDirections[d.get2DDataValue()]) {
                ms.pushPose();
                float yRot = -d.toYRot() - 90.0F;
                CachedBuffers.partial(BITS[0], blockState).rotateYDegrees(yRot).uncenter().translate((float)be.getWidth() / 2.0F - 0.375F, 0.0F, 0.0F).light(light).renderInto(ms, vb);
                CachedBuffers.partial(BITS[1], blockState).rotateYDegrees(yRot).uncenter().translate((float)be.getWidth() / 2.0F - 0.375F, 0.0F, 0.0F).translate(0.0F, dialPivotY, dialPivotZ).rotateXDegrees(-145.0F * progress + 90.0F).translate(0.0F, -dialPivotY, -dialPivotZ).light(light).renderInto(ms, vb);
                ms.popPose();
            }
        }

        ms.popPose();
    }

    public boolean shouldRenderOffScreen(FluidTankBlockEntity be) {
        return be.isController();
    }
}
