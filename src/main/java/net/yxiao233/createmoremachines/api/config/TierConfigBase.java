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
    private ModConfigSpec.IntValue FLUID_TANK_CAPABILITY;
    private ModConfigSpec.IntValue STEAM_ENGINE_GENERATED_SPEED;
    private ModConfigSpec.DoubleValue STEAM_ENGINE_CAPACITY;
    private int tiredItemCapability;
    private int tiredFluidCapability;
    private int tiredProcessingMultiple;
    private int tiredDeployerProcessingMultiple;
    private double tiredMechanicalPressImpact;
    private double tiredMechanicalMixerImpact;
    private double tiredDeployerImpact;
    private int tiredFluidTankCapability;
    private int tiredSteamEngineGeneratedSpeed;
    private double tiredSteamEngineCapacity;
    private final String tier;
    private final int itemCapability;
    private final int fluidCapability;
    private final int processingMultiple;
    private final int deployerProcessingMultiple;
    private final double mechanicalPressImpact;
    private final double mechanicalMixerImpact;
    private final double deployerImpact;
    private final int fluidTankCapability;
    private final int steamEngineGeneratedSpeed;
    private final double steamEngineCapacity;

    private TierConfigBase(String tier, int itemCapability, int fluidCapability, int fluidTankCapability, int processingMultiple, int deployerProcessingMultiple, int steamEngineGeneratedSpeed, double steamEngineCapacity, double mechanicalPressImpact, double mechanicalMixerImpact, double deployerImpact){
        this.tier = tier;
        this.itemCapability = itemCapability;
        this.fluidCapability = fluidCapability;
        this.fluidTankCapability = fluidTankCapability;
        this.processingMultiple = processingMultiple;
        this.mechanicalPressImpact = mechanicalPressImpact;
        this.mechanicalMixerImpact = mechanicalMixerImpact;
        this.deployerImpact = deployerImpact;
        this.deployerProcessingMultiple = deployerProcessingMultiple;
        this.steamEngineGeneratedSpeed = steamEngineGeneratedSpeed;
        this.steamEngineCapacity = steamEngineCapacity;
    }

    public static TierConfigBase create(String tier, int itemCapability, int fluidCapability, int fluidTankCapability, int processingMultiple,int deployerProcessingMultiple, int steamEngineGeneratedSpeed, double steamEngineCapacity, double mechanicalPressImpact, double mechanicalMixerImpact, double deployerImpact){
        return new TierConfigBase(tier,itemCapability,fluidCapability,fluidTankCapability,processingMultiple,deployerProcessingMultiple,steamEngineGeneratedSpeed,steamEngineCapacity,mechanicalPressImpact,mechanicalMixerImpact,deployerImpact);
    }
    public void registry(ModConfigSpec.Builder BUILDER){
        BUILDER.translation(key(tier + "_tier")).push(upperCaseForFirstChar(tier) + "Tier");

        ITEM_CAPABILITY = BUILDER
                .translation(key("item_capability"))
                .comment("Item capability for " + tier + " tier mechanical[default:" + itemCapability + "]")
                .defineInRange(tier + "_item_capability",itemCapability,1,Integer.MAX_VALUE);

        FLUID_CAPABILITY = BUILDER
                .translation(key("fluid_capability"))
                .comment("Fluid capability for " + tier + " tier mechanical[default:" + fluidCapability + "]")
                .defineInRange(tier + "_fluid_capability",fluidCapability,1,Integer.MAX_VALUE);

        FLUID_TANK_CAPABILITY = BUILDER
                .translation(key("fluid_tank_capability"))
                .comment("Fluid Tank capability for " + tier + " tier mechanical[default:" + fluidTankCapability + "]")
                .defineInRange(tier + "_fluid_tank_capability",fluidTankCapability,1,Integer.MAX_VALUE);

        PROCESSING_MULTIPLE = BUILDER
                .translation(key("processing_multiple"))
                .comment("Processing multiple for " + tier + " tier mechanical[default:" + processingMultiple + "]")
                .defineInRange(tier + "_processing_multiple",processingMultiple,1,Integer.MAX_VALUE);

        DEPLOYER_PROCESSING_MULTIPLE = BUILDER
                .translation(key("deployer_processing_multiple"))
                .comment("Processing multiple for " + tier + " tier Deployer[default:" + deployerProcessingMultiple + "]")
                .defineInRange(tier + "_deployer_processing_multiple",deployerProcessingMultiple,1,64);

        STEAM_ENGINE_GENERATED_SPEED = BUILDER
                .translation(key("steam_engine_generated_speed"))
                .comment("Generated Speed for " + tier + " tier Steam Engine[default:" + steamEngineGeneratedSpeed + "]")
                .defineInRange(tier + "_steam_engine_generated_speed",steamEngineGeneratedSpeed,1,256);

        STEAM_ENGINE_CAPACITY = BUILDER
                .translation(key("steam_engine_capacity"))
                .comment("Capacity for " + tier + " tier Steam Engine[default:" + steamEngineCapacity + "]")
                .defineInRange(tier + "_steam_engine_capacity",steamEngineCapacity,1,Integer.MAX_VALUE);

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
                .comment("Impact needed for" + tier + " tier deployer[default:" + deployerImpact + "]")
                .defineInRange(tier + "deployer_impact",deployerImpact,1,Double.MAX_VALUE);

        BUILDER.pop();
    }

    @SuppressWarnings("unused")
    public void onLoad(ModConfigEvent.Loading event){
        this.tiredItemCapability = ITEM_CAPABILITY.get();
        this.tiredFluidCapability = FLUID_CAPABILITY.get();
        this.tiredFluidTankCapability = FLUID_TANK_CAPABILITY.get();
        this.tiredProcessingMultiple = PROCESSING_MULTIPLE.get();
        this.tiredDeployerProcessingMultiple = DEPLOYER_PROCESSING_MULTIPLE.get();
        this.tiredMechanicalPressImpact = MECHANICAL_PRESS_IMPACT.get();
        this.tiredMechanicalMixerImpact = MECHANICAL_MIXER_IMPACT.get();
        this.tiredDeployerImpact = DEPLOYER_IMPACT.get();
        this.tiredSteamEngineGeneratedSpeed = STEAM_ENGINE_GENERATED_SPEED.get();
        this.tiredSteamEngineCapacity = STEAM_ENGINE_CAPACITY.get();
    }

    public int getItemCapability(){
        return tiredItemCapability;
    }

    public int getFluidCapability(){
        return tiredFluidCapability;
    }

    public int getFluidTankCapability() {
        return tiredFluidTankCapability;
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

    public double getSteamEngineCapacity() {
        return tiredSteamEngineCapacity;
    }

    public int getSteamEngineGeneratedSpeed() {
        return tiredSteamEngineGeneratedSpeed;
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
