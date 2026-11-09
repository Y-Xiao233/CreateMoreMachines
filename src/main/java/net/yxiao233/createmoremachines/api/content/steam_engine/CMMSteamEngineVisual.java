package net.yxiao233.createmoremachines.api.content.steam_engine;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.yxiao233.createmoremachines.api.registry.CMMPartialModelsRegistry;

import java.util.Objects;
import java.util.function.Consumer;

public class CMMSteamEngineVisual extends AbstractBlockEntityVisual<CMMSteamEngineBlockEntity> implements SimpleDynamicVisual {
    protected final TransformedInstance piston;
    protected final TransformedInstance linkage;
    protected final TransformedInstance connector;
    private Float lastAngle = Float.NaN;
    private Direction.Axis lastAxis = null;

    public CMMSteamEngineVisual(VisualizationContext context, CMMSteamEngineBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        PartialModel[] models = CMMPartialModelsRegistry.getPartialModels(blockEntity.getTier(), "steam_engine");
        this.piston = this.instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(models[0])).createInstance();
        this.linkage = this.instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(models[1])).createInstance();
        this.connector = this.instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(models[2])).createInstance();
        this.animate();
    }

    public void beginFrame(DynamicVisual.Context ctx) {
        this.animate();
    }

    private void animate() {
        Float angle = this.blockEntity.getTargetAngle();
        Direction.Axis axis = Direction.Axis.Y;
        PoweredShaftBlockEntity shaft = this.blockEntity.getShaft();
        if (shaft != null) {
            axis = KineticBlockEntityRenderer.getRotationAxisOf(shaft);
        }

        if (!Objects.equals(angle, this.lastAngle) || this.lastAxis != axis) {
            this.lastAngle = angle;
            this.lastAxis = axis;
            if (angle == null) {
                this.piston.setVisible(false);
                this.linkage.setVisible(false);
                this.connector.setVisible(false);
            } else {
                this.piston.setVisible(true);
                this.linkage.setVisible(true);
                this.connector.setVisible(true);
                Direction facing = SteamEngineBlock.getFacing(this.blockState);
                Direction.Axis facingAxis = facing.getAxis();
                boolean roll90 = facingAxis.isHorizontal() && axis == Direction.Axis.Y || facingAxis.isVertical() && axis == Direction.Axis.Z;
                float piston = 0.375F * Mth.sin(angle) - Mth.sqrt(Mth.square(0.875F) - Mth.square(0.375F) * Mth.square(Mth.cos(angle)));
                float distance = Mth.sqrt(Mth.square(piston - 0.375F * Mth.sin(angle)));
                float angle2 = (float)Math.acos((double)(distance / 0.875F)) * (Mth.cos(angle) >= 0.0F ? 1.0F : -1.0F);
                this.transformed(this.piston, facing, roll90).translate(0.0F, piston + 1.25F, 0.0F).setChanged();
                this.transformed(this.linkage, facing, roll90).center().translate(0.0F, 1.0F, 0.0F).uncenter().translate(0.0F, piston + 1.25F, 0.0F).translate(0.0F, 0.25F, 0.5F).rotateX(angle2).translate(0.0F, -0.25F, -0.5F).setChanged();
                this.transformed(this.connector, facing, roll90).translate(0.0F, 2.0F, 0.0F).center().rotateX(-(angle + ((float)Math.PI / 2F))).uncenter().setChanged();
            }
        }
    }

    protected TransformedInstance transformed(TransformedInstance modelData, Direction facing, boolean roll90) {
        return modelData.setIdentityTransform().translate(this.getVisualPosition()).center().rotateYDegrees(AngleHelper.horizontalAngle(facing)).rotateXDegrees(AngleHelper.verticalAngle(facing) + 90.0F).rotateYDegrees(roll90 ? -90.0F : 0.0F).uncenter();
    }

    public void updateLight(float partialTick) {
        this.relight(this.piston, this.linkage, this.connector);
    }

    protected void _delete() {
        this.piston.delete();
        this.linkage.delete();
        this.connector.delete();
    }

    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(this.piston);
        consumer.accept(this.linkage);
        consumer.accept(this.connector);
    }
}
