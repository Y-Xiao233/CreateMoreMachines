package net.yxiao233.createmoremachines.api.config;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.yxiao233.createmoremachines.CreateMoreMachines;

public class TierConfigBase {
    private ModConfigSpec.IntValue ITEM_CAPABILITY;
    private ModConfigSpec.IntValue FLUID_CAPABILITY;
    private ModConfigSpec.IntValue PROCESSING_MULTIPLE;
    private ModConfigSpec.IntValue DEPLOYER_PROCESSING_MULTIPLE;
    private ModConfigSpec.DoubleValue MECHANICAL_PRESS_IMPACT;
    private ModConfigSpec.DoubleValue MECHANICAL_MIXER_IMPACT;
    private ModConfigSpec.DoubleValue DEPLOYER_IMPACT;
    private int tiredItemCapability;
    private int tiredFluidCapability;
    private int tiredProcessingMultiple;
    private int tiredDeployerProcessingMultiple;
    private double tiredMechanicalPressImpact;
    private double tiredMechanicalMixerImpact;
    private double tiredDeployerImpact;
    private final String tier;
    private final int itemCapability;
    private final int fluidCapability;
    private final int processingMultiple;
    private final int deployerProcessingMultiple;
    private final double mechanicalPressImpact;
    private final double mechanicalMixerImpact;
    private final double deployerImpact;

    private TierConfigBase(String tier, int itemCapability, int fluidCapability, int processingMultiple, int deployerProcessingMultiple, double mechanicalPressImpact, double mechanicalMixerImpact, double deployerImpact){
        this.tier = tier;
        this.itemCapability = itemCapability;
        this.fluidCapability = fluidCapability;
        this.processingMultiple = processingMultiple;
        this.mechanicalPressImpact = mechanicalPressImpact;
        this.mechanicalMixerImpact = mechanicalMixerImpact;
        this.deployerImpact = deployerImpact;
        this.deployerProcessingMultiple = deployerProcessingMultiple;
    }

    public static TierConfigBase create(String tier, int itemCapability, int fluidCapability, int processingMultiple,int deployerProcessingMultiple, double mechanicalPressImpact, double mechanicalMixerImpact, double deployerImpact){
        return new TierConfigBase(tier,itemCapability,fluidCapability,processingMultiple,deployerProcessingMultiple,mechanicalPressImpact,mechanicalMixerImpact,deployerImpact);
    }
    public void registry(ModConfigSpec.Builder BUILDER){
        BUILDER.translation(key(tier + "_tier")).push(upperCaseForFirstChar(tier) + "Tier");

        ITEM_CAPABILITY = BUILDER
                .translation(key("item_capability"))
                .comment("Item capability for " + tier + " tier mechanical[default:" + itemCapability + "]")
                .defineInRange(tier + "_item_capability",itemCapability,1,Integer.MAX_VALUE);

        FLUID_CAPABILITY = BUILDER
                .translation(key("fluid_capability"))
                .comment("Fluid capability for " + tier + "tier mechanical[default:" + fluidCapability + "]")
                .defineInRange(tier + "_fluid_capability",fluidCapability,1,Integer.MAX_VALUE);

        PROCESSING_MULTIPLE = BUILDER
                .translation(key("processing_multiple"))
                .comment("Processing multiple for " + tier + " tier mechanical[default:" + processingMultiple + "]")
                .defineInRange(tier + "_processing_multiple",processingMultiple,1,Integer.MAX_VALUE);

        DEPLOYER_PROCESSING_MULTIPLE = BUILDER
                .translation(key("deployer_processing_multiple"))
                .comment("Processing multiple for " + tier + " tier Deployer[default:" + deployerProcessingMultiple + "]")
                .defineInRange(tier + "_deployer_processing_multiple",deployerProcessingMultiple,1,64);

        MECHANICAL_PRESS_IMPACT = BUILDER
                .translation(key("mechanical_press_impact"))
                .comment("Impact needed for " + tier + " tier mechanical press[default:" + mechanicalPressImpact + "]")
                .defineInRange(tier + "_mechanical_press_impact",mechanicalPressImpact,1,Double.MAX_VALUE);

        MECHANICAL_MIXER_IMPACT = BUILDER
                .translation(key("mechanical_mixer_impact"))
                .comment("Impact needed for " + tier + " tier mechanical mixer[default:" + mechanicalMixerImpact + "]")
                .defineInRange(tier + "_mechanical_mixer_impact",mechanicalMixerImpact,1,Double.MAX_VALUE);

        DEPLOYER_IMPACT = BUILDER
                .translation(key("deployer_impact"))
                .comment("Impact needed for" + tier + "tier deployer[default:" + deployerImpact + "]")
                .defineInRange(tier + "deployer_impact",deployerImpact,1,Double.MAX_VALUE);

        BUILDER.pop();
    }

    @SuppressWarnings("unused")
    public void onLoad(ModConfigEvent.Loading event){
        this.tiredItemCapability = ITEM_CAPABILITY.get();
        this.tiredFluidCapability = FLUID_CAPABILITY.get();
        this.tiredProcessingMultiple = PROCESSING_MULTIPLE.get();
        this.tiredDeployerProcessingMultiple = DEPLOYER_PROCESSING_MULTIPLE.get();
        this.tiredMechanicalPressImpact = MECHANICAL_PRESS_IMPACT.get();
        this.tiredMechanicalMixerImpact = MECHANICAL_MIXER_IMPACT.get();
        this.tiredDeployerImpact = DEPLOYER_IMPACT.get();
    }

    public int getItemCapability(){
        return tiredItemCapability;
    }

    public int getFluidCapability(){
        return tiredFluidCapability;
    }

    public int getProcessingMultiple(){
        return tiredProcessingMultiple;
    }
    public int getDeployerProcessingMultiple(){
        return tiredDeployerProcessingMultiple;
    }

    public double getMechanicalPressImpact(){
        return tiredMechanicalPressImpact;
    }

    public double getMechanicalMixerImpact(){
        return tiredMechanicalMixerImpact;
    }

    public double getDeployerImpact() {
        return tiredDeployerImpact;
    }

    public static String key(String... prefix){
        StringBuilder builder = new StringBuilder();
        builder.append("config.");
        builder.append(CreateMoreMachines.MODID + ".");
        for (int i = 0; i < prefix.length; i++) {
            if(i != prefix.length - 1){
                builder.append(".");
            }
            builder.append(prefix[i]);
        }
        return builder.toString();
    }

    private static String upperCaseForFirstChar(String s){
        char first = s.toCharArray()[0];
        return s.replaceFirst(String.valueOf(first), String.valueOf(first).toUpperCase());
    }
}
