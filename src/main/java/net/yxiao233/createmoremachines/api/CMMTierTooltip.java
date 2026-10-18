package net.yxiao233.createmoremachines.api;

import net.minecraft.network.chat.Component;
import net.yxiao233.createmoremachines.api.registry.BuiltInAdvancedMachineTypes;
import net.yxiao233.createmoremachines.api.registry.CMMTier;

import java.util.List;
import java.util.Objects;

public class CMMTierTooltip {
    public static void byTypes(List<Component> tooltips, CMMTier tier, BuiltInAdvancedMachineTypes.AdvancedMachineType<?> type, Type... types){
        for (Type t : types) {
            if (t.equals(Type.PROCESSING_MULTIPLE)) {
                processingMultiple(tooltips, tier, type);
            }
            if (t.equals(Type.FLUID_CAPABILITY)) {
                fluidCapability(tooltips, tier, type);
            }
            if (t.equals(Type.ITEM_CAPABILITY)) {
                itemCapability(tooltips, tier, type);
            }
        }
    }
    private static void itemCapability(List<Component> tooltips, CMMTier tier, BuiltInAdvancedMachineTypes.AdvancedMachineType<?> type){
        Objects.requireNonNull(tooltips);
        Objects.requireNonNull(tier);

        tooltips.add(Component.translatable("tooltip.createmoremachines.item_capability",tier.getItemCapability(type)));
    }

    private static void fluidCapability(List<Component> tooltips, CMMTier tier, BuiltInAdvancedMachineTypes.AdvancedMachineType<?> type){
        Objects.requireNonNull(tooltips);
        Objects.requireNonNull(tier);

        tooltips.add(Component.translatable("tooltip.createmoremachines.fluid_capability",tier.getFluidCapability(type) / 1000));
    }

    private static void processingMultiple(List<Component> tooltips, CMMTier tier, BuiltInAdvancedMachineTypes.AdvancedMachineType<?> type){
        Objects.requireNonNull(tooltips);
        Objects.requireNonNull(tier);

        tooltips.add(Component.translatable("tooltip.createmoremachines.processing_multiple",tier.getProcessingMultiple(type)));
    }

    public enum Type{
        PROCESSING_MULTIPLE,
        ITEM_CAPABILITY,
        FLUID_CAPABILITY,
        DEPLOYER_PROCESSING_MULTIPLE,
        FLUID_TANK_CAPABILITY
    }
}
