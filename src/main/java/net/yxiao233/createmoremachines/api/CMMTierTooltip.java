package net.yxiao233.createmoremachines.api;

import net.minecraft.network.chat.Component;
import net.yxiao233.createmoremachines.api.registry.CMMTier;

import java.util.List;
import java.util.Objects;

public class CMMTierTooltip {
    public static void byTypes(List<Component> tooltips, CMMTier tier, Type... types){
        for (Type type : types) {
            if (type.equals(Type.PROCESSING_MULTIPLE)) {
                processingMultiple(tooltips, tier);
            }
            if (type.equals(Type.DEPLOYER_PROCESSING_MULTIPLE)) {
                deployerProcessingMultiple(tooltips, tier);
            }
            if (type.equals(Type.FLUID_CAPABILITY)) {
                fluidCapability(tooltips, tier);
            }
            if (type.equals(Type.ITEM_CAPABILITY)) {
                itemCapability(tooltips, tier);
            }
            if(type.equals(Type.FLUID_TANK_CAPABILITY)){
                fluidTankCapability(tooltips,tier);
            }
        }
    }
    private static void itemCapability(List<Component> tooltips, CMMTier tier){
        Objects.requireNonNull(tooltips);
        Objects.requireNonNull(tier);

        tooltips.add(Component.translatable("tooltip.createmoremachines.item_capability",tier.getItemCapability()));
    }

    private static void fluidCapability(List<Component> tooltips, CMMTier tier){
        Objects.requireNonNull(tooltips);
        Objects.requireNonNull(tier);

        tooltips.add(Component.translatable("tooltip.createmoremachines.fluid_capability",tier.getFluidCapability() / 1000));
    }

    private static void processingMultiple(List<Component> tooltips, CMMTier tier){
        Objects.requireNonNull(tooltips);
        Objects.requireNonNull(tier);

        tooltips.add(Component.translatable("tooltip.createmoremachines.processing_multiple",tier.getProcessingMultiple()));
    }

    private static void deployerProcessingMultiple(List<Component> tooltips, CMMTier tier){
        Objects.requireNonNull(tooltips);
        Objects.requireNonNull(tier);

        tooltips.add(Component.translatable("tooltip.createmoremachines.processing_multiple",tier.getDeployerProcessingMultiple()));
    }

    private static void fluidTankCapability(List<Component> tooltips, CMMTier tier){
        Objects.requireNonNull(tooltips);
        Objects.requireNonNull(tier);

        tooltips.add(Component.translatable("tooltip.createmoremachines.fluid_tank_capability",tier.getFluidTankCapability()));
    }

    public enum Type{
        PROCESSING_MULTIPLE,
        ITEM_CAPABILITY,
        FLUID_CAPABILITY,
        DEPLOYER_PROCESSING_MULTIPLE,
        FLUID_TANK_CAPABILITY
    }
}
