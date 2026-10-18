package net.yxiao233.createmoremachines;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.yxiao233.createmoremachines.api.config.BaseConfigBuilder;
import net.yxiao233.createmoremachines.api.config.TierConfigBase;
import net.yxiao233.createmoremachines.api.registry.BuiltInAdvancedMachineTypes;

public class CMMConfig {
    private static final ModConfigSpec.Builder BUILDER;
    public static final TierConfigBase BRASS = BaseConfigBuilder.builder("brass",256,4000,4)
            .overrideFluidCap(BuiltInAdvancedMachineTypes.FLUID_TANK,32000)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"generated_speed",64)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"capacity",2048)
            .overrideImpact(BuiltInAdvancedMachineTypes.PRESS,32)
            .overrideImpact(BuiltInAdvancedMachineTypes.MIXER,16)
            .overrideImpact(BuiltInAdvancedMachineTypes.DEPLOYER,16)
            .overrideImpact(BuiltInAdvancedMachineTypes.SAW,8)
            .overrideItemCap(BuiltInAdvancedMachineTypes.SAW,64)
            .noFluidCap(BuiltInAdvancedMachineTypes.DEPOT,BuiltInAdvancedMachineTypes.DEPLOYER,BuiltInAdvancedMachineTypes.MIXER,BuiltInAdvancedMachineTypes.PRESS,BuiltInAdvancedMachineTypes.SAW,BuiltInAdvancedMachineTypes.STEAM_ENGINE)
            .noItemCap(BuiltInAdvancedMachineTypes.FLUID_TANK,BuiltInAdvancedMachineTypes.MIXER,BuiltInAdvancedMachineTypes.PRESS,BuiltInAdvancedMachineTypes.SPOUT,BuiltInAdvancedMachineTypes.STEAM_ENGINE)
            .noMultiple(BuiltInAdvancedMachineTypes.STEAM_ENGINE,BuiltInAdvancedMachineTypes.FLUID_TANK,BuiltInAdvancedMachineTypes.DEPOT,BuiltInAdvancedMachineTypes.BASIN)
            .build();

    public static final TierConfigBase NETHERITE = BaseConfigBuilder.builder("netherite",1024,16000,16)
            .overrideFluidCap(BuiltInAdvancedMachineTypes.FLUID_TANK,64000)
            .overrideMultiple(BuiltInAdvancedMachineTypes.DEPLOYER,8)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"generated_speed",64)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"capacity",4096)
            .overrideImpact(BuiltInAdvancedMachineTypes.PRESS,128)
            .overrideImpact(BuiltInAdvancedMachineTypes.MIXER,64)
            .overrideImpact(BuiltInAdvancedMachineTypes.DEPLOYER,32)
            .overrideImpact(BuiltInAdvancedMachineTypes.SAW,32)
            .overrideItemCap(BuiltInAdvancedMachineTypes.SAW,128)
            .noFluidCap(BuiltInAdvancedMachineTypes.DEPOT,BuiltInAdvancedMachineTypes.DEPLOYER,BuiltInAdvancedMachineTypes.MIXER,BuiltInAdvancedMachineTypes.PRESS,BuiltInAdvancedMachineTypes.SAW,BuiltInAdvancedMachineTypes.STEAM_ENGINE)
            .noItemCap(BuiltInAdvancedMachineTypes.FLUID_TANK,BuiltInAdvancedMachineTypes.MIXER,BuiltInAdvancedMachineTypes.PRESS,BuiltInAdvancedMachineTypes.SPOUT,BuiltInAdvancedMachineTypes.STEAM_ENGINE)
            .noMultiple(BuiltInAdvancedMachineTypes.STEAM_ENGINE,BuiltInAdvancedMachineTypes.FLUID_TANK,BuiltInAdvancedMachineTypes.DEPOT,BuiltInAdvancedMachineTypes.BASIN)
            .build();

    public static final TierConfigBase END = BaseConfigBuilder.builder("end",4096,64000,64)
            .overrideFluidCap(BuiltInAdvancedMachineTypes.FLUID_TANK,256000)
            .overrideMultiple(BuiltInAdvancedMachineTypes.DEPLOYER,16)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"generated_speed",128)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"capacity",8192)
            .overrideImpact(BuiltInAdvancedMachineTypes.PRESS,512)
            .overrideImpact(BuiltInAdvancedMachineTypes.MIXER,256)
            .overrideImpact(BuiltInAdvancedMachineTypes.DEPLOYER,64)
            .overrideImpact(BuiltInAdvancedMachineTypes.SAW,128)
            .overrideItemCap(BuiltInAdvancedMachineTypes.SAW,256)
            .noFluidCap(BuiltInAdvancedMachineTypes.DEPOT,BuiltInAdvancedMachineTypes.DEPLOYER,BuiltInAdvancedMachineTypes.MIXER,BuiltInAdvancedMachineTypes.PRESS,BuiltInAdvancedMachineTypes.SAW,BuiltInAdvancedMachineTypes.STEAM_ENGINE)
            .noItemCap(BuiltInAdvancedMachineTypes.FLUID_TANK,BuiltInAdvancedMachineTypes.MIXER,BuiltInAdvancedMachineTypes.PRESS,BuiltInAdvancedMachineTypes.SPOUT,BuiltInAdvancedMachineTypes.STEAM_ENGINE)
            .noMultiple(BuiltInAdvancedMachineTypes.STEAM_ENGINE,BuiltInAdvancedMachineTypes.FLUID_TANK,BuiltInAdvancedMachineTypes.DEPOT,BuiltInAdvancedMachineTypes.BASIN)
            .build();

    public static final TierConfigBase BEYOND = BaseConfigBuilder.builder("beyond",16384,256000,256)
            .overrideFluidCap(BuiltInAdvancedMachineTypes.FLUID_TANK,1024000)
            .overrideMultiple(BuiltInAdvancedMachineTypes.DEPLOYER,32)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"generated_speed",128)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"capacity",16384)
            .overrideImpact(BuiltInAdvancedMachineTypes.PRESS,2048)
            .overrideImpact(BuiltInAdvancedMachineTypes.MIXER,1024)
            .overrideImpact(BuiltInAdvancedMachineTypes.DEPLOYER,128)
            .overrideImpact(BuiltInAdvancedMachineTypes.SAW,512)
            .overrideItemCap(BuiltInAdvancedMachineTypes.SAW,512)
            .noFluidCap(BuiltInAdvancedMachineTypes.DEPOT,BuiltInAdvancedMachineTypes.DEPLOYER,BuiltInAdvancedMachineTypes.MIXER,BuiltInAdvancedMachineTypes.PRESS,BuiltInAdvancedMachineTypes.SAW,BuiltInAdvancedMachineTypes.STEAM_ENGINE)
            .noItemCap(BuiltInAdvancedMachineTypes.FLUID_TANK,BuiltInAdvancedMachineTypes.MIXER,BuiltInAdvancedMachineTypes.PRESS,BuiltInAdvancedMachineTypes.SPOUT,BuiltInAdvancedMachineTypes.STEAM_ENGINE)
            .noMultiple(BuiltInAdvancedMachineTypes.STEAM_ENGINE,BuiltInAdvancedMachineTypes.FLUID_TANK,BuiltInAdvancedMachineTypes.DEPOT,BuiltInAdvancedMachineTypes.BASIN)
            .build();

    public static final TierConfigBase CREATIVE = BaseConfigBuilder.builder("creative",Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE)
            .overrideMultiple(BuiltInAdvancedMachineTypes.DEPLOYER,64)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"generated_speed",256)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"capacity",Integer.MAX_VALUE)
            .overrideImpact(BuiltInAdvancedMachineTypes.PRESS,8)
            .overrideImpact(BuiltInAdvancedMachineTypes.MIXER,4)
            .overrideImpact(BuiltInAdvancedMachineTypes.DEPLOYER,4)
            .overrideImpact(BuiltInAdvancedMachineTypes.SAW,8)
            .overrideItemCap(BuiltInAdvancedMachineTypes.SAW,4096)
            .noFluidCap(BuiltInAdvancedMachineTypes.DEPOT,BuiltInAdvancedMachineTypes.DEPLOYER,BuiltInAdvancedMachineTypes.MIXER,BuiltInAdvancedMachineTypes.PRESS,BuiltInAdvancedMachineTypes.SAW,BuiltInAdvancedMachineTypes.STEAM_ENGINE)
            .noItemCap(BuiltInAdvancedMachineTypes.FLUID_TANK,BuiltInAdvancedMachineTypes.MIXER,BuiltInAdvancedMachineTypes.PRESS,BuiltInAdvancedMachineTypes.SPOUT,BuiltInAdvancedMachineTypes.STEAM_ENGINE)
            .noMultiple(BuiltInAdvancedMachineTypes.STEAM_ENGINE,BuiltInAdvancedMachineTypes.FLUID_TANK,BuiltInAdvancedMachineTypes.DEPOT,BuiltInAdvancedMachineTypes.BASIN)
            .build();

    protected static final ModConfigSpec SPEC;
    static {
        BUILDER = new ModConfigSpec.Builder();
        {
            BUILDER.translation(TierConfigBase.key("tier_settings")).push("TierSettings");
            {
                BRASS.registry(BUILDER);
                NETHERITE.registry(BUILDER);
                END.registry(BUILDER);
                BEYOND.registry(BUILDER);
                CREATIVE.registry(BUILDER);
            }
            BUILDER.pop();
        }
        SPEC = BUILDER.build();
    }

    protected static void loadConfig(ModConfigEvent.Loading event){
        BRASS.onLoad(event);
        NETHERITE.onLoad(event);
        END.onLoad(event);
        BEYOND.onLoad(event);
        CREATIVE.onLoad(event);
    }
}