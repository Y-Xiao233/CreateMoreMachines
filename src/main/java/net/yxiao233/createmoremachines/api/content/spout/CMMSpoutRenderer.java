package net.yxiao233.createmoremachines.api.content.spout;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.platform.NeoForgeCatnipServices;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidStack;

public class CMMSpoutRenderer extends SafeBlockEntityRenderer<CMMSpoutBlockEntity> {
    PartialModel[] BITS;

    public CMMSpoutRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void initBits(CMMSpoutBlockEntity be) {
        if(BITS == null || BITS.length == 0){
            BITS = be.getPartialModels();
        }
    }

    protected void renderSafe(CMMSpoutBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        SmartFluidTankBehaviour tank = be.tank;
        if (tank != null) {
            initBits(be);
            SmartFluidTankBehaviour.TankSegment primaryTank = tank.getPrimaryTank();
            FluidStack fluidStack = primaryTank.getRenderedFluid();
            float level = primaryTank.getFluidLevel().getValue(partialTicks);
            float processingPT;
            float max;
            float yOffset;
            if (!fluidStack.isEmpty() && level != 0.0F) {
                boolean top = fluidStack.getFluid().getFluidType().isLighterThanAir();
                level = Math.max(level, 0.175F);
                processingPT = 0.15625F;
                max = processingPT + 0.6875F;
                yOffset = 0.6875F * level;
                ms.pushPose();
                if (!top) {
                    ms.translate(0.0F, yOffset, 0.0F);
                } else {
                    ms.translate(0.0F, max - processingPT, 0.0F);
                }

                NeoForgeCatnipServices.FLUID_RENDERER.renderFluidBox(fluidStack, processingPT, processingPT - yOffset, processingPT, max, processingPT, max, buffer, ms, light, false, true);
                ms.popPose();
            }

            int processingTicks = be.processingTicks;
            processingPT = (float)processingTicks - partialTicks;
            max = 1.0F - (processingPT - 5.0F) / 10.0F;
            max = Mth.clamp(max, 0.0F, 1.0F);
            yOffset = 0.0F;
            if (!fluidStack.isEmpty() && processingTicks != -1) {
                yOffset = (float)(Math.pow((double)(2.0F * max - 1.0F), 2.0) - 1.0);
                AABB bb = (new AABB(0.5, 0.0, 0.5, 0.5, -1.2, 0.5)).inflate(yOffset / 32.0F);
                NeoForgeCatnipServices.FLUID_RENDERER.renderFluidBox(fluidStack, (float)bb.minX, (float)bb.minY, (float)bb.minZ, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ, buffer, ms, light, true, true);
            }

            float squeeze = yOffset;
            if (processingPT < 0.0F) {
                squeeze = 0.0F;
            } else if (processingPT < 2.0F) {
                squeeze = Mth.lerp(processingPT / 2.0F, 0.0F, -1.0F);
            } else if (processingPT < 10.0F) {
                squeeze = -1.0F;
            }

            ms.pushPose();

            for (PartialModel bit : BITS) {
                CachedBuffers.partial(bit, be.getBlockState()).light(light).renderInto(ms, buffer.getBuffer(RenderType.solid()));
                ms.translate(0.0F, -3.0F * squeeze / 32.0F, 0.0F);
            }

            ms.popPose();
        }
    }
}
